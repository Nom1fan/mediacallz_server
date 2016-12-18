package mediacallz.com.server.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Mor on 10/03/2016.
 */
@Data
@ToString
public class CallRecord implements Serializable {

    private String sourceId;
    private String destinationId;

    private MediaFile visualMediaFile;
    private MediaFile audioMediaFile;

    private SpecialMediaType specialMediaType;

//    @Override
//    public String toString() {
//
//        StringBuilder builder = new StringBuilder();
//        builder.
//                append(", [Source]:").append(sourceId).
//                append(", [Destination]:").append(destinationId).
//                append(", [Special Media Type]:").append(specialMediaType.toString());
//
//        if (visualMediaFile != null) {
//            builder.append(", [Visual Media File]:").append(visualMediaFile);
//            builder.append(", [visual_md5]:").append(visualMediaFile.getMd5());
//        }
//        if (audioMediaFile != null) {
//            builder.append(", [Audio Media File]:").append(audioMediaFile);
//            builder.append(", [audio_md5]:").append(audioMediaFile.getMd5());
//        }
//
//        return builder.toString();
//    }
}
