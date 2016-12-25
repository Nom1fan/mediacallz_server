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
public class ClearMediaRequest extends Request {

    private String destinationId;

    private SpecialMediaType specialMediaType;

    private String sourceId;
}
