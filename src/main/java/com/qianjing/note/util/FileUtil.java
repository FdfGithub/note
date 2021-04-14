package com.qianjing.note.util;

import java.io.File;

public class FileUtil {
    /**
     * 获得文件后缀名
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 删除文件
     * @param file
     */
    public static void delFile(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File tempFile:files){
                delFile(tempFile);
            }
        }else{
            file.delete();
        }
    }

    /**
     * 后缀名判断
     */
    public static boolean isImageByEndName(String fileName){
        //后缀名数组
        String[] endNames = new String[]{
             "bmp","gif","jpg","png","tif"
        };
        for(String endName:endNames){
            if(endName.equals(FileUtil.getSuffix(fileName))){
                return true;
            }
        }
        return false;
    }
}
