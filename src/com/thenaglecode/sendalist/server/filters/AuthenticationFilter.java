package com.thenaglecode.sendalist.server.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 27/07/12
 * Time: 10:35 AM
 */
public class AuthenticationFilter implements Filter {
    private FilterConfig filterConfig;
    private ArrayList<String> urlList = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        urlList = new ArrayList<String>();
        String urls = filterConfig.getInitParameter("avoid-urls");
        if(urls != null){
            StringTokenizer token = new StringTokenizer(urls, ",");

            while (token.hasMoreTokens()){
                urlList.add(token.nextToken());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        if (req.isSecure()) {
            System.out.println("this request was made using HTTPS");
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String url = request.getServletPath();
        boolean allowedRequest = false;

        if(urlList.contains(url)) {
            allowedRequest = true;
        }

        if (!allowedRequest) {
            HttpSession session = request.getSession(false);
            if (null == session) {
                response.sendRedirect("/login.jsp");
            }
        }

        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
