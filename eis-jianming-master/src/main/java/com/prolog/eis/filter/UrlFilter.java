package com.prolog.eis.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class UrlFilter implements Filter {

    static Logger logger = LoggerFactory.getLogger(UrlFilter.class);

    ServletContext context;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) request;
        String path = r.getQueryString();
        if (path == null) {
            Map<String, String> map = new HashMap<String, String>();
            Enumeration headerNames = ((HttpServletRequest) request).getHeaderNames();
            while (headerNames.hasMoreElements()) {//循环遍历Header中的参数，把遍历出来的参数放入Map中
                String key = (String) headerNames.nextElement();
                String value = ((HttpServletRequest) request).getHeader(key);
                map.put(key, value);
            }
            System.out.println(map.toString());
            path = map.toString();
        }
        String url = r.getRequestURI();
        RequestWrapper requestWrapper = null;
        String repalceUrl = url.replaceAll("/", "");
        if (null != repalceUrl) {
            repalceUrl = repalceUrl.trim();
        } else {
            return;
        }
        StringBuffer buffer=new StringBuffer("\n-----------------  url:" + url + " & queryString:" + path);

        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

        if (request instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) request);
            try {
               // Map map = request.getParameterMap();
                BufferedReader bufferedReader = requestWrapper.getReader();
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                buffer.append("\nparams:" + sb.toString());
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("p2ps doFilter :", e);
            }

        }
        if (null == requestWrapper) {
            filter.doFilter(request, response);
        } else {
            filter.doFilter(requestWrapper, responseWrapper);
        }

        String result = new String(responseWrapper.getResponseData());

        response.setContentLength(-1);//解决可能在运行的过程中页面只输出一部分
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(result);
        out.flush();
        out.close();
        buffer.append("\n return data:" + result);
        buffer.append("\n end url:" + url + " httpstatus:" + ((HttpServletResponse) response).getStatus() + "");
        logger.info(buffer.toString());
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        context = filterConfig.getServletContext();
    }

}
