package ir.mosata.woil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SplashScreen extends AppCompatActivity {
    LinearLayout fastSplash;
    Integer sleepTime = 3900;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setThemeColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(sleepTime);
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();


        fastSplash = (LinearLayout) findViewById(R.id.fastSplash);
        fastSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background.interrupt();
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });
    }
    private void setThemeColor(){
        if (MyPreferenceManager.getInstance(this).getThemeColor().equals("Dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if (MyPreferenceManager.getInstance(this).getThemeColor().equals("Light")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}
