package com.example.testcoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class voidActivity extends AppCompatActivity {
    EditText et_ecrVd;
    Button btn_voidTrans, btn_cancel;
    int voidAmt, ecrV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void);
        btn_voidTrans = findViewById(R.id.btn_voidTrans);
        btn_cancel = findViewById(R.id.btn_cancel);
        et_ecrVd = findViewById(R.id.et_ecrVd);

        btn_voidTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // gets called when clicked

                String stringEcrVoid = et_ecrVd.getText().toString();
                System.out.println(stringEcrVoid);
                if(stringEcrVoid != null && !" ".equals(stringEcrVoid)){
                    try{
                        ecrV = Integer.parseInt(stringEcrVoid);
                    }
                    catch (NumberFormatException e){
                        System.out.println("String Ecr format "+stringEcrVoid);
                        ecrV = 0;
                    }
                }

            /*    try{
                    if (stringEcrVoid!=null){
                        ecrV = Integer.parseInt(stringEcrVoid);
                    }
                    else if(stringTIDVoid != null) {
                        System.out.println("String TID NOT NULLL"+stringTIDVoid);
                        transIDV = Integer.parseInt(stringTIDVoid);
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("String TID IS NULLL"+stringTIDVoid);
                    ecrV = 0;
                    transIDV = 0;
                }*/
                //Change later when fixing UI
                Intent data = new Intent();
                data.putExtra("voidECR", ecrV);
                setResult(RESULT_OK, data);
                //setResult(RESULT_CODE,data);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // gets called when clicked


                //Change later when fixing UI
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                // setResult(RESULT_CODE,data);
                finish();
            }
        });
    }

}