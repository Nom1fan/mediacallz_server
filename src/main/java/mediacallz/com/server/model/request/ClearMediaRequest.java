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
public class ClearMediaRequest extends Request {

    private String destinationId;

    private SpecialMediaType specialMediaType;

    private String sourceId;
}
