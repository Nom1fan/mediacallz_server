package com.mediacallz.server;

import com.mediacallz.server.controllers.logic.AttachMediaLogic;
import com.mediacallz.server.dao.UsersDao;
import com.mediacallz.server.db.dbo.UserDBO;
import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.enums.UserStatus;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.model.push.AttachMediaData;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.model.push.PushResponse;
import com.mediacallz.server.model.request.AttachMediaRequest;
import com.mediacallz.server.services.PushSender;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Mor on 22/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AttachMediaTests {

    @Autowired
    private MapperFacade mapperFacade;

    @Mock
    private UsersDao usersDao;

    @Mock
    private PushSender pushSender;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    public void attachMediaSuccess() throws IOException {

        AttachMediaRequest request = new AttachMediaRequest();
        UserDTO srcUser = new UserDTO();
        srcUser.setUid("srcTestId");
        srcUser.setUserStatus(UserStatus.REGISTERED);
        request.setUser(srcUser);
        request.setAttachMediaGuid(UUID.randomUUID().toString());
        request.setDestinationId("destTestId");
        request.setMediaUrl("mediaTestUrl");
        request.setSpecialMediaType(SpecialMediaType.CALLER_MEDIA);

        UserDBO destUser = new UserDBO();
        destUser.setUserStatus(UserStatus.REGISTERED);
        destUser.setUid("destTestId");
        destUser.setToken("destTestToken");

        when(usersDao.getUserRecord("destTestId")).thenReturn(destUser);

        PushResponse pushResponse = new PushResponse(200);

        when(pushSender.getPushResponse()).thenReturn(pushResponse);

        AttachMediaLogic attachMediaLogic = new AttachMediaLogic(usersDao, pushSender, mapperFacade);
        attachMediaLogic.execute(request, httpServletResponse);

        AttachMediaData expectedAttachMediaData = new AttachMediaData();
        expectedAttachMediaData.setAttachMediaGuid(request.getAttachMediaGuid());
        expectedAttachMediaData.setDestinationId(request.getDestinationId());
        expectedAttachMediaData.setMediaUrl(request.getMediaUrl());
        expectedAttachMediaData.setSourceId(request.getUser().getUid());
        expectedAttachMediaData.setSpecialMediaType(request.getSpecialMediaType());

        verify(pushSender).sendPush("destTestToken", PushEventKeys.ATTACH_MEDIA, expectedAttachMediaData);
        verifyZeroInteractions(httpServletResponse);
    }

    @Test
    public void attachMediaDestUserNotFoundInDbFailure() throws IOException {

        AttachMediaRequest request = new AttachMediaRequest();
        UserDTO srcUser = new UserDTO();
        srcUser.setUid("srcTestId");
        srcUser.setUserStatus(UserStatus.REGISTERED);
        request.setUser(srcUser);
        request.setAttachMediaGuid(UUID.randomUUID().toString());
        request.setDestinationId("destTestId");
        request.setMediaUrl("mediaTestUrl");
        request.setSpecialMediaType(SpecialMediaType.CALLER_MEDIA);

        when(usersDao.getUserRecord("destTestId")).thenReturn(null);

        AttachMediaLogic attachMediaLogic = new AttachMediaLogic(usersDao, pushSender, mapperFacade);
        attachMediaLogic.execute(request, httpServletResponse);

        verify(httpServletResponse).sendError(SC_BAD_REQUEST, String.format("Cannot find user %s", request.getDestinationId()));
    }

    @Test
    public void attachMediaDestUserTokenNotFoundFailure() throws IOException {

        AttachMediaRequest request = new AttachMediaRequest();
        UserDTO srcUser = new UserDTO();
        srcUser.setUid("srcTestId");
        srcUser.setUserStatus(UserStatus.REGISTERED);
        request.setUser(srcUser);
        request.setAttachMediaGuid(UUID.randomUUID().toString());
        request.setDestinationId("destTestId");
        request.setMediaUrl("mediaTestUrl");
        request.setSpecialMediaType(SpecialMediaType.CALLER_MEDIA);

        UserDBO destUser = new UserDBO();
        destUser.setUserStatus(UserStatus.REGISTERED);
        destUser.setUid("destTestId");
        destUser.setToken("destTestToken");

        when(usersDao.getUserRecord("destTestId")).thenReturn(destUser);

        PushResponse pushResponse = new PushResponse(404);

        when(pushSender.getPushResponse()).thenReturn(pushResponse);

        AttachMediaLogic attachMediaLogic = new AttachMediaLogic(usersDao, pushSender, mapperFacade);
        attachMediaLogic.execute(request, httpServletResponse);

        verify(httpServletResponse).sendError(SC_BAD_REQUEST, String.format("Cannot find user %s's push token", request.getDestinationId()));
    }

    @Test
    public void attachMediaFailedToSendPushFailure() throws IOException {

        AttachMediaRequest request = new AttachMediaRequest();
        UserDTO srcUser = new UserDTO();
        srcUser.setUid("srcTestId");
        srcUser.setUserStatus(UserStatus.REGISTERED);
        request.setUser(srcUser);
        request.setAttachMediaGuid(UUID.randomUUID().toString());
        request.setDestinationId("destTestId");
        request.setMediaUrl("mediaTestUrl");
        request.setSpecialMediaType(SpecialMediaType.CALLER_MEDIA);

        UserDBO destUser = new UserDBO();
        destUser.setUserStatus(UserStatus.REGISTERED);
        destUser.setUid("destTestId");
        destUser.setToken("destTestToken");

        when(usersDao.getUserRecord("destTestId")).thenReturn(destUser);

        PushResponse pushResponse = new PushResponse(500);

        when(pushSender.getPushResponse()).thenReturn(pushResponse);

        AttachMediaLogic attachMediaLogic = new AttachMediaLogic(usersDao, pushSender, mapperFacade);
        attachMediaLogic.execute(request, httpServletResponse);

        verify(httpServletResponse).setStatus(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void attachMediaDestUserEqualsSrcUserFailure() throws IOException {

        AttachMediaRequest request = new AttachMediaRequest();
        UserDTO srcUser = new UserDTO();
        srcUser.setUid("srcTestId");
        request.setUser(srcUser);
        request.setDestinationId("srcTestId");

        AttachMediaLogic attachMediaLogic = new AttachMediaLogic(usersDao, pushSender, mapperFacade);
        attachMediaLogic.execute(request, httpServletResponse);

        verify(httpServletResponse).sendError(SC_BAD_REQUEST, "Invalid destination ID. Cannot attach media to self.");
    }

}
