package com.mediacallz.server;

import com.mediacallz.server.validators.GUIDValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

/**
 * Created by Mor on 22/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GuidValidatorTests {

    private GUIDValidator guidValidator = new GUIDValidator();

    @Mock
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Test
    public void validGuid() {
        String guid = UUID.randomUUID().toString();
        Assert.assertTrue(guidValidator.isValid(guid, constraintValidatorContextMock));
    }

    @Test
    public void invalidGuid() {
        String guid = "d0d91f1c-ec54-490f-8d1b-f1e440e66f78Almost but not quite";
        Assert.assertFalse(guidValidator.isValid(guid, constraintValidatorContextMock));
    }


}
