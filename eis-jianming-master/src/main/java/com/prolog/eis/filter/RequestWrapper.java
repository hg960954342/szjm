package com.prolog.eis.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class RequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    /**
     * 这个必须加,复制request中的bufferedReader中的值
     *
     * @param request
     * @throws IOException
     */
    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is = request.getInputStream();
        byte[] bytes = new byte[1024 * 1024];
         			int nRead = 1;
         		int nTotalRead = 0;
      		while (nRead > 0) {
        			nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
        			if (nRead > 0)
         				nTotalRead = nTotalRead + nRead;
         		}

        body = new String(bytes, 0, nTotalRead, "utf-8").getBytes();

    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            public boolean isFinished() {
                return false;
            }

            public boolean isReady() {
                return false;
            }

            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

}

