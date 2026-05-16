package com.vehiclerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * SE1020 OOP Project - Vehicle Rental Platform
 *
 * Uses pure Jakarta Servlet (@WebServlet) + JSP/JSTL for backend.
 * Spring Boot is used only for:
 *   - Embedded Tomcat (so we don't need a separate server)
 *   - Dependency injection for Service classes
 *   - @ServletComponentScan to register @WebServlet classes
 *
 * Spring MVC DispatcherServlet is deliberately excluded so that
 * our @WebServlet mappings handle ALL HTTP requests directly.
 */
@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class})
@ServletComponentScan
public class VehicleRentalApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehicleRentalApplication.class, args);
    }
}

@Component
class BrowserLauncher {
    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        String url = "http://localhost:8080";
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", url});
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", url});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
            }
            System.out.println("✅ App running at: " + url);
        } catch (Exception e) {
            System.out.println("⚠ Visit: " + url);
        }
    }
}
