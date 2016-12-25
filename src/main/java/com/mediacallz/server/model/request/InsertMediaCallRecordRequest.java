package com.mediacallz.server.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.mediacallz.server.model.dto.MediaCallDTO;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class InsertMediaCallRecordRequest extends Request {

    private MediaCallDTO mediaCallDTO;
}


