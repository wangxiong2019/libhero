package com.hero.libhero.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.hero.libhero.R;

import java.lang.ref.WeakReference;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 *
 */
public class LoadingDialog extends Handler {

    private Dialog load = null;

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Context context;
    private boolean cancelable;

    private final WeakReference<Context> reference;
    private TextView loadingLabel;

    public LoadingDialog(Context context,
                         boolean cancelable) {
        super();
        this.reference = new WeakReference<Context>(context);

        this.cancelable = cancelable;

    }

    private void create(String msg){
        if (load == null) {
            context  = reference.get();

            load = new Dialog(context, R.style.Dialog1);
            View dialogView = LayoutInflater.from(context).inflate(
                    R.layout.dialog_loading, null);
            loadingLabel = dialogView.findViewById(R.id.loading_text);
            loadingLabel.setText(msg);
            load.setCanceledOnTouchOutside(false);
            load.setCancelable(cancelable);
            load.setContentView(dialogView);

            Window dialogWindow = load.getWindow();
            dialogWindow.setGravity(Gravity.CENTER_VERTICAL
                    | Gravity.CENTER_HORIZONTAL);
        }else {
            if (loadingLabel!=null){
                loadingLabel.setText(msg);
            }
        }
        if (load!=null && !load.isShowing()&&context!=null) {
            try{
                load.show();
            }catch (Exception e){


            }

        }
    }


    public boolean isShow(){
        if (load != null&&load.isShowing()) {
            return true;
        }else {
            return false;
        }

    }

    public void show(String msg){
        create(msg);
    }
    public void show(){
        create("加载中");
    }


    public  void dismiss() {
        context  = reference.get();
        if (load != null&&load.isShowing()&&!((Activity) context).isFinishing()) {
            String name = Thread.currentThread().getName();
            load.dismiss();
            load = null;
        }
    }

    @Override
    public void publish(LogRecord record) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }


}
