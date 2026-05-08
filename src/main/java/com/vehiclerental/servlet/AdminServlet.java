package com.vehiclerental.servlet;

import com.vehiclerental.model.AdminUser;
import com.vehiclerental.model.User;
import com.vehiclerental.service.AdminService;
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
 * AdminServlet - handles all /admin/* routes
 * GET  /admin/manage      → list admins
 * GET  /admin/register    → show register admin form
 * POST /admin/register    → save new admin
 * GET  /admin/delete/{id} → delete admin
 * GET  /admin/log         → activity log
 */
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private AdminService adminService;

    @Override
    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.adminService = ctx.getBean(AdminService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        req.setAttribute("user", user);

        if ("/manage".equals(pathInfo)) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            req.setAttribute("admins", adminService.getAllAdmins());
            req.getRequestDispatcher("/WEB-INF/jsp/admin/manage.jsp").forward(req, resp);

        } else if ("/register".equals(pathInfo)) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            req.getRequestDispatcher("/WEB-INF/jsp/admin/register.jsp").forward(req, resp);

        } else if (pathInfo != null && pathInfo.startsWith("/delete/")) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            String id = pathInfo.substring("/delete/".length());
            if (!user.getUserId().equals(id)) adminService.delete(id); // prevent self-delete
            resp.sendRedirect(req.getContextPath() + "/admin/manage");

        } else if ("/log".equals(pathInfo)) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            req.setAttribute("logs", adminService.getActivityLog());
            req.getRequestDispatcher("/WEB-INF/jsp/admin/log.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        req.setAttribute("user", user);

        if ("/register".equals(pathInfo)) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

            String name        = req.getParameter("name");
            String nic         = req.getParameter("nic");
            String contact     = req.getParameter("contact");
            String email       = req.getParameter("email");
            String password    = req.getParameter("password");
            String role        = req.getParameter("role");
            String permissions = req.getParameter("permissions");

            AdminUser admin = new AdminUser(null, name, nic, contact, email, password, role, permissions);
            boolean ok = adminService.register(admin);
            if (!ok) {
                req.setAttribute("error", "Email already registered.");
            } else {
                adminService.logActivity(user.getUserId(), "Registered new admin: " + email);
                req.setAttribute("success", "Admin account created successfully!");
            }
            req.getRequestDispatcher("/WEB-INF/jsp/admin/register.jsp").forward(req, resp);
        }
    }

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("user");
        return u != null && "admin".equalsIgnoreCase(u.getUserType());
    }
}
