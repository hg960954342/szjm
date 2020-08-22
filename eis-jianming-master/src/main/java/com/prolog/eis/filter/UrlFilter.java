package com.prolog.eis.filter;

import com.prolog.eis.logs.LogServices;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class UrlFilter extends OncePerRequestFilter {



    ServletContext context;

    public void destroy() {
    }




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {        request = new RequestWrapper(request);
        response = new ResponseWrapper(response);
        filterChain.doFilter(request, response);
        String requestparmas=printRequestLog(request);
        String result=printResponseLog((ResponseWrapper) response);
        LogServices.logEis(request.getRequestURI().toString(),requestparmas,"",result);
    }

    private String printRequestLog(final HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        if (request instanceof RequestWrapper && !isMultipart(request) && !isBinaryContent(request)) {
            RequestWrapper requestWrapper = (RequestWrapper) request;
            msg.append(requestWrapper.getRequestBodyString(request));
        }

        return msg.toString();
    }

    private boolean isBinaryContent(final HttpServletRequest request) {
        if (request.getContentType() == null) {
            return false;
        }
        return request.getContentType().startsWith("image")
                || request.getContentType().startsWith("video")
                || request.getContentType().startsWith("audio");
    }

    private boolean isMultipart(final HttpServletRequest request) {
        return request.getContentType() != null
                && request.getContentType().startsWith("multipart/form-data");
    }

    private String printResponseLog(final ResponseWrapper response) {
        StringBuilder msg = new StringBuilder();
        try {
            msg.append(new String(response.toByteArray(), response.getCharacterEncoding()));
        } catch (UnsupportedEncodingException e) {
            LogServices.logSys(e.getMessage());
        }

        return msg.toString();
    }
}

