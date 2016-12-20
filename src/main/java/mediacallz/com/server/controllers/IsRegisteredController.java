package mediacallz.com.server.controllers;

import ma.glasnost.orika.MapperFacade;
import mediacallz.com.server.database.Dao;
import mediacallz.com.server.model.ClientActionType;
import mediacallz.com.server.model.dto.UserDTO;
import mediacallz.com.server.model.request.IsRegisteredRequest;
import mediacallz.com.server.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Mor on 24/08/2016.
 */
@Controller
public class IsRegisteredController extends AbstractController {

    private final Dao dao;

    private final MapperFacade mapperFacade;

    @Autowired
    public IsRegisteredController(Dao dao, MapperFacade mapperFacade) {
        this.dao = dao;
        this.mapperFacade = mapperFacade;
    }

    @ResponseBody
    @RequestMapping(value = "/v1/IsRegistered", method = RequestMethod.POST)
    public Response isRegistered(@RequestBody IsRegisteredRequest request, HttpServletResponse response) {

        String messageInitiaterId = request.getMessageInitiaterId();
        String destId = request.getDestinationId();
        logger.info(messageInitiaterId + " is checking if " + destId + " is logged in...");
        try {
            UserDTO userDTO = dao.getUserRecord(destId).toDTO(mapperFacade);
            return new Response<>(ClientActionType.IS_REGISTERED_RES, userDTO);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }


    }
}
