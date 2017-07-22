package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.ContactDBO;
import com.mediacallz.server.model.dto.ContactDTO;
import com.mediacallz.server.model.request.SyncContactsRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mor on 22/07/2017.
 */
@Component
@Setter
@Slf4j
public class SyncContactsLogicImpl extends AbstractServerLogic implements SyncContactsLogic {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public void execute(SyncContactsRequest request) {
        List<ContactDTO> contacts = request.getContacts();
        log.info("Initiating sync contacts from source:{}, for contacts:{}", request.getUser().getUid(), request.getContacts());
        List<ContactDBO> contactDBOS = new ArrayList<>();
        contacts.forEach(contact -> contactDBOS.add(contact.toInternal(mapperFacade)));
        usersDao.syncContacts(contactDBOS);
    }
}
