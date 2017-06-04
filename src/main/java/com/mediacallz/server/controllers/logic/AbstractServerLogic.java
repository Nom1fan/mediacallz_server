package com.mediacallz.server.controllers.logic;

import com.mediacallz.server.lang.StringsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public abstract class AbstractServerLogic implements ServerLogic {

    @Autowired
    protected StringsFactory stringsFactory;
}
