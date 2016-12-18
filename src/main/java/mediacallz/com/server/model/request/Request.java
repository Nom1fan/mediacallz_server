package mediacallz.com.server.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Mor on 17/12/2016.
 */
@Data
@NoArgsConstructor
@ToString
public class Request {

    private String messageInitiaterId;

    private String pushToken;

    private String androidVersion;

    private String iosVersion;

    private String appVersion;
}
