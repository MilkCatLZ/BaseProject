package com.base.util.crash;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.base.app.BaseApplicationInterface;
import com.base.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class CrashHandler implements UncaughtExceptionHandler {

    public interface CrashCallBack{
        void crashed(Throwable ex);
    }

    CrashCallBack callBack;

    public static final String TAG = "CrashHandler";
    private String CrashDir = "/LianNiCrashFile/";

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    // private DateFormat formatter = new
    // SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private BaseApplicationInterface app;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context,CrashCallBack callBack) {
        mContext = context;
        this.callBack=callBack;
        try {
            app = (BaseApplicationInterface) context.getApplicationContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
            // android.os.Process.killProcess(android.os.Process.myPid());
            app.exit();

        } else {
            try {
                Thread.sleep(3000);

                ex.printStackTrace();
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);

            }
            // 退出程序
            // android.os.Process.killProcess(android.os.Process.myPid());
            app.exit();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        String className = "";
        try {
            className = ex.getStackTrace()[0].getClassName();
        } catch (Exception e) {

        }
        final String fileName = "crash-" + getNowDate() + "-" + className + ".log";
        if (ex == null) {
            return false;
        }

        if (callBack != null) {
            callBack.crashed(ex);
        }

        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "捕捉到系统崩溃错误,请查看'" + CrashDir + fileName + "'日志文件", Toast.LENGTH_LONG)
                     .show();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
ex.printStackTrace();
        saveCrashInfo2File(ex, fileName);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                app.exit();
            }
        }.start();
        return true;
    }

//	private void saveCrashInfoToServer(String result) {
//		String url =app.interfaceURL.URL_BUSINESS + "user/error_log_collector";
//		RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);
//		JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Method.GET, url,  new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				if (response != null) {
//					Log.d(TAG, response.optString(Constant.AttributeName.CODE) + ":" + response.optString(Constant.AttributeName.MSG));
//				}
//
//			}
//		}, null, getParams(result), null);
//		mRequestQueue.add(JsonObjectRequest);
//	}

    private Map<String, String> getParams(String result) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("error_msg", result);
        params.put("phone_model", Build.MODEL);
        return params;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for(Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null)
                                                .toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     *
     * @return 返回错误信息字符串，传给服务器
     */
    private String saveCrashInfo2File(Throwable ex, String fileName) {

        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {

            // long timestamp = System.currentTimeMillis();
            // String time = getNowDate();

            if (Environment.getExternalStorageState()
                           .equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory()
                                         .getPath() + "/" + CrashDir;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
                fos.write(sb.toString()
                            .getBytes());
                fos.close();
            }
            return result;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            e.printStackTrace();

        }
//        Log.e(TAG, "Crash cause by:", ex);
//        ex.printStackTrace();
        return result;
    }

    public static String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);
        String dateString = formatter.format(currentTime);
        // ParsePosition pos = new ParsePosition(8);
        // Date currentTime_2 = formatter.parse(dateString, pos);
        return dateString;
    }
}