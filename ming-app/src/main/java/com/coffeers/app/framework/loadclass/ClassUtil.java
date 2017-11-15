package com.coffeers.app.framework.loadclass;

import com.coffeers.app.framework.annotation.entity.Entity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类操作工具类
 */
public final class ClassUtil {
    private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    public static List<String> entityList = new ArrayList<>();

    /**
     * 获取类加载器
     * 获取加载器类的实现比较简单，只需获取当前线程的ClassLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * 加载类需要提供类名与是否初始化的标志，这里提到的初始化指是否执行类的静态代码块;
     * 为了提高加载类的性能，可以将loadClass方法的isInitialized参数设置false
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls = null;
        try {
            //进行类加载
            // 该方法类加载器有值的时候调用的是原生方法
            cls = Class.forName(className, isInitialized, getClassLoader());
            logger.info(cls.getName());

            // 获得指定类型的注解对象
            Entity entity = cls.getAnnotation(Entity.class);
            if(entity!=null){
                entityList.add(cls.getName());
            }
        } catch (ClassNotFoundException e) {
            logger.error("load class failure.", e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 获取指定包名下所有的类；
     * 获取指定包名下所有的类，需要根据包名并将其转换为文件路径，读取class文件或jar包，获取指定的类名去加载类
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        /**
         * 1. 获取指定包名下的的所有类
         * 2. 根据包名将其转换为文件路径
         * 3. 读取class文件或jar包
         * 4. 获取指定的类名去加载类
         */
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();////获取此 URL 的协议名称
                    if ("file".equals(protocol)) {
                        String packagePath = url.getPath().replace("%20", "");
                        addClass(classSet, packagePath, packageName);
                    } else if ("jar".equals(protocol)) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
                                                .replaceAll("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("get class set failure.", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * 如果是文件,就根据包名 和 文件名 组成类的全限定名称,然后 加载类
     * @param classSet
     * @param packagePath 文件(夹)的绝对路径
     * @param packageName 和当前文件(夹) 对应的包名
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                // 只需要 文件并且是.class的文件,或则是目录 都返回true
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {// 是指定的文件 就获取到全限定类名 然后装载它
                String className = fileName.substring(0, fileName.lastIndexOf("."));// 把.class后最截取掉
                if (StringUtils.isNotEmpty(packageName)) {
                    className = packageName + "." + className;// 根据包名 + 文件名 得到这个类的全限定名称
                }
                doAddClass(classSet, className);
            } else {// 是文件 就递归自己. 获取 文件夹的绝对路径,和 当前文件夹对应的 限定包名.方便 文件里面直接使用
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packageName)){
                    subPackagePath = packagePath +"/"+subPackagePath;// 第一次:由基础包名 得到绝对路径,再加上当前文件夹名称 = 当前文件夹的绝对路径
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)){
                    subPackageName = packageName +"."+subPackageName;// 第一次: 基础包名 加文件夹名称 组合成 当前包名 +
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }

    /**
     * 加载类,并把该类对象 添加到集合中
     * @param classSet
     * @param className
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }
}