package com.mediacallz.server.model.request;

import com.mediacallz.server.model.dto.ContactDTO;
import com.mediacallz.server.validators.UidsList;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
public class GetContactsRequest extends Request {

    @UidsList
    private List<String> contactsUids;
}
