package test.onlinecafe.web;

import org.slf4j.Logger;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.*;
import test.onlinecafe.to.CoffeeOrderItemTo;
import test.onlinecafe.to.CoffeeOrderTo;
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
import static test.onlinecafe.util.CoffeeOrderItemUtil.getOrderItemFromTo;

@WebServlet({"/", "/order", "/confirmation", "/cancel"})
public class CoffeeServlet extends HttpServlet {
    private static final Logger log = getLogger(CoffeeServlet.class);

    private static final String APP_PROPERTIES_FILE = "app.properties";
    private static Set<String> supportedLanguages;
    private static String defaultLanguage;

    private static ResourceBundle messages;

    private CoffeeTypeRepository coffeeTypeRepository;
    private CoffeeOrderRepository coffeeOrderRepository;

    private static void removeSessionAttributes(HttpSession session) {
        session.removeAttribute("lastErrorMessage");
        session.removeAttribute("orderTo");
        session.removeAttribute("name");
        session.removeAttribute("address");
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
        String sessionLanguage = (String) session.getAttribute("language");

        if (langParam == null && sessionLanguage == null) {
            session.setAttribute("language", defaultLanguage);
        } else {
            if (supportedLanguages.contains(langParam)) {
                session.setAttribute("language", langParam);
            } else if (sessionLanguage == null) {
                session.setAttribute("language", defaultLanguage);
            }
        }

        if ("/".equals(action)) {
            log.debug("Show main page");
            String lastErrorMessage = (String) session.getAttribute("lastErrorMessage");
            if (lastErrorMessage != null) {
                session.removeAttribute("lastErrorMessage");
            }
            request.setAttribute("lastErrorMessage", lastErrorMessage);
            request.setAttribute("coffeeTypes", CoffeeTypeUtil.filterEnabled(coffeeTypeRepository.getAll()));
            request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
            return;
        } else if ("/order".equals(action)) {
            CoffeeOrderTo orderTo = (CoffeeOrderTo) session.getAttribute("orderTo");
            if (orderTo != null && orderTo.getOrderItems() != null && !orderTo.getOrderItems().isEmpty()) {
                request.getRequestDispatcher("WEB-INF/order.jsp").forward(request, response);
                log.debug("Show order details page");
                return;
            } else {
                log.debug("Order is empty. Redirect to main page.");
                session.removeAttribute("orderTo");
                session.setAttribute("lastErrorMessage", messages.getString("error.empty.order"));
            }
        } else if ("/confirmation".equals(action)) {
            removeSessionAttributes(session);
            request.getRequestDispatcher("WEB-INF/confirmation.jsp").forward(request, response);
            log.debug("Show order confirmation page");
            return;
        } else if ("/cancel".equals(action)) {
            removeSessionAttributes(session);
        }
        response.sendRedirect("/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getRequestURI();
        log.debug("POST request received: {}", action);
        HttpSession session = request.getSession(true);

        if ("/".equals(action)) {
            String[] typeIds = request.getParameterValues("id[]");
            String[] typeQuantities = request.getParameterValues("quantity[]");
            if (typeIds != null && typeQuantities != null && typeIds.length == typeQuantities.length) {
                List<CoffeeOrderItemTo> orderItemToList = new ArrayList<>();
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
                        CoffeeOrderItemTo orderItemTo = new CoffeeOrderItemTo(type, quantity, discountedItemCost, discounted);
                        orderItemToList.add(orderItemTo);
                        orderTotalCost += discountedItemCost;
                    }
                }
                double deliveryCost = CoffeeOrderUtil.getDeliveryCost(orderTotalCost);
                orderTotalCost += deliveryCost;
                if (!orderItemToList.isEmpty()) {
                    CoffeeOrderTo orderTo = new CoffeeOrderTo(orderItemToList, deliveryCost, orderTotalCost);
                    session.setAttribute("orderTo", orderTo);
                    response.sendRedirect("/order");
                    return;
                }
            }
            session.setAttribute("lastErrorMessage", messages.getString("error.empty.order"));
        } else if ("/order".equals(action)) {
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            CoffeeOrderTo orderTo = (CoffeeOrderTo) session.getAttribute("orderTo");
            if (orderTo == null || orderTo.getOrderItems() == null || orderTo.getOrderItems().isEmpty()) {
                session.removeAttribute("orderTo");
                session.setAttribute("lastErrorMessage", messages.getString("error.empty.order"));
                response.sendRedirect("/");
                return;
            }
            if (name != null && address != null && !address.isEmpty()) {
                List<CoffeeOrderItem> orderItems = new ArrayList<>();
                for (CoffeeOrderItemTo orderItemTo : orderTo.getOrderItems()) {
                    orderItems.add(getOrderItemFromTo(orderItemTo));
                }
                CoffeeOrder order = new CoffeeOrder(LocalDateTime.now().withNano(0), name, address, orderItems, orderTo.getCost());
                coffeeOrderRepository.save(order);
                response.sendRedirect("/confirmation");
                return;
            } else {
                session.setAttribute("lastErrorMessage", messages.getString("error.empty.address"));
                response.sendRedirect("/order");
                return;
            }
        }
        response.sendRedirect("/");
    }
}
