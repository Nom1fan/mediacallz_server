package mediacallz.com.server.controllers;

import mediacallz.com.server.database.Dao;
import mediacallz.com.server.database.dbos.AppMetaDBO;
import mediacallz.com.server.model.ClientActionType;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.MessageToClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Mor on 19/08/2016.
 */
@Controller
public class GetAppMetaController extends AbstractController {

    @Autowired
    private Dao dao;

    private HttpServletResponse response;

    @ResponseBody
    @RequestMapping(value = "/v1/GetAppMeta", method = RequestMethod.POST)
    public MessageToClient getAppMeta(HttpServletResponse response) throws IOException {

        this.response = response;
        HashMap<DataKeys,Object> replyData = new HashMap<>();
        try {
            AppMetaDBO appMetaDBO = dao.getAppMetaRecord();

            replyData.put(DataKeys.MIN_SUPPORTED_VERSION, appMetaDBO.getLast_supported_version());
           return new MessageToClient<>(ClientActionType.GET_APP_RECORD_RES, replyData);
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = "Failed to retrieve app meta from DB. " + (e.getMessage()!=null ? "Exception:" + e.getMessage() : "");
            logger.severe(errMsg);
            replyData.put(DataKeys.ERR_MSG, errMsg);
            return new MessageToClient<>(ClientActionType.GET_APP_RECORD_RES, replyData);
        }
    }
}
