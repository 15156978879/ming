package com.coffeers.app.framework.dao;

import com.coffeers.app.framework.service.DatabaseHelper;
import com.coffeers.app.framework.utils.io.ReadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25 0025.
 */
public class SqlUtil {

    private static Logger logger = LoggerFactory.getLogger(SqlUtil.class);

    /**
     * 读取 SQL 文件，获取 SQL 语句
     * @param sqlFile SQL 脚本文件
     * @return List<sql> 返回所有 SQL 语句的 List
     * @throws Exception
     */
    private List<String> loadSql(String sqlFile) throws Exception {
        List<String> sqlList = new ArrayList<String>();
        try {
            InputStream sqlFileIn = new FileInputStream(sqlFile);
            StringBuffer sqlSb = new StringBuffer();
            byte[] buff = new byte[1024];
            int byteRead = 0;
            while ((byteRead = sqlFileIn.read(buff)) != -1) {
                sqlSb.append(new String(buff, 0, byteRead));
            }
            // Windows 下换行是 /r/n, Linux 下是 /n
            String[] sqlArr = sqlSb.toString().split("(;//s*//r//n)|(;//s*//n)");
            for (int i = 0; i < sqlArr.length; i++) {
                String sql = sqlArr[i].replaceAll("--.*", "").trim();
                if (!sql.equals("")) {
                    sqlList.add(sql);
                }
            }
            return sqlList;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 读取 SQL 文件，获取 SQL 语句  一次读取一行
     * @param sqlFile SQL 脚本文件
     * @return List<sql> 返回所有 SQL 语句的 List
     * @throws Exception
     */
    private List<String> loadSqlLine(String sqlFile) throws Exception {
        List<String> sqlList = new ArrayList<String>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            String str = "";
            String str1 = "";
            fis = new FileInputStream(sqlFile);// FileInputStream
            // 从文件系统中的某个文件中获取字节
            isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
            br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
            while ((str = br.readLine()) != null) {
                str1 += str + "\r\n";
                if(!"".equals(str)){
                    sqlList.add(str);
                }
            }
            // 当读取的一行不为空时,把读到的str的值赋给str1
            //System.out.println(str1);// 打印出str1
            return sqlList;
        }  catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        } finally {
            try {
                br.close();
                isr.close();
                fis.close();
                // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 自建连接，独立事物中执行 SQL 文件   执行多条
     * @throws Exception
     */
    public void execute(List listSql) throws Exception {
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = null;
        List<String> sqlList = listSql;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (String sql : sqlList) {
                stmt.addBatch(sql);
                logger.info(sql);
            }
            int[] rows = stmt.executeBatch();
            logger.info("Row count:" + Arrays.toString(rows));
            DatabaseHelper.commitTransaction();
        } catch (Exception ex) {
            DatabaseHelper.rollbackTransaction();
            throw ex;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 传入连接来执行 SQL 脚本文件，这样可与其外的数据库操作同处一个事物中
     * @param conn 传入数据库连接
     * @param sqlFile SQL 脚本文件
     * @throws Exception
     */
    public void execute(Connection conn, String sqlFile) throws Exception {
        Statement stmt = null;
        List<String> sqlList = loadSql(sqlFile);
        stmt = conn.createStatement();
        for (String sql : sqlList) {
            stmt.addBatch(sql);
        }
        int[] rows = stmt.executeBatch();
        System.out.println("Row count:" + Arrays.toString(rows));
    }

    /**
     * 自建连接，独立事物中执行 SQL 文件
     * @param sqlFile SQL 脚本文件
     * @throws Exception
     */
    public void execute(String sqlFile) throws Exception {
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = null;
        List<String> sqlList = loadSql(sqlFile);
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (String sql : sqlList) {
                stmt.addBatch(sql);
            }
            int[] rows = stmt.executeBatch();
            System.out.println("Row count:" + Arrays.toString(rows));
            DatabaseHelper.commitTransaction();
        } catch (Exception ex) {
            DatabaseHelper.rollbackTransaction();
            throw ex;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception e) {

            }
        }
    }

    private String getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        //getResource()方法会去classpath下找这个文件，获取到url resource, 得到这个资源后，调用url.getFile获取到 文件 的绝对路径
        URL url = classLoader.getResource(fileName);
        //url.getFile() 得到这个文件的绝对路径
        //System.out.println(url.getFile());
        //File file = new File(url.getFile());
        //System.out.println(file.exists());
        return url.getFile();
    }

    public void create(List<String> list){
        String realPath = this.getClass().getResource("/sql").getPath();

        try {
            List<String> fileList = ReadFile.readfile(realPath);
            SqlUtil sqlUtil = new SqlUtil();
            for (String file : fileList){
                if(list!=null&&list.size()!=0){
                    String path = sqlUtil.getFile("sql/"+file);
                    list.addAll(sqlUtil.loadSqlLine(path));
                }
            }
            logger.info("size:" + list.size());
            sqlUtil.execute(list);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
