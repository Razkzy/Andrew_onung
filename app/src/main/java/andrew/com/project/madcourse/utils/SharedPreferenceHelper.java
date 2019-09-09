package andrew.com.project.madcourse.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;

import andrew.com.project.madcourse.model.Exercise;

;

/**
 * Created by Andrew.
 */
public class SharedPreferenceHelper {
    private static final String SIZE = "SIZE";
    private static final String STORAGE = "STORAGE";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    public SharedPreferenceHelper(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
    }

    public ArrayList<Exercise> getStoragePreference() {
        Gson gson = new Gson();
        ArrayList<Exercise> exercises = new ArrayList<>();

        int size = mSharedPreferences.getInt(SIZE , 0);
        for(int i = 0; i < size; i++) {
            String exercise = mSharedPreferences.getString(STORAGE + i, "");
            exercises.add(gson.fromJson(exercise, Exercise.class));
        }
        return exercises;
    }

    public void setSharedPreferences(ArrayList<Exercise> exercises) {
        Gson gson = new Gson();
        mEditor.putInt(SIZE , exercises.size());
        for(int i = 0; i < exercises.size(); i++){
            mEditor.putString(STORAGE + i, gson.toJson(exercises.get(i)));
        }
        mEditor.apply();
    }

    public void clearSharedPreferences(){
        mEditor.clear();
        mEditor.commit();
    }
}
