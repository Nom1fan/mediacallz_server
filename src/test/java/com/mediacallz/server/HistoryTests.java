package com.mediacallz.server;

import com.google.gson.Gson;
import com.mediacallz.server.model.CallRecord;
import com.mediacallz.server.model.MediaFile;
import com.mediacallz.server.model.SpecialMediaType;
import com.mediacallz.server.utils.MediaFilesUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Mor on 03/10/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HistoryTests {

    //Tested with postman
    @Test
    public void prepareCallRecordJson() {

        CallRecord callRecord = new CallRecord();
        callRecord.setSourceId("0542258808");
        callRecord.setDestinationId("0500000000");
        callRecord.setSpecialMediaType(SpecialMediaType.CALLER_MEDIA);

        MediaFile mediaFile = MediaFilesUtils.createMediaFile(new File(Paths.get("").toAbsolutePath().toString() + "\\sampleFiles\\sample.mp4"));
        callRecord.setVisualMd5(mediaFile.getMd5());
        callRecord.setVisualMediaFile(mediaFile);

        System.out.println(new Gson().toJson(callRecord));
    }
}
