package com.mediacallz.server.database.dbo;

import com.mediacallz.server.model.SpecialMediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
