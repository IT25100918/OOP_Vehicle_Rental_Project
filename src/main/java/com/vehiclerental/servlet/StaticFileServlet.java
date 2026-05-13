package com.vehiclerental.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * StaticFileServlet — serves CSS, images, JS and other static files
 * from /src/main/webapp/ directory.
 *
 * Required because Spring MVC is excluded (to avoid intercepting
 * our @WebServlet routes), so Tomcat's default servlet needs help.
 */
@WebServlet(urlPatterns = {"/css/*", "/js/*", "/images/*", "/logo.png", "/favicon.ico"})
public class StaticFileServlet extends HttpServlet {

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("css",  "text/css");
        MIME_TYPES.put("js",   "application/javascript");
        MIME_TYPES.put("png",  "image/png");
        MIME_TYPES.put("jpg",  "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("gif",  "image/gif");
        MIME_TYPES.put("svg",  "image/svg+xml");
        MIME_TYPES.put("ico",  "image/x-icon");
        MIME_TYPES.put("woff", "font/woff");
        MIME_TYPES.put("woff2","font/woff2");
        MIME_TYPES.put("ttf",  "font/ttf");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Build the resource path inside webapp
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String resourcePath = uri.substring(contextPath.length()); // e.g. /css/style.css or /logo.png

        // Detect MIME type from extension
        String ext = "";
        int dot = resourcePath.lastIndexOf('.');
        if (dot >= 0) ext = resourcePath.substring(dot + 1).toLowerCase();
        String mimeType = MIME_TYPES.getOrDefault(ext, "application/octet-stream");

        // Load from servlet context (webapp root)
        InputStream is = getServletContext().getResourceAsStream(resourcePath);
        if (is == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType(mimeType);
        // Cache static files for 1 hour
        resp.setHeader("Cache-Control", "public, max-age=3600");

        try (OutputStream os = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } finally {
            is.close();
        }
    }
}
