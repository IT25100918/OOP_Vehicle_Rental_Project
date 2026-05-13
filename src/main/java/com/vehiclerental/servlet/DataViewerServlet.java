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
 * DataViewerServlet — Admin-only live data viewer
 * GET /admin/data?tab=vehicles|bookings|payments|users|reviews|log
 */
@WebServlet("/admin/data")
public class DataViewerServlet extends HttpServlet {

    private UserService    userService;
    private VehicleService vehicleService;
    private BookingService bookingService;
    private PaymentService paymentService;
    private FeedbackService feedbackService;
    private AdminService   adminService;

    @Override
    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.userService     = ctx.getBean(UserService.class);
        this.vehicleService  = ctx.getBean(VehicleService.class);
        this.bookingService  = ctx.getBean(BookingService.class);
        this.paymentService  = ctx.getBean(PaymentService.class);
        this.feedbackService = ctx.getBean(FeedbackService.class);
        this.adminService    = ctx.getBean(AdminService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Admin only
        if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String tab = req.getParameter("tab");
        if (tab == null) tab = "vehicles";

        req.setAttribute("user",      user);
        req.setAttribute("activeTab", tab);

        // Load data for every tab so the counts show in the tab bar
        req.setAttribute("vehicles",  vehicleService.getAllVehicles());
        req.setAttribute("bookings",  bookingService.getAllBookings());
        req.setAttribute("payments",  paymentService.getAllPayments());
        req.setAttribute("users",     userService.getAllUsers());
        req.setAttribute("feedbacks", feedbackService.getAllFeedbackAdmin());
        req.setAttribute("logs",      adminService.getActivityLog());
        req.setAttribute("admins",    adminService.getAllAdmins());

        req.getRequestDispatcher("/WEB-INF/jsp/admin/dataviewer.jsp").forward(req, resp);
    }
}
