package andrew.com.project.madcourse.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import andrew.com.project.madcourse.R;
import andrew.com.project.madcourse.model.Exercise;

/**
 * Created by Andrew
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private ArrayList<Exercise> mExercises;
    private Context mContext;

    public ExerciseAdapter(Context context, ArrayList<Exercise> exercises) {
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
            dialog.setContentView(R.layout.confirm_dialog);

            dialog.setTitle(R.string.confirmation);
            //
            TextView text = (TextView) dialog.findViewById(R.id.textDialog);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelDialogButton);
            Button confirmButton = (Button) dialog.findViewById(R.id.confirmDialogButton);

            text.setText(R.string.confirmationDeleteExercise);

            // if button is clicked, close the custom dialog
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExercises.remove(getPosition());
                    notifyItemRemoved(getPosition());

                    dialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }
}

