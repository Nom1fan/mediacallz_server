package mediacallz.com.server;

import com.google.gson.reflect.TypeToken;
import mediacallz.com.server.client.ConnectionToServer;
import mediacallz.com.server.exceptions.FileDoesNotExistException;
import mediacallz.com.server.exceptions.FileExceedsMaxSizeException;
import mediacallz.com.server.exceptions.FileInvalidFormatException;
import mediacallz.com.server.exceptions.FileMissingExtensionException;
import mediacallz.com.server.logs.LogFactory;
import mediacallz.com.server.model.*;
import mediacallz.com.server.model.response.Response;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UploadFileTests implements IServerProxy, ProgressListener {

    private final Logger logger = LogFactory.getLogger(UploadFileTests.class.getSimpleName());

    private static final String SAMPLE_FILEPATH = Paths.get("").toAbsolutePath().toString() + "\\sampleFiles\\sample.mp4";
    private static final String ROOT_URL = "http://localhost:8080";
    private static final String UPLOAD_URL = ROOT_URL + "/v1/UploadFile";

    private static final Type responseType = new TypeToken<Response<EventReport>>() {
    }.getType();

    @Test
    public void uploadCallerMedia() throws FileMissingExtensionException, FileInvalidFormatException, FileExceedsMaxSizeException, FileDoesNotExistException, IOException {

        String srcId = "0542258808";
        String destId = "0500000000";
        String destName = "Abubu";
        File fileForUpload = new File(SAMPLE_FILEPATH);
        FileManager fileManager = new FileManager(fileForUpload);
        long fileSize = fileManager.getFileSize();

        ProgressiveEntity progressiveEntity = prepareProgressiveEntity(srcId, destId, destName, fileForUpload, fileManager, fileSize);
        ConnectionToServer connToServer = new ConnectionToServer(this, responseType);
        connToServer.sendMultipartToServer(UPLOAD_URL, progressiveEntity);
    }

    @Override
    public void publishProgress(int percentage) {
        System.out.println("File upload progress:" + percentage + "%");
    }

    @Override
    public void handleMessageFromServer(Response msg, ConnectionToServer connectionToServer) {
        Response<EventReport> msgEventReport = (Response<EventReport>) msg;
        Assert.assertEquals(ClientActionType.TRIGGER_EVENT, msgEventReport.getActionType());
        Assert.assertEquals(EventType.UPLOAD_SUCCESS, msgEventReport.getResult().getStatus());
        connectionToServer.closeConnection();
    }

    @Override
    public void handleDisconnection(ConnectionToServer cts, String msg) {

    }

    private ProgressiveEntity prepareProgressiveEntity(String srcId, String destId, String destName, File fileForUpload, FileManager fileManager, long fileSize) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fb = new FileBody(fileForUpload);
        builder.addPart("fileForUpload", fb);
        prepareDataForUpload(builder, srcId, destId, destName, fileManager, SpecialMediaType.CALLER_MEDIA);
        HttpEntity httpEntity = builder.build();
        return new ProgressiveEntity(httpEntity, fileSize, this);
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
}


