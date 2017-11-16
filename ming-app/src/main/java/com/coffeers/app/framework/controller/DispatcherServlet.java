package com.coffeers.app.framework.controller;

import com.coffeers.app.framework.annotation.Json;
import com.coffeers.app.framework.annotation.Result;
import com.coffeers.app.framework.bean.BeanHelper;
import com.coffeers.app.framework.bean.ReflectionUtil;
import com.coffeers.app.framework.config.Config;
import com.coffeers.app.framework.config.ConfigHelper;
import com.coffeers.app.framework.config.PropsUtil;
import com.coffeers.app.framework.controller.utils.CodeUtil;
import com.coffeers.app.framework.controller.utils.StreamUtil;
import com.coffeers.app.framework.ioc.ArrayUtil;
import com.coffeers.app.framework.main.HelperLoader;
import com.coffeers.app.framework.utils.StringUtil;
import org.beetl.ext.servlet.ServletGroupTemplate;
import org.beetl.json.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jack on 2017/6/27.
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    /**
     * 初始化init函数
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        //super.init(config);
        //初始化相关Helper类
        HelperLoader.init();
        //获取ServletContext对象，用于注册servlet
        ServletContext servletContext = config.getServletContext();
        //注册处理jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //注册处理静态资源的默认servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");

        // 然后这个是所有的servlet . 里面有我们自己的 和 框架注册的
        Map<String, ? extends ServletRegistration> servletRegistrations = servletContext.getServletRegistrations();
    }

    /**
     * servlet的核心处理方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        //super.service(req, resp);
        //获取请求方法与请求路径
        String requestMethod = req.getMethod().toUpperCase();
        String requestPath = req.getPathInfo();// 获取请求路径
        logger.info("mapping method and path ::: "+requestMethod+" --> "+requestPath);//mapping method and path ::: get --> /index

        //获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);// 获取对应的处理类


        if (handler != null){//找到了对应的处理器
            logger.info("Handler mapping controller ::: "+handler+"("+handler.getControllerClass()+" , "+handler.getActionMethod()+")");

            //获取Controller类及其bean实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass); // 处理器实例
            //创建请求参数对象(目前不支持 一参多值)
            Map<String,Object> paramMap = new HashMap<String, Object>();
            //获取请求所有请求参数的名字
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                //String[] paramValue = req.getParameterValues(paramName);
                paramMap.put(paramName,paramValue);
            }
            //获取url后面的参数
            String body = CodeUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String [] params = StringUtil.splitString(body,"&");
                if (ArrayUtil.isNotEmpty(params)){
                    for (String param : params){
                        String [] array = StringUtil.splitString(param,"=");
                        if (ArrayUtil.isNotEmpty(array) && array.length == 2){
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            //通过获取到的参数，创建参数对象
            Param param = new Param(paramMap);
            logger.info("RequestParam ::: "+param.toString()+" size:"+param.getParamMap().size()+" param:"+param.getParamMap().toString());
            //调用At方法
            Method actionMethod = handler.getActionMethod();
            //判断当前方法是否有Result注解
            String resultValue = "";
            if (actionMethod.isAnnotationPresent(Result.class)){
                Result result = actionMethod.getAnnotation(Result.class);
                resultValue = result.value();
            }


            //返回值
            Object result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,paramMap,req,resp);
            if (actionMethod.isAnnotationPresent(Json.class)){
                Json json = actionMethod.getAnnotation(Json.class);
                JsonTool tool = new JsonTool();
                String jsonString = tool.serialize(result);
                logger.info("Json ::: "+jsonString);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter printWriter = resp.getWriter();
                printWriter.write(jsonString);
                printWriter.flush();
                printWriter.close();
            }else if("beetl".equalsIgnoreCase(resultValue) || "".equalsIgnoreCase(resultValue)){
                logger.info("Forward ::: "+result+".html");
                //转发
                req.setAttribute("base", "ming");
                ServletGroupTemplate.instance().render(result+".html", req, resp);
            }

            //重定向
            if("re".equalsIgnoreCase(resultValue)){
                logger.info("Redirect ::: "+req.getContextPath()+result);
                resp.sendRedirect(req.getContextPath()+result);
            }
        }else{
            logger.info("Handler mapping controller ::: "+handler);
        }
    }
}