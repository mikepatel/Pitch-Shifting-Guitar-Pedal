package com.stompbox.project6.stompbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.method.TextKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

/**
 * Created by mrpatel5 on 4/7/2017.
 *
 * Record Screen will be a Dialog
 * Will automatically begin recording with Timer decrementing
 * User can hit "Cancel" to stop recording at any time
 * When timer expires, the user will be prompted with a Dialog to save the recorded sample with a name
 */

public class RecordDialogFragment extends DialogFragment {

    // Class variables, so that they can be accessed from MainActivity
    AlertDialog.Builder builder;
    CountDownTimer countDownTimer;
    EditText timerText;
    EditText saveField;
    InputRecorded inputRecorded;
    AlertDialog alertDialog;
    int numSaveButtonClicks = 0;
    final static int ONE_SECOND = 1000;

    //
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        // Use Builder class for convenient dialog construction
        builder = new AlertDialog.Builder(getActivity()); // instantiate new AlertDialog.Builder object
        //builder.setTitle(title);
        builder.setTitle("Record");

        // set up layout and XML objects
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View recordAlertDialogLayout = layoutInflater.inflate(R.layout.record_dialog_layout, null);

        // Set up countdown timerText editText
        timerText = (EditText) recordAlertDialogLayout.findViewById(R.id.timerText);
        timerText.setKeyListener(null); // timerText not focusable, editable
        timerText.setBackgroundColor(Color.TRANSPARENT); // removes the underline

        // Set up saveField editText
        saveField = (EditText) recordAlertDialogLayout.findViewById(R.id.saveField);
        saveField.setKeyListener(null); // initially do not want focusable, editable
        saveField.setHint("Name");
        saveField.setBackgroundColor(Color.TRANSPARENT); // removes the underline

        // set alert dialog view w/ (record alert dialog layout)
        builder.setView(recordAlertDialogLayout);

        // Cancel button
        builder.setNegativeButton("Cancel", null); // Override onClick() later

        // Save button
        builder.setPositiveButton("Save", null); // Override onClick() later

        // Preview button
        builder.setNeutralButton("Preview", null); // Override onClick() later

        // Create AlertDialog object alertDialog from builder
        alertDialog = builder.create();
        alertDialog.show(); /* Must call show() before getButton()
                                the Views in a Dialog aren't created until the
                                Dialog is actually shown
                            */

        alertDialog.setCanceledOnTouchOutside(false); // dialog will not dismiss if user clicks outside dialog area

        // Override handler action for Negative button
        // Cancel button
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCurrentRecording(); // ACTION - stop recording immediately
                alertDialog.cancel(); // rather than dismiss()
            }
        });

        // Override handler action for Positive button
        // Save button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCurrentRecording(); // ACTION - stop recording immediately
                makeEditTextEditable(saveField); // ACTION - make EditText visible to user
                numSaveButtonClicks++;

                if(numSaveButtonClicks == 2){ // hit Save button the second time
                    // ACTION - Save the recording with user input file name
                    renameTempfile();
                    numSaveButtonClicks = 0; // reset count
                    alertDialog.dismiss();
                }

            }
        });

        // Override handler action for Neutral button
        // Preview button
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCurrentRecording(); // ACTION - stop recording immediately
                previewRecording(); // ACTION - play back the recorded audio
                // but what if haven't recorded anything yet - oh wait, nvm, automatically started recording when click on fab
            }
        });

        // upon clicking on the record fab, immediately begin recording
        inputRecorded.onRecord(true);

        //
        return alertDialog;
    }

    //
    public void runCountdownTimer(){
        countDownTimer = new CountDownTimer(5*ONE_SECOND, ONE_SECOND){ // 5s timer

            @Override
            public void onTick(long millisUntilFinished) { // update countdown timer on every second
                timerText.setText(getString(R.string.secondsRemain) + millisUntilFinished / 1000); // 00:0x
            }

            @Override
            public void onFinish() {
                timerText.setText(getString(R.string.secondsRemain) + "0"); // 00:00
                // prompt user to either 'Cancel' or 'Save'
                stopCurrentRecording(); // on timer expiration, stop recordingw

                // on timer expiration, allow saveField to take user input
                numSaveButtonClicks++;
                makeEditTextEditable(saveField);

                // on timer expiration, open keyboard
                //openKeyboard(saveField);
            }

        }.start(); // start countdown timer
    }

    //
    public void recordAudio(){
        inputRecorded = new InputRecorded(); // instantiate new object to record audio
        //inputRecorded.onRecord(true); // start recording immediately
    }

    //
    public void cancelCountdownTimer(){ // ACTION - stop countdown timer
        countDownTimer.cancel();
    }

    //
    public void stopCurrentRecording(){ // ACTION - stop recording immediately
        if(inputRecorded.mediaRecorder != null){
            inputRecorded.onRecord(false);
        }
        cancelCountdownTimer();
    }

    //
    public void previewRecording(){
        inputRecorded.onPlay(true);
    }

    //
    public void makeEditTextEditable(EditText field){ // passed in EditText can now take user input
        field.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.NONE, false)); // make EditText visible
        field.requestFocus(); // blink cursor
        openKeyboard();
    }

    //
    public void openKeyboard(){ // pulls open keyboard for passed in EditText
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    //
    public void renameTempfile(){
        String filename = saveField.getText().toString();
        File from = new File(inputRecorded.output);
        File to = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Stompbox/" + filename + ".mp3");
        from.renameTo(to);
    }


}
