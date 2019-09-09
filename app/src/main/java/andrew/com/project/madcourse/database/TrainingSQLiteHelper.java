package andrew.com.project.madcourse.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Andrew
 *
 *
 *
 */
public class TrainingSQLiteHelper extends SQLiteOpenHelper {
    // naming database
    private static final String DB_NAME = "training.db";
    private static final int DB_VERSION = 1;

    //putting sql into CREATE_TRAINING to create the table training
    public static final String TRAINING_TABLE = "TRAINING";
    public static final String COLUMN_TRAINING_TITLE = "TITLE";
    public static final String COLUMN_TRAINING_EXERCISE = "EXERCISE";
    //sql
    private static String CREATE_TRAINING =
            "CREATE TABLE " + TRAINING_TABLE +
                    "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TRAINING_TITLE + " TEXT," +
                    COLUMN_TRAINING_EXERCISE + " TEXT)";


    //putting sql into CREATE_EXERCISES to create the table exercise
    public static final String EXERCISES_TABLE = "EXERCISES";
    public static final String COLUMN_EXERCISE_NAME = "NAME";
    public static final String COLUMN_EXERCISE_QUANTITY = "QUANTITY";
    public static final String COLUMN_EXERCISE_REPETITION = "REPETITION";
    public static final String COLUMN_FOREIGN_KEY_TRAINING = "TRAINING_ID";
    //sql
    private static final String CREATE_EXERCISES = "CREATE TABLE " + EXERCISES_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_EXERCISE_NAME + " TEXT, " +
            COLUMN_EXERCISE_QUANTITY + " TEXT, " +
            COLUMN_EXERCISE_REPETITION + " INTEGER, " +
            COLUMN_FOREIGN_KEY_TRAINING + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_FOREIGN_KEY_TRAINING + ") REFERENCES TRAINING(_ID))";

    public TrainingSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //preparing for creation
        sqLiteDatabase.execSQL(CREATE_TRAINING);
        sqLiteDatabase.execSQL(CREATE_EXERCISES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
