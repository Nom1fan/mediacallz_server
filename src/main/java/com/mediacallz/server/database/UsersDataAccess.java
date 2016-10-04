package com.mediacallz.server.database;


import com.mediacallz.server.database.dbos.UserDBO;

/**
 * Created by Mor on 25/07/2016.
 */
public interface UsersDataAccess {
    boolean unregisterUser(String userId, String token);

    boolean isRegistered(String userId);

    UserDBO getUserRecord(String destId);
}
