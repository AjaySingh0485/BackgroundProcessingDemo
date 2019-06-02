package com.development.ajaysingh.backgroundintentdemo;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Data data = new Data.Builder()
                .putString(Myworker.TASK_DESC, "The task data passed from MainActivity")
                .build();
        //creating constraints
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true) // you can add as many constraints as you want
                .build();


        final OneTimeWorkRequest oneTimeWorkRequest=new OneTimeWorkRequest.Builder(Myworker.class).setConstraints(constraints).setInputData(data).build();
        findViewById(R.id.m).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enqueuing the work request
                WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            }
        });

        final TextView textView = findViewById(R.id.textViewStatus);
        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {

                        //Displaying the status into TextView
                        textView.append(workInfo.getState().name() + "\n");
                    }
                });

        final TextView textView2 = findViewById(R.id.textViewStatus2);

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {

                        //receiving back the data
                        if(workInfo != null && workInfo.getState().isFinished())
                            textView.append(workInfo.getOutputData().getString(Myworker.TASK_DESC) + "\n");

                        textView2.append(workInfo.getState().name() + "\n");
                    }
                });
        /*AlertDialog.Builder dd = new AlertDialog.Builder(this);
        dd.setMessage("Are you sure, You wanted to make decision");
                dd.setPositiveButton("",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(MainActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                            }
                        });

        dd.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = dd.create();
        alertDialog.show();*/

    }



}
