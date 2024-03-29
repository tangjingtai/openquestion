package com.jt.openquestion.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 访问任何页面都加上session
        ((HttpServletRequest)request).getSession(true);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
