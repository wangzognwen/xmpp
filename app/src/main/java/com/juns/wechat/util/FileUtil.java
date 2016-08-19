package com.juns.wechat.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by 王者 on 2016/8/19.
 */
public class FileUtil {

    public static InputStream readFile(File file){
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            return in;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
