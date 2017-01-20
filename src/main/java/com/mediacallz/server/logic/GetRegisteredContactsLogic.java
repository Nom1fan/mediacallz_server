package com.mediacallz.server.logic;

import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.database.dbo.UserDBO;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.GetRegisteredContactsRequest;
import com.mediacallz.server.model.response.Response;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mor on 1/18/2017.
 */
@Component
public class GetRegisteredContactsLogic extends AbstractServerLogic {

    private final UsersDataAccess usersDataAccess;

    private final MapperFacade mapperFacade;

    @Autowired
    public GetRegisteredContactsLogic(UsersDataAccess usersDataAccess, MapperFacade mapperFacade) {
        this.usersDataAccess = usersDataAccess;
        this.mapperFacade = mapperFacade;
    }

    public Response<List<UserDTO>> execute(GetRegisteredContactsRequest request) {
        List<UserDBO> registeredContacts = usersDataAccess.getRegisteredContacts(request.getContactsUids());
        List<UserDTO> userDTOS = mapperFacade.mapAsList(registeredContacts, UserDTO.class);
        return new Response<>(userDTOS);
    }
}
