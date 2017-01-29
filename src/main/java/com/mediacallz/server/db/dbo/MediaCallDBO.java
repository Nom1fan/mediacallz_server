package com.mediacallz.server.db.dbo;

import com.mediacallz.server.enums.SpecialMediaType;
import lombok.Data;

import java.util.Date;

/**
 * Created by Mor on 27/09/2016.
 */
@Data
public class MediaCallDBO extends DBOEntity {

    private int call_id;
    private String uid_src;
    private String uid_dest;
    private SpecialMediaType type;
    private String md5_visual;
    private String md5_audio;
    private Date datetime;
}
