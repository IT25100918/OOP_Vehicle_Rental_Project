package com.vehiclerental.servlet;

import com.vehiclerental.model.User;
import com.vehiclerental.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

/**
 * UserServlet - handles all /users/* routes
 * GET  /users           → list all users (admin only)
 * GET  /users/profile   → show profile form
 * POST /users/profile   → update profile
 * GET  /users/delete/*  → delete user by ID (admin only)
 */
@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

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

        String pathInfo = req.getPathInfo(); // e.g., null, "/profile", "/delete/USR002"
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (pathInfo == null || pathInfo.equals("/")) {
            // GET /users — admin list
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            req.setAttribute("users", userService.getAllUsers());
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/jsp/users/list.jsp").forward(req, resp);

        } else if (pathInfo.equals("/profile")) {
            if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/jsp/users/profile.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/delete/")) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            String id = pathInfo.substring("/delete/".length());
            userService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/users");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if ("/profile".equals(pathInfo)) {
            if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

            user.setName(req.getParameter("name"));
            user.setContact(req.getParameter("contact"));
            user.setEmail(req.getParameter("email"));
            String password = req.getParameter("password");
            if (password != null && !password.isBlank()) user.setPassword(password);

            userService.update(user);
            session.setAttribute("user", user);
            req.setAttribute("user", user);
            req.setAttribute("success", "Profile updated successfully!");
            req.getRequestDispatcher("/WEB-INF/jsp/users/profile.jsp").forward(req, resp);
        }
    }

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("user");
        return u != null && "admin".equalsIgnoreCase(u.getUserType());
    }
}
