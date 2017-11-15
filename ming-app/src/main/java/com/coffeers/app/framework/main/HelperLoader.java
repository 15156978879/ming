package com.coffeers.app.framework.main;

import com.coffeers.app.framework.annotation.entity.Entity;
import com.coffeers.app.framework.aop.AopHelper;
import com.coffeers.app.framework.bean.BeanHelper;
import com.coffeers.app.framework.controller.ControllerHelper;
import com.coffeers.app.framework.dao.CrateTable;
import com.coffeers.app.framework.dao.SqlUtil;
import com.coffeers.app.framework.ioc.IocHelper;
import com.coffeers.app.framework.loadclass.ClassHelper;
import com.coffeers.app.framework.loadclass.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载相应类的，帮助类
 */
public class HelperLoader {
    private static Logger logger = LoggerFactory.getLogger(HelperLoader.class);

    //初始化，加载类
    public static void init(){
        //DispatcherServlet中调用
        logger.info("loading...");
        System.out.println("  ____    __    ____  ___      .__   __.   _______ ");
        System.out.println("  \\   \\  /  \\  /   / /   \\     |  \\ |  |  /  _____|");
        System.out.println("   \\   \\/    \\/   / /  ^  \\    |   \\|  | |  |  __  ");
        System.out.println("    \\            / /  /_\\  \\   |  . `  | |  | |_ | ");
        System.out.println("     \\    /\\    / /  _____  \\  |  |\\   | |  |__| | ");
        System.out.println("      \\__/  \\__/ /__/     \\__\\ |__| \\__|  \\______| ");

        ClassUtil.entityList = new ArrayList<>();
        //AopHelper要在IocHelper之前加载,因为首先需要通过AopHelper获取代理对象，然后才能通过IocHelper进行依赖注入
        Class<?> [] classList = {ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class};
        for (Class<?> cls: classList) {
            Class clazz = ClassUtil.loadClass(cls.getName(),true);
        }

        List<String> list =  CrateTable.createTable();
        new SqlUtil().create(list);

        logger.info("load complete");
//        System.out.println("       __    __ .__..__   __.   _______ ");
//        System.out.println("      /  \\  /  ||  ||  \\ |  |  /  _____|");
//        System.out.println("     /    \\/   ||  ||   \\|  | |  |  __  ");
//        System.out.println("    /          ||  ||  . `  | |  | |_ | ");
//        System.out.println("   /   /\\   /| ||  ||  |\\   | |  |__| | ");
//        System.out.println("  /___/  \\_/ |_||__||__| \\__|  \\______| ");

        System.out.println("\t   __    __ .__..__   __.   _______    .     _____    ______   .______  .______  .______   _______    ______ ");
        System.out.println("      /  \\  /  ||  ||  \\ |  |  /  _____|   |    /  ___|  /  __  \\  |  ____| |  ____| |  ____| |   ___ \\  |  ____|");
        System.out.println("\t /    \\/   ||  ||   \\|  | |  |  __.    |   |  |     |  |  |  . | |____  | |____  | |____  |  |___| | | |____ ");
        System.out.println("\t/          ||  ||  . `  | |  | |_ |    |   |  |     |  |  |  | |  ____| |  ____| |  ____| |   _   _| |____  |");
        System.out.println("   /   /\\   /| ||  ||  |\\   | |  |__| |    |   |  |___  |  |__|  . | |      | |      | |____  |  | \\  \\   ____| |");
        System.out.println("  /___/  \\_/ |_||__||__| \\__|  \\______|    |    \\_____|  \\______/  |_|      |_|      |______| |__|  \\__\\ |______|");
    }
}
/*
  ____    __    ____  ___      .__   __.   _______
  \   \  /  \  /   / /   \     |  \ |  |  /  _____|
   \   \/    \/   / /  ^  \    |   \|  | |  |  __
    \            / /  /_\  \   |  . `  | |  | |_ |
     \    /\    / /  _____  \  |  |\   | |  |__| |
      \__/  \__/ /__/     \__\ |__| \__|  \______|


	   __    __ .__..__   __.   _______
      /  \  /  ||  ||  \ |  |  /  _____|
	 /    \/   ||  ||   \|  | |  |  __
	/          ||  ||  . `  | |  | |_ |
   /   /\   /| ||  ||  |\   | |  |__| |
  /___/  \_/ |_||__||__| \__|  \______|


	   __    __ .__..__   __.   _______    .     _____    ______   .______  .______  .______   _______    ______
      /  \  /  ||  ||  \ |  |  /  _____|   |    /  ___|  /  __  \  |  ____| |  ____| |  ____| |   ___ \  |  ____|
	 /    \/   ||  ||   \|  | |  |  __.    |   |  |     |  |  |  . | |____  | |____  | |____  |  |___| | | |____
	/          ||  ||  . `  | |  | |_ |    |   |  |     |  |  |  | |  ____| |  ____| |  ____| |   _   _| |____  |
   /   /\   /| ||  ||  |\   | |  |__| |    |   |  |___  |  |__|  . | |      | |      | |____  |  | \  \   ____| |
  /___/  \_/ |_||__||__| \__|  \______|    |    \_____|  \______/  |_|      |_|      |______| |__|  \__\ |______|
*/