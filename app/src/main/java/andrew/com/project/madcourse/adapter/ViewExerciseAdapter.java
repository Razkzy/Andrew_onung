package andrew.com.project.madcourse.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import andrew.com.project.madcourse.R;
import andrew.com.project.madcourse.database.TrainingDataSource;
import andrew.com.project.madcourse.model.Exercise;

/**
 * Created by Andrew
 */
public class ViewExerciseAdapter extends RecyclerView.Adapter<ViewExerciseAdapter.ExerciseViewHolder> {

    private ArrayList<Exercise> mExercises;
    private Context mContext;

    public ViewExerciseAdapter(Context context, ArrayList<Exercise> exercises) {
        mContext = context;
        mExercises = exercises;
    }


    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.create_training_list_item, parent, false);
        ExerciseViewHolder viewHolder = new ExerciseViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        holder.bindExercise(mExercises.get(position));
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mRepetitionLabel;
        private TextView mQuantityLabel;
        private TextView mExerciseLabel;

        public ExerciseViewHolder(View itemView) {
            super(itemView);

            mRepetitionLabel = (TextView) itemView.findViewById(R.id.trainingTitleLabel);
            mQuantityLabel = (TextView) itemView.findViewById(R.id.quantityLabel);
            mExerciseLabel = (TextView) itemView.findViewById(R.id.exerciseLabel);

            itemView.setOnClickListener(this);
        }

        public void bindExercise(Exercise exercise) {
            mRepetitionLabel.setText(exercise.getRepetition() + "");
            mQuantityLabel.setText(exercise.getQuantity());
            mExerciseLabel.setText(exercise.getName());
        }

        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(view.getContext());
            dialog.setContentView(R.layout.alter_exercise_dialog);
            dialog.setTitle(R.string.ChangeExerciseDialogTitle);

            // getting the object selected
            final Exercise exercise = mExercises.get(getPosition());

            final Button cancelButton = (Button) dialog.findViewById(R.id.alterCancel);
            final Button deleteButton = (Button) dialog.findViewById(R.id.alterDeleteExercise);
            final Button updateButton = (Button) dialog.findViewById(R.id.alterUpdateExercise);
            final Spinner quanitySpinner = (Spinner) dialog.findViewById(R.id.alterQuantitySpinner);
            final Spinner repititionSpinner = (Spinner) dialog.findViewById(R.id.alteRepetitionSpinner);
            final EditText exerciseEditText = (EditText) dialog.findViewById(R.id.alterExerciseEditText);

            setSpinnerPosition(view.getResources().getStringArray(R.array.quantitySpinnerItems),
                    exercise.getQuantity(), quanitySpinner);
            setSpinnerPosition(view.getResources().getStringArray(R.array.repetitionSpinnerItems),
                    exercise.getRepetition() + "", repititionSpinner);
            exerciseEditText.setText(exercise.getName());


            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exercise.setName(exerciseEditText.getText().toString());
                    exercise.setRepetition(Integer.parseInt(repititionSpinner.getSelectedItem().toString()));
                    exercise.setQuantity(quanitySpinner.getSelectedItem().toString());

                    TrainingDataSource dataSource = new TrainingDataSource(view.getContext());
                    dataSource.updateExercise(exercise);
                    notifyItemChanged(getPosition());
                    dialog.dismiss();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TrainingDataSource dataSource = new TrainingDataSource(view.getContext());
                    dataSource.deleteExercise(exercise);
                    mExercises.remove(exercise);
                    notifyItemRemoved(getPosition());
                    dialog.dismiss();
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


        /*this method get the strings from array.xml, a string from the database and set the
        spinner with the saved value*/
        public void setSpinnerPosition(String[] strings, String string, Spinner spinner) {
            for (int i = 0; i < strings.length; i++) {
                if (spinner.getItemAtPosition(i).toString().equals(string)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }
}

