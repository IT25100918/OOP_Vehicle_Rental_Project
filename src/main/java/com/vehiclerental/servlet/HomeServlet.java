package com.vehiclerental.servlet;

import com.vehiclerental.model.User;
import com.vehiclerental.service.*;
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
 * HomeServlet - handles GET / (index) and GET /dashboard
 * Replaces HomeController index + dashboard functionality
 */
@WebServlet(urlPatterns = {"/", "/dashboard"})
public class HomeServlet extends HttpServlet {

    private UserService userService;
    private VehicleService vehicleService;
    private BookingService bookingService;
    private PaymentService paymentService;
    private FeedbackService feedbackService;

    @Override
    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.userService    = ctx.getBean(UserService.class);
        this.vehicleService = ctx.getBean(VehicleService.class);
        this.bookingService = ctx.getBean(BookingService.class);
        this.paymentService = ctx.getBean(PaymentService.class);
        this.feedbackService = ctx.getBean(FeedbackService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if ("/dashboard".equals(path)) {
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            req.setAttribute("user", user);
            req.setAttribute("totalVehicles",   vehicleService.getAllVehicles().size());
            req.setAttribute("availableVehicles", vehicleService.countAvailable());
            req.setAttribute("totalBookings",   bookingService.getAllBookings().size());
            req.setAttribute("activeBookings",  bookingService.countActive());
            req.setAttribute("totalRevenue",    paymentService.getTotalRevenue());
            req.setAttribute("totalUsers",      userService.getAllUsers().size());
            req.setAttribute("recentBookings",  bookingService.getAllBookings().stream().limit(5).toList());

            if ("admin".equalsIgnoreCase(user.getUserType())) {
                req.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/WEB-INF/jsp/users/dashboard.jsp").forward(req, resp);
            }
        } else {
            // GET / — home/index page
            req.setAttribute("vehicles", vehicleService.getAvailableVehicles());
            req.setAttribute("reviews",  feedbackService.getAllFeedback());
            req.setAttribute("avgRating", feedbackService.getAverageRating());
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
        }
    }
}
