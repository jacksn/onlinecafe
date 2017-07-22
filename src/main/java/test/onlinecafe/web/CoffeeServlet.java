package test.onlinecafe.web;

import org.slf4j.Logger;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeOrderRepository;
import test.onlinecafe.repository.CoffeeTypeRepository;
import test.onlinecafe.repository.JdbcCoffeeOrderRepository;
import test.onlinecafe.repository.JdbcCoffeeTypeRepository;
import test.onlinecafe.to.CoffeeOrderItemTo;
import test.onlinecafe.util.CoffeeOrderUtil;
import test.onlinecafe.util.CoffeeTypeUtil;
import test.onlinecafe.util.ConfigurationUtil;
import test.onlinecafe.util.DbUtil;
import test.onlinecafe.util.discount.DefaultDiscountStrategy;
import test.onlinecafe.util.discount.DiscountStrategy;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet({"/", "/order", "/confirmation", "/reset"})
public class CoffeeServlet extends HttpServlet {
    private static final Logger LOG = getLogger(CoffeeServlet.class);

    private CoffeeTypeRepository coffeeTypeRepository;
    private CoffeeOrderRepository coffeeOrderRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Connection connection = DbUtil.getConnection();
        coffeeTypeRepository = new JdbcCoffeeTypeRepository(connection);
        coffeeOrderRepository = new JdbcCoffeeOrderRepository(connection);
        Properties appProperties = null;
        try {
            appProperties = ConfigurationUtil.getPropertiesFromFile("app.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DiscountStrategy discountStrategy = null;
        if (appProperties != null) {
            String discountStrategyClassName = appProperties.getProperty("app.discount.strategy");
            try {
                discountStrategy = (DiscountStrategy) Class.forName(discountStrategyClassName).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                discountStrategy = new DefaultDiscountStrategy();
            }
        }

        CoffeeOrderUtil.setDiscountStrategy(discountStrategy);
    }

    @Override
    public void destroy() {
        super.destroy();
        DbUtil.closeConnection();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        String action = request.getRequestURI();
        if ("/".equals(action)) {
            LOG.info("Show all CoffeeTypes");
            String lastErrorMessage = (String) session.getAttribute("lastErrorMessage");
            if (lastErrorMessage != null) {
                session.removeAttribute("lastErrorMessage");
            }
            request.setAttribute("lastErrorMessage", lastErrorMessage);
            request.setAttribute("coffeeTypes", CoffeeTypeUtil.filterEnabled(coffeeTypeRepository.getAll()));
            request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
            return;
        } else if ("/order".equals(action)) {
            List<?> orderItemToList = (List<?>) session.getAttribute("coffeeOrderItemTos");
            if (orderItemToList != null) {
                request.getRequestDispatcher("WEB-INF/order.jsp").forward(request, response);
                LOG.info("Show confirm order page");
                return;
            }
        } else if ("/confirmation".equals(action)) {
            request.getRequestDispatcher("WEB-INF/confirmation.jsp").forward(request, response);
            LOG.info("Show thank you page");
            return;
        } else if ("/reset".equals(action)) {
            session.invalidate();
        }
        response.sendRedirect("/");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String action = request.getRequestURI();
        HttpSession session = request.getSession(true);

        if ("/".equals(action)) {
            String[] typeIds = request.getParameterValues("id[]");
            String[] typeQuantities = request.getParameterValues("quantity[]");

            List<CoffeeOrderItemTo> orderItemToList = new ArrayList<>();
            for (int i = 0; i < typeIds.length; i++) {
                int quantity = Integer.valueOf(typeQuantities[i]);
                if (quantity > 0) {
                    int id = Integer.valueOf(typeIds[i]);
                    CoffeeType type = coffeeTypeRepository.get(id);
                    CoffeeOrderItemTo orderItemTo = new CoffeeOrderItemTo(
                            type,
                            quantity,
                            CoffeeOrderUtil.getDiscountedItemPrice(quantity, type.getPrice()));
                    orderItemToList.add(orderItemTo);
                }
            }
            if (!orderItemToList.isEmpty()) {
                session.setAttribute("coffeeOrderItemTos", orderItemToList);
                response.sendRedirect("/order");
                return;
            } else {
                session.setAttribute("lastErrorMessage", "Your order is empty. Enter order quantity and try again.");
            }
        } else if ("/order".equals(action)) {
            String name = request.getParameter("name");
            String address = request.getParameter("address");

            List<CoffeeOrderItemTo> orderItemToList = (List<CoffeeOrderItemTo>) session.getAttribute("coffeeOrderItemTos");

            if (orderItemToList == null || orderItemToList.isEmpty()) {
                session.removeAttribute("coffeeOrderItemTos");
                session.setAttribute("lastErrorMessage", "Your order is empty. Enter order quantity and try again.");
            } else if (name != null && !name.isEmpty() && address != null && !address.isEmpty()) {
                double orderTotalCost = 0;
                List<CoffeeOrderItem> orderItems = new ArrayList<>();
                for (CoffeeOrderItemTo orderItemTo : orderItemToList) {
                    orderItems.add(CoffeeOrderUtil.getCoffeeOrderItemFromTo(orderItemTo));
                    orderTotalCost += orderItemTo.getCost();
                }
                double deliveryCost = CoffeeOrderUtil.getDeliveryCost(orderTotalCost);
                orderTotalCost += deliveryCost;

                CoffeeOrder order =
                        new CoffeeOrder(LocalDateTime.now().withNano(0),
                                name, address, orderItems,
                                orderTotalCost);
                coffeeOrderRepository.save(order);
                session.invalidate();
                response.sendRedirect("/confirmation");
                return;
            } else {
                session.setAttribute("lastErrorMessage", "Please fill all fields and try again.");
                response.sendRedirect("/order");
                return;
            }
        }
        response.sendRedirect("/");
    }
}
