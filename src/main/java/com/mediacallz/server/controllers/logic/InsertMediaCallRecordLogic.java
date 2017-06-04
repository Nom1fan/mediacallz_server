package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.Dao;
import com.mediacallz.server.db.dbo.MediaCallDBO;
import com.mediacallz.server.db.dbo.MediaFileDBO;
import com.mediacallz.server.model.dto.MediaCallDTO;
import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.model.request.InsertMediaCallRecordRequest;
import com.mediacallz.server.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
@Slf4j
public class InsertMediaCallRecordLogic extends AbstractServerLogic {

    private final Dao dao;

    private final MapperFacade mapperFacade;

    @Autowired
    public InsertMediaCallRecordLogic(Dao dao, MapperFacade mapperFacade) {
        this.dao = dao;
        this.mapperFacade = mapperFacade;
    }

    public Response<Integer> execute(InsertMediaCallRecordRequest request, HttpServletResponse response) {
        int callId = -1;
        MediaCallDTO mediaCallDTO = null;
        try {
            mediaCallDTO = request.getMediaCall();
            MediaCallDBO mediaCallDBO = mediaCallDTO.toInternal(mapperFacade);
            mediaCallDBO.setDatetime(new Date());
            List<MediaFileDBO> mediaFileDBOS = prepareMediaFiles(mediaCallDTO.getVisualMediaFile(), mediaCallDTO.getAudioMediaFile());
            callId = dao.insertMediaCallRecord(mediaCallDBO, mediaFileDBOS);
            log.info("Insert call record was successful. Call Id returned:[" + callId + "]. " + mediaCallDTO);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Insert call record failed. Exception:[" + e.getMessage() + "] " + mediaCallDTO , e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return new Response<>(callId);
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
