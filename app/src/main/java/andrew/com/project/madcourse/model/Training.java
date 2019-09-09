package andrew.com.project.madcourse.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Andrew
 */
public class Training implements Serializable {

    private int mId;
    private String mTitle;
    private ArrayList<Exercise> mExercises;
    private String mExerciseLocation;

    public Training() {

    }

    ;

    public Training(int id, String title, ArrayList<Exercise> exercises) {
        mId = id;
        mTitle = title;
        mExercises = exercises;
    }

    public String getExerciseLocation() {
        return mExerciseLocation;
    }

    public void setExerciseLocation(String exerciseLocation) {
        mExerciseLocation = exerciseLocation;
    }

    public boolean hasBeenSaved() {
        return (getId() != -1);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<Exercise> getExercises() {
        return mExercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        mExercises = exercises;
    }
}
