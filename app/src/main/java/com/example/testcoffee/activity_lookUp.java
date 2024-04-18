package com.example.testcoffee;

import androidx.appcompat.app.AppCompatActivity;
///import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class activity_lookUp extends AppCompatActivity {

    //References to buttons and recycler view
    ScrollView scrollView;
    TextView tv_lookupRes;
    EditText et_ecr, et_ref;
    Button btn_search, btn_cancel;
    //RecyclerView.Adapter transactionAdapter;
   // DatabaseHelper databaseHelper;
    int searchECR, searchREF;

    ArrayList everything;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up);

        //Buttons, textviews and recylcerview
        btn_cancel = findViewById(R.id.btn_cancelsearch);
        btn_search = findViewById(R.id.btn_search);
        et_ref = findViewById(R.id.et_ref);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // gets called when clicked

                String stringREF = et_ref.getText().toString();
                searchREF = Integer.parseInt(stringREF);
                //Change later when fixing UI
                Intent data = new Intent();
                //data.putExtra("ECR", searchECR);
                data.putExtra("REF", searchREF);
                setResult(RESULT_OK, data);
                // setResult(RESULT_CODE,data);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClicked(view);
            }
        });
    }
    public void cancelButtonClicked(View v){
        System.out.println("Cancelling Transaction");
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }
//        databaseHelper = new DatabaseHelper(activity_lookUp.this);
//        //Creating data source
//        ArrayAdapter  transactionArrayAdapter = new ArrayAdapter<transactionModel>(activity_lookUp.this,
//                android.R.layout.simple_list_item_1, everything);
//
//        System.out.println("Heres EVERYTHING: "+everything);
//        lv_dbContents.setAdapter(transactionArrayAdapter);

        //Get all items from database

       // showTransactionListView(databaseHelper);
//    public void showTransactionListView(DatabaseHelper dbh){
//
//        transactionAdapter = new RecyclerView.Adapter<transactionModel>(activity_lookUp.this, android.R.layout.simple_list_item_1, dbh.getEverything());
//
//    }
}

