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
public class UploadFileRequest extends Request {

    private String sourceId;
    private String sourceLocale;
    private String destinationId;
    private String destinationContactName;
    private MediaFile mediaFile;
    private String filePathOnSrcSd;
    private SpecialMediaType specialMediaType;
    private String sourceWithExtension;
}
