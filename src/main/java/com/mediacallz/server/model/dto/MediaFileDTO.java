package com.mediacallz.server.model.dto;

import com.mediacallz.server.database.dbo.MediaFileDBO;
import com.mediacallz.server.model.dto.DTOEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class MediaFileDTO extends DTOEntity<MediaFileDBO> {

    @NotBlank
    private String md5;

    @NotBlank
    private String extension;

    @NotNull
    private Long size;

    @NotNull
    private FileType fileType;

    private boolean isCompressed = false;

    public enum FileType { IMAGE, VIDEO, AUDIO }
}
