package com.coffeers.app.framework.utils.io;

import com.coffeers.app.framework.dao.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25 0025.
 */
public class ReadFile {
    private static Logger logger = LoggerFactory.getLogger(ReadFile.class);

    public ReadFile() {
    }
    /**
     * 读取某个文件夹下的所有文件
     */
    public static List<String> readfile(String filepath) throws FileNotFoundException, IOException {
        List<String> list = new ArrayList<>();
        try {
            File file = new File(filepath);
            if (file.isDirectory()) {//文件夹
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
//                        System.out.println("path=" + readfile.getPath());
//                        System.out.println("absolutepath=" + readfile.getAbsolutePath());
//                        System.out.println("name=" + readfile.getName());
                        logger.info("load sql file "+readfile.getName()+" ("+readfile.getPath()+")");
                        list.add(readfile.getName());
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return list;
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     */


        /*public static boolean deletefile(String delpath)
                        throws FileNotFoundException, IOException {
                try {

                        File file = new File(delpath);
                        if (!file.isDirectory()) {
                                System.out.println("1");
                                file.delete();
                        } else if (file.isDirectory()) {
                                System.out.println("2");
                                String[] filelist = file.list();
                                for (int i = 0; i < filelist.length; i++) {
                                        File delfile = new File(delpath + "\\" + filelist[i]);
                                        if (!delfile.isDirectory()) {
                                                System.out.println("path=" + delfile.getPath());
                                                System.out.println("absolutepath="
                                                                + delfile.getAbsolutePath());
                                                System.out.println("name=" + delfile.getName());
                                                delfile.delete();
                                                System.out.println("删除文件成功");
                                        } else if (delfile.isDirectory()) {
                                                deletefile(delpath + "\\" + filelist[i]);
                                        }
                                }
                                file.delete();

                        }

                } catch (FileNotFoundException e) {
                        System.out.println("deletefile()   Exception:" + e.getMessage());
                }
                return true;
        }*/

    public static void main(String[] args) {
        try {
            readfile("e:/videos");
            // deletefile("D:/file");
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        System.out.println("ok");
    }

}