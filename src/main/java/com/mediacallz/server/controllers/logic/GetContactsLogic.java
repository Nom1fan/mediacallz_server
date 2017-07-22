package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.model.dto.ContactDTO;
import com.mediacallz.server.model.request.GetContactsRequest;

import java.util.List;

public interface GetContactsLogic extends ServerLogic {

    void setUsersDao(UsersDao usersDao);

    List<ContactDTO> execute(GetContactsRequest request);
}
