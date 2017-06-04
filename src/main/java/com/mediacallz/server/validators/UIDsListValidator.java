package com.mediacallz.server.validators;

import com.mediacallz.server.validators.handlers.uid.UIDValidationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mor on 1/6/2017.
 */
@Component
public class UIDsListValidator implements ConstraintValidator<UidsList, List<String>> {

    @Value("${server.locale}")
    private String serverLocale;

    private Map<String, UIDValidationHandler> locale2HandlerMap = new HashMap<>();;

    @Autowired
    public void initHandlers(List<UIDValidationHandler> handlers) {
        for (UIDValidationHandler handler : handlers) {
            locale2HandlerMap.put(handler.getLocale(), handler);
        }
    }

    @Override
    public void initialize(UidsList uid) {

    }

    @Override
    public boolean isValid(List<String> uidList, ConstraintValidatorContext context) {
        UIDValidationHandler handler = locale2HandlerMap.get(serverLocale);

        if(uidList == null || uidList.isEmpty()) {
            return false;
        }

        for (String uid : uidList) {
            boolean isValid = handler.isValid(uid);

            if(!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(handler.getValidationFailedMessage())
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
