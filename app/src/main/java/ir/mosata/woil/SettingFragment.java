package ir.mosata.woil;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import static ir.mosata.woil.MainActivity.bell;
import static ir.mosata.woil.MainActivity.bkgame;
import static ir.mosata.woil.MainActivity.bkmenu;
import static ir.mosata.woil.MainActivity.bpre;
import static ir.mosata.woil.MainActivity.brel;
import static ir.mosata.woil.MainActivity.comp;
import static ir.mosata.woil.MainActivity.countdown;
import static ir.mosata.woil.MainActivity.letsgo;
import static ir.mosata.woil.MainActivity.mepo;
import static ir.mosata.woil.MainActivity.newrec;
import static ir.mosata.woil.MainActivity.volumeGame;

public class SettingFragment extends Fragment {

    ImageView soundGame, exitFromSettings,faLanguage,enLanguage,lightMode,darkMode,autoMode;
    LinearLayout upBlackNothing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView(view);
        checkStatus();

        exitFromSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MainActivity.bell.start();
                fm.popBackStack();
            }
        });

        //        soundGame.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AudioManager woilAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//                int current_volume = woilAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                if (current_volume==0){
//                    soundGame.setImageResource(R.drawable.ic_round_music_note);
//                    woilAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
//                }
//                else{
//                    soundGame.setImageResource(R.drawable.ic_round_music_off);
//                    woilAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
//                }
//
//
//            }
//        });

        soundGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!volumeGame) {
                    volumeGame = true;
                    soundGame.setImageResource(R.drawable.ic_round_music_note);
                    bell.setVolume((float) 0.5, (float) 0.5);
                    bpre.setVolume(1, 1);
                    brel.setVolume(1, 1);
                    comp.setVolume(1, 1);
                    mepo.setVolume(1, 1);
                    bkgame.setVolume(1, 1);
                    bkmenu.setVolume(1, 1);
                    countdown.setVolume(1, 1);
                    letsgo.setVolume(1, 1);
                    newrec.setVolume(1, 1);
                } else {
                    volumeGame = false;
                    soundGame.setImageResource(R.drawable.ic_round_music_off);
                    bell.setVolume(0, 0);
                    bpre.setVolume(0, 0);
                    brel.setVolume(0, 0);
                    comp.setVolume(0, 0);
                    mepo.setVolume(0, 0);
                    bkgame.setVolume(0, 0);
                    bkmenu.setVolume(0, 0);
                    countdown.setVolume(0, 0);
                    letsgo.setVolume(0, 0);
                    newrec.setVolume(0, 0);
                }
                bell.start();
            }
        });

        faLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                if(!MyPreferenceManager.getInstance(getActivity()).getLanguage().equals("fa")){
                    MyPreferenceManager.getInstance(getActivity()).putLanguage("fa");
                    setLocale("fa");

                    ((MainActivity)getActivity()).recreate();

                    Toast.makeText(v.getContext(), getString(R.string.alert_language), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(v.getContext(), getString(R.string.alert_language_dc), Toast.LENGTH_SHORT).show();
                }
            }
        });

        enLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                if(!MyPreferenceManager.getInstance(getActivity()).getLanguage().equals("en")){
                    MyPreferenceManager.getInstance(getActivity()).putLanguage("en");
                    setLocale("en");

                    ((MainActivity)getActivity()).recreate();

                    Toast.makeText(v.getContext(), getString(R.string.alert_language), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(v.getContext(), getString(R.string.alert_language_dc), Toast.LENGTH_SHORT).show();
                }
            }
        });

        autoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                MyPreferenceManager.getInstance(getActivity()).putThemeColor("Auto");

                Toast active = Toast.makeText(v.getContext(), getString(R.string.alert_auto_theme), Toast.LENGTH_SHORT);
                active.show();

                volumeGame = true;
                soundGame.setImageResource(R.drawable.ic_round_music_note);
                bell.setVolume(1, 1);
                bpre.setVolume(1, 1);
                brel.setVolume(1, 1);
                comp.setVolume(1, 1);
                mepo.setVolume(1, 1);
                bkgame.setVolume(1, 1);
                bkmenu.setVolume(1, 1);
                countdown.setVolume(1, 1);
                letsgo.setVolume(1, 1);
                newrec.setVolume(1, 1);
            }
        });

        lightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                MyPreferenceManager.getInstance(getActivity()).putThemeColor("Light");

                Toast active = Toast.makeText(v.getContext(), getString(R.string.alert_light_theme), Toast.LENGTH_SHORT);
                active.show();

                volumeGame = true;
                soundGame.setImageResource(R.drawable.ic_round_music_note);
            }
        });

        darkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.start();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                MyPreferenceManager.getInstance(getActivity()).putThemeColor("Dark");

                Toast active = Toast.makeText(v.getContext(), getString(R.string.alert_dark_theme), Toast.LENGTH_SHORT);
                active.show();

                volumeGame = true;
                soundGame.setImageResource(R.drawable.ic_round_music_note);
            }
        });

        darkMode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                upBlackNothing.setVisibility(View.VISIBLE);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        upBlackNothing.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
                return true;
            }
        });

    }

    private void findView(View view) {
        soundGame = (ImageView) view.findViewById(R.id.sound_game);
        exitFromSettings = (ImageView) view.findViewById(R.id.exit_from_settings);
        faLanguage = (ImageView) view.findViewById(R.id.fa_lang);
        enLanguage = (ImageView) view.findViewById(R.id.en_lang);
        lightMode = (ImageView) view.findViewById(R.id.light_mode);
        darkMode = (ImageView) view.findViewById(R.id.dark_mode);
        autoMode = (ImageView) view.findViewById(R.id.auto_mode);
        upBlackNothing = (LinearLayout) view.findViewById(R.id.dark_mode_easter_egg);
    }

    public void checkStatus() {
        if (volumeGame) {
            soundGame.setImageResource(R.drawable.ic_round_music_note);
        } else {
            soundGame.setImageResource(R.drawable.ic_round_music_off);
        }
    }
    public void setLocale(String localeCode) {
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();
        conf.setLocale(new Locale(localeCode));
        res.updateConfiguration(conf,dm);
    }

}
