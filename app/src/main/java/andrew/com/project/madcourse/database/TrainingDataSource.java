package andrew.com.project.madcourse.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

import andrew.com.project.madcourse.model.Exercise;
import andrew.com.project.madcourse.model.Training;

/**
 * Created by Andrew
 */
public class TrainingDataSource {

    private Context mContext;
    private TrainingSQLiteHelper mTrainingSQLiteHelper;

    public TrainingDataSource(Context context) {
        mContext = context;

        //this code create the database when the constructor is call
        mTrainingSQLiteHelper = new TrainingSQLiteHelper(context);
        SQLiteDatabase database = mTrainingSQLiteHelper.getReadableDatabase();
        database.close();
    }

    private SQLiteDatabase open() {
        return mTrainingSQLiteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public void create(Training training) {
        SQLiteDatabase database = open();
        database.beginTransaction();
        //implementation
        ContentValues trainingValues = new ContentValues();
        trainingValues.put(TrainingSQLiteHelper.COLUMN_TRAINING_TITLE, training.getTitle());
        trainingValues.put(TrainingSQLiteHelper.COLUMN_TRAINING_EXERCISE, training.getExerciseLocation());
        long trainingId = database.insert(TrainingSQLiteHelper.TRAINING_TABLE, null, trainingValues);

        for (Exercise exercise : training.getExercises()) {
            ContentValues exerciseValues = new ContentValues();
            exerciseValues.put(TrainingSQLiteHelper.COLUMN_EXERCISE_NAME, exercise.getName());
            exerciseValues.put(TrainingSQLiteHelper.COLUMN_EXERCISE_QUANTITY, exercise.getQuantity());
            exerciseValues.put(TrainingSQLiteHelper.COLUMN_EXERCISE_REPETITION, exercise.getRepetition());
            exerciseValues.put(TrainingSQLiteHelper.COLUMN_FOREIGN_KEY_TRAINING, trainingId);

            database.insert(TrainingSQLiteHelper.EXERCISES_TABLE, null, exerciseValues);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    //read all the training from database and return in an ArrayList
    public ArrayList<Training> read() {
        ArrayList<Training> trainings = readTrainings();
        return trainings;
    }

    //return title and id from the training database
    public ArrayList<Training> readTrainings() {
        // open the database
        SQLiteDatabase database = open();

        //creating query with the SQLiteHelper help
        Cursor cursor = database.query(
                TrainingSQLiteHelper.TRAINING_TABLE,
                new String[]{TrainingSQLiteHelper.COLUMN_TRAINING_TITLE, BaseColumns._ID, TrainingSQLiteHelper.COLUMN_TRAINING_EXERCISE},
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null); //order by


        ArrayList<Training> trainings = new ArrayList<Training>();
        // getting all the trainings from the database
        if (cursor.moveToFirst()) {
            do {
                Training training = new Training(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, TrainingSQLiteHelper.COLUMN_TRAINING_TITLE),
                        null);
                trainings.add(training);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);
        return trainings;
    }

    public ArrayList<Exercise> readExercises(int id) {
        SQLiteDatabase database = open();
        ArrayList<Exercise> exercises = new ArrayList   <Exercise>();

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + TrainingSQLiteHelper.EXERCISES_TABLE +
                        " WHERE TRAINING_ID = " + id, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(
                        getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, TrainingSQLiteHelper.COLUMN_EXERCISE_NAME),
                        getStringFromColumnName(cursor, TrainingSQLiteHelper.COLUMN_EXERCISE_QUANTITY),
                        getIntFromColumnName(cursor, TrainingSQLiteHelper.COLUMN_EXERCISE_REPETITION));
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
        return exercises;
    }


    private int getIntFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }


    public void updateExercise(Exercise exercise) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues updateExercise = new ContentValues();
        updateExercise.put(TrainingSQLiteHelper.COLUMN_EXERCISE_NAME, exercise.getName());
        updateExercise.put(TrainingSQLiteHelper.COLUMN_EXERCISE_QUANTITY, exercise.getQuantity());
        updateExercise.put(TrainingSQLiteHelper.COLUMN_EXERCISE_REPETITION, exercise.getRepetition());

        if (exercise.hasBeenSaved()) {
            database.update(TrainingSQLiteHelper.EXERCISES_TABLE,
                    updateExercise,
                    String.format("%s=%d", BaseColumns._ID, exercise.getId()),
                    null);
        } else {
            database.insert(TrainingSQLiteHelper.EXERCISES_TABLE,
                    null,
                    updateExercise);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void updateTraining(Training training) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues updateTraining = new ContentValues();
        updateTraining.put(TrainingSQLiteHelper.COLUMN_TRAINING_TITLE, training.getTitle());

        if (training.hasBeenSaved()) {
            database.update(TrainingSQLiteHelper.TRAINING_TABLE,
                    updateTraining,
                    String.format("%s=%d", BaseColumns._ID, training.getId()),
                    null);
        } else {
            database.insert(TrainingSQLiteHelper.TRAINING_TABLE,
                    null,
                    updateTraining);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void deleteExercise(Exercise exercise) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        database.delete(TrainingSQLiteHelper.EXERCISES_TABLE,
                String.format("%s=%d", BaseColumns._ID, exercise.getId())
                , null);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void deleteTraining(Training training) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        database.delete(TrainingSQLiteHelper.EXERCISES_TABLE,
                String.format("%s=%s", TrainingSQLiteHelper.COLUMN_FOREIGN_KEY_TRAINING, String.valueOf(training.getId()))
                , null);

        database.delete(TrainingSQLiteHelper.TRAINING_TABLE,
                String.format("%s=%s", BaseColumns._ID, String.valueOf(training.getId()))
                , null);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }

    public void addExercise(Exercise exercise, Training training) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put(TrainingSQLiteHelper.COLUMN_EXERCISE_NAME, exercise.getName());
        exerciseValues.put(TrainingSQLiteHelper.COLUMN_EXERCISE_QUANTITY, exercise.getQuantity());
        exerciseValues.put(TrainingSQLiteHelper.COLUMN_EXERCISE_REPETITION, exercise.getRepetition());
        exerciseValues.put(TrainingSQLiteHelper.COLUMN_FOREIGN_KEY_TRAINING, training.getId());

        database.insert(TrainingSQLiteHelper.EXERCISES_TABLE, null, exerciseValues);

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
