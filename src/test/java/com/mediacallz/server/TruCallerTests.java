package com.mediacallz.server;

import com.mediacallz.server.controllers.logic.GetContactsLogicImpl;
import com.mediacallz.server.controllers.logic.SyncContactsLogic;
import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.ContactDBO;
import com.mediacallz.server.enums.OsType;
import com.mediacallz.server.enums.UserStatus;
import com.mediacallz.server.model.dto.ContactDTO;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.GetContactsRequest;
import com.mediacallz.server.model.request.SyncContactsRequest;
import com.mediacallz.server.model.response.Response;
import ma.glasnost.orika.MapperFacade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mor on 22/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TruCallerTests {

    @Autowired
    SyncContactsLogic syncContactsLogic;

    @Autowired
    GetContactsLogicImpl getContactsLogic;

    @Autowired
    MapperFacade mapperFacade;

    @Mock
    UsersDao usersDao;

    @Test
    public void sync2Contacts() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUid("0542258808");
        userDTO.setToken(UUID.randomUUID().toString());
        userDTO.setUserStatus(UserStatus.REGISTERED);
        userDTO.setOs(OsType.ANDROID);

        List<ContactDTO> contacts = new ArrayList<ContactDTO>() {{
            add(new ContactDTO("05444556543", "רוני אידלין"));
            add(new ContactDTO("05455783830", "סהר מרחב"));
        }};

        List<ContactDBO> contactDBOS = new ArrayList<>();
        contacts.forEach(contact -> contactDBOS.add(contact.toInternal(mapperFacade)));
        contactDBOS.forEach(contactDBO -> contactDBO.setContact_source("0542258808"));

        SyncContactsRequest syncContactsRequest = new SyncContactsRequest();
        syncContactsRequest.setContacts(contacts);
        syncContactsRequest.setUser(userDTO);

        when(usersDao.syncContacts(anyList())).thenReturn(anyBoolean());

        syncContactsLogic.setUsersDao(usersDao);
        syncContactsLogic.execute(syncContactsRequest);

        verify(usersDao).syncContacts(contactDBOS);
    }


    @Test
    public void getContacts() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUid("0542258808");
        userDTO.setToken(UUID.randomUUID().toString());
        userDTO.setUserStatus(UserStatus.REGISTERED);
        userDTO.setOs(OsType.ANDROID);

        List<String> uids = new ArrayList<String>() {{
            add("05444556543");
            add("05455783830");
        }};

        GetContactsRequest request = new GetContactsRequest();
        request.setUser(userDTO);
        request.setContactsUids(uids);

        List<ContactDBO> contactDBOS = new ArrayList<ContactDBO>() {{
            add(new ContactDBO("0544556543", "רוני אידלין", "0541111111"));
            add(new ContactDBO("0545783830", "Sahar", "0541111111"));
        }};

        when(usersDao.getContacts(uids)).thenReturn(contactDBOS);

        getContactsLogic.setUsersDao(usersDao);
        Response<List<ContactDTO>> response = getContactsLogic.execute(request);

        List<ContactDTO> expectedContacts = new ArrayList<ContactDTO>() {{
            add(new ContactDTO("0544556543", "רוני אידלין"));
            add(new ContactDTO("0545783830", "Sahar"));
        }};

        Assert.assertEquals(expectedContacts, response.getResult());
    }

    @Test
    public void integration_SyncAndThenGet() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUid("0542258808");
        userDTO.setToken(UUID.randomUUID().toString());
        userDTO.setUserStatus(UserStatus.REGISTERED);
        userDTO.setOs(OsType.ANDROID);

        ContactDTO contactA = new ContactDTO("0544556543", "רוני אידלין");
        ContactDTO contactB = new ContactDTO("0545783830", "סהר מרחב");

        List<ContactDTO> expectedContacts = new ArrayList<ContactDTO>() {{
            add(contactA);
            add(contactB);
        }};

        List<ContactDBO> contactDBOS = new ArrayList<>();
        expectedContacts.forEach(contact -> contactDBOS.add(contact.toInternal(mapperFacade)));
        contactDBOS.forEach(contactDBO -> contactDBO.setContact_source("0542258808"));

        SyncContactsRequest syncContactsRequest = new SyncContactsRequest();
        syncContactsRequest.setContacts(expectedContacts);
        syncContactsRequest.setUser(userDTO);

        syncContactsLogic.execute(syncContactsRequest);

        List<String> uids = expectedContacts.stream().map(ContactDTO::getContactUid).collect(Collectors.toList());

        GetContactsRequest request = new GetContactsRequest();
        request.setUser(userDTO);
        request.setContactsUids(uids);

        Response<List<ContactDTO>> response = getContactsLogic.execute(request);
        List<ContactDTO> actualContacts = response.getResult();

        expectedContacts.sort(Comparator.comparing(ContactDTO::getContactUid));
        actualContacts.sort(Comparator.comparing(ContactDTO::getContactUid));

        Assert.assertEquals(expectedContacts, actualContacts);
    }



}
