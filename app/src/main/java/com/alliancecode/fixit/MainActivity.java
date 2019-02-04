package com.alliancecode.fixit;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button nProvider, nConsumer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nProvider = (Button) findViewById(R.id.provider);
        nConsumer = (Button) findViewById(R.id.consumer);

        nProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SplashProvider.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        nConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Splash.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}



