package com.coffeers.app.framework.dao;

import com.coffeers.app.framework.annotation.entity.*;
import com.coffeers.app.framework.enums.ColType;
import com.coffeers.app.framework.loadclass.ClassUtil;
import com.coffeers.app.framework.service.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25 0025.
 */
public class CrateTable {

    private static Logger logger = LoggerFactory.getLogger(CrateTable.class);

    public static List<String> createTable(){
        List<String> entityList = ClassUtil.entityList;

        Connection con = DatabaseHelper.getConnection();
        ResultSet rs = null;
        List<String> sqlList = new ArrayList<>();
        try {
            if(entityList!=null){
                for (String entity : entityList){
                    String sql = getCreateSQL(entity);
                    String[] tableName = sql.split(" ",4);
                    rs = con.getMetaData().getTables(null, null, tableName[2], null);
                    if (rs.next()) {//yourTable exist
                        logger.info("table "+tableName[2]+" exist!");
                    }else {//yourTable not exist
                        logger.info("table "+tableName[2]+" no exist!");
                        sqlList.add(sql);
                    }
                }
                try {
                    return sqlList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    @SuppressWarnings("unused")
    public static String getCreateSQL(String className){
        try {
            // 加载实体类
            Class<?> c = Class.forName(className);
            // 获得指定类型的注解对象
            Entity entity = c.getAnnotation(Entity.class);
            if(entity == null){
                System.out.println("No Table annotation in class : "+ className);
                return null;
            }
            String tableName = entity.value();
            if(tableName.length() == 0){
                // 如果没指定长度， 可以默认以类的名字 命名表名
                tableName = c.getName().toLowerCase();
            }
            List<String> columns = new ArrayList<String>();

            // 遍历所有字段
            for(Field field : c.getDeclaredFields()){
                String columnName = "";
                String columnType0 = "";
                ColType columnType = null;
                String columnWidth = "";
                String columnComment = "";
                String columnDefault = "";
                // 获得每个字段上的注解信息,这里不需要继承的注解
                Annotation[] anns = field.getDeclaredAnnotations();
                if(anns.length == 0){
                    continue;// 如果该字段没有注解，表示这个字段，不需要生成
                }else{
                    Id id = field.getAnnotation(Id.class);
                    Column column = field.getAnnotation(Column.class);
                    ColumnType type = field.getAnnotation(ColumnType.class);
                    Comment comment = field.getAnnotation(Comment.class);
                    Default defaultValue = field.getAnnotation(Default.class);

                    String constraint = "";
                    if(id!=null){
                        constraint = getConstraints(id.constraints());//获取约束
                    }

                    if(column!=null){
                        String colName = column.value();
                        if(colName.length() == 0){
                            columnName = field.getName();// 获得默认字段名
                        }else{
                            columnName = colName;
                        }
                    }

                    if(type!=null){
                        if(type.type() == null){
                            // 获得默认类型
                            //columnType0 = field.getType().toString();
                            columnType = ColType.VARCHAR;
                        }else{
                            columnType = type.type();
                        }
                        if(type.width() == 0){
                            // 获取默认长度
                            columnWidth = "32";
                        }else{
                            columnWidth = type.width()+"";
                        }
                    }

                    if(comment != null){
                        columnComment = "COMMENT '"+comment.value()+"'";
                    }else{
                        columnComment = "";
                    }

                    if(defaultValue!=null){
                        columnDefault = "DEFAULT '"+defaultValue.value()+"'";
                    }
                    columns.add(columnName + " "+ columnType+"("+columnWidth+") "+columnDefault+" "+columnComment+" "+constraint);

                }
            }
            if(columns.size() == 0){
                throw new RuntimeException("There is no field in "+className);
            }
            StringBuilder createCommand = new StringBuilder("CREATE TABLE "+tableName +" (");
            for(String column : columns){
                createCommand.append("\n "+column +" ,");
            }
            String createTable = createCommand.substring(0,createCommand.length()-1)+" \n );";
            return createTable;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得约束条件
     * @param con
     * @return
     */
    private static String getConstraints(Constraints con){
        String constraints = "";
        if(!con.allowNull()){
            constraints += " NOT NULL";
        }
        if(con.primaryKey()){
            constraints += " PRIMARY KEY";
        }
        if(con.unique()){
            constraints += " UNIQUE ";
        }
        return constraints;
    }

    /**
     * 获得所需要的字段
     * @param fields
     * @return
     */
    public static List<Field> getNeedField(Field[] fields){
        List<Field> allFileds = new ArrayList<Field>();
        for(Field field : fields){
            // 获得每个字段上的注解信息,这里不需要继承的注解
            Annotation[] anns = field.getDeclaredAnnotations();
            if(anns.length != 0){
                // 如果该字段没有注解，表示这个字段，不需要生成
                allFileds.add(field);
            }
        }
        return allFileds;
    }
}
