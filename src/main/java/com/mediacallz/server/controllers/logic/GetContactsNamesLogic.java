package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.model.dto.ContactDTO;
import com.mediacallz.server.model.request.GetContactsRequest;
import com.mediacallz.server.model.response.Response;

import java.util.List;

public interface GetContactsNamesLogic extends ServerLogic {

    void setUsersDao(UsersDao usersDao);

    Response<List<ContactDTO>> execute(GetContactsRequest request);
}
