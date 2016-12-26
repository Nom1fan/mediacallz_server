package com.mediacallz.server.controllers;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.lang.LangStrings;
import com.mediacallz.server.model.*;
import com.mediacallz.server.model.response.Response;
import com.mediacallz.server.services.PushSender;
import com.mediacallz.server.utils.RequestUtils;
import com.mediacallz.server.model.request.NotifyMediaClearedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mor on 03/10/2016.
 */
@Controller
public class NotifyMediaClearedController extends AbstractController {

    private final Dao dao;

    private final PushSender pushSender;

    @Autowired
    public NotifyMediaClearedController(PushSender pushSender, Dao dao) {
        this.pushSender = pushSender;
        this.dao = dao;
    }

    @ResponseBody
    @RequestMapping("/v1/NotifyMediaCleared")
    public void notifyMediaCleared(@RequestBody NotifyMediaClearedRequest request, HttpServletResponse response) {

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
            boolean sent = pushSender.sendPush(clearRequesterToken, PushEventKeys.CLEAR_SUCCESS, title, msgBody, convertRequest2Map(request));
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

    private Map<DataKeys, Object> convertRequest2Map(NotifyMediaClearedRequest request) {
        Map<DataKeys,Object> map = new HashMap<>();
        map.put(DataKeys.MESSAGE_INITIATER_ID, request.getMessageInitiaterId());
        map.put(DataKeys.DESTINATION_CONTACT_NAME, request.getDestinationContactName());
        map.put(DataKeys.SPECIAL_MEDIA_TYPE, request.getSpecialMediaType());
        map.put(DataKeys.SOURCE_LOCALE, request.getSourceLocale());
        map.put(DataKeys.SOURCE_ID, request.getSourceId());
        return map;
    }
}
