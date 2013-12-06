package com.dappervision.wearscript;


import android.view.ViewGroup;

import com.joshdholtz.sentry.Sentry;
import com.joshdholtz.sentry.Sentry.SentryEventBuilder.SentryEventLevel;


public class Log {

    private static boolean inited = false;
    private static String dsn;
    private static MainActivity activity;

    static void register(MainActivity act) {
        setup();
        activity = act;
        Sentry.init(activity, dsn);
    }

    static void setup() {
        if (inited)
            return;
        inited = true;
        dsn = "https://528123420aa94452a7dfb6dc08ff4a34:5e26315ac6d54b94bc23c6cb05f55854@app.getsentry.com/16563";
    }

    static private void logRaven(final String tag, final String message, final SentryEventLevel level) {
        setup();
        if (dsn == null)
            return;
        activity.runOnUiThread(new Thread() {
            public void run() {
                Sentry.captureEvent(new Sentry.SentryEventBuilder()
                        .setMessage(message)
                        .setLevel(level)
                        .setCulprit(tag)
                        .setTimestamp(System.currentTimeMillis())
                );
            }
        });
    }

    static private void logRaven(final String tag, final String message, final SentryEventLevel level, final Throwable tr) {
        setup();
        if (dsn == null)
            return;
        activity.runOnUiThread(new Thread() {
            public void run() {
                Sentry.captureEvent(new Sentry.SentryEventBuilder()
                        .setMessage(message)
                        .setLevel(level)
                        .setCulprit(tag)
                        .setException(tr)
                        .setTimestamp(System.currentTimeMillis())
                );
            }
        });
    }

    static int d(String tag, String message) {
        //logRaven(tag, message, SentryEventLevel.DEBUG);
        return android.util.Log.d(tag, message);
    }

    static int i(String tag, String message) {
        logRaven(tag, message, SentryEventLevel.INFO);
        return android.util.Log.i(tag, message);
    }

    static int w(String tag, String message) {
        logRaven(tag, message, SentryEventLevel.WARNING);
        return android.util.Log.w(tag, message);
    }

    static int e(String tag, String message) {
        logRaven(tag, message, SentryEventLevel.ERROR);
        return android.util.Log.e(tag, message);
    }

    static int e(String tag, String message, Throwable tr) {
        logRaven(tag, message, SentryEventLevel.ERROR);
        return android.util.Log.e(tag, message, tr);
    }
}