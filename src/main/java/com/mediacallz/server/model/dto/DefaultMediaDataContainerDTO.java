package com.mediacallz.server.model.dto;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.model.dto.DefaultMediaDataDTO;
import com.mediacallz.server.validators.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by Mor on 10/06/2017.
 */
@Data
public class DefaultMediaDataContainerDTO {

    private String uid;

    private SpecialMediaType specialMediaType;

    private List<DefaultMediaDataDTO> defaultMediaDataList;
}
