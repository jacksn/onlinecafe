package test.onlinecafe.web;

import org.slf4j.Logger;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeOrderRepository;
import test.onlinecafe.repository.CoffeeTypeRepository;
import test.onlinecafe.repository.JdbcCoffeeOrderRepository;
import test.onlinecafe.repository.JdbcCoffeeTypeRepository;
import test.onlinecafe.util.CoffeeOrderUtil;
import test.onlinecafe.util.CoffeeTypeUtil;
import test.onlinecafe.util.DbUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class CoffeeServlet extends HttpServlet {
    private static final Logger LOG = getLogger(CoffeeServlet.class);

    private CoffeeTypeRepository coffeeTypeRepository;
    private CoffeeOrderRepository coffeeOrderRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        coffeeTypeRepository = new JdbcCoffeeTypeRepository();
        coffeeOrderRepository = new JdbcCoffeeOrderRepository();
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
            List<?> coffeeOrderItems = (List<?>) session.getAttribute("coffeeOrderItems");
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
            String[] ids = request.getParameterValues("id[]");
            String[] quantities = request.getParameterValues("quantity[]");

            List<CoffeeOrderItem> orderItems = new ArrayList<>();
            for (int i = 0; i < ids.length; i++) {
                int quantity = Integer.valueOf(quantities[i]);
                if (quantity > 0) {
                    int id = Integer.valueOf(ids[i]);
                    CoffeeType type = coffeeTypeRepository.get(id);
                    orderItems.add(new CoffeeOrderItem(type, quantity));
                }
            }
            if (!orderItems.isEmpty()) {
                session.setAttribute("coffeeOrderItems", orderItems);
                response.sendRedirect("/order");
                return;
            } else {
                session.setAttribute("lastErrorMessage", "Your order is empty. Enter order quantity and try again.");
            }
        } else if ("/order".equals(action)) {
            String name = request.getParameter("name");
            String address = request.getParameter("address");

            List<CoffeeOrderItem> coffeeOrderItems = (List<CoffeeOrderItem>) session.getAttribute("coffeeOrderItems");

            if (coffeeOrderItems == null || coffeeOrderItems.isEmpty()) {
                session.removeAttribute("coffeeOrderItems");
                session.setAttribute("lastErrorMessage", "Your order is empty. Enter order quantity and try again.");
            } else if (name != null && !name.isEmpty() && address != null && !address.isEmpty()) {
                CoffeeOrder coffeeOrder =
                        new CoffeeOrder(LocalDateTime.now().withNano(0),
                                name, address, coffeeOrderItems,
                                CoffeeOrderUtil.getTotalCost(coffeeOrderItems));
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
