package com.thenaglecode.sendalist.server.filters;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 25/07/12
 * Time: 10:43 PM
 */
public class HitCountFilter implements Filter{

    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        if(filterConfig.getServletContext().getAttribute("hitCounter") == null){
            filterConfig.getServletContext().setAttribute("hitCounter", new Counter());
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

      if (filterConfig == null)
         return;
      StringWriter sw = new StringWriter();
      PrintWriter writer = new PrintWriter(sw);
      Counter counter = (Counter)filterConfig.
         getServletContext().
         getAttribute("hitCounter");
      writer.println();
      writer.println("===============");
      writer.println("The number of hits is: " +
         counter.incCounter());
      writer.println("===============");

      // Log the resulting string
      writer.flush();
      filterConfig.getServletContext().
         log(sw.getBuffer().toString());
      filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
