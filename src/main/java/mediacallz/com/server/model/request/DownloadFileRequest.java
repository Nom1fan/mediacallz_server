package mediacallz.com.server.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mediacallz.com.server.model.MediaFile;
import mediacallz.com.server.model.SpecialMediaType;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class DownloadFileRequest extends Request {

    private int commId;
    private String sourceId;
    private String destinationId;
    private String destinationContactName;
    private String sourceLocale;
    private String filePathOnServer;
    private SpecialMediaType specialMediaType;
    private MediaFile.FileType fileType;
    private String filePathOnSrcSd;
}
