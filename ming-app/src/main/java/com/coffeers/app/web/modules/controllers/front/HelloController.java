package com.coffeers.app.web.modules.controllers.front;

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
@At("/hello")
public class HelloController {

    private static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Inject
    private HelloService helloService;

    //转发
    //localhost:8080/hello/index
    @At(value = "/index",method = "get")
    public String index(HttpServletRequest request){
        request.setAttribute("currentTime",new Date());
        return "hello";
    }

    //返回json
    //localhost:8080/hello/getJson?name=wang&age=25
    @At("/getJson")
    @Json
    public Object getJson(Hello hello, HttpServletRequest request){
        System.out.println(hello);
        return hello;
    }

    //重定向
    //localhost:8080/hello/hello/aa?a=1&b=2&c=3&name=wang&age=25
    @At("/hello/aa")
    @Result("re")
    public String hello(String a,String c, String b, Hello hello, HttpServletRequest request, HttpServletResponse response){
        System.out.println("a:"+a);
        System.out.println("c:"+c);
        System.out.println("b:"+b);

        System.out.println("hello:"+hello);
        System.out.println("request:"+request);
        System.out.println("response:"+response);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("1",hello);
        map.put("2",hello);
        System.out.println("0000000000000000000000");
        hello = new Hello();
        hello.setName("zhangsan");
        hello.setAge(22);

        Cnd cnd = Cnd.NEW();
        cnd.where("name","=","zhangsan");
        cnd.and("age","=",22).or("age","=",25);

        System.out.println(helloService.insert(hello,cnd));

        return "/hello/index";
    }


}
