package com.mediacallz.server.database.dbos;

import com.mediacallz.server.model.SpecialMediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * Created by Mor on 31/03/2016.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MediaTransferDBO {

    int transfer_id;
    final SpecialMediaType type;
    final String md5;
    final String uid_src;
    final String uid_dest;
    Date datetime;
    boolean transfer_success;
    Date transfer_datetime;
}
