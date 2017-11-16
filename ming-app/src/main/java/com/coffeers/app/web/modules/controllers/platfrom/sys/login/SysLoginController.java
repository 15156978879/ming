package com.coffeers.app.web.modules.controllers.platfrom.sys.login;

import com.coffeers.app.framework.annotation.*;
import com.coffeers.app.framework.dao.Cnd;
import com.coffeers.app.modular.index.modules.models.Hello;
import com.coffeers.app.modular.index.modules.services.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Controller
@At("/sys")
public class SysLoginController {

    private static Logger logger = LoggerFactory.getLogger(SysLoginController.class);

    @Inject
    private HelloService helloService;

    @At(value = "/login")
    public String index(HttpServletRequest request){

        return "platform/pc/sys/login/index";
    }


}
