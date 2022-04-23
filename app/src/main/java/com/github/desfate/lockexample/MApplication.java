package com.github.desfate.lockexample;

import android.app.Application;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.Printer;

public class MApplication extends Application {

    private Printer printer = null;
    @Override
    public void onCreate() {
        super.onCreate();
        XLog.init(LogLevel.ALL);
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }


    private static volatile MApplication application = null;

    public static MApplication getInstance() {
        if (application == null) {
            synchronized (MApplication.class) {
                if (application == null) {
                    application = new MApplication();
                }
            }
        }
        return application;
    }

}
