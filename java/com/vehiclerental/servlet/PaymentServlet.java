package com.vehiclerental.servlet;

import com.vehiclerental.model.Booking;
import com.vehiclerental.model.Payment;
import com.vehiclerental.model.User;
import com.vehiclerental.service.BookingService;
import com.vehiclerental.service.PaymentService;
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
 * PaymentServlet - handles all /payments/* routes
 * GET  /payments          → list payments
 * GET  /payments/pay/{id} → show payment form for a booking
 * POST /payments/pay      → process payment
 * GET  /payments/delete/* → delete payment (admin)
 */
@WebServlet("/payments/*")
public class PaymentServlet extends HttpServlet {

    private PaymentService paymentService;
    private BookingService bookingService;

    @Override
    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.paymentService = ctx.getBean(PaymentService.class);
        this.bookingService = ctx.getBean(BookingService.class);
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
            String sortBy = req.getParameter("sortBy");
            if (sortBy == null) sortBy = "date";
            req.setAttribute("sortBy", sortBy);

            if ("admin".equalsIgnoreCase(user.getUserType())) {
                req.setAttribute("payments", paymentService.getAllPayments(sortBy));
                req.setAttribute("totalRevenue", paymentService.getTotalRevenue());
                req.setAttribute("overdueBookings",
                        paymentService.getOverdueBookings(bookingService.getAllBookings()));
            } else {
                req.setAttribute("payments", paymentService.getPaymentsByUser(user.getUserId()));
                req.setAttribute("overdueBookings",
                        paymentService.getOverdueBookings(bookingService.getBookingsByUser(user.getUserId())));
            }
            req.setAttribute("paymentService", paymentService);
            req.getRequestDispatcher("/WEB-INF/jsp/payments/list.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/pay/")) {
            String bookingId = pathInfo.substring("/pay/".length());
            Booking booking = bookingService.findById(bookingId);
            if (booking == null) { resp.sendRedirect(req.getContextPath() + "/bookings"); return; }

            double suggestedLateFee = paymentService.calculateLateFee(booking);
            req.setAttribute("booking", booking);
            req.setAttribute("suggestedLateFee", suggestedLateFee);
            req.setAttribute("isOverdue", suggestedLateFee > 0);
            req.getRequestDispatcher("/WEB-INF/jsp/payments/pay.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/delete/")) {
            if (!"admin".equalsIgnoreCase(user.getUserType())) {
                resp.sendRedirect(req.getContextPath() + "/login"); return;
            }
            String id = pathInfo.substring("/delete/".length());
            paymentService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/payments");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        if ("/pay".equals(pathInfo)) {
            String bookingId     = req.getParameter("bookingId");
            String paymentMethod = req.getParameter("paymentMethod");
            double lateFee       = Double.parseDouble(req.getParameter("lateFee"));

            Booking booking = bookingService.findById(bookingId);
            if (booking == null) { resp.sendRedirect(req.getContextPath() + "/bookings"); return; }

            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setUserId(user.getUserId());
            payment.setUserName(user.getName());
            payment.setAmount(booking.getTotalCost());
            payment.setLateFee(lateFee);
            payment.setPaymentMethod(paymentMethod);

            paymentService.createPayment(payment);
            bookingService.updateStatus(bookingId, "completed");
            resp.sendRedirect(req.getContextPath() + "/payments");
        }
    }
}
