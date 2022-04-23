package com.github.desfate.lockexample.tools;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.Printer;
import com.github.desfate.lockexample.MApplication;
import com.github.desfate.lockexample.R;

import java.util.Objects;

public class LogDialog extends Dialog {

    android.widget.ImageView backBottom;
    RecyclerView recyclerView;
    Printer viewPrinter;

    public LogDialog(@NonNull Context context) {
        super(context, R.style.BottomDialogStyle);
        setContentView(R.layout.layout_dialog_log);
        backBottom = findViewById(R.id.back_img);
        recyclerView = findViewById(R.id.log_container);
        backBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        viewPrinter = new RecyclerViewPrinter(recyclerView);

        // Print welcome message.
        MApplication.getInstance().setPrinter(viewPrinter);

        // Print welcome message.
        XLog.printers(viewPrinter).i("XLog is ready.\nPrint your log now!");
    }

    void showDialog() {
        if(getWindow() != null){
            getWindow().setGravity(Gravity.BOTTOM);
        }
        WindowManager.LayoutParams lp = Objects.requireNonNull(getWindow()).getAttributes();
        lp.width = (int)(ScreenUtils.getScreenSize(getContext()).getWidth() * 1.0);
        getWindow().setAttributes(lp);
        show();
    }
}
