package andrew.com.project.madcourse.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import andrew.com.project.madcourse.R;
import andrew.com.project.madcourse.model.Exercise;

/**
 * Created by Andrew.
 */
public class DialogCreateExercise implements Runnable {
    private Context mContext;
    public Exercise mExercise = new Exercise();

    Button dialogButtonOk;
    Button dialogButtonCancel;
    Spinner quanitySpinner;
    Spinner repititionSpinner;
    EditText exerciseEditText;

    public DialogCreateExercise(Context context) {
        mContext = context;
    }


    @Override
    public void run() {

    }


    public Exercise createExercise() {
        //Creating new dialog
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.add_exercise_dialog);
        dialog.setTitle(R.string.CreateExerciseDialogTitle);

        //getting the buttons from the dialog layout
        dialogButtonOk = (Button) dialog.findViewById(R.id.addExerDialogOK);
        dialogButtonCancel = (Button) dialog.findViewById(R.id.addExerDialogCancel);
        quanitySpinner = (Spinner) dialog.findViewById(R.id.quantitySpinner);
        repititionSpinner = (Spinner) dialog.findViewById(R.id.repetitionSpinner);
        exerciseEditText = (EditText) dialog.findViewById(R.id.exerciseEditText);


        new Thread(new Runnable() {
            @Override
            public void run() {
                // Ok button click, make a exercise with the items selected
                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mExercise.setQuantity(quanitySpinner.getSelectedItem().toString());
                        mExercise.setRepetition(Integer.parseInt(repititionSpinner.getSelectedItem().toString()));
                        mExercise.setName(exerciseEditText.getText().toString());

                        dialog.dismiss();
                    }
                });

                //cancel button
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mExercise.setId(-1);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }).start();

        //return exercise
        return mExercise;
    }

}
