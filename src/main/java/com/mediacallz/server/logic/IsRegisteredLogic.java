package com.mediacallz.server.logic;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.IsRegisteredRequest;
import com.mediacallz.server.model.response.Response;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class IsRegisteredLogic extends AbstractServerLogic {

    private final Dao dao;

    private final MapperFacade mapperFacade;

    @Autowired
    public IsRegisteredLogic(Dao dao, MapperFacade mapperFacade) {
        this.dao = dao;
        this.mapperFacade = mapperFacade;
    }

    public Response<UserDTO> execute(IsRegisteredRequest request, HttpServletResponse response) {
        String messageInitiaterId = request.getMessageInitiaterId();
        String destId = request.getDestinationId();
        logger.info(messageInitiaterId + " is checking if " + destId + " is logged in...");
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.fromInternal(dao.getUserRecord(destId), mapperFacade);
            return new Response<>(userDTO);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }
}
