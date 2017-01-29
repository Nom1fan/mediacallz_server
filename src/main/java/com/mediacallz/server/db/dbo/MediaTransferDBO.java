package com.mediacallz.server.db.dbo;

import com.mediacallz.server.enums.SpecialMediaType;
import lombok.Data;

import java.util.Date;

/**
 * Created by Mor on 31/03/2016.
 */
@Data
public class MediaTransferDBO extends DBOEntity {

    private int transfer_id;
    private SpecialMediaType type;
    private String md5;
    private String uid_src;
    private String uid_dest;
    private Date datetime;
    private boolean transfer_success;
    private Date transfer_datetime;
}
