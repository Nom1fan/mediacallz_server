package com.mediacallz.server.controllers;

import com.mediacallz.server.lang.StringsFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

/**
 * Created by Mor on 20/08/2016.
 */
public abstract class AbstractController {

    @Autowired
    protected Logger logger;

    @Autowired
    protected StringsFactory stringsFactory;


}
