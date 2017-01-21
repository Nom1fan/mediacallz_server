package com.mediacallz.server.logic;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbo.MediaCallDBO;
import com.mediacallz.server.database.dbo.MediaFileDBO;
import com.mediacallz.server.model.EventReport;
import com.mediacallz.server.model.EventType;
import com.mediacallz.server.model.dto.MediaCallDTO;
import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.model.request.InsertMediaCallRecordRequest;
import com.mediacallz.server.model.response.Response;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class InsertMediaCallRecordLogic extends AbstractServerLogic {

    private final Dao dao;

    private final MapperFacade mapperFacade;

    @Autowired
    public InsertMediaCallRecordLogic(Dao dao, MapperFacade mapperFacade) {
        this.dao = dao;
        this.mapperFacade = mapperFacade;
    }

    public Response execute(InsertMediaCallRecordRequest request, HttpServletResponse response) {
        int callId = -1;
        try {
            MediaCallDTO mediaCallDTO = request.getMediaCall();
            MediaCallDBO mediaCallDBO = mediaCallDTO.toInternal(mapperFacade);
            List<MediaFileDBO> mediaFileDBOS = prepareMediaFiles(mediaCallDTO.getVisualMediaFile(), mediaCallDTO.getAudioMediaFile());
            callId = dao.insertMediaCallRecord(mediaCallDBO, mediaFileDBOS);
            logger.info("Insert call record was successful. Call Id returned:[" + callId + "]");

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Insert call record failed. Exception:[" + e.getMessage() + "]", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return new Response<>(new EventReport(EventType.NO_ACTION_REQUIRED, callId));
    }

    private List<MediaFileDBO> prepareMediaFiles(MediaFileDTO visualMediaFileDTO, MediaFileDTO audioMediaFileDTO) {
        LinkedList<MediaFileDBO> mediaFileDBOS = new LinkedList<>();
        MediaFileDBO visualMediaFileDBO;
        MediaFileDBO audioMediaFileDBO;

        if (visualMediaFileDTO != null) {
            visualMediaFileDBO = visualMediaFileDTO.toInternal(mapperFacade);
            mediaFileDBOS.add(visualMediaFileDBO);
        }
        if (audioMediaFileDTO != null) {
            audioMediaFileDBO = audioMediaFileDTO.toInternal(mapperFacade);
            mediaFileDBOS.add(audioMediaFileDBO);
        }

        return mediaFileDBOS;
    }
}
