package com.mediacallz.server.lang;

import org.springframework.stereotype.Component;

/**
 * Created by Mor on 21/04/2016.
 */
@Component
public class IvritStrings extends AbstractStrings {
    @Override
    public Languages getLanguage() {
        return Languages.IVRIT;
    }

}
