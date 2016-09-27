package com.mediacallz.server.database.dbos;

import com.mediacallz.server.model.SpecialMediaType;
import lombok.Data;

import java.util.Date;

/**
 * Created by Mor on 27/09/2016.
 */
@Data
public class MediaCallDBO {

    int call_id;
    SpecialMediaType type;
    String md5_visual;
    String md5_audio;
    String uid_src;
    String uid_dest;
    Date datetime;
}
