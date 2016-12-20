package mediacallz.com.server.database.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import ma.glasnost.orika.MapperFacade;
import mediacallz.com.server.model.dto.AppMetaDTO;

/**
 * Created by Mor on 26/03/2016.
 */
@Data
@AllArgsConstructor
public class AppMetaDBO implements DBO<AppMetaDTO> {

    private double last_supported_version;

    @Override
    public AppMetaDTO toDTO(MapperFacade mapperFacade) {
        return mapperFacade.map(this, AppMetaDTO.class);
    }
}
