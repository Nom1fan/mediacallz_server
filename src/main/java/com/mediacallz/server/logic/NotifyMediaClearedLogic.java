package com.mediacallz.server.logic;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.model.push.ClearSuccessData;
import com.mediacallz.server.model.request.NotifyMediaClearedRequest;
import com.mediacallz.server.services.PushSender;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class NotifyMediaClearedLogic extends AbstractServerLogic {

    private final Dao dao;

    private final PushSender pushSender;

    private final MapperFacade mapperFacade;

    @Autowired
    public NotifyMediaClearedLogic(PushSender pushSender, Dao dao, MapperFacade mapperFacade) {
        this.pushSender = pushSender;
        this.dao = dao;
        this.mapperFacade = mapperFacade;
    }

    public void execute(NotifyMediaClearedRequest request, HttpServletResponse response) {
        String clearerId = request.getMessageInitiaterId();
        String clearRequesterId = request.getSourceId();
        String clearerName = request.getDestinationContactName();
        SpecialMediaType specialMediaType = request.getSpecialMediaType();
        String sourceLocale = request.getSourceLocale();

        logger.info("Informing [Clear media requester]:" +
                clearRequesterId + " that [User]:" + clearerId +
                " cleared his media of [SpecialMediaType]: " + specialMediaType);

        try {

            String clearRequesterToken = dao.getUserRecord(clearRequesterId).getToken();
            LangStrings strings = this.stringsFactory.getStrings(sourceLocale);

            String title = strings.media_cleared_title();
            String msgBody = String.format(strings.media_cleared_body(), clearerName != null && !clearerName.equals("") ? clearerName : clearerId);
            ClearSuccessData clearSuccessData = mapperFacade.map(request, ClearSuccessData.class);
            clearSuccessData.setDestinationId(clearerId);
            boolean sent = pushSender.sendPush(clearRequesterToken, PushEventKeys.CLEAR_SUCCESS, title, msgBody, clearSuccessData);
            if (!sent) {
                logger.severe("Failed to inform [Clear media requester]:" +
                        clearRequesterId + "that [User]:" + clearerId +
                        " cleared his media of [SpecialMediaType]:" +
                        specialMediaType + ". Push not sent");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("Failed to inform [Clear media requester]:" +
                    clearRequesterId + " that [User]:" + clearerId +
                    " cleared his media of [SpecialMediaType]: " +
                    specialMediaType + ". Exception:[" + e.getMessage() + "]");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
