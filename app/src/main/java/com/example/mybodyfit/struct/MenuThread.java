package com.example.mybodyfit.struct;

import javax.inject.Singleton;

public class MenuThread extends Thread {

    private static MenuThread instance = null;

    private MenuThread(Runnable runnable) {
        super(runnable);
    }

    @Singleton
    public static synchronized MenuThread getInstance() {
        return instance;
    }

    public static void init(Runnable runnable) {
        instance = new MenuThread(runnable);
    }
}
