package com.mediacallz.server.logic;

import com.mediacallz.server.lang.StringsFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Created by Mor on 1/15/2017.
 */
@Component
public abstract class AbstractServerLogic implements ServerLogic {

    @Autowired
    protected StringsFactory stringsFactory;
}
