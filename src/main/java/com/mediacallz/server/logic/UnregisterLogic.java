package com.mediacallz.server.logic;

import com.mediacallz.server.database.UsersDataAccess;
import com.mediacallz.server.model.request.UnRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public class UnregisterLogic extends AbstractServerLogic {

    private final UsersDataAccess usersDataAccess;

    @Autowired
    public UnregisterLogic(UsersDataAccess usersDataAccess) {
        this.usersDataAccess = usersDataAccess;
    }

    public void execute(UnRegisterRequest request) {
        String messageInitiaterId = request.getMessageInitiaterId();
        String token = request.getPushToken();
        usersDataAccess.unregisterUser(messageInitiaterId, token);
    }
}
