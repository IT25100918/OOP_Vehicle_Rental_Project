package com.vehiclerental.servlet;

import com.vehiclerental.model.Vehicle;
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
 * VehicleServlet - handles all /vehicles/* routes
 * GET  /vehicles          → list vehicles (with optional ?sortBy= and ?type=)
 * GET  /vehicles/add      → show add form (admin)
 * POST /vehicles/add      → save new vehicle (admin)
 * GET  /vehicles/edit/*   → show edit form (admin)
 * POST /vehicles/edit/*   → save edited vehicle (admin)
 * GET  /vehicles/delete/* → delete vehicle (admin)
 */
@WebServlet("/vehicles/*")
public class VehicleServlet extends HttpServlet {

    private VehicleService vehicleService;

    @Override
    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.vehicleService = ctx.getBean(VehicleService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();

        // Pass session attributes to JSP without depending on User model class
        req.setAttribute("userType", session.getAttribute("userType"));
        req.setAttribute("userName", session.getAttribute("userName"));

        if (pathInfo == null || pathInfo.equals("/")) {
            // GET /vehicles — public list
            String sortBy = req.getParameter("sortBy");
            String type   = req.getParameter("type");
            req.setAttribute("sortBy", sortBy == null ? "availability" : sortBy);

            if (type != null && !type.isEmpty()) {
                req.setAttribute("vehicles",   vehicleService.getByType(type));
                req.setAttribute("filterType", type);
            } else {
                req.setAttribute("vehicles", vehicleService.getAllVehicles(sortBy));
            }
            req.getRequestDispatcher("/WEB-INF/jsp/vehicles/list.jsp").forward(req, resp);

        } else if (pathInfo.equals("/add")) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            req.getRequestDispatcher("/WEB-INF/jsp/vehicles/add.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/edit/")) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            String id = pathInfo.substring("/edit/".length());
            Vehicle v = vehicleService.findById(id);
            if (v == null) { resp.sendRedirect(req.getContextPath() + "/vehicles"); return; }
            req.setAttribute("vehicle", v);
            req.getRequestDispatcher("/WEB-INF/jsp/vehicles/edit.jsp").forward(req, resp);

        } else if (pathInfo.startsWith("/delete/")) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
            String id = pathInfo.substring("/delete/".length());
            vehicleService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/vehicles");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();

        req.setAttribute("userType", session.getAttribute("userType"));
        req.setAttribute("userName", session.getAttribute("userName"));

        if ("/add".equals(pathInfo)) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

            String type        = req.getParameter("type");
            String brand       = req.getParameter("brand");
            String model       = req.getParameter("model");
            String plateNumber = req.getParameter("plateNumber");
            double rentPrice   = Double.parseDouble(req.getParameter("rentPrice"));
            String imageUrl    = req.getParameter("imageUrl");
            String location    = req.getParameter("location");

            Vehicle v = new Vehicle(null, type, brand, model, plateNumber, rentPrice, "available",
                    imageUrl == null || imageUrl.isEmpty()
                            ? "https://via.placeholder.com/400x250?text=Vehicle" : imageUrl);
            v.setLocation(location != null ? location : "");

            boolean ok = vehicleService.addVehicle(v);
            if (!ok) req.setAttribute("error", "Plate number already exists!");
            else     req.setAttribute("success", "Vehicle added successfully!");
            req.getRequestDispatcher("/WEB-INF/jsp/vehicles/add.jsp").forward(req, resp);

        } else if (pathInfo != null && pathInfo.startsWith("/edit/")) {
            if (!isAdmin(session)) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

            String id           = pathInfo.substring("/edit/".length());
            String type         = req.getParameter("type");
            String brand        = req.getParameter("brand");
            String vehicleModel = req.getParameter("vehicleModel");
            String plateNumber  = req.getParameter("plateNumber");
            double rentPrice    = Double.parseDouble(req.getParameter("rentPrice"));
            String availability = req.getParameter("availability");
            String imageUrl     = req.getParameter("imageUrl");
            String location     = req.getParameter("location");

            Vehicle v = new Vehicle(id, type, brand, vehicleModel, plateNumber,
                    rentPrice, availability, imageUrl);
            v.setLocation(location != null ? location : "");
            vehicleService.update(v);
            resp.sendRedirect(req.getContextPath() + "/vehicles");
        }
    }

    private boolean isAdmin(HttpSession session) {
        String userType = (String) session.getAttribute("userType");
        return "admin".equalsIgnoreCase(userType);
    }
}
