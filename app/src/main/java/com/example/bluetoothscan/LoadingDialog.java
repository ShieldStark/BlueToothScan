package com.example.bluetoothscan;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

import androidx.constraintlayout.motion.widget.MotionLayout;

public class LoadingDialog {

    private static final long DEFAULT_TIMEOUT_MS = 30000; // 5 seconds timeout by default

    private Dialog dialog;
    private TextView titleTextView, messageTextView;
    private Handler handler;
    private Runnable dismissRunnable;
    boolean isLooped=false;
    MotionLayout transition;

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
        transition = dialog.findViewById(R.id.motion);
        transition.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Start animation only when the layout is ready
                transition.getViewTreeObserver().removeOnGlobalLayoutListener(this); // Remove listener to prevent multiple calls
                transition.setTransitionListener(new MotionLayout.TransitionListener() {
                    @Override
                    public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {}

                    @Override
                    public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {}

                    @Override
                    public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                        // Animation completed, start it again
                        if (!isLooped){
                            transition.transitionToStart();
                        }
                        else {
                            transition.transitionToEnd();
                        }
                        isLooped=!isLooped;
                    }

                    @Override
                    public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {}
                });
                transition.transitionToEnd();
            }
        });
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(transition, "rotation", 0f, 360f);
        rotateAnimation.setDuration(1000); // 2 seconds
        rotateAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnimation.start();
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
