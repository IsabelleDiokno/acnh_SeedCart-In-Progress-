package com.example.testcoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class displayResultsActivity extends AppCompatActivity {

    ScrollView scrollView;
    TextView tv_results;
    Button btn_okay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        System.out.println("C" + "/n");

        tv_results = findViewById(R.id.tv_results);
        scrollView = findViewById(R.id.sv_scroll);
        btn_okay = findViewById(R.id.btn_okay);

        System.out.println("GOT THE RESULTS!!!!!!!!!");
        Bundle bundle = getIntent().getExtras();
        String result = bundle.getString("RESULTS");
        System.out.println("IN DISPLAY: " +result);
        if(result != null){
            tv_results.setText(result);
        }

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // gets called when clicked
                finish();
            }
        });

    }
/*    displayResultsActivity (Activity activity, Context context){

    }*/
}