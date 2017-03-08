package com.mediacallz.server.logic;

import com.mediacallz.server.dao.Dao;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.UpdateUserRecordRequest;
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
public class UpdateUserRecordLogic extends AbstractServerLogic {

    private final Dao dao;
    private MapperFacade mapperFacade;

    @Autowired
    public UpdateUserRecordLogic(Dao dao, MapperFacade mapperFacade) {
        this.dao = dao;
        this.mapperFacade = mapperFacade;
    }

    public void execute(UpdateUserRecordRequest request, HttpServletResponse response) {
        UserDTO user = request.getUser();
        log.debug(user.getUid() + " is updating its record...");

        UserDBO userDBO = user.toInternal(mapperFacade);

        try {
            dao.updateUserRecord(userDBO);
        } catch (SQLException e) {
            e.printStackTrace();
            log.debug("Failed to update record of user: " + user.getUid() + ". [Exception]:" + (e.getMessage()!=null ? e.getMessage() : e));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
