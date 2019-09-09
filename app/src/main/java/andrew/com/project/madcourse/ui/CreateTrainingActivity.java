package andrew.com.project.madcourse.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import andrew.com.project.madcourse.R;
import andrew.com.project.madcourse.utils.SharedPreferenceHelper;
import andrew.com.project.madcourse.adapter.ExerciseAdapter;
import andrew.com.project.madcourse.database.TrainingDataSource;
import andrew.com.project.madcourse.model.Exercise;
import andrew.com.project.madcourse.model.Training;

public class CreateTrainingActivity extends AppCompatActivity {

    private static final String IS_PREVIOUSLY_OPENED = "isPreviouslyOpened";
    private SharedPreferenceHelper mSharedPreferenceHelper;

    private Training mTraining = new Training();
    private ExerciseAdapter mAdapter;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    boolean isPreviouslyOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);
        ButterKnife.bind(this);

        mSharedPreferenceHelper = new SharedPreferenceHelper(this);

        mTraining.setExercises(mSharedPreferenceHelper.getStoragePreference());


        mAdapter = new ExerciseAdapter(this, mTraining.getExercises());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        if(savedInstanceState != null){
            isPreviouslyOpened = savedInstanceState.getBoolean("isPreviouslyOpened");
        }

        if(!isPreviouslyOpened) {
            Toast.makeText(this, R.string.createTrainingMainToast, Toast.LENGTH_SHORT).show();
            isPreviouslyOpened = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSharedPreferenceHelper.setSharedPreferences(mTraining.getExercises());
    }

    @OnClick(R.id.createTraiAddButton)
    public void startCreateNewExercise(final View view) {
        // custom dialog
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.add_exercise_dialog);
        dialog.setTitle(R.string.addExercise);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        Button dialogButtonOk = (Button) dialog.findViewById(R.id.addExerDialogOK);
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.addExerDialogCancel);
        final Spinner quanitySpinner = (Spinner) dialog.findViewById(R.id.quantitySpinner);
        final Spinner repititionSpinner = (Spinner) dialog.findViewById(R.id.repetitionSpinner);
        final EditText exerciseEditText = (EditText) dialog.findViewById(R.id.exerciseEditText);

        // if button is clicked, close the custom dialog
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exercise exercise = new Exercise();
                exercise.setQuantity(quanitySpinner.getSelectedItem().toString());
                exercise.setRepetition(Integer.parseInt(repititionSpinner.getSelectedItem().toString()));
                exercise.setName(exerciseEditText.getText().toString());

                mTraining.getExercises().add(exercise);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @OnClick(R.id.saveTrainingButton)
    public void saveTraining(final View view) {
        // custom dialog
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.save_training_dialog);
        dialog.setTitle("Save Training");

        //
        Button saveButton = (Button) dialog.findViewById(R.id.saveTraining);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelSaveButton);
        final EditText title = (EditText) dialog.findViewById(R.id.trainingTitleEditText);

        // if button is clicked, close the custom dialog
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTraining.setTitle(title.getText().toString());
                TrainingDataSource dataSource = new TrainingDataSource(view.getContext());
                dataSource.create(mTraining);

                mSharedPreferenceHelper = new SharedPreferenceHelper(v.getContext());
                mSharedPreferenceHelper.clearSharedPreferences();
                mTraining.setExercises(new ArrayList<Exercise>());

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_PREVIOUSLY_OPENED, isPreviouslyOpened);
    };

}
