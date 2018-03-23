package com.mediacallz.server;

import com.mediacallz.server.enums.SpecialMediaType;
import com.mediacallz.server.validators.SpecialMediaTypeFilter;
import com.mediacallz.server.validators.SpecialMediaTypeValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintValidatorContext;

import static com.mediacallz.server.enums.SpecialMediaType.*;
import static org.mockito.Mockito.when;

/**
 * Created by Mor on 22/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpecialMediaTypeValidatorTests {

    private SpecialMediaTypeValidator specialMediaTypeValidator = new SpecialMediaTypeValidator();

    @Mock
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Mock
    private SpecialMediaTypeFilter specialMediaTypeFilterMock;

    @Test
    public void onlyAllowCallerMediaGetCallerMediaSuccess() {
        when(specialMediaTypeFilterMock.allowOnly()).thenReturn(new SpecialMediaType[]{CALLER_MEDIA});
        specialMediaTypeValidator.initialize(specialMediaTypeFilterMock);
        Assert.assertTrue(specialMediaTypeValidator.isValid(CALLER_MEDIA, constraintValidatorContextMock));
    }

    @Test
    public void onlyAllowCallerMediaAndProfileMediaGetCallerMediaSuccess() {
        when(specialMediaTypeFilterMock.allowOnly()).thenReturn(new SpecialMediaType[]{CALLER_MEDIA, PROFILE_MEDIA});
        specialMediaTypeValidator.initialize(specialMediaTypeFilterMock);
        Assert.assertTrue(specialMediaTypeValidator.isValid(CALLER_MEDIA, constraintValidatorContextMock));
    }

    @Test
    public void onlyAllowCallerMediaAndProfileMediaGetProfileMediaSuccess() {
        when(specialMediaTypeFilterMock.allowOnly()).thenReturn(new SpecialMediaType[]{CALLER_MEDIA, PROFILE_MEDIA});
        specialMediaTypeValidator.initialize(specialMediaTypeFilterMock);
        Assert.assertTrue(specialMediaTypeValidator.isValid(PROFILE_MEDIA, constraintValidatorContextMock));
    }

    @Test
    public void onlyAllowCallerMediaGetProfileMediaFailure() {
        when(specialMediaTypeFilterMock.allowOnly()).thenReturn(new SpecialMediaType[]{CALLER_MEDIA});
        specialMediaTypeValidator.initialize(specialMediaTypeFilterMock);
        Assert.assertFalse(specialMediaTypeValidator.isValid(PROFILE_MEDIA, constraintValidatorContextMock));
    }

    @Test
    public void onlyAllowCallerMediaAndProfileMediaGetDefaultProfileMediaFailure() {
        when(specialMediaTypeFilterMock.allowOnly()).thenReturn(new SpecialMediaType[]{CALLER_MEDIA, PROFILE_MEDIA});
        specialMediaTypeValidator.initialize(specialMediaTypeFilterMock);
        Assert.assertFalse(specialMediaTypeValidator.isValid(DEFAULT_PROFILE_MEDIA, constraintValidatorContextMock));
    }

    @Test
    public void onlyAllowCallerMediaAndProfileMediaGetDefaultCallerMediaFailure() {
        when(specialMediaTypeFilterMock.allowOnly()).thenReturn(new SpecialMediaType[]{CALLER_MEDIA, PROFILE_MEDIA});
        specialMediaTypeValidator.initialize(specialMediaTypeFilterMock);
        Assert.assertFalse(specialMediaTypeValidator.isValid(DEFAULT_CALLER_MEDIA, constraintValidatorContextMock));
    }


}
