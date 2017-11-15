package com.coffeers.app.framework.dao.impl;

import com.coffeers.app.framework.dao.BaseDao;
import com.coffeers.app.framework.dao.Cnd;
import com.coffeers.app.framework.loadclass.ClassHelper;
import com.coffeers.app.framework.service.DatabaseHelper;
import com.coffeers.app.hello.modules.models.Hello;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Administrator on 2017/8/21 0021.
 */
public class BaseDaoImpl<T> implements BaseDao<T>{
    private static Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    private QueryRunner queryRunner = new QueryRunner(); //线程安全 ，直接new

    private Class<T> clazz; // 获取实体类

    private T t;

    /** 操作常量 */
    public static final String SQL_INSERT = "insert";
    public static final String SQL_UPDATE = "update";
    public static final String SQL_DELETE = "delete";
    public static final String SQL_SELECT = "select";

    private PreparedStatement statement;

    private String sql;

    private Object argType[];

    private ResultSet rs;

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        clazz = (Class<T>) type.getActualTypeArguments()[0];
    }

    @Override
    public void add(T t) {
        // TODO Auto-generated method stub
        sql = this.getSql(SQL_INSERT,null);   //获取sql.
        // 赋值.
        try {
            argType = setArgs(t, SQL_INSERT);
            statement = DatabaseHelper.getPreparedStatement(sql);  //实例化PreparedStatement.
            //为sql语句赋值.
            statement = DatabaseHelper.setPreparedStatementParam(statement,argType);
            statement.executeUpdate(); //执行语句.
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DatabaseHelper.release(statement, null);  //释放资源.
        }
    }

    @Override
    public void delete(T t,Cnd cnd) {
        // TODO Auto-generated method stub
        sql = this.getSql(SQL_DELETE,cnd);
        try {
            argType = this.setArgs(t, SQL_DELETE);
            statement = DatabaseHelper.getPreparedStatement(sql);
            statement = DatabaseHelper.setPreparedStatementParam(statement,argType);
            statement.executeUpdate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DatabaseHelper.release(statement, null);
        }
    }

    @Override
    public void update(T t,Cnd cnd) {
        // TODO Auto-generated method stub
        sql = this.getSql(SQL_UPDATE,cnd);
        try {
            argType = setArgs(t, SQL_UPDATE);
            statement = DatabaseHelper.getPreparedStatement(sql);
            statement = DatabaseHelper.setPreparedStatementParam(statement,argType);
            statement.executeUpdate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DatabaseHelper.release(statement, null);
        }
    }

    @Override
    public T select(T t,Cnd cnd) {
        // TODO Auto-generated method stub
        sql = this.getSql(SQL_SELECT,cnd);
        T obj = null;
        try {
            argType = setArgs(t, SQL_SELECT);
            statement = DatabaseHelper.getPreparedStatement(sql);
            statement = DatabaseHelper.setPreparedStatementParam(statement,
                    argType);
            rs = statement.executeQuery();
            Field fields[] = clazz.getDeclaredFields();
            while (rs.next()) {
                obj = clazz.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fields[i].set(obj, rs.getObject(fields[i].getName()));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;

    }

    @Override
    public void delete(T t) {
        delete(t,null);
    }

    @Override
    public void update(T t) {
        update(t,null);
    }

    @Override
    public T select(T t) {
        return select(t,null);
    }

    @Override
    public T fetch(T t) {
        return null;
    }

    @Override
    public T fetch(T t, Cnd cnd) {
        return null;
    }

    @Override
    public List<T> query(T t) {
        return null;
    }

    @Override
    public List<T> query(T t, Cnd cnd) {
        return null;
    }

    @Override
    public List<T> nativeSql(String sql) {
        return null;
    }

    // sql拼接函数 形如 : insert into User(id,username,password,email,grade) values(?,?,?,?,?)
    private String getSql(String operator,Cnd cnd) {
        String cndSql = "";
        if(cnd!=null){
            cndSql = " or ("+cnd.getSql()+") ";
        }
        StringBuffer sql = new StringBuffer();
        // 通过反射获取实体类中的所有变量
        Field fields[] = clazz.getDeclaredFields();
        // 插入操作
        if (operator.equals(SQL_INSERT)) {
            sql.append("insert into " + clazz.getSimpleName());
            sql.append("(");
            for (int i = 0; fields != null && i < fields.length; i++) {
                fields[i].setAccessible(true);    //这句话必须要有,否则会抛出异常.
                String column = fields[i].getName();
                sql.append(column).append(",");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
            sql.append(") values (");
            for (int i = 0; fields != null && i < fields.length; i++) {
                sql.append("?,");
            }
            sql.deleteCharAt(sql.length() - 1);
            // 是否需要添加分号
            sql.append(") ");
        } else if (operator.equals(SQL_UPDATE)) {
            sql.append("update " + clazz.getSimpleName() + " set ");
            for (int i = 0; fields != null && i < fields.length; i++) {
                fields[i].setAccessible(true);
                String column = fields[i].getName();
                if (column.equals("id")) {
                    continue;
                }
                sql.append(column).append("=").append("?,");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" where id=? "+cndSql);
        } else if (operator.equals(SQL_DELETE)) {
            sql.append("delete from " + clazz.getSimpleName()
                    + " where id=? "+cndSql);
        } else if (operator.equals(SQL_SELECT)) {
            sql.append("select * from " + clazz.getSimpleName()
                    + " where id=? "+cndSql);
        }
        logger.info(sql.toString());
        return sql.toString();
    }

    // 获取参数.
    private Object[] setArgs(T entity, String operator)
            throws IllegalArgumentException, IllegalAccessException {

        Field fields[] = clazz.getDeclaredFields();
        if (operator.equals(SQL_INSERT)) {

            Object obj[] = new Object[fields.length];
            for (int i = 0; obj != null && i < fields.length; i++) {
                fields[i].setAccessible(true);
                obj[i] = fields[i].get(entity);
            }
            return obj;

        } else if (operator.equals(SQL_UPDATE)) {

            Object Tempobj[] = new Object[fields.length];
            for (int i = 0; Tempobj != null && i < fields.length; i++) {
                fields[i].setAccessible(true);
                Tempobj[i] = fields[i].get(entity);
            }

            Object obj[] = new Object[fields.length];
            System.arraycopy(Tempobj, 1, obj, 0, Tempobj.length - 1);
            obj[obj.length - 1] = Tempobj[0];
            return obj;

        } else if (operator.equals(SQL_DELETE)) {

            Object obj[] = new Object[1];
            fields[0].setAccessible(true);
            obj[0] = fields[0].get(entity);
            return obj;
        } else if (operator.equals(SQL_SELECT)) {

            Object obj[] = new Object[1];
            fields[0].setAccessible(true);
            obj[0] = fields[0].get(entity);
            return obj;
        }
        return null;
    }


}
