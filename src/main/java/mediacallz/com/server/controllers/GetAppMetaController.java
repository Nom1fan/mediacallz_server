package mediacallz.com.server.controllers;

import mediacallz.com.server.database.Dao;
import mediacallz.com.server.database.dbos.AppMetaDBO;
import mediacallz.com.server.model.ClientActionType;
import mediacallz.com.server.model.DataKeys;
import mediacallz.com.server.model.response.Response;
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

    @ResponseBody
    @RequestMapping(value = "/v1/GetAppMeta", method = RequestMethod.POST)
    public Response getAppMeta(HttpServletResponse response) throws IOException {

        HashMap<DataKeys,Object> replyData = new HashMap<>();
        try {
            AppMetaDBO appMetaDBO = dao.getAppMetaRecord();

            replyData.put(DataKeys.MIN_SUPPORTED_VERSION, appMetaDBO.getLast_supported_version());
           return new Response<>(ClientActionType.GET_APP_RECORD_RES, replyData);
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errMsg = "Failed to retrieve app meta from DB. " + (e.getMessage()!=null ? "Exception:" + e.getMessage() : "");
            logger.severe(errMsg);
            replyData.put(DataKeys.ERR_MSG, errMsg);
            return new Response<>(ClientActionType.GET_APP_RECORD_RES, replyData);
        }
    }
}
