package com.technicalrj.halanxscouts.Home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.technicalrj.halanxscouts.HomeActivity;
import com.technicalrj.halanxscouts.R;
import com.technicalrj.halanxscouts.RetrofitAPIClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoutAcceptanceActivity extends AppCompatActivity {

    TextView tv_task,tv_time;
    FloatingActionButton accept_button;
    //Ringtone r;
    int taskId;
    String key;
    private int count = 0;
    private static final long[] VIBRATE_PATTERN = { 500, 500 };
    Vibrator vibrator;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout_acceptance);
        tv_task = findViewById(R.id.tv_task);
        tv_time = findViewById(R.id.time);
        accept_button = findViewById(R.id.accept_button);


        taskId =  getIntent().getIntExtra("id",0);
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String taskName = getIntent().getStringExtra("task_name");

        tv_task.setText(taskName);
        tv_time.setText(date + ", " +time);

        accept_button.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        accept_button.setOnTouchListener(touchListener);



        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26 and above
            //vibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN,0  ));
        } else {
            // Below API 26
            //vibrator.vibrate(VIBRATE_PATTERN, 0);
        }


        Uri notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + this.getPackageName() + "/raw/scout_ringtone");
        player = MediaPlayer.create(this, notification);;
        player.setLooping(true);
        player.start();

        //r = RingtoneManager.getRingtone(this, notification);
        //r.play();

        (findViewById(R.id.swipe_right)).setAnimation(getRightAnimationSet());
        (findViewById(R.id.swipe_right_2)).setAnimation(getRightAnimationSet());
        (findViewById(R.id.swipe_left_2)).setAnimation(getLeftAnimationSet());
        (findViewById(R.id.swipe_left)).setAnimation(getLeftAnimationSet());




        final SharedPreferences prefs = getSharedPreferences("login_user_halanx_scouts", MODE_PRIVATE);
        key = prefs.getString("login_key", null);


        //Close this activity after 2 minutes
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                player.stop();
                vibrator.cancel();
            }
        }, 20*1000);








    }

    private AnimationSet getRightAnimationSet() {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 50.0f, 0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(1005);  // animation repeat count
        animation.setRepeatMode(0);   // repeat animation (left to right, right to left )

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        anim.setRepeatCount(1005);
        anim.setRepeatMode(Animation.RESTART);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(anim);
        animationSet.addAnimation(animation);


        return animationSet;
    }

    private AnimationSet getLeftAnimationSet() {
        TranslateAnimation animation = new TranslateAnimation(0.0f, -50.0f, 0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(1005);  // animation repeat count
        animation.setRepeatMode(0);   // repeat animation (left to right, right to left )

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        anim.setRepeatCount(1005);
        anim.setRepeatMode(Animation.RESTART);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(anim);
        animationSet.addAnimation(animation);


        return animationSet;
    }

    public int getWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }


    private SpringAnimation xAnimation;
    private float dX;



    // create X and Y animations for view's initial position once it's known
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            xAnimation = createSpringAnimation(accept_button, SpringAnimation.X, accept_button.getX(),
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    // capture the difference between view's top left corner and touch point
                    dX = v.getX() - event.getRawX();
                    //dY = v.getY() - event.getRawY();
                    // cancel animations
                    xAnimation.cancel();
                    break;

                case MotionEvent.ACTION_MOVE:
                    //  a different approach would be to change the view's LayoutParams.
                    accept_button.animate()
                            .x(event.getRawX() + dX)
                            //.y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;

                case MotionEvent.ACTION_UP:
                    xAnimation.start();
                    break;
            }

            Log.i("InfoText","getx"+v.getX());
            if(v.getX()>=getWidth()- 270){
                Toast.makeText(ScoutAcceptanceActivity.this,"Task accepted",Toast.LENGTH_SHORT).show();
                player.stop();

                if(count==0) {
                    taskAccepted(true);
                }
                finish();
                vibrator.cancel();

            }else if(v.getX()<=0){
                Toast.makeText(ScoutAcceptanceActivity.this,"Task Rejected",Toast.LENGTH_SHORT).show();
                player.stop();

                if(count==0){
                    taskAccepted(false);
                }
                finish();
                vibrator.cancel();
            }

            return true;
        }
    };

    private void taskAccepted(boolean bool) {

        //TO check it is the first and only time we are calling this function
        count++;
        Log.i("InfoText","Task request send:"+bool);

        JsonObject jsonObject = new JsonObject();
        vibrator.cancel();

        if(bool)
            jsonObject.addProperty("status","accepted");
        else
            jsonObject.addProperty("status","rejected");



        RetrofitAPIClient.DataInterface retrofitAPIClient = RetrofitAPIClient.getClient().create(RetrofitAPIClient.DataInterface.class);
        Call<JsonObject> call = retrofitAPIClient.taskRequest(jsonObject,taskId,"Token "+key);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Log.i("InfoText","Task request status acc/rej:"+response.body());
                    Intent intent = new Intent(ScoutAcceptanceActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }else {
                    try {
                        Log.i("InfoText","Task request error:"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public static SpringAnimation createSpringAnimation(View view,
                                                        DynamicAnimation.ViewProperty property,
                                                        float finalPosition,
                                                        float stiffness,
                                                        float dampingRatio) {

        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new  SpringForce(finalPosition);
        springForce.setStiffness(stiffness);
        springForce.setDampingRatio(dampingRatio);
        animation.setSpring(springForce);
        return animation;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
        vibrator.cancel();

    }
}
