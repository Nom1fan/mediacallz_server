package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.ContactDBO;
import com.mediacallz.server.model.dto.ContactDTO;
import com.mediacallz.server.model.request.GetContactsRequest;
import com.mediacallz.server.model.response.Response;
import lombok.Setter;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Setter
public class GetContactsNamesLogicImpl extends AbstractServerLogic implements GetContactsNamesLogic {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public Response<List<ContactDTO>> execute(GetContactsRequest request) {
        List<ContactDTO> contacts = new ArrayList<>();
        List<ContactDBO> contactDBOS = usersDao.getContacts(request.getContactsUids());
        for (ContactDBO contactDBO : contactDBOS) {
            ContactDTO contactDTO = new ContactDTO();
            contactDTO.fromInternal(contactDBO, mapperFacade);
            contacts.add(contactDTO);
        }
        return new Response<>(contacts);
    }
}
