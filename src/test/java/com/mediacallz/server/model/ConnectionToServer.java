package com.mediacallz.server.model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.util.List;

import static java.util.AbstractMap.SimpleEntry;
@Component
public class ConnectionToServer {

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final String REQUEST_METHOD_POST = "POST";
    private static final String ENCODING = "UTF-8";
    private static final String delimiter = "--";
    private static final String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";

    private Gson gson;
    private HttpURLConnection conn;
    private OutputStream os;

    public ConnectionToServer() {
        gson = new Gson();
    }

    public void connectForMultipart(URL url) {
        try {
            conn = (HttpURLConnection) (url).openConnection();

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.connect();
        os = conn.getOutputStream();
        } catch (IOException e) {
            connectionException(e);
        }
    }

    public void finishMultipart() {
        try {
            os.write( (delimiter + boundary + delimiter + "\r\n").getBytes());
        } catch (IOException e) {
            connectionException(e);
        }
    }

    public void addFormPart(String paramName, String value) {

        try {
        os.write( (delimiter + boundary + "\r\n").getBytes());
        os.write( "Content-Type: text/plain\r\n".getBytes());
        os.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
        os.write( ("\r\n" + value + "\r\n").getBytes());
        } catch (IOException e) {
            connectionException(e);
        }
    }

    public void addFilePart(String paramName, String fileName, byte[] data) {
        try {
            os.write((delimiter + boundary + "\r\n").getBytes());
            os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
            os.write(("Content-Type: application/octet-stream\r\n").getBytes());
            os.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
            os.write("\r\n".getBytes());


            os.write(data);

            os.write("\r\n".getBytes());
        } catch(IOException e) {
            connectionException(e);
        }
    }

    public <RESPONSE> void sendToServer(URL url, List<SimpleEntry> params, TypeToken<MessageToClient<RESPONSE>> responseTypeToken) {

        try {
            sendRequest(url, params);
            readResponse(responseTypeToken);
        } catch (IOException e) {
            connectionException(e);
        } finally {
            if(conn!=null)
                conn.disconnect();
        }
    }

    public void sendToServer(URL url, List<SimpleEntry> params) {
        sendToServer(url, params, new TypeToken<MessageToClient<Void>>(){});
    }

    public void asyncSendToServer(URL url, List<SimpleEntry> params) {
        try {
            sendRequest(url, params);
        } catch (IOException e) {
            connectionException(e);
        }
    }

    public <RESPONSE> MessageToClient<RESPONSE> readResponse(TypeToken<MessageToClient<RESPONSE>> responseTypeToken) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String responseMessage = br.readLine();
        return extractResponse(responseMessage, responseTypeToken);
    }

    public MessageToClient<Void> readResponse() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String responseMessage = br.readLine();
        return extractResponse(responseMessage, new TypeToken<MessageToClient<Void>>(){});
    }

    public URLConnection openConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setRequestMethod(REQUEST_METHOD_POST);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }

    public void closeConnection() {
        conn.disconnect();
    }

    public HttpURLConnection getConnection() {
        return conn;
    }

    public void connectionException(Exception e) {

        String errMsg = "Connection error";
    }

    public boolean ping(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    private void sendRequest(URL url, List<SimpleEntry> params) throws IOException {
        conn = (HttpURLConnection) openConnection(url);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, ENCODING));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
        conn.connect();
    }

    private String getQuery(List<SimpleEntry> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (SimpleEntry pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey().toString(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }

    private <RESPONSE> MessageToClient<RESPONSE> extractResponse(String resJson, TypeToken<MessageToClient<RESPONSE>> resType) {
        return gson.fromJson(resJson, resType.getType());
    }
}
