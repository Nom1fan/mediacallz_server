package com.mediacallz.server.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.model.request.UnRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class UnregisterLogic extends AbstractServerLogic {

    private final UsersDao usersDao;

    @Autowired
    public UnregisterLogic(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public void execute(UnRegisterRequest request) {
        String messageInitiaterId = request.getUser().getUid();
        String token = request.getUser().getToken();
        usersDao.unregisterUser(messageInitiaterId, token);
    }
}
