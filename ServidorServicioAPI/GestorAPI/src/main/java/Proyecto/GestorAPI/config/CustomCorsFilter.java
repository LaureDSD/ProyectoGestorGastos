package Proyecto.GestorAPI.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom filter to add specific CORS headers to HTTP responses.
 *
 * This filter sets headers to allow certain HTTP methods and headers
 * for cross-origin requests. It complements the CORS configuration
 * to ensure that responses include these headers.
 *
 * Annotated with @Component so Spring automatically registers it as a filter bean.
 */
@Component
public class CustomCorsFilter implements Filter {

    /**
     * Filters each HTTP request/response to add CORS headers before continuing the filter chain.
     *
     * @param request  the ServletRequest object contains the client's request
     * @param response the ServletResponse object contains the response to the client
     * @param chain    the FilterChain to pass control to the next filter
     *
     * @throws IOException      if an input or output error occurs during filtering
     * @throws ServletException if the request cannot be handled
     *
     * This method sets the following headers on the response:
     * - "Access-Control-Allow-Methods" to specify allowed HTTP methods: GET, POST, PUT, DELETE, OPTIONS
     * - "Access-Control-Allow-Headers" to specify allowed request headers: Authorization, Content-Type
     *
     * Then it continues the filter chain by calling chain.doFilter().
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        chain.doFilter(request, response);
    }
}
