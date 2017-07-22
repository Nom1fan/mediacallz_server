package com.mediacallz.server.dao;


import com.mediacallz.server.db.dbo.ContactDBO;
import com.mediacallz.server.db.dbo.UserDBO;

import java.util.List;

/**
 * Created by Mor on 25/07/2016.
 */
public interface UsersDao {
    boolean registerUser(UserDBO user);

    boolean updateUser(UserDBO user);

    boolean unregisterUser(String userId, String token);

    boolean isRegistered(String userId);

    UserDBO getUserRecord(String destId);

    List<UserDBO> getRegisteredContacts(List<String> contactsUids);

    boolean syncContacts(List<ContactDBO> contacts);

    List<ContactDBO> getContacts(List<String> contactsUids);
}
