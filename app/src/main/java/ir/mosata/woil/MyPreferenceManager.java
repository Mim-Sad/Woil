package ir.mosata.woil;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class MyPreferenceManager {

    private static MyPreferenceManager instance = null;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;



    public static MyPreferenceManager getInstance(Context context){
        if(instance==null){
            instance= new MyPreferenceManager(context);
        }

        return instance;
    }

    private MyPreferenceManager(Context context){
         sharedPreferences = context.getSharedPreferences("my_preferences" , Context.MODE_PRIVATE);
         editor = sharedPreferences.edit();
    }


    //***********************
    public int getHighScore(){
        return sharedPreferences.getInt("high_score",0);
    }

    public void putHighScore(int highScore){
        editor.putInt("high_score",highScore);
        editor.apply();
    }
    //***********************

    //***********************
    public boolean getIsFirstEntry(){
        return sharedPreferences.getBoolean("is_first_entry",false);
    }

    public void putIsFirstEntry(Boolean YorN){
        editor.putBoolean("is_first_entry",YorN);
        editor.apply();
    }
    //***********************

    //***********************
    public String getLanguage(){
        return sharedPreferences.getString("what_is_language","en");
    }

    public void putLanguage(String Lang){
        editor.putString("what_is_language",Lang);
        editor.apply();
    }
    //***********************

    //***********************
    public String getThemeColor(){
        return sharedPreferences.getString("theme_mode","Auto");
    }

    public void putThemeColor(String Theme){
        editor.putString("theme_mode",Theme);
        editor.apply();
    }
    //***********************

    public void bestUserScore(User bestUser){
        Gson gson=new Gson();
        String userJson = gson.toJson(bestUser,User.class);
        editor.putString("best_user", userJson);
        editor.apply();
    }

    public User getBestUser(){
        String userJson = sharedPreferences.getString("best_user",null);
        if(userJson==null){
            return null;
        }
        Gson gson=new Gson();
        return gson.fromJson(userJson,User.class);
    }
}
