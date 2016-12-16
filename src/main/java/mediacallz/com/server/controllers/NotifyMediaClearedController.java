package mediacallz.com.server.controllers;

import mediacallz.com.server.database.Dao;
import mediacallz.com.server.lang.LangStrings;
import mediacallz.com.server.services.PushSender;
import mediacallz.com.server.utils.ServletRequestUtils;
import mediacallz.com.server.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Mor on 03/10/2016.
 */
@Controller
public class NotifyMediaClearedController extends AbstractController {

    @Autowired
    private ServletRequestUtils servletRequestUtils;

    @Autowired
    private Dao dao;

    @Autowired
    private PushSender pushSender;

    @ResponseBody
    @RequestMapping("/v1/NotifyMediaCleared")
    public MessageToClient notifyMediaCleared(HttpServletRequest request) {

        Map<DataKeys, Object> data = servletRequestUtils.extractParametersMap(request);
        String clearerId = (String) data.get(DataKeys.MESSAGE_INITIATER_ID);
        String clearRequesterId = (String) data.get(DataKeys.SOURCE_ID);
        String clearerName = (String) data.get(DataKeys.DESTINATION_CONTACT_NAME);
        SpecialMediaType specialMediaType = SpecialMediaType.valueOf(data.get(DataKeys.SPECIAL_MEDIA_TYPE).toString());
        String sourceLocale = (String) data.get(DataKeys.SOURCE_LOCALE);

        logger.info("Informing [Clear media requester]:" +
                clearRequesterId + " that [User]:" + clearerId +
                " cleared his media of [SpecialMediaType]: " + specialMediaType);

        try {

            String clearRequesterToken = dao.getUserRecord(clearRequesterId).getToken();
            LangStrings strings = this.stringsFactory.getStrings(sourceLocale);

            String title = strings.media_cleared_title();
            String msgBody = String.format(strings.media_cleared_body(), clearerName != null && !clearerName.equals("") ? clearerName : clearerId);
            boolean sent = pushSender.sendPush(clearRequesterToken, PushEventKeys.CLEAR_SUCCESS, title, msgBody, data);
            if (!sent) {
                logger.severe("Failed to inform [Clear media requester]:" +
                        clearRequesterId + "that [User]:" + clearerId +
                        " cleared his media of [SpecialMediaType]:" +
                        specialMediaType + ". Push not sent");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("Failed to inform [Clear media requester]:" +
                    clearRequesterId + " that [User]:" + clearerId +
                    " cleared his media of [SpecialMediaType]: " +
                    specialMediaType + ". Exception:[" + e.getMessage() + "]");
        }

        return new MessageToClient<>(ClientActionType.TRIGGER_EVENT, new EventReport(EventType.NO_ACTION_REQUIRED));
    }
}
