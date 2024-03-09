package ru.kuznetsov.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Locale;

@WebFilter(filterName = "RegionFilter", value = "/ru")
@Slf4j
public class RegionFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
        log.info(">>> My WebFilter is alive <<<");
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        Locale locale = request.getLocale();
        String country = locale.getCountry();
        log.info(locale.getLanguage());
        log.info(country);

        if (!country.equals("RU")){
           HttpServletResponse httpResponse = (HttpServletResponse) response;
           httpResponse.sendRedirect("http://yandex.ru");
        }

        chain.doFilter(request, response);
    }
}
