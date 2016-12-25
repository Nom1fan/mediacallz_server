package com.mediacallz.server.database.dbo;

import com.mediacallz.server.model.SpecialMediaType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import com.mediacallz.server.model.dto.MediaCallDTO;

import java.util.Date;

/**
 * Created by Mor on 27/09/2016.
 */
@Data
@RequiredArgsConstructor
public class MediaCallDBO extends DBOEntity {

    int call_id;
    final SpecialMediaType type;
    final String md5_visual;
    final String md5_audio;
    final String uid_src;
    final String uid_dest;
    final Date datetime;
}
