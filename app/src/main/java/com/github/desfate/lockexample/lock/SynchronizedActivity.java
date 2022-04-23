package com.github.desfate.lockexample.lock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elvishew.xlog.XLog;
import com.github.desfate.lockexample.MApplication;
import com.github.desfate.lockexample.R;
import com.github.desfate.lockexample.tools.LogDialog;

/**
 * synchronized 是比较常出现的锁  是利用系统关键字去实现锁的功能
 * <p>
 * 1。synchronized 是悲观锁
 * 2。synchronized 是独占锁（互斥锁）
 * 3。synchronized 是非公平锁
 * 4。synchronized 是可重入锁（递归锁）
 * <p>
 * 用法
 * synchronized 锁对象
 * synchronized 锁类(.class)
 * synchronized 锁方法
 * synchronized 锁代码块(this)
 * synchronized 锁静态方法
 * <p>
 * 实现原理
 * JVM中使用ACC_SYNCHRONIZED来标记  同步方法
 * JVM中使用monitorenter 和 monitorexit 来实现同步  同步代码块
 * 而无论是ACC_SYNCHRONIZED/onitorenter 和 monitorexit 都是基于Monitor实现的
 * <p>
 * 几个性质
 * 1。原子性
 * 2。可见性
 * 3。有序性
 * <p>
 * 锁优化
 * 1.6之前 synchronized 是直接调用 ObjectMonitor中的enter和exit 等于重量级锁
 * 1.6之后 synchronized 会从无锁 -》 偏向锁 -》轻量级 -》 重量级
 * 1.4就有自旋 但是默认关闭
 */
public class SynchronizedActivity extends AppCompatActivity {

    Button lock_object_btn;
    Button lock_class_btn;
    Button lock_fun_btn;
    Button lock_block_btn;
    Button lock_static_fun_btn;
    Button dialog_button;
    LogDialog logDialog;

    String lockedObject = "unlock"; // 需要被竞争的对象

    static String staticObject = "unlock"; // 静态对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronized);
        logDialog = new LogDialog(this);
        lock_object_btn = findViewById(R.id.lock_object_btn);
        lock_class_btn = findViewById(R.id.lock_class_btn);
        lock_fun_btn = findViewById(R.id.lock_fun_btn);
        lock_block_btn = findViewById(R.id.lock_block_btn);
        lock_static_fun_btn = findViewById(R.id.lock_static_fun_btn);
        dialog_button = findViewById(R.id.dialog_button);

        //查看log
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logDialog.show();
            }
        });

        // 锁对象
        lock_object_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testLockObject();
            }
        });

        // 锁class 类对象锁和普通对象锁是两个不同的锁 尽管他们操作的是同一个对象
        lock_class_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建多个线程争夺对象的读写权限
                testLockObject();

                // 创建多个线程争夺Class中对象的读写权限
                testLockClass();
            }
        });

        // 锁方法
        lock_fun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建多个线程争夺fun的读写权限
                testLockFun();
            }
        });

        // 锁代码块 锁代码块和锁方法是互斥的 等于效果相同
        lock_block_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建多个线程争夺fun的读写权限
                testLockFun();

                // 创建多个线程争夺block的读写权限
                testLockBlock();
            }
        });

        // 锁静态方法 等效于锁Class 即使对象不同
        lock_static_fun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建多个线程争夺Class中对象的读写权限
                testLockClass();

                // 创建多个线程争夺static fun的读写权限
                testStaticFun();
            }
        });
    }

    private void testLockObject(){
        // 创建多个线程争夺对象的读写权限
        ChangeThread[] threads = new ChangeThread[4];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ChangeThread();
        }

        for (ChangeThread thread : threads) {
            thread.start();  // 启动多个线程开始争夺对象使用权
        }
    }

    private void testLockClass(){
        // 创建多个线程争夺Class中对象的读写权限
        ChangeClassThread[] classThreads = new ChangeClassThread[4];

        for (int i = 0; i < classThreads.length; i++) {
            classThreads[i] = new ChangeClassThread();
        }

        for (ChangeClassThread thread : classThreads) {
            thread.start();  // 启动多个线程开始争夺对象使用权
        }
    }

    private void testLockFun(){
        // 创建多个线程争夺fun的读写权限
        ChangeFunThread[] funThreads = new ChangeFunThread[4];

        for (int i = 0; i < funThreads.length; i++) {
            funThreads[i] = new ChangeFunThread();
        }

        for (ChangeFunThread thread : funThreads) {
            thread.start();  // 启动多个线程开始争夺fun使用权
        }
    }

    private void testLockBlock(){
        // 创建多个线程争夺block的读写权限
        ChangeBlockThread[] blockThreads = new ChangeBlockThread[4];

        for (int i = 0; i < blockThreads.length; i++) {
            blockThreads[i] = new ChangeBlockThread();
        }

        for (ChangeBlockThread thread : blockThreads) {
            thread.start();  // 启动多个线程开始争夺block使用权
        }
    }

    private void testStaticFun(){
        // 创建多个线程争夺static fun的读写权限
        ChangeStaticFunThread[] staticFunThreads = new ChangeStaticFunThread[4];

        for (int i = 0; i < staticFunThreads.length; i++) {
            staticFunThreads[i] = new ChangeStaticFunThread();
        }

        for (ChangeStaticFunThread thread : staticFunThreads) {
            thread.start();  // 启动多个线程开始争夺block使用权
        }
    }




    // Synchronized 锁对象
    private void lockObject() {
        synchronized (lockedObject) {
            XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "object read lockedObject = " + lockedObject);  // 读取信息
            try {
                lockedObject = "lock";  // 修改信息
                Thread.sleep(500);  // 执行耗时任务
                XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "object change lockedObject = " + lockedObject); // 确认是否修改成功
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lockedObject = "unlocked";  // 修改信息
                XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "object changed lockedObject = " + lockedObject); // 确认是否修改成功
            }
        }
    }

    // Synchronized 锁class
    private void lockClass() {
        synchronized (SynchronizedActivity.class) {
            XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "class read lockedObject = " + lockedObject);  // 读取信息
            try {
                lockedObject = "lock";  // 修改信息
                Thread.sleep(500);  // 执行耗时任务
                XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "class change lockedObject = " + lockedObject); // 确认是否修改成功
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lockedObject = "unlocked";  // 修改信息
                XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "class changed lockedObject = " + lockedObject); // 确认是否修改成功
            }
        }
    }

    // Synchronized 锁func
    private synchronized void lockFun() {
        XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "func read lockedObject = " + lockedObject);  // 读取信息
        try {
            lockedObject = "lock";  // 修改信息
            Thread.sleep(500);  // 执行耗时任务
            XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "func change lockedObject = " + lockedObject); // 确认是否修改成功
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lockedObject = "unlocked";  // 修改信息
            XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "func changed lockedObject = " + lockedObject); // 确认是否修改成功
        }
    }

    // Synchronized 锁Block
    private void lockBlock() {
        synchronized (this) {
            XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "block read lockedObject = " + lockedObject);  // 读取信息
            try {
                lockedObject = "lock";  // 修改信息
                Thread.sleep(500);  // 执行耗时任务
                XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "block change lockedObject = " + lockedObject); // 确认是否修改成功
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lockedObject = "unlocked";  // 修改信息
                XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "block changed lockedObject = " + lockedObject); // 确认是否修改成功
            }
        }
    }

    // Synchronized 锁func
    private static synchronized void lockStaticFun() {
        XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "func read lockedObject = " + staticObject);  // 读取信息
        try {
            staticObject = "lock";  // 修改信息
            Thread.sleep(500);  // 执行耗时任务
            XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "func change lockedObject = " + staticObject); // 确认是否修改成功
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            staticObject = "unlocked";  // 修改信息
            XLog.printers(MApplication.getInstance().getPrinter()).d(Thread.currentThread().getName() + "func changed lockedObject = " + staticObject); // 确认是否修改成功
        }
    }




    class ChangeThread extends Thread {
        @Override
        public void run() {
            super.run();
            lockObject();
        }
    }

    class ChangeClassThread extends Thread {
        @Override
        public void run() {
            super.run();
            lockClass();
        }
    }

    class ChangeFunThread extends Thread {
        @Override
        public void run() {
            super.run();
            lockFun();
        }
    }

    class ChangeBlockThread extends Thread {
        @Override
        public void run() {
            super.run();
            lockBlock();
        }
    }

    class ChangeStaticFunThread extends Thread {
        @Override
        public void run() {
            super.run();
            lockStaticFun();
        }
    }


}
