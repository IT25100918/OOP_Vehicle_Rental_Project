package com.vehiclerental.servlet;

import com.vehiclerental.model.Booking;
import com.vehiclerental.model.User;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.BookingService;
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
 * BookingServlet - handles all /bookings/* routes
 * GET  /bookings               → list bookings
 * GET  /bookings/create/{id}   → show booking form
 * POST /bookings/create        → save new booking
 * GET  /bookings/cancel/{id}   → cancel booking
 * GET  /bookings/complete/{id} → mark booking complete (admin)
 */
@WebServlet("/bookings/*")
public class BookingServlet extends HttpServlet {

    private BookingService bookingService;
    private VehicleService vehicleService;

    @Override
    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.bookingService = ctx.getBean(BookingService.class);
        this.vehicleService = ctx.getBean(VehicleService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        req.setAttribute("user", user);

        if (pathInfo == null || pathInfo.equals("/")) {
            // List bookings
            if ("admin".equalsIgnoreCase(user.getUserType())) {
                req.setAttribute("bookings", bookingService.getAllBookings());
            } else {
                req.setAttribute("bookings", bookingService.getBookingsByUser(user.getUserId()));
            }
            req.getRequestDispatcher("/WEB-INF/jsp/bookings/list.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/create/")) {
            String vehicleId = pathInfo.substring("/create/".length());
            Vehicle v = vehicleService.findById(vehicleId);
            if (v == null || !v.isAvailable()) {
                resp.sendRedirect(req.getContextPath() + "/vehicles"); return;
            }
            req.setAttribute("vehicle", v);
            req.getRequestDispatcher("/WEB-INF/jsp/bookings/create.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/cancel/")) {
            String id = pathInfo.substring("/cancel/".length());
            Booking booking = bookingService.findById(id);
            if (booking != null) {
                boolean isOwner = booking.getUserId().equals(user.getUserId());
                boolean isAdmin = "admin".equalsIgnoreCase(user.getUserType());
                if (isOwner || isAdmin) bookingService.cancelBooking(id);
            }
            resp.sendRedirect(req.getContextPath() + "/bookings");

        } else if (pathInfo.startsWith("/complete/")) {
            if (!"admin".equalsIgnoreCase(user.getUserType())) {
                resp.sendRedirect(req.getContextPath() + "/login"); return;
            }
            String id = pathInfo.substring("/complete/".length());
            Booking b = bookingService.findById(id);
            if (b != null) {
                bookingService.updateStatus(id, "completed");
                vehicleService.updateAvailability(b.getVehicleId(), "available");
            }
            resp.sendRedirect(req.getContextPath() + "/bookings");
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

        if ("/create".equals(pathInfo)) {
            String vehicleId   = req.getParameter("vehicleId");
            String startDate   = req.getParameter("startDate");
            String endDate     = req.getParameter("endDate");
            String bookingType = req.getParameter("bookingType");

            Vehicle v = vehicleService.findById(vehicleId);
            Booking booking = new Booking();
            booking.setUserId(user.getUserId());
            booking.setVehicleId(vehicleId);
            booking.setUserName(user.getName());
            booking.setVehicleInfo(v != null ? v.getBrand() + " " + v.getModel() : vehicleId);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setBookingType(bookingType);

            boolean ok = bookingService.createBooking(booking);
            if (!ok) {
                req.setAttribute("error", "Vehicle is not available for the selected dates.");
                req.setAttribute("vehicle", v);
                req.getRequestDispatcher("/WEB-INF/jsp/bookings/create.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("booking", booking);
            req.getRequestDispatcher("/WEB-INF/jsp/bookings/confirmation.jsp").forward(req, resp);
        }
    }
}
