package test.onlinecafe.web;

import org.slf4j.Logger;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeOrderRepository;
import test.onlinecafe.repository.CoffeeTypeRepository;
import test.onlinecafe.repository.JdbcCoffeeOrderRepository;
import test.onlinecafe.repository.JdbcCoffeeTypeRepository;
import test.onlinecafe.util.CoffeeTypeUtil;
import test.onlinecafe.util.DbUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
        HttpSession session = request.getSession(false);

        String action = request.getParameter("action");
        if (action == null) {
            LOG.info("Show all CoffeeTypes");
            request.setAttribute("coffeeTypes", CoffeeTypeUtil.filterEnabled(coffeeTypeRepository.getAll()));
            request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
        } else if ("confirm".equals(action)) {
            if (session != null) {
                Object orderItemsObject = session.getAttribute("orderItems");
                if (orderItemsObject != null && orderItemsObject instanceof List) {
                    List<CoffeeOrderItem> coffeeOrderItems = (List<CoffeeOrderItem>) orderItemsObject;
                    LOG.info("Show confirm order page");
                    request.setAttribute("coffeeOrderItems", coffeeOrderItems);
                    request.getRequestDispatcher("WEB-INF/order.jsp").forward(request, response);
                }
            }
        } else if ("thankyou".equals(action)) {
            LOG.info("Show thank you page");
            request.getRequestDispatcher("WEB-INF/confirmation.jsp").forward(request, response);
        } else {
            response.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
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
            if (orderItems.isEmpty()) {
                response.sendRedirect("/?error=empty_order");
            } else {
                HttpSession session = request.getSession(true);
                session.setAttribute("orderItems", orderItems);
                response.sendRedirect("/?action=confirm");
            }
        } else if ("confirm".equals(action)) {
            response.sendRedirect("/?action=thankyou");
        } else {
            response.sendRedirect("/");
        }
    }
}
