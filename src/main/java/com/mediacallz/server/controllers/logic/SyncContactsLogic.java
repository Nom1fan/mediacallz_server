package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.model.request.SyncContactsRequest;

/**
 * Created by Mor on 22/07/2017.
 */
public interface SyncContactsLogic extends ServerLogic {

    void setUsersDao(UsersDao usersDao);

    void execute(SyncContactsRequest request);
}
