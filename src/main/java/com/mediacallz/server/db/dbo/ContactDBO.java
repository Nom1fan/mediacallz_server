package com.mediacallz.server.db.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Mor on 22/07/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDBO extends DBOEntity {

    private String contact_uid;

    private String contact_name;

    private String contact_source;
}
