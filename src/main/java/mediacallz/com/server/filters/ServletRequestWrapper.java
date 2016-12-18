package mediacallz.com.server.filters;

/**
 * Created by Mor on 23/09/2016.
 */

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which is used to wrap a request in order that the wrapped request's input stream can be
 * read once and later be read again in a pseudo fashion by virtue of keeping the original payload
 * as a string which is actually what is returned by subsequent calls to getInputStream().
 */
public class ServletRequestWrapper
        extends HttpServletRequestWrapper {

    private static Logger logger = Logger.getLogger(ServletRequestWrapper.class.getName());

    private String jsonPayload;

    public ServletRequestWrapper(HttpServletRequest request)
            throws ServletException {

        super(request);
        if(request.getContentType().contains("multipart"))
            readJsonPayloadFromMultiPartHttpServletRequest(request);
        else
            readJsonPayloadFromHttpServletRequest(request);
    }

    private void readJsonPayloadFromMultiPartHttpServletRequest(HttpServletRequest request) throws ServletException {

        // read the original payload into the jsonPayload variable
        try {
            // read the payload into the StringBuilder
            InputStream inputStream = request.getPart("jsonPart").getInputStream();
            jsonPayload = readFromInputStream(inputStream);
        } catch (IOException | ServletException | NullPointerException ex) {
            logger.log(Level.SEVERE, "Error reading the request payload", ex);
            throw new ServletException(ServletRequestWrapper.class.getSimpleName() + ": Error reading the request payload", ex);
        }
    }

    private void readJsonPayloadFromHttpServletRequest(HttpServletRequest request) throws ServletException {
        // read the original payload into the jsonPayload variable
        try {
            // read the payload into the StringBuilder
            InputStream inputStream = request.getInputStream();
            jsonPayload = readFromInputStream(inputStream);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error reading the request payload", ex);
            throw new ServletException(ServletRequestWrapper.class.getSimpleName() + ": Error reading the request payload");
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                // make an empty string since there is no payload
                stringBuilder.append("");
            }
        } finally {
            if(bufferedReader!=null) {
                bufferedReader.close();
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Override of the getInputStream() method which returns an InputStream that reads from the
     * stored Json payload string instead of from the request's actual InputStream.
     */
    @Override
    public ServletInputStream getInputStream()
            throws IOException {

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonPayload.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read()
                    throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    public String getJsonPayload() {
        return jsonPayload;
    }

}
