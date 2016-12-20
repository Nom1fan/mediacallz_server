package mediacallz.com.server.database.dbo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import mediacallz.com.server.model.SpecialMediaType;
import mediacallz.com.server.model.dto.MediaCallDTO;

import java.util.Date;

/**
 * Created by Mor on 27/09/2016.
 */
@Data
@RequiredArgsConstructor
public class MediaCallDBO implements DBO<MediaCallDTO> {

    int call_id;
    final SpecialMediaType type;
    final String md5_visual;
    final String md5_audio;
    final String uid_src;
    final String uid_dest;
    final Date datetime;

    @Override
    public MediaCallDTO toDTO(MapperFacade mapperFacade) {
        return mapperFacade.map(this, MediaCallDTO.class);
    }
}
