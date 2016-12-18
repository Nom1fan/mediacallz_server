package mediacallz.com.server.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mediacallz.com.server.model.SpecialMediaType;

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
    private String sourceLocale;
}
