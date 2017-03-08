package com.mediacallz.server.logic;

import com.mediacallz.server.dao.Dao;
import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.model.push.PendingDownloadData;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.model.request.NotifyMediaReadyRequest;
import com.mediacallz.server.services.PushSender;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
@Slf4j
public class NotifyMediaReadyLogic extends AbstractServerLogic {

    private final Dao dao;

    private final PushSender pushSender;

    private final MapperFacade mapperFacade;

    private final UsersDao usersDao;


    @Autowired
    public NotifyMediaReadyLogic(PushSender pushSender, Dao dao, MapperFacade mapperFacade, UsersDao usersDao) {
        this.pushSender = pushSender;
        this.dao = dao;
        this.mapperFacade = mapperFacade;
        this.usersDao = usersDao;
    }

    public void execute(NotifyMediaReadyRequest request, HttpServletResponse response) {
        PendingDownloadData pendingDownloadData = mapperFacade.map(request, PendingDownloadData.class);
        informSrcOfSuccess(pendingDownloadData);

        // Marking in communication history record that the transfer was successful
        try {
            char TRUE = '1';
            dao.updateMediaTransferRecord(pendingDownloadData.getCommId(), Dao.COL_TRANSFER_SUCCESS, TRUE);
        } catch (SQLException e) {
            e.printStackTrace();
            log.debug("Failed to write transfer success from [Source]:" +
                    pendingDownloadData.getSourceId() + " to [Dest]:" +
                    pendingDownloadData.getDestinationId() + " for [commId]:" +
                    pendingDownloadData.getCommId());
        }
    }

    // Informing source (uploader) that file received by user (downloader)
    private void informSrcOfSuccess(PendingDownloadData pendingDownloadData) {
        String sourceId = pendingDownloadData.getSourceId();
        String destId = pendingDownloadData.getDestinationId();
        String destContactName = pendingDownloadData.getDestinationContactName();
        LangStrings strings = stringsFactory.getStrings(pendingDownloadData.getSourceLocale());
        String title = strings.media_ready_title();
        String msg = String.format(strings.media_ready_body(), !destContactName.equals("") ? destContactName : destId);
        String token = usersDao.getUserRecord(sourceId).getToken();
        boolean sent = pushSender.sendPush(token, PushEventKeys.TRANSFER_SUCCESS, title, msg, pendingDownloadData);
        if (!sent) {
            log.debug("Failed to inform user " + sourceId + " of transfer success to user: " + destId);
        }
    }


}
