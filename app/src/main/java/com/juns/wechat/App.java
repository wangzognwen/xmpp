package com.juns.wechat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;


import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

import cn.smssdk.SMSSDK;

public class App extends Application {

	private static Context _context;

	@Override
	public void onCreate() {
		super.onCreate();
		_context = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
        SMSSDK.initSDK(this, Constants.MOB_SDK_KEY, Constants.MOB_SDK_SECRET);
        // 百度MAP sdk initialize
        SDKInitializer.initialize(this);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
	}


    /**
     * 自定义异常处理类
     */
    class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
        /*
         * @param thread the thread that has an uncaught exception
         * @param ex the exception that was thrown
         */
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            String errorLogPath = Constants.ERROR_LOG_PATH;
            if (errorLogPath != null) {
                File file = new File(errorLogPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                try {
                    FileOutputStream fos = new FileOutputStream(errorLogPath + "errorlog.txt", true);
                    PrintStream ps = new PrintStream(fos);
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatDate = sdf.format(date);
                    ps.append("-------------------crash time ：");
                    ps.append(formatDate);
                    ps.append("-------------------\n");
                    ex.printStackTrace(ps);
                    ps.append("\n");
                    ps.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Process.killProcess(Process.myPid());
        }
    }


	@Override
	public void onLowMemory() {
		super.onLowMemory();
		try {
			deleteCacheDirFile(getHJYCacheDir(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.gc();
	}

	public static Context getInstance() {
		return _context;
	}

	public static String getHJYCacheDir() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().toString()
					+ "/Health/Cache";
		else
			return "/System/com.juns.Walk/Walk/Cache";
	}

	public static void deleteCacheDirFile(String filePath,
			boolean deleteThisPath) throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteCacheDirFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}
}
