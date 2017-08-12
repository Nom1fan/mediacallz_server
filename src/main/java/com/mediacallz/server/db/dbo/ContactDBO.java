package com.mediacallz.server.db.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Mor on 22/07/2017.
 */
@Data
@EqualsAndHashCode(of = "contact_uid")
@NoArgsConstructor
@AllArgsConstructor
public class ContactDBO extends DBOEntity {

    private String contact_uid;

    private String contact_name;

    private String contact_source;

    public String getContact_uid() {
        return this.contact_uid;
    }
}
