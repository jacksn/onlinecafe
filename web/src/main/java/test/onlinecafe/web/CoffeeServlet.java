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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;
import static test.onlinecafe.util.CoffeeOrderItemUtil.getOrderItemFromTo;

@WebServlet({"/", "/order", "/confirmation", "/reset"})
public class CoffeeServlet extends HttpServlet {
    private static final Logger log = getLogger(CoffeeServlet.class);

    private static final String ERROR_MESSAGE_EMPTY_ORDER = "Your order is empty. Please enter quantity and try again.";
    private static final String ERROR_MESSAGE_EMPTY_ADDRESS = "Address must not be empty. Please enter address and try again.";
    private static final String APP_PROPERTIES_FILE = "app.properties";

    private CoffeeTypeRepository coffeeTypeRepository;
    private CoffeeOrderRepository coffeeOrderRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.debug("Servlet initialization - start");
        Connection connection = DbUtil.getConnection();

        coffeeTypeRepository = new JdbcCoffeeTypeRepository(connection);
        coffeeOrderRepository = new JdbcCoffeeOrderRepository(connection);
        ConfigurationRepository configurationRepository = new JdbcConfigurationRepository(connection);

        Properties appProperties = new Properties();
        String discountStrategyClassName = null;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(APP_PROPERTIES_FILE)) {
            appProperties.load(inputStream);
            discountStrategyClassName = appProperties.getProperty("app.discount.strategy");
        } catch (IOException e) {
            log.debug("Creating CoffeeTypeRepository");
            e.printStackTrace();
        }
        DiscountStrategy discountStrategy = null;
        if (discountStrategyClassName != null) {
            try {
                Constructor c = Class.forName(discountStrategyClassName).getConstructor(ConfigurationRepository.class);
                discountStrategy = (DiscountStrategy) c.newInstance(configurationRepository);
            } catch (ClassNotFoundException | InstantiationException | InvocationTargetException
                    | IllegalAccessException | NoSuchMethodException e) {
                log.debug("Error instantiating discount strategy class {}", discountStrategyClassName);
                e.printStackTrace();
            }
        }
        try {
            if (discountStrategy != null) {
                discountStrategy.init();
                CoffeeOrderUtil.setDiscountStrategy(discountStrategy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String action = request.getRequestURI();
        log.debug("GET request received: {}", action);

        HttpSession session = request.getSession(true);

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
                session.setAttribute("lastErrorMessage", ERROR_MESSAGE_EMPTY_ORDER);
            }
        } else if ("/confirmation".equals(action)) {
            request.getRequestDispatcher("WEB-INF/confirmation.jsp").forward(request, response);
            log.debug("Show order confirmation page");
            return;
        } else if ("/reset".equals(action)) {
            session.invalidate();
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
            session.setAttribute("lastErrorMessage", ERROR_MESSAGE_EMPTY_ORDER);
        } else if ("/order".equals(action)) {
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            CoffeeOrderTo orderTo = (CoffeeOrderTo) session.getAttribute("orderTo");
            if (orderTo == null || orderTo.getOrderItems() == null || orderTo.getOrderItems().isEmpty()) {
                session.removeAttribute("orderTo");
                session.setAttribute("lastErrorMessage", ERROR_MESSAGE_EMPTY_ORDER);
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
                session.invalidate();
                response.sendRedirect("/confirmation");
                return;
            } else {
                session.setAttribute("lastErrorMessage", ERROR_MESSAGE_EMPTY_ADDRESS);
                response.sendRedirect("/order");
                return;
            }
        }
        response.sendRedirect("/");
    }
}