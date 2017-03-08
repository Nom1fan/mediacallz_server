package com.mediacallz.server.logic;

import com.mediacallz.server.dao.Dao;
import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.enums.UserStatus;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.IsRegisteredRequest;
import com.mediacallz.server.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
@Slf4j
public class IsRegisteredLogic extends AbstractServerLogic {

    private final UsersDao usersDao;

    @Autowired
    public IsRegisteredLogic(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public Response<UserDTO> execute(IsRegisteredRequest request) {
        String messageInitiaterId = request.getUser().getUid();
        String destId = request.getDestinationId();
        log.info(messageInitiaterId + " is checking if " + destId + " is logged in...");
        UserDTO userDTO = new UserDTO();
        UserDBO userDBO = usersDao.getUserRecord(destId);
        userDTO.setUid(destId);
        userDTO.setUserStatus(userDBO == null ? UserStatus.UNREGISTERED : userDBO.getUserStatus());
        return new Response<>(userDTO);
    }
}
