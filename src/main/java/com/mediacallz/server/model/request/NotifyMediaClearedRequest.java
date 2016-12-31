package com.mediacallz.server.model.request;

import com.mediacallz.server.model.SpecialMediaType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class NotifyMediaClearedRequest extends Request {

    private String sourceId;
    private String destinationContactName;
    private SpecialMediaType specialMediaType;
}
