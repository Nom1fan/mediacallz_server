package mediacallz.com.server.model.dto;

import lombok.Data;
import ma.glasnost.orika.MapperFacade;
import mediacallz.com.server.database.dbo.UserDBO;
import mediacallz.com.server.model.UserStatus;

/**
 * Created by Mor on 21/12/2016.
 */
@Data
public class UserDTO implements DTO<UserDBO> {

    private String uid;
    private UserStatus userStatus;

    @Override
    public UserDBO toDBO(MapperFacade mapperFacade) {
        return mapperFacade.map(this, UserDBO.class);
    }
}
