//package com.example.bluetoothscan;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.motion.widget.MotionLayout;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.method.HideReturnsTransformationMethod;
//import android.text.method.PasswordTransformationMethod;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private MotionLayout transition;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        Button login = findViewById(R.id.buttonLogin);
//        ImageButton back = findViewById(R.id.btnBack);
//        final EditText passwordEditText = findViewById(R.id.editTextPassword);
//        back.setVisibility(View.INVISIBLE);
//        passwordEditText.setTextIsSelectable(false);
//        CheckBox showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
//        transition = findViewById(R.id.motion);
//
//        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // If the checkbox is checked, show the password, otherwise hide it
//                if (isChecked) {
//                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                } else {
//                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }
//            }
//        });
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // Start animation
//        startAnimation();
//    }
//
//    private void startAnimation() {
//        // Start animation
//        transition.transitionToEnd();
//
//        // Restart animation after completion
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                transition.transitionToStart();
//                transition.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        transition.transitionToEnd();
//                    }
//                }, 50); // Small delay to ensure smooth transition
//            }
//        }, getAnimationDuration());
//    }
//
//    private int getAnimationDuration() {
//        // Adjust this value based on the duration of your animation
//        return 1000; // Milliseconds
//    }
//}


package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;


public class LoginActivity extends AppCompatActivity {
    boolean isLooped=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.buttonLogin);
        ImageButton back = findViewById(R.id.btnBack);
        final EditText passwordEditText = findViewById(R.id.editTextPassword);
        back.setVisibility(View.INVISIBLE);
        passwordEditText.setTextIsSelectable(false);
        CheckBox showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        final MotionLayout transition = findViewById(R.id.motion);

        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If the checkbox is checked, show the password, otherwise hide it
                if (isChecked) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

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
//                        transition.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                transition.transitionToEnd();
//                            }
//                        });
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
}
