package mediacallz.com.server.controllers;

import org.springframework.stereotype.Controller;

/**
 * Created by Mor on 24/08/2016.
 */
@Controller
public abstract class PreRegistrationController extends AbstractController {
    public abstract String getUrl();
}
