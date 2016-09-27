package com.mediacallz.server.controllers;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbos.AppMetaDBO;
import com.mediacallz.server.model.ClientActionType;
import com.mediacallz.server.model.DataKeys;
import com.mediacallz.server.model.MessageToClient;
import com.mediacallz.server.model.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public MessageToClient getAppMeta() throws IOException {

        HashMap<DataKeys,Object> replyData = new HashMap<>();
        try {
            AppMetaDBO appMetaDBO = dao.getAppMetaRecord();

            replyData.put(DataKeys.MIN_SUPPORTED_VERSION, appMetaDBO.getLast_supported_version());
           return new MessageToClient<>(ClientActionType.GET_APP_RECORD_RES, replyData);
        } catch(Exception e) {
            replyData.put(DataKeys.RESPONSE_CODE, ResponseCodes.INTERNAL_SERVER_ERR);
            String errMsg = "Failed to retrieve app meta from DB. " + (e.getMessage()!=null ? "Exception:" + e.getMessage() : "");
            replyData.put(DataKeys.ERR_MSG, errMsg);
            return new MessageToClient<>(ClientActionType.GENERIC_ERROR, replyData);
        }
    }
}
