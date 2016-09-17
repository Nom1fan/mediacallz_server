package com.mediacallz.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mediacallz.server.exceptions.FileDoesNotExistException;
import com.mediacallz.server.exceptions.FileExceedsMaxSizeException;
import com.mediacallz.server.exceptions.FileInvalidFormatException;
import com.mediacallz.server.exceptions.FileMissingExtensionException;
import com.mediacallz.server.model.*;
import logs.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersAPIsTests {

    @Autowired
    ConnectionToServer connectionToServer;

    private final Logger logger = LogFactory.getLogger("uploadCallerMedia");

    private static final String SAMPLE_FILEPATH = Paths.get("").toAbsolutePath().toString() + "\\sampleFiles\\sample.mp4";
    private static final String ROOT_URL = "http://localhost:8080";
    private static final String REGISTER_URL = ROOT_URL + "/v1/Register";
    private static final String UPLOAD_URL = ROOT_URL + "/v1/UploadFile";

    private static final Type responseType = new TypeToken<MessageToClient<EventReport>>() {
    }.getType();

    private long fileSize;

    @Test
    public void uploadCallerMedia() throws FileMissingExtensionException, FileInvalidFormatException, FileExceedsMaxSizeException, FileDoesNotExistException, IOException {

        String srcId = "0542258808";
        String destId = "0500000000";
        String destName = "Abubu";
        File fileForUpload = new File(SAMPLE_FILEPATH);
        FileManager fileManager = new FileManager(fileForUpload);
        fileSize = fileManager.getFileSize();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(UPLOAD_URL);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        FileBody fb = new FileBody(fileForUpload);

        builder.addPart("fileForUpload", fb);
        prepareDataForUpload(builder, srcId, destId, destName, fileManager, SpecialMediaType.CALLER_MEDIA);

        final HttpEntity httpEntity = builder.build();
        ProgressiveEntity progressiveEntity = new ProgressiveEntity(httpEntity);
        post.setEntity(progressiveEntity);
        HttpResponse response = client.execute(post);

        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String responseMessage = br.readLine();
        MessageToClient<EventReport> reportMessageToClient = extractResponse(responseMessage, responseType);
        System.out.println(reportMessageToClient);

        Assert.assertEquals(ClientActionType.TRIGGER_EVENT, reportMessageToClient.getActionType());
        Assert.assertEquals(EventType.UPLOAD_SUCCESS, reportMessageToClient.getResult().getStatus());
    }

    private void publishProgress(int percentage) {
        System.out.println("File upload progress:" + percentage + "%");
    }

    private void prepareDataForUpload(MultipartEntityBuilder builder, String srcId, String destId, String destName, FileManager fileForUpload, SpecialMediaType specialMediaType) {
        String appVersion = "1.43";

        builder.addTextBody(DataKeys.MESSAGE_INITIATER_ID.toString(), srcId);
        builder.addTextBody(DataKeys.APP_VERSION.toString(), appVersion);
        builder.addTextBody(DataKeys.SOURCE_ID.toString(), srcId);
        builder.addTextBody(DataKeys.SOURCE_LOCALE.toString(), "en");
        builder.addTextBody(DataKeys.DESTINATION_ID.toString(), destId);
        builder.addTextBody(DataKeys.DESTINATION_CONTACT_NAME.toString(), destName);
        builder.addTextBody(DataKeys.MD5.toString(), fileForUpload.getMd5());
        builder.addTextBody(DataKeys.EXTENSION.toString(), fileForUpload.getFileExtension());
        builder.addTextBody(DataKeys.FILE_PATH_ON_SRC_SD.toString(), fileForUpload.getFile().getAbsolutePath());
        builder.addTextBody(DataKeys.FILE_SIZE.toString(), String.valueOf(fileForUpload.getFileSize()));
        builder.addTextBody(DataKeys.FILE_TYPE.toString(), fileForUpload.getFileType().toString());
        builder.addTextBody(DataKeys.SPECIAL_MEDIA_TYPE.toString(), specialMediaType.toString());
        builder.addTextBody(DataKeys.SOURCE_WITH_EXTENSION.toString(), srcId + "." + fileForUpload.getFileExtension());
    }

    private MessageToClient extractResponse(String resJson, Type type) {
        return new Gson().fromJson(resJson, type);
    }

    class ProgressiveEntity implements HttpEntity {
        private HttpEntity httpEntity;

        public ProgressiveEntity(HttpEntity httpEntity) {
            this.httpEntity = httpEntity;
        }

        @Override
        public void consumeContent() throws IOException {
            httpEntity.consumeContent();
        }

        @Override
        public InputStream getContent() throws IOException,
                IllegalStateException {
            return httpEntity.getContent();
        }

        @Override
        public Header getContentEncoding() {
            return httpEntity.getContentEncoding();
        }

        @Override
        public long getContentLength() {
            return httpEntity.getContentLength();
        }

        @Override
        public Header getContentType() {
            return httpEntity.getContentType();
        }

        @Override
        public boolean isChunked() {
            return httpEntity.isChunked();
        }

        @Override
        public boolean isRepeatable() {
            return httpEntity.isRepeatable();
        }

        @Override
        public boolean isStreaming() {
            return httpEntity.isStreaming();
        } // CONSIDER put a _real_ delegator into here!

        @Override
        public void writeTo(OutputStream outstream) throws IOException {

            class ProxyOutputStream extends FilterOutputStream {
                /**
                 * @author Stephen Colebourne
                 */

                public ProxyOutputStream(OutputStream proxy) {
                    super(proxy);
                }

                public void write(int idx) throws IOException {
                    out.write(idx);
                }

                public void write(byte[] bts) throws IOException {
                    out.write(bts);
                }

                public void write(byte[] bts, int st, int end) throws IOException {
                    out.write(bts, st, end);
                }

                public void flush() throws IOException {
                    out.flush();
                }

                public void close() throws IOException {
                    out.close();
                }
            } // CONSIDER import this class (and risk more Jar File Hell)

            class ProgressiveOutputStream extends ProxyOutputStream {
                private long totalSent;
                private int percentage;
                private int lastPercentagePublished;

                private ProgressiveOutputStream(OutputStream proxy) {
                    super(proxy);
                    totalSent = 0;
                }

                public void write(byte[] bts, int st, int end) throws IOException {

                    totalSent += end;
                    percentage = ((int) ((totalSent / (float) fileSize) * 100));
                    if (percentage - lastPercentagePublished > 0) {
                        publishProgress(percentage);
                        lastPercentagePublished = percentage;
                    }
                    out.write(bts, st, end);
                }
            }

            httpEntity.writeTo(new ProgressiveOutputStream(outstream));
        }

    }
}


