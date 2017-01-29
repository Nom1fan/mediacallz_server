package com.mediacallz.server.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.GetRegisteredContactsRequest;
import com.mediacallz.server.model.response.Response;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Mor on 1/18/2017.
 */
@Component
public class GetRegisteredContactsLogic extends AbstractServerLogic {

    private final UsersDao usersDao;

    private final MapperFacade mapperFacade;

    @Autowired
    public GetRegisteredContactsLogic(UsersDao usersDao, MapperFacade mapperFacade) {
        this.usersDao = usersDao;
        this.mapperFacade = mapperFacade;
    }

    public Response<List<UserDTO>> execute(GetRegisteredContactsRequest request) {
        List<UserDBO> registeredContacts = usersDao.getRegisteredContacts(request.getContactsUids());
        List<UserDTO> userDTOS = mapperFacade.mapAsList(registeredContacts, UserDTO.class);
        return new Response<>(userDTOS);
    }
}
