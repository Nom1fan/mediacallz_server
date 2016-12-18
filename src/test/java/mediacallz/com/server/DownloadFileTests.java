package mediacallz.com.server;

import com.google.gson.reflect.TypeToken;
import mediacallz.com.server.client.ConnectionToServer;
import mediacallz.com.server.exceptions.FileDoesNotExistException;
import mediacallz.com.server.exceptions.FileExceedsMaxSizeException;
import mediacallz.com.server.exceptions.FileInvalidFormatException;
import mediacallz.com.server.exceptions.FileMissingExtensionException;
import mediacallz.com.server.logs.LogFactory;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.IServerProxy;
import mediacallz.com.server.model.response.Response;
import mediacallz.com.server.model.SpecialMediaType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.AbstractMap.SimpleEntry;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DownloadFileTests implements IServerProxy {

    private final Logger logger = LogFactory.getLogger(DownloadFileTests.class.getSimpleName());

    private static final String FILE_PATH_ON_SERVER = Paths.get("").toAbsolutePath().toString() + "\\sampleFiles\\sample.mp4";
    private static final String ROOT_URL = "http://localhost:8080";
    private static final String DOWNLOAD_URL = ROOT_URL + "/v1/DownloadFile";

    private static final Type responseType = new TypeToken<Response<Map<DataKeys,Object>>>() {
    }.getType();

    @Test
    public void downloadCallerMedia() throws FileMissingExtensionException, FileInvalidFormatException, FileExceedsMaxSizeException, FileDoesNotExistException, IOException {

        String myId = "0542258808";
        String srcId = "0500000000";
        String destName = "Abubu";
        String commId = "1234";
        SpecialMediaType specialMediaType = SpecialMediaType.CALLER_MEDIA;
        List<SimpleEntry> data = prepareDataForDownload(myId, srcId, destName, commId, specialMediaType);
        SimpleEntry fileSizeEntry = getSimpleEntryFromList(DataKeys.FILE_SIZE, data);
        ConnectionToServer connToServer = new ConnectionToServer(this, responseType);
        connToServer.download(DOWNLOAD_URL, "/Downloaded/", "downloaded.mp4", (long)fileSizeEntry.getValue() ,data);
    }

    @Override
    public void handleMessageFromServer(Response msg, ConnectionToServer connectionToServer) {

    }

    @Override
    public void handleDisconnection(ConnectionToServer cts, String msg) {

    }

    private List<SimpleEntry> prepareDataForDownload(String messageInitiaterId, String srcId, String destName, String commId, SpecialMediaType specialMediaType) {
        List<SimpleEntry> data = new ArrayList<>();
        String appVersion = "1.43";

        data.add(new SimpleEntry<>(DataKeys.MESSAGE_INITIATER_ID.toString(), messageInitiaterId));
        data.add(new SimpleEntry<>(DataKeys.COMM_ID.toString(), commId));
        data.add(new SimpleEntry<>(DataKeys.APP_VERSION.toString(), appVersion));
        data.add(new SimpleEntry<>(DataKeys.SOURCE_ID.toString(), srcId));
        data.add(new SimpleEntry<>(DataKeys.SOURCE_LOCALE.toString(), "en"));
        data.add(new SimpleEntry<>(DataKeys.DESTINATION_ID.toString(), messageInitiaterId));
        data.add(new SimpleEntry<>(DataKeys.DESTINATION_CONTACT_NAME.toString(), destName));
        data.add(new SimpleEntry<>(DataKeys.FILE_PATH_ON_SERVER, FILE_PATH_ON_SERVER));
        data.add(new SimpleEntry<>(DataKeys.SOURCE_WITH_EXTENSION, srcId+".mp4"));
        data.add(new SimpleEntry<>(DataKeys.SPECIAL_MEDIA_TYPE, specialMediaType));
        data.add(new SimpleEntry<>(DataKeys.FILE_SIZE, new File(FILE_PATH_ON_SERVER).length()));

        return data;
    }

    private <T> SimpleEntry getSimpleEntryFromList(T key, List<SimpleEntry> simpleEntryList) {
        SimpleEntry result = null;
        for (SimpleEntry simpleEntry : simpleEntryList) {
            if(simpleEntry.getKey().equals(key)) {
                result = simpleEntry;
                break;
            }
        }
        return result;
    }
}


