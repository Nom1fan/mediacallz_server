package com.mediacallz.server.model.request;

import com.mediacallz.server.validators.UidsList;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created by Mor on 1/18/2017.
 */
@Data
public class GetRegisteredContactsRequest extends Request {

    @UidsList
    private List<String> contactsUids;
}
