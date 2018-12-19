package com.base.util;


public class Log {
    private static boolean debug = true;

    public static void initLog(boolean debug) {
        Log.debug = debug;
    }

    public static void i(String tag, String msg) {
        if (debug) {
            try {
                android.util.Log.i(tag, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void d(String tag, String msg) {
        if (debug) {
            try {
                android.util.Log.d(tag, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void v(String tag, String msg) {
        if (debug) {
            try {
                android.util.Log.v(tag, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void w(String tag, String msg) {
        if (debug) {
            try {
                android.util.Log.w(tag, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void e(String tag, String msg) {
        if (debug) {
            try {
                android.util.Log.e(tag, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (debug) {
            try {
                android.util.Log.e(tag, msg, tr);
            } catch (Exception e) {

            }
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (debug) {
            try {
                android.util.Log.e(tag, msg, tr);
            } catch (Exception e) {

            }
        }
    }

}
