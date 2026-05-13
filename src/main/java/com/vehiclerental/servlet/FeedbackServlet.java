package com.vehiclerental.servlet;

import com.vehiclerental.model.Feedback;
import com.vehiclerental.model.User;
import com.vehiclerental.service.FeedbackService;
import com.vehiclerental.service.VehicleService;
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
 * FeedbackServlet - handles all /feedback/* routes
 * GET  /feedback           → list feedback/reviews
 * GET  /feedback/add       → show add form
 * POST /feedback/add       → save new feedback
 * GET  /feedback/edit/{id} → show edit form
 * POST /feedback/edit/{id} → save edited feedback
 * GET  /feedback/toggle/*  → toggle visibility (admin)
 * GET  /feedback/delete/*  → delete feedback (admin)
 */
@WebServlet("/feedback/*")
public class FeedbackServlet extends HttpServlet {

    private FeedbackService feedbackService;
    private VehicleService vehicleService;

    @Override
    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.feedbackService = ctx.getBean(FeedbackService.class);
        this.vehicleService  = ctx.getBean(VehicleService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        req.setAttribute("user", user);

        if (pathInfo == null || pathInfo.equals("/")) {
            if (user != null && "admin".equalsIgnoreCase(user.getUserType())) {
                req.setAttribute("feedbacks", feedbackService.getAllFeedbackAdmin());
            } else {
                req.setAttribute("feedbacks", feedbackService.getAllFeedback());
            }
            req.setAttribute("avgRating", feedbackService.getAverageRating());
            req.getRequestDispatcher("/WEB-INF/jsp/feedback/list.jsp").forward(req, resp);

        } else if (pathInfo.equals("/add")) {
            if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            req.setAttribute("vehicles", vehicleService.getAllVehicles());
            req.getRequestDispatcher("/WEB-INF/jsp/feedback/add.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/edit/")) {
            if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            String id = pathInfo.substring("/edit/".length());
            Feedback f = feedbackService.findById(id);
            if (f == null || !f.getUserId().equals(user.getUserId())) {
                resp.sendRedirect(req.getContextPath() + "/feedback"); return;
            }
            req.setAttribute("feedback", f);
            req.setAttribute("vehicles", vehicleService.getAllVehicles());
            req.getRequestDispatcher("/WEB-INF/jsp/feedback/edit.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/toggle/")) {
            if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
                resp.sendRedirect(req.getContextPath() + "/login"); return;
            }
            String id = pathInfo.substring("/toggle/".length());
            feedbackService.toggleStatus(id);
            resp.sendRedirect(req.getContextPath() + "/feedback");

        } else if (pathInfo.startsWith("/delete/")) {
            if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
                resp.sendRedirect(req.getContextPath() + "/login"); return;
            }
            String id = pathInfo.substring("/delete/".length());
            feedbackService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/feedback");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        req.setAttribute("user", user);

        if ("/add".equals(pathInfo)) {
            String vehicleId = req.getParameter("vehicleId");
            int rating       = Integer.parseInt(req.getParameter("rating"));
            String comment   = req.getParameter("comment");
            String type      = req.getParameter("type");

            var vehicle = vehicleService.findById(vehicleId);
            Feedback f = new Feedback();
            f.setUserId(user.getUserId());
            f.setVehicleId(vehicleId);
            f.setUserName(user.getName());
            f.setVehicleInfo(vehicle != null ? vehicle.getBrand() + " " + vehicle.getModel() : vehicleId);
            f.setRating(rating);
            f.setComment(comment);
            f.setType(type);

            feedbackService.addFeedback(f);
            resp.sendRedirect(req.getContextPath() + "/feedback");

        } else if (pathInfo != null && pathInfo.startsWith("/edit/")) {
            String id      = pathInfo.substring("/edit/".length());
            int rating     = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");
            String type    = req.getParameter("type");

            Feedback f = feedbackService.findById(id);
            if (f == null || !f.getUserId().equals(user.getUserId())) {
                resp.sendRedirect(req.getContextPath() + "/feedback"); return;
            }
            f.setRating(rating);
            f.setComment(comment);
            f.setType(type);
            feedbackService.update(f);
            resp.sendRedirect(req.getContextPath() + "/feedback");
        }
    }
}
