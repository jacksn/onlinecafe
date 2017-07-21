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
            List<?> coffeeOrderItems = (List<?>) session.getAttribute("coffeeOrderItemTos");
            if (coffeeOrderItems != null) {
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
            String[] coffeeTypeIds = request.getParameterValues("id[]");
            String[] coffeeTypeQuantities = request.getParameterValues("quantity[]");

            List<CoffeeOrderItemTo> orderItemTos = new ArrayList<>();
            for (int i = 0; i < coffeeTypeIds.length; i++) {
                int coffeeTypeQuantity = Integer.valueOf(coffeeTypeQuantities[i]);
                if (coffeeTypeQuantity > 0) {
                    int id = Integer.valueOf(coffeeTypeIds[i]);
                    CoffeeType coffeeType = coffeeTypeRepository.get(id);
                    CoffeeOrderItemTo coffeeOrderItemTo = new CoffeeOrderItemTo(
                            coffeeType,
                            coffeeTypeQuantity,
                            CoffeeOrderUtil.getDiscountedItemPrice(coffeeTypeQuantity, coffeeType.getPrice()));
                    orderItemTos.add(coffeeOrderItemTo);
                }
            }
            if (!orderItemTos.isEmpty()) {
                session.setAttribute("coffeeOrderItemTos", orderItemTos);
                response.sendRedirect("/order");
                return;
            } else {
                session.setAttribute("lastErrorMessage", "Your order is empty. Enter order quantity and try again.");
            }
        } else if ("/order".equals(action)) {
            String name = request.getParameter("name");
            String address = request.getParameter("address");

            List<CoffeeOrderItemTo> coffeeOrderItemTos = (List<CoffeeOrderItemTo>) session.getAttribute("coffeeOrderItemTos");

            if (coffeeOrderItemTos == null || coffeeOrderItemTos.isEmpty()) {
                session.removeAttribute("coffeeOrderItemTos");
                session.setAttribute("lastErrorMessage", "Your order is empty. Enter order quantity and try again.");
            } else if (name != null && !name.isEmpty() && address != null && !address.isEmpty()) {
                double orderTotalCost = 0;
                List<CoffeeOrderItem> coffeeOrderItems = new ArrayList<>();
                for (CoffeeOrderItemTo coffeeOrderItemTo : coffeeOrderItemTos) {
                    coffeeOrderItems.add(CoffeeOrderUtil.getCoffeeOrderItemFromTo(coffeeOrderItemTo));
                    orderTotalCost += coffeeOrderItemTo.getCost();
                }
                orderTotalCost += CoffeeOrderUtil.getDeliveryCost(orderTotalCost);
                CoffeeOrder coffeeOrder =
                        new CoffeeOrder(LocalDateTime.now().withNano(0),
                                name, address, coffeeOrderItems,
                                orderTotalCost);
                coffeeOrderRepository.save(coffeeOrder);
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
