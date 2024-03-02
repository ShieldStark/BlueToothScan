package com.example.bluetoothscan;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class LoadingDialog {

    private static final long DEFAULT_TIMEOUT_MS = 30000; // 5 seconds timeout by default

    private Dialog dialog;
    private TextView titleTextView, messageTextView;
    private Handler handler;
    private Runnable dismissRunnable;

    public LoadingDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.common_loading_layout);

        titleTextView = dialog.findViewById(R.id.titleTextView);
        messageTextView = dialog.findViewById(R.id.messageTextView);

        handler = new Handler();
        dismissRunnable = new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        };
    }

    public void setTitle(String title) {
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
    }

    public void setMessage(String message) {
        if (messageTextView != null) {
            messageTextView.setText(message);
        }
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
            // Set timeout to dismiss dialog after a certain period
            handler.postDelayed(dismissRunnable, DEFAULT_TIMEOUT_MS);
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            // Remove any pending dismiss callbacks
            handler.removeCallbacks(dismissRunnable);
        }
    }
    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}
