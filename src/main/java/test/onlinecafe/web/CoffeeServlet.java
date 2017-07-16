package test.onlinecafe.web;

import test.onlinecafe.repository.CoffeeOrderRepository;
import test.onlinecafe.repository.CoffeeTypeRepository;
import test.onlinecafe.repository.JdbcCoffeeOrderRepository;
import test.onlinecafe.repository.JdbcCoffeeTypeRepository;
import test.onlinecafe.util.CoffeeTypeUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CoffeeServlet extends HttpServlet {
    private CoffeeTypeRepository coffeeTypeRepository;
    private CoffeeOrderRepository coffeeOrderRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        coffeeTypeRepository = new JdbcCoffeeTypeRepository();
        coffeeOrderRepository = new JdbcCoffeeOrderRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("coffeeTypes", CoffeeTypeUtil.filterEnabled(coffeeTypeRepository.getAll()));
            request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
        }
    }
}
