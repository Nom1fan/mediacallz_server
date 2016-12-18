package mediacallz.com.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MediaFile implements Serializable {

    private String md5;
    private String extension;
    private long size;
    private FileType fileType;
    private boolean isCompressed = false;

    public enum FileType { IMAGE, VIDEO, AUDIO }
}
