package mediacallz.com.server.model.dto;

import lombok.Data;
import lombok.ToString;
import ma.glasnost.orika.MapperFacade;
import mediacallz.com.server.database.dbo.MediaCallDBO;
import mediacallz.com.server.model.MediaFile;
import mediacallz.com.server.model.SpecialMediaType;

/**
 * Created by Mor on 10/03/2016.
 */
@Data
@ToString
public class MediaCallDTO implements DTO<MediaCallDBO> {

    private String sourceId;
    private String destinationId;

    private MediaFile visualMediaFile;
    private MediaFile audioMediaFile;

    private SpecialMediaType specialMediaType;

    @Override
    public MediaCallDBO toDBO(MapperFacade mapperFacade) {
        return mapperFacade.map(this, MediaCallDBO.class);
    }
}
