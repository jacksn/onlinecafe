package test.onlinecafe.web;

import org.slf4j.Logger;
import test.onlinecafe.dto.CoffeeOrderDto;
import test.onlinecafe.dto.CoffeeOrderItemDto;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.*;
import test.onlinecafe.util.CoffeeOrderUtil;
import test.onlinecafe.util.CoffeeTypeUtil;
import test.onlinecafe.util.DbUtil;
import test.onlinecafe.util.discount.DiscountStrategy;
import test.onlinecafe.util.discount.NoDiscountStrategy;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;
import static test.onlinecafe.util.CoffeeOrderItemUtil.getOrderItemFromDto;
import static test.onlinecafe.web.CoffeeServlet.*;

@WebServlet({PATH_ROOT, PATH_ORDER, PATH_CONFIRMATION, PATH_CANCEL})
public class CoffeeServlet extends HttpServlet {

    private static final String APP_PROPERTIES_FILE = "app.properties";
    
    static final String JSP_ROOT = "WEB-INF/";
    static final String PAGE_COFFEE_TYPES_LIST = JSP_ROOT + "index.jsp";
    static final String PAGE_ORDER_DETAILS = JSP_ROOT + "order.jsp";
    static final String PAGE_ORDER_CONFIRMATION = JSP_ROOT + "confirmation.jsp";
    
    static final String PATH_ROOT = "/";
    static final String PATH_ORDER = "/order";
    static final String PATH_CONFIRMATION = "/confirmation";
    static final String PATH_CANCEL = "/cancel";
    
    private static final String MODEL_ATTR_LANGUAGE = "language";
    private static final String MODEL_ATTR_ORDER = "orderDto";
    private static final String MODEL_ATTR_ORDER_NAME = "name";
    private static final String MODEL_ATTR_ORDER_ADDRESS = "address";
    private static final String MODEL_ATTR_COFFEE_TYPES = "coffeeTypes";
    private static final String MODEL_ATTR_ERROR_MESSAGE = "lastErrorMessage";

    private static final Logger log = getLogger(CoffeeServlet.class);
    private static Set<String> supportedLanguages;
    private static String defaultLanguage;

    private static ResourceBundle messages;

    private CoffeeTypeRepository coffeeTypeRepository;
    private CoffeeOrderRepository coffeeOrderRepository;

    private static void removeSessionAttributes(HttpSession session) {
        session.removeAttribute(MODEL_ATTR_ERROR_MESSAGE);
        session.removeAttribute(MODEL_ATTR_ORDER);
        session.removeAttribute(MODEL_ATTR_ORDER_NAME);
        session.removeAttribute(MODEL_ATTR_ORDER_ADDRESS);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        log.debug("Servlet initialization - start");
        messages = ResourceBundle.getBundle("messages");

        DataSource dataSource = DbUtil.getDataSource();

        coffeeTypeRepository = new JdbcCoffeeTypeRepository(dataSource);
        coffeeOrderRepository = new JdbcCoffeeOrderRepository(dataSource);
        ConfigurationRepository configurationRepository = new JdbcConfigurationRepository(dataSource);

        DiscountStrategy discountStrategy = null;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(APP_PROPERTIES_FILE)) {
            Properties appProperties = new Properties();
            appProperties.load(inputStream);
            String discountStrategyClassName = appProperties.getProperty("app.discount.strategy");

            if (discountStrategyClassName != null) {
                try {
                    Constructor c = Class.forName(discountStrategyClassName).getConstructor(ConfigurationRepository.class);
                    discountStrategy = (DiscountStrategy) c.newInstance(configurationRepository);
                } catch (ClassNotFoundException | InstantiationException | InvocationTargetException
                        | IllegalAccessException | NoSuchMethodException e) {
                    log.error("Error instantiating discount strategy class {}", discountStrategyClassName);
                    e.printStackTrace();
                }
            }
            List<String> languages = Arrays.asList(appProperties.getProperty("i18n.supported.languages").split(","));
            defaultLanguage = languages.get(0);
            supportedLanguages = Collections.unmodifiableSet(new HashSet<>(languages));
        } catch (IOException e) {
            log.error("Error loading application properties from {}", APP_PROPERTIES_FILE);
            e.printStackTrace();
        }

        if (discountStrategy == null) {
            discountStrategy = new NoDiscountStrategy(configurationRepository);
        }
        discountStrategy.init();
        CoffeeOrderUtil.setDiscountStrategy(discountStrategy);
        log.debug("Servlet initialization - end");
    }

    @Override
    public void destroy() {
        log.debug("Servlet deinitialization - start");
        super.destroy();
        DbUtil.closeConnection();
        log.debug("Servlet deinitialization - end");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getRequestURI();
        log.debug("GET request received: {}", action);

        HttpSession session = request.getSession(true);

        String langParam = request.getParameter("lang");
        String sessionLanguage = (String) session.getAttribute(MODEL_ATTR_LANGUAGE);

        if (langParam == null && sessionLanguage == null) {
            session.setAttribute(MODEL_ATTR_LANGUAGE, defaultLanguage);
        } else {
            if (supportedLanguages.contains(langParam)) {
                session.setAttribute(MODEL_ATTR_LANGUAGE, langParam);
            } else if (sessionLanguage == null) {
                session.setAttribute(MODEL_ATTR_LANGUAGE, defaultLanguage);
            }
        }

        if (PATH_ROOT.equals(action)) {
            log.debug("Show main page");
            String lastErrorMessage = (String) session.getAttribute(MODEL_ATTR_ERROR_MESSAGE);
            if (lastErrorMessage != null) {
                session.removeAttribute(MODEL_ATTR_ERROR_MESSAGE);
            }
            request.setAttribute(MODEL_ATTR_ERROR_MESSAGE, lastErrorMessage);
            request.setAttribute(MODEL_ATTR_COFFEE_TYPES, CoffeeTypeUtil.filterEnabled(coffeeTypeRepository.getAll()));
            request.getRequestDispatcher(PAGE_COFFEE_TYPES_LIST).forward(request, response);
            return;
        } else if (PATH_ORDER.equals(action)) {
            CoffeeOrderDto orderDto = (CoffeeOrderDto) session.getAttribute(MODEL_ATTR_ORDER);
            if (orderDto != null && orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
                request.getRequestDispatcher(PAGE_ORDER_DETAILS).forward(request, response);
                log.debug("Show order details page");
                return;
            } else {
                log.debug("Order is empty. Redirect to main page.");
                session.removeAttribute(MODEL_ATTR_ORDER);
                session.setAttribute(MODEL_ATTR_ERROR_MESSAGE, messages.getString("error.empty.order"));
            }
        } else if (PATH_CONFIRMATION.equals(action)) {
            removeSessionAttributes(session);
            request.getRequestDispatcher(PAGE_ORDER_CONFIRMATION).forward(request, response);
            log.debug("Show order confirmation page");
            return;
        } else if (PATH_CANCEL.equals(action)) {
            removeSessionAttributes(session);
        }
        response.sendRedirect(PATH_ROOT);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getRequestURI();
        log.debug("POST request received: {}", action);
        HttpSession session = request.getSession(true);

        if (PATH_ROOT.equals(action)) {
            String[] typeIds = request.getParameterValues("id[]");
            String[] typeQuantities = request.getParameterValues("quantity[]");
            if (typeIds != null && typeQuantities != null && typeIds.length == typeQuantities.length) {
                List<CoffeeOrderItemDto> orderItemDtoList = new ArrayList<>();
                double orderTotalCost = 0;
                for (int i = 0; i < typeIds.length; i++) {
                    int quantity = Integer.parseInt(typeQuantities[i]);
                    if (quantity > 0) {
                        int id = Integer.parseInt(typeIds[i]);
                        CoffeeType type = coffeeTypeRepository.get(id);
                        if (type == null) continue;
                        double itemCost = quantity * type.getPrice();
                        double discountedItemCost = CoffeeOrderUtil.getDiscountedItemCost(quantity, type.getPrice());
                        boolean discounted = discountedItemCost < itemCost;
                        CoffeeOrderItemDto orderItemDto = new CoffeeOrderItemDto(type, quantity, discountedItemCost, discounted);
                        orderItemDtoList.add(orderItemDto);
                        orderTotalCost += discountedItemCost;
                    }
                }
                double deliveryCost = CoffeeOrderUtil.getDeliveryCost(orderTotalCost);
                orderTotalCost += deliveryCost;
                if (!orderItemDtoList.isEmpty()) {
                    CoffeeOrderDto orderDto = new CoffeeOrderDto(orderItemDtoList, deliveryCost, orderTotalCost);
                    session.setAttribute(MODEL_ATTR_ORDER, orderDto);
                    response.sendRedirect(PATH_ORDER);
                    return;
                }
            }
            session.setAttribute(MODEL_ATTR_ERROR_MESSAGE, messages.getString("error.empty.order"));
        } else if (PATH_ORDER.equals(action)) {
            String name = request.getParameter(MODEL_ATTR_ORDER_NAME);
            String address = request.getParameter(MODEL_ATTR_ORDER_ADDRESS);
            CoffeeOrderDto orderDto = (CoffeeOrderDto) session.getAttribute(MODEL_ATTR_ORDER);
            if (orderDto == null || orderDto.getOrderItems() == null || orderDto.getOrderItems().isEmpty()) {
                session.removeAttribute(MODEL_ATTR_ORDER);
                session.setAttribute(MODEL_ATTR_ERROR_MESSAGE, messages.getString("error.empty.order"));
                response.sendRedirect(PATH_ROOT);
                return;
            }
            if (name != null && address != null && !address.isEmpty()) {
                List<CoffeeOrderItem> orderItems = new ArrayList<>();
                for (CoffeeOrderItemDto orderItemDto : orderDto.getOrderItems()) {
                    orderItems.add(getOrderItemFromDto(orderItemDto));
                }
                CoffeeOrder order = new CoffeeOrder(LocalDateTime.now().withNano(0), name, address, orderItems, orderDto.getCost());
                coffeeOrderRepository.save(order);
                response.sendRedirect(PATH_CONFIRMATION);
                return;
            } else {
                session.setAttribute(MODEL_ATTR_ERROR_MESSAGE, messages.getString("error.empty.address"));
                response.sendRedirect(PATH_ORDER);
                return;
            }
        }
        response.sendRedirect(PATH_ROOT);
    }
}
