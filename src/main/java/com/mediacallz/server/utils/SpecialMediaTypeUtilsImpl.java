package com.mediacallz.server.utils;

import com.mediacallz.server.enums.SpecialMediaType;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 04/06/2017.
 */
@Component
public class SpecialMediaTypeUtilsImpl implements SpecialMediaTypeUtils {
    @Override
    public boolean isDefaultMediaType(SpecialMediaType specialMediaType) {
        return specialMediaType.equals(SpecialMediaType.DEFAULT_PROFILE_MEDIA) || specialMediaType.equals(SpecialMediaType.DEFAULT_CALLER_MEDIA);
    }
}
