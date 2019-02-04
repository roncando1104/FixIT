package com.alliancecode.fixit;

        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.PixelFormat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Window;
        import android.view.WindowManager;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

public class Splash extends Activity {
    public void onAttachedtoWindow(){
        super.onAttachedToWindow();
        Window window = getWindow();

        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        startAnimations();

    }
    private void startAnimations(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.alpha);
        animation.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lay);
        l.clearAnimation();
        l.startAnimation(animation);



        animation = AnimationUtils.loadAnimation(this,R.anim.translate);
        animation.reset();
        ImageView imgLogo = (ImageView)findViewById(R.id.imgLogo);
        imgLogo.clearAnimation();
        imgLogo.startAnimation(animation);


        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    //pause time
                    while (waited < 6000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splash.this, ConsumerLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splash.this.finish();
                } catch (InterruptedException e) {

                } finally {
                    Splash.this.finish();
                }
            }
        };

        splashThread.start();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
