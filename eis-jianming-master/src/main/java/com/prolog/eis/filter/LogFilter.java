package com.prolog.eis.filter;

import com.prolog.framework.authority.core.interceptor.RestTemplateInterceptor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Slf4j
public class LogFilter extends RestTemplateInterceptor implements Filter  {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)	throws IOException {
        HttpServletRequest requestx= (HttpServletRequest)request ;
        String requestURL=requestx.getRequestURL().toString();
        long start = System.currentTimeMillis();
        Set<String> set=requestx.getParameterMap().keySet();
        StringBuffer buf =new StringBuffer();
        for(String str:set){
            buf.append(str).append("=").append(Arrays.toString(requestx.getParameterMap().get(str))).append("|");
        }
        log.info(requestURL+"   intercept Execute cost="+(System.currentTimeMillis()-start)+"  "+buf.toString());
        return super.intercept(request, body, execution);

    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest)servletRequest ;
        String requestURL=request.getRequestURL().toString();
        long start = System.currentTimeMillis();
        Set<String> set=request.getParameterMap().keySet();
        StringBuffer buf =new StringBuffer();
        for(String str:set){
            buf.append(str).append("=").append(Arrays.toString(request.getParameterMap().get(str))).append("|");
        }
        log.info(requestURL+"   doFilter Execute cost="+(System.currentTimeMillis()-start)+"  "+buf.toString());
        filterChain.doFilter(servletRequest,servletResponse);


    }

    @Override
    public void destroy() {

    }
}
