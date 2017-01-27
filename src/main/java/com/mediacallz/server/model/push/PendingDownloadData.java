package com.mediacallz.server.model.push;

import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.enums.SpecialMediaType;
import lombok.Data;

/**
 * Created by Mor on 12/31/2016.
 */
@Data
public class PendingDownloadData extends AbstractPushData {

    private String sourceId;
    private String sourceLocale;
    private String destinationId;
    private String destinationContactName;
    private SpecialMediaType specialMediaType;
    private String filePathOnServer;
    private String filePathOnSrcSd;
    private MediaFileDTO mediaFile;
    private int commId;
}
