package andrew.com.project.madcourse.ui;

import android.app.Dialog;
import android.content.Context;
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
import andrew.com.project.madcourse.adapter.ViewExerciseAdapter;
import andrew.com.project.madcourse.database.TrainingDataSource;
import andrew.com.project.madcourse.model.Exercise;
import andrew.com.project.madcourse.model.Training;

public class ViewExerciseActivity extends AppCompatActivity {

    private static final String IS_PREVIOUSLY_OPENED = "isPreviouslyOpened";

    private static final String TRAINING = "TRAINING";
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private ArrayList<Exercise> mExercises;
    private ViewExerciseAdapter mAdapter;
    private Training mTraining;
    boolean isPreviouslyOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);
        ButterKnife.bind(this);

        mTraining = (Training) getIntent().getSerializableExtra(TRAINING);
        setTitle(mTraining.getTitle());
        mExercises = new ArrayList<Exercise>();
        refreshExercises();

        if(savedInstanceState != null){
            isPreviouslyOpened = savedInstanceState.getBoolean("isPreviouslyOpened");
        }

        if(!isPreviouslyOpened) {
            Toast.makeText(this, R.string.viewExerciseMainToast, Toast.LENGTH_SHORT).show();
            isPreviouslyOpened = true;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        refreshExercises();
    }

    @OnClick(R.id.viewTraiAddButton)
    public void viewTrainingAddExercise(final View view) {
        //custom dialog
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

        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exercise exercise = new Exercise();
                exercise.setQuantity(quanitySpinner.getSelectedItem().toString());
                exercise.setRepetition(Integer.parseInt(repititionSpinner.getSelectedItem().toString()));
                exercise.setName(exerciseEditText.getText().toString());

                TrainingDataSource dataSource = new TrainingDataSource(view.getContext());

                dataSource.addExercise(exercise, mTraining);

                refreshExercises();
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


    //set the adapter
    private void setAdapter(Context context, ArrayList<Exercise> exercises) {
        mAdapter = new ViewExerciseAdapter(context, exercises);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    ;

    private void refreshExercises() {
        TrainingDataSource dataSource = new TrainingDataSource(this);
        ArrayList<Exercise> exercises = dataSource.readExercises(mTraining.getId());
        setAdapter(this, exercises);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_PREVIOUSLY_OPENED, isPreviouslyOpened);
    };
}
