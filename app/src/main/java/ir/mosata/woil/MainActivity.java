package ir.mosata.woil;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Button startGame, showBestScore, showAboutMe, showAboutGame, setGameTime;
    ImageView exitGame, goToSettings;

    // NF
//    Button showNotification;

    ViewPager viewPager;
    LinearLayoutCompat getHelpBar;
    MainActivity mainActivity;

    public static Boolean volumeGame = true;
    public static MediaPlayer bell, bpre, brel, comp, mepo, bkgame, bkmenu, countdown, letsgo,newrec;
    public static final String CHANNEL_ID = "personal_notifications";
    public Integer alarmHour = 12, alarmMinute = 12, alarmSecond = 12;
    public ShakeListener mShaker;


    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAppLang();

        setContentView(R.layout.activity_main);

        findView();
        configure();
        calenderNote();

        shakeConfigure();

    }

    private void findView() {
        startGame = (Button) findViewById(R.id.start_game);
        setGameTime = (Button) findViewById(R.id.set_time);
        showAboutMe = (Button) findViewById(R.id.about_me);
        showAboutGame = (Button) findViewById(R.id.about_game);
        showBestScore = (Button) findViewById(R.id.show_best_score);

        goToSettings = (ImageView) findViewById(R.id.go_to_settings);
        exitGame = (ImageView) findViewById(R.id.exit_game);

        getHelpBar = (LinearLayoutCompat) findViewById(R.id.get_help_bar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // NF
//        showNotification=(Button)findViewById(R.id.show_notification);
    }

    private void configure() {
        bell = MediaPlayer.create(this, R.raw.bell);
        bpre = MediaPlayer.create(this, R.raw.button_pressed);
        brel = MediaPlayer.create(this, R.raw.button_released);
        comp = MediaPlayer.create(this, R.raw.complete);
        mepo = MediaPlayer.create(this, R.raw.menu_popup);
        bkgame = MediaPlayer.create(this, R.raw.back_focus);
        bkmenu = MediaPlayer.create(this, R.raw.october_trees);
        countdown = MediaPlayer.create(this, R.raw.beep_countdown);
        letsgo = MediaPlayer.create(this, R.raw.lets_go);
        newrec = MediaPlayer.create(this,R.raw.trumpet_charge);
        bkmenu.setLooping(true);
        bkgame.setLooping(true);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                SetLevelFragment setLevelFragment = new SetLevelFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, setLevelFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        showBestScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                BestScoreFragment bestScoreFragment = new BestScoreFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, bestScoreFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        showAboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                AboutMeFragment aboutMeFragment = new AboutMeFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, aboutMeFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        showAboutGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                AboutGameFragment aboutGameFragment = new AboutGameFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, aboutGameFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        setGameTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                SetTimeFragment setTimeFragment = new SetTimeFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, setTimeFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        getHelpBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://woildev.blog.ir/page/FAQ"));
                startActivity(browserIntent);

            }
        });

        goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                SettingFragment settingFragment = new SettingFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, settingFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        exitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        // NF
//        showNotification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fastShowNotification(v);
//            }
//
//        });

        if (!MyPreferenceManager.getInstance(MainActivity.this).getIsFirstEntry()) {
            setFirstLang();
            IntroAdapter adapter = new IntroAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
        } else return;

    }

//    public void fastShowNotification(View view){
//        createNotificationChannel();
//
//        Intent repeating_intent = new Intent(this,MainActivity.class);
//        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.drawable.ic_woillogo_white)
//                .setContentTitle(getString(R.string.notif_me_title))
//                .setContentText(getString(R.string.notif_me_text))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(88,builder.build());
//    }


    private void calenderNote() {
        createNotificationChannel();

        AlarmManager woilAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        calendar.set(Calendar.SECOND, alarmSecond);
        Log.d(String.valueOf(calendar.getTimeInMillis()), "TIME");

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 88, intent, 0);

        woilAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setLocale(String localeCode) {
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();
        conf.setLocale(new Locale(localeCode));
        res.updateConfiguration(conf, dm);
    }

    public void setFirstLang() {
        String deviceLanguage = Locale.getDefault().getDisplayLanguage();
        if (deviceLanguage.equals("en")) {
            setLocale("en");
        } else if (deviceLanguage.equals("fa")) {
            setLocale("fa");
        } else {
            setLocale("en");
        }
        MyPreferenceManager.getInstance(MainActivity.this).putLanguage(deviceLanguage);
    }

    private void setAppLang() {
        setLocale(MyPreferenceManager.getInstance(MainActivity.this).getLanguage());
    }

    public void shakeConfigure() {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mShaker = new ShakeListener(MainActivity.this);

        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(400);

                Toast shakeEE = Toast.makeText(MainActivity.this, getString(R.string.alert_shake), Toast.LENGTH_SHORT);
                shakeEE.show();

//                new AlertDialog.Builder(MainActivity.this)
//                        .setPositiveButton(android.R.string.ok, null)
//                        .setMessage("Shooken!")
//                        .show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        bkmenu.start();

        // reset volumes
        bell.setVolume((float) 0.5, (float) 0.5);
        bpre.setVolume(1, 1);
        brel.setVolume(1, 1);
        comp.setVolume(1, 1);
        mepo.setVolume(1, 1);
        bkgame.setVolume(1, 1);
        bkmenu.setVolume(1, 1);
        countdown.setVolume(1, 1);
        letsgo.setVolume(1, 1);
        newrec.setVolume(1,1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bkmenu.start();
        mShaker.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.bkmenu.pause();
        mShaker.resume();
    }

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        Fragment mainFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        GameFragmentEasy easyFragment = (GameFragmentEasy)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        GameFragmentMedium mediumFragment = (GameFragmentMedium)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        GameFragmentHard hardFragment = (GameFragmentHard)getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (mainFragment instanceof GameFragmentEasy || mainFragment instanceof GameFragmentMedium || mainFragment instanceof GameFragmentHard) {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.alert_exit_game), Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }else if (mainFragment==null){
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.alert_exit), Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }else {
            super.onBackPressed();
        }
    }

}