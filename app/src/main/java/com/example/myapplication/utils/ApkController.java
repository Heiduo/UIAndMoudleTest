package com.example.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.example.myapplication.service.DownloadService;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * 描述：app静默安装
 *
 * @author Created by heiduo
 * @time Created on 2020/1/6
 */
public class ApkController {
    private static final String TAG = "ApkController";
    /**
     * 描述: 安装
     */
    public static boolean install(String apkPath, Context context){
        // 先判断手机是否有root权限
        if(hasRootPerssion()){
            Logger.d(TAG,"root权限获取成功");
            // 有root权限，利用静默安装实现
            return clientInstall(apkPath);
        }
        else{
            Logger.d(TAG,"没有root权限");
            // 没有root权限，利用意图进行安装
            File file = new File(apkPath);
            if(!file.exists())
                return false;
            Intent intent = new Intent();
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context,
                        context.getApplicationInfo().packageName + ".fileProvider",file);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//重点！！
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            } else {
                uri = Uri.fromFile(file);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setAction(Intent.ACTION_VIEW);
//            intent.addCategory("android.intent.category.DEFAULT");
            intent.setDataAndType(uri,"application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
    }

    /**
     * 描述: 卸载
     */
    public static boolean uninstall(String packageName,Context context){
        if(hasRootPerssion()){
            // 有root权限，利用静默卸载实现
            return clientUninstall(packageName);
        }else{
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
            return true;
        }
    }

    /**
     * 判断手机是否有root权限
     */
    public static boolean hasRootPerssion(){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
//        return false;
    }

    /**
     * 静默安装
     */
    public static boolean clientInstall(String apkPath){
        Logger.e(TAG,"apkPath:"+apkPath);
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 "+apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r "+apkPath);//-r 重新安装应用，保留应用数据
            UIUtils.showToastSafe("安装中，稍后重启");
            execLinuxCommand();
          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 静默卸载
     */
    public static boolean clientUninstall(String packageName){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall "+packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }

    private static boolean returnResult(int value){
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }

    /**
     * 一定时间后重启
     */
    public static void execLinuxCommand(){
        String cmd= "sleep 60; am start -n com.example.myapplication/com.example.myapplication.MainActivity";
        //Runtime对象
        Runtime runtime = Runtime.getRuntime();
        try {
            Process localProcess = runtime.exec("su");
            OutputStream localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes(cmd);
            localDataOutputStream.flush();
            Logger.e(TAG,"设备准备重启");
        } catch (IOException e) {
            Logger.i(TAG,"strLine:"+e.getMessage());
            e.printStackTrace();
        }


    }
}
