package com.mediacallz.server.model.request;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.validators.Guid;
import com.mediacallz.server.validators.SpecialMediaTypeFilter;
import com.mediacallz.server.validators.Uid;
import com.mediacallz.server.validators.Url;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.mediacallz.server.enums.SpecialMediaType.CALLER_MEDIA;
import static com.mediacallz.server.enums.SpecialMediaType.PROFILE_MEDIA;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
public class AttachMediaRequest extends Request {

    @Guid
    private String attachMediaGuid;

    //@Uid
    private String destinationId;

    @SpecialMediaTypeFilter(allowOnly = {CALLER_MEDIA, PROFILE_MEDIA})
    private SpecialMediaType specialMediaType;

    @Url
    private String mediaUrl;
}
