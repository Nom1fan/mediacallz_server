package mediacallz.com.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Mor on 10/03/2016.
 */
@Data
public class CallRecord implements Serializable {

    private static final long serialVersionUID = 7408472793374531808L;
    private static final String TAG = CallRecord.class.getSimpleName();

    private String sourceId;
    private String destinationId;

    private MediaFile visualMediaFile;
    private MediaFile audioMediaFile;

    private String visualMd5;
    private String audioMd5;

    private SpecialMediaType specialMediaType;

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.
                append(", [Source]:").append(sourceId).
                append(", [Destination]:").append(destinationId).
                append(", [Special Media Type]:").append(specialMediaType.toString());

        if (visualMediaFile != null) {
            builder.append(", [Visual Media File]:").append(visualMediaFile);
            builder.append(", [visual_md5]:").append(visualMd5);
        }
        if (audioMediaFile != null) {
            builder.append(", [Audio Media File]:").append(audioMediaFile);
            builder.append(", [audio_md5]:").append(audioMd5);
        }

        return builder.toString();
    }
}
