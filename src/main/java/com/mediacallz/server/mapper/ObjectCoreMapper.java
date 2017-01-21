package com.mediacallz.server.mapper;

import com.mediacallz.server.database.dbo.AppMetaDBO;
import com.mediacallz.server.database.dbo.MediaCallDBO;
import com.mediacallz.server.database.dbo.MediaFileDBO;
import com.mediacallz.server.database.dbo.MediaTransferDBO;
import com.mediacallz.server.model.dto.AppMetaDTO;
import com.mediacallz.server.model.dto.MediaCallDTO;
import com.mediacallz.server.model.dto.MediaFileDTO;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.request.UploadFileRequest;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 20/12/2016.
 */
@Component
public class ObjectCoreMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(AppMetaDBO.class, AppMetaDTO.class)
                .field("last_supported_version", "lastSupportedAppVersion")
                .byDefault()
                .register();

        mapperFactory.classMap(MediaCallDBO.class, MediaCallDTO.class)
                .field("uid_src", "sourceId")
                .field("uid_dest", "destinationId")
                .field("md5_visual", "visualMediaFile.md5")
                .field("md5_audio", "audioMediaFile.md5")
                .field("type", "specialMediaType")
                .byDefault()
                .register();

        mapperFactory.classMap(MediaTransferDBO.class, UploadFileRequest.class)
                .field("uid_src", "sourceId")
                .field("uid_dest", "destinationId")
                .field("type", "specialMediaType")
                .field("md5", "mediaFile.md5")
                .byDefault()
                .register();

        mapperFactory.classMap(MediaFileDBO.class, MediaFileDTO.class)
                .field("content_ext", "extension")
                .field("content_size", "size")
                .byDefault()
                .register();
    }




}
