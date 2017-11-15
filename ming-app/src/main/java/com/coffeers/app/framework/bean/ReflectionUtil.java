package com.coffeers.app.framework.bean;


import com.coffeers.app.framework.annotation.RequestParam;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by jack on 2017/5/23.
 * 反射工具类,让它封装java反射相关的API,对外提供更好用的工具方法
 */
public class ReflectionUtil {

    private static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);
    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls) {
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
//            LOGGER.error("new instance failue", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        method.setAccessible(true);
        try {
            //获取方法上所有的形参名称
            String[] strings = getMethodParameterNamesByAsm4(obj.getClass(), method);
            String[] paramName =null;
            String[] paramType =null;
            if(strings!=null){
                paramName = new String[strings.length];//形参名称
                paramType = new String[strings.length];//形参类型
                for (int i = 0; i < strings.length; i++) {
                    paramName[i] = strings[i].split("=")[0];
                    paramType[i] = strings[i].split("=")[1];
                }
            }
            logger.info(method+Arrays.toString(strings));
            ArrayList<Object> arrayList = new ArrayList<Object>();
            if(strings!=null){
                for (int i = 0; i < strings.length; i++) {
                    int arraySize = arrayList.size();
                    Class clazz = null;
                    Object o = null;
                    if (!"javax.servlet.http.HttpServletRequest".equals(paramType[i]) && !"javax.servlet.http.HttpServletResponse".equals(paramType[i])&&!"javax.servlet.http.HttpSession".equals(paramType[i])) {
                        clazz = Class.forName(paramType[i]);//包名.类名
                        o = clazz.newInstance();
                    }
                    if(paramMap.size()!=0){
                        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                            if (entry.getKey().equals(paramName[i])) {
                                if ("java.lang.String".equalsIgnoreCase(paramType[i])) {
                                    arrayList.add(entry.getValue() + "");
                                }
                                if ("java.lang.Integer".equalsIgnoreCase(paramType[i])) {
                                    arrayList.add(Integer.parseInt(entry.getValue().toString().trim()));
                                }
                                break;
                            } else if ("javax.servlet.http.HttpServletRequest".equalsIgnoreCase(paramType[i])) {
                                arrayList.add(request);
                                break;
                            } else if ("javax.servlet.http.HttpServletResponse".equalsIgnoreCase(paramType[i])) {
                                arrayList.add(response);
                                break;
                            }else if ("javax.servlet.http.HttpSession".equalsIgnoreCase(paramType[i])) {
                                arrayList.add(request.getSession());
                                break;
                            } else if (paramType[i].contains("modules.models")) {
                                Field[] field = clazz.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
                                for (int j = 0; j < field.length; j++) { // 遍历所有属性
                                    for (Map.Entry<String, Object> entryName : paramMap.entrySet()) {
                                        String name = field[j].getName(); // 获取属性的名字
                                        if (entryName.getKey().equalsIgnoreCase(name)) {
                                            name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                                            String type = field[j].getGenericType().toString(); // 获取属性的类型
                                            if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                                                Method m = clazz.getMethod("get" + name);
                                                String value = (String) m.invoke(o); // 调用getter方法获取属性值
                                                if (value == null) {
                                                    m = clazz.getMethod("set" + name, String.class);
                                                    m.setAccessible(true);
                                                    m.invoke(o, entryName.getValue() + "");
                                                }
                                            }
                                            if (type.equals("class java.lang.Integer")) {
                                                Method m = clazz.getMethod("get" + name);
                                                Integer value = (Integer) m.invoke(o);
                                                if (value == null) {
                                                    m = clazz.getMethod("set" + name, Integer.class);
                                                    m.setAccessible(true);
                                                    m.invoke(o, Integer.parseInt(entryName.getValue() + ""));
                                                }
                                            }
                                            if (type.equals("class java.lang.Boolean")) {
                                                Method m = clazz.getMethod("get" + name);
                                                Boolean value = (Boolean) m.invoke(o);
                                                if (value == null) {
                                                    m = clazz.getMethod("set" + name, Boolean.class);
                                                    m.setAccessible(true);
                                                    m.invoke(o, false);
                                                }
                                            }
                                            if (type.equals("class java.util.Date")) {
                                                Method m = clazz.getMethod("get" + name);
                                                Date value = (Date) m.invoke(o);
                                                if (value == null) {
                                                    m = clazz.getMethod("set" + name, Date.class);
                                                    m.setAccessible(true);
                                                    m.invoke(o, new Date());
                                                }
                                            }
                                            // 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断

                                        }
                                    }
                                }
                                arrayList.add(o);
                                break;
                            }
                        }
                    }else if(strings!=null && strings.length!=0){
                        if ("javax.servlet.http.HttpServletRequest".equalsIgnoreCase(paramType[i])) {
                            arrayList.add(request);
                        } else if ("javax.servlet.http.HttpServletResponse".equalsIgnoreCase(paramType[i])) {
                            arrayList.add(response);
                        } else if ("javax.servlet.http.HttpSession".equalsIgnoreCase(paramType[i])) {
                            arrayList.add(request.getSession());
                        }
                    }
                    if (arrayList.size() == arraySize) {
                        arrayList.add("");
                    }

                }
            }else{
                result = method.invoke(obj);
                return result;
            }
//            for (Object list : arrayList) {
//                logger.info(list.toString());
//            }
            //arrayList
            Object[] args=(Object[])arrayList.toArray();
            result = method.invoke(obj,args);
            /*if (strings.length == 1) {
                result = method.invoke(obj, arrayList.get(0));
            } else if (strings.length == 2) {
                result = method.invoke(obj, arrayList.get(0), arrayList.get(1));
            } else if (strings.length == 3) {
                result = method.invoke(obj, arrayList.get(0), arrayList.get(1), arrayList.get(2));
            } else if (strings.length == 4) {
                result = method.invoke(obj, arrayList.get(0), arrayList.get(1), arrayList.get(2), arrayList.get(3));
            } else if (strings.length == 5) {
                result = method.invoke(obj, arrayList.get(0), arrayList.get(1), arrayList.get(2), arrayList.get(3), arrayList.get(4));
            } else if (strings.length == 6) {
                result = method.invoke(obj, arrayList.get(0), arrayList.get(1), arrayList.get(2), arrayList.get(3), arrayList.get(4), arrayList.get(5));
            } else if (strings.length == 7) {
                result = method.invoke(obj, arrayList.get(0), arrayList.get(1), arrayList.get(2), arrayList.get(3), arrayList.get(4), arrayList.get(5), arrayList.get(6));

            } else {
                result = method.invoke(obj);
            }*/

        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

        /**
         * 设置成员变量的值
         */
    public static void setField(Object obj, Field field,Object value){
        try {
            field.setAccessible(true);
            field.set(obj,value);
        } catch (IllegalAccessException e) {
//            LOGGER.error("set field failue",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定方法的参数名
     *
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表
     */
    public static String[] getMethodParameterNamesByAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        String[] parameterNames = new String[parameterAnnotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestParam) {
                    RequestParam param = (RequestParam) annotation;
                    parameterNames[i++] = param.value();
                }
            }
        }
        return parameterNames;
    }

    /**
     * 获取指定类指定方法的参数名
     *
     * @param clazz 要获取参数名的方法所属的类
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表，如果没有参数，则返回null
     */
    public static String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }

//        for (Method m : clazz.getMethods()) {
//            System.out.println("----------------------------------------");
//            System.out.println(" method: " + m.getName());
//            System.out.println(" return: " + m.getReturnType().getName());
//            for (Parameter p : m.getParameters()) {
//                System.out.println("parameter: " + p.getType().getName() + ", " + p.getName());
//            }
//        }








        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];

        //helloController.class
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = clazz.getResourceAsStream(className);

        //获取参数类型，有序
        Class<?>[] parameter_Types = method.getParameterTypes();
        ArrayList<String> typeList = new ArrayList<String>();
        for (Class<?> clas : parameter_Types) {
            typeList.add(clas.getName());
        }

        try {
            ClassReader classReader = new ClassReader(is);
            StringBuffer sb = new StringBuffer("");
            classReader.accept(new ClassVisitor(Opcodes.ASM5) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    return new MethodVisitor(Opcodes.ASM5) {
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            // 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
                            sb.append("["+index+":"+name+"]");
                            if (Modifier.isStatic(method.getModifiers()) && index <= typeList.size()) {
                                parameterNames[index] = name+"="+typeList.get(index);
                            }else{
                                if (index > 0 && index <= typeList.size()) {//排除方法里的变量
                                    parameterNames[index - 1] = name+"="+typeList.get(index-1);
                                }
                            }
                        }

                    };

                }
            }, 0);
            logger.info(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parameterNames;
    }
}
