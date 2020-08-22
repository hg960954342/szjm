package com.prolog.eis.filter;

import org.bouncycastle.util.io.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class ResponseWrapper extends HttpServletResponseWrapper{


        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        private PrintWriter writer = new PrintWriter(bos);

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletResponse getResponse() {
            return this;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener listener) {

                }

                private TeeOutputStream tee = new TeeOutputStream(ResponseWrapper.super.getOutputStream(), bos);

                @Override
                public void write(int b) throws IOException {
                    tee.write(b);
                }
            };
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new TeePrintWriter(super.getWriter(), writer);
        }

        public byte[] toByteArray() {
            return bos.toByteArray();
        }
}