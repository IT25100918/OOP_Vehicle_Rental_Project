package com.vehiclerental.servlet;

import com.vehiclerental.model.RegularUser;
import com.vehiclerental.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

/**
 * RegisterServlet - handles GET /register, POST /register
 * Replaces HomeController register functionality
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.userService = ctx.getBean(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession().getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name      = req.getParameter("name");
        String nic       = req.getParameter("nic");
        String contact   = req.getParameter("contact");
        String email     = req.getParameter("email");
        String password  = req.getParameter("password");
        String licenceNo = req.getParameter("licenceNo");

        RegularUser user = new RegularUser(null, name, nic, contact, email, password, licenceNo, "basic");
        boolean success = userService.register(user);

        if (!success) {
            req.setAttribute("error", "Email already registered.");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("success", "Account created! Please login.");
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }
}
