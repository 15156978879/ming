package com.coffeers.app.framework.config;

/**
 * Created by jack on 2017/5/22.
 * 读取ming.properties文件
 */
public interface Config {
    //配置文件
    String CONFIG_FILE="ming.properties";
    //数据库
    String JDBC_DRIVER="ming.framework.jdbc.driver";
    String JDBC_URL="ming.framework.jdbc.url";
    String JDBC_USERNAME="ming.framework.jdbc.username";
    String JDBC_PASSWORD="ming.framework.jdbc.password";

    //ming项目的基础包名
    String APP_BASE_PACKAGE="ming.framework.app.base_package";
    //jsp的基础路径
    String APP_JSP_PATH="ming.framework.app.jsp_path";
    //静态资源文件的基础路径，比如js，css，图片等
    String APP_ASSET_PATH="ming.framework.app.asset_path";


}