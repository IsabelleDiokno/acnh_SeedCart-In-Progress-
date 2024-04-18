package com.example.testcoffee;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;


import com.pax.poscomm.aidl.poslink.AIDL_poslink;
import com.pax.poscore.commsetting.AidlSetting;
import com.pax.poscore.commsetting.CommSetting;
import com.pax.poscore.LogSetting;

import com.pax.poslinkadmin.ExecutionResult;
import com.pax.poslinkadmin.manage.InitRsp;
import com.pax.poslinksemiintegration.POSLinkSemi;
import com.pax.poslinksemiintegration.Terminal;

@SuppressWarnings("deprecation")

public class MainActivity extends AppCompatActivity{
    TextView tv_version, tv_serialNm, tv_appNm, tv_model, tv_osVersion, tv_demoWarning;
    Button btn_tran_sale, btn_lookUp, btn_report, btn_refundTrans, btn_voidTrans,
    btn_clBatch, btn_loyalty;
    Switch sw_safMode;
    String grandT, sn, appName, appVersion;
    /*
    Note that for requestCode for startActivityForResult:
    Result_Code:
    0-->Look up app info
    1-->Sale Results (grandTotal)
    2--> Look Up a transaction
    3--> Report
    4--> Refund
    5--> Void
    6--> Close Batch
     */


    @Override
    //public void onCreate(Bundle savedInstanceState) {
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Buttons and Textviews
        btn_tran_sale = findViewById(R.id.btn_tran_sale);
        btn_lookUp = findViewById(R.id.btn_lookUp);
        btn_report = findViewById(R.id.btn_report);
        btn_refundTrans = findViewById(R.id.btn_refundTrans);
        btn_voidTrans = findViewById(R.id.btn_voidTrans);
        btn_clBatch = findViewById(R.id.btn_clBatch);
        btn_loyalty = findViewById(R.id.btn_loyalty);

        tv_appNm = findViewById(R.id.tv_appNm);
        tv_serialNm = findViewById(R.id.tv_serialNm);
        tv_model = findViewById(R.id.tv_model);
        tv_osVersion = findViewById(R.id.tv_osVersion);

        tv_demoWarning = findViewById(R.id.tv_demoWarning);
        tv_demoWarning.setVisibility(View.INVISIBLE);

        sw_safMode = (Switch) findViewById(R.id.sw_safMode);

        //Set log settings
        //setLogSetting();
        //Set Comm Setting and Init
        //MainActivity mainAct = new MainActivity();
        Context context = getApplicationContext();
        retrieveInfo(this, context);
        // set a listener on Sale button
     btn_tran_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // gets called when clicked
                processTransaction(v,1);
            }
        });
         //Set a listener for Look Up button
        btn_lookUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { processTransaction(v,2);}
        });
        /*
        //Set a listener for Report button
        btn_report.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { report(v); }
        });
        */
      //Set a listener for Refund button
        btn_refundTrans.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { processTransaction(v,4 ); }
        });
        //Set a listener for Void button
        btn_voidTrans.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { processTransaction(v,5); }
        });
        //Set a listener for batch close button
        btn_clBatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { processTransaction(v,6); }
        });

        btn_loyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        sw_safMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(sw_safMode.isPressed()){
                    int newMode = (b) ? 1 : 0;
                    System.out.println("Boolean b: "+b+" Int newMode: "+newMode);
                    updateSafMode(newMode);
                }
/*                else if (!sw_safMode.isChecked()){
                    updateSafMode(1);
                }*/
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
    /*public void displayInfo(Terminal terminal){

        ExecutionResult<InitRsp> executionResult = terminal.getManage().init();
        if (executionResult.isSuccessful()){
            InitRsp initRsp = new InitRsp();
            //Save sn, appName, and appVersions
            sn = initRsp.sn();
            appName = initRsp.appName();
            appVersion = initRsp.appVersion();
            //Set text views for display
            tv_serialNm.setText(sn);
            tv_appNm.setText(appName);
            tv_version.setText(appVersion);

            System.out.println(executionResult.isSuccessful()+" "+sn+" "+appName+" "+appVersion);
        }
        else{
            System.out.println(executionResult.isSuccessful()+"ERROR");
        }
    }*/
    @Override
    protected void onResume() {
        super.onResume();
        Context context = getApplicationContext();
        retrieveInfo(this, context);
    }
    public void setLogSetting(){
        LogSetting logSetting = new LogSetting();
        logSetting.setEnable(true);
        logSetting.setLevel(LogSetting.LogLevel.DEBUG);
        logSetting.setDays(30);
        logSetting.setFileName("POSLog");
        logSetting.setFilePath("/poslink");
        POSLinkSemi.getInstance().setLogSetting(logSetting);
    }
    public void updateSafMode(Integer safMode){
        Context context = getApplicationContext();
        transaction_Driver td = new transaction_Driver(this,context); //Create a new transaction driver class
        td.execute(7, safMode); //execute the transaction driver thread
    }
    public static void retrieveInfo(MainActivity activity, Context context){
    //public static void retrieveInfo(MainActivity activity,Context context){
        //MainActivity activity = new MainActivity();
            transaction_Driver td = new transaction_Driver(activity,context); //Create a new transaction driver class
            td.execute(0); //execute the transaction driver thread
        //Create thread herr
    }
    /*
         public void report(View v){
             System.out.println("Making a report");
             Context context = getApplicationContext();
             //Create a new transaction driver class
             transaction_Driver td = new transaction_Driver(this, context);
             td.execute(3);
         }

         public void lookUp(View view) {
             System.out.println("Looking things up..");
             Intent intent = new Intent(MainActivity.this, activity_lookUp.class);
             int START_LOOKUP_ACTIVITY = 2;
             startActivityForResult(intent, START_LOOKUP_ACTIVITY);

         } */
    public void processTransaction (View view, int transactionActivity){
        Intent intent;
        switch(transactionActivity){
            case 1:
                System.out.println("Starting to build a sale ");
                intent = new Intent(MainActivity.this, saleActivity.class);
                int START_SALE_ACTIVITY = 1;
                startActivityForResult(intent, START_SALE_ACTIVITY);
                break;
            case 2:
                System.out.println("Looking things up..");
                intent = new Intent(MainActivity.this, activity_lookUp.class);
                int START_LOOKUP_ACTIVITY = 2;
                startActivityForResult(intent, START_LOOKUP_ACTIVITY);
                break;
            case 4:
                System.out.println("Starting Refund.. ");
                intent = new Intent(MainActivity.this, refundActivity.class);
                int START_REFUND_ACTIVITY = 4;
                startActivityForResult(intent, START_REFUND_ACTIVITY);
                break;
            case 5:
                System.out.println("Starting Void.. ");
                intent = new Intent(MainActivity.this, voidActivity.class);
                int START_VOID_ACTIVITY = 5;
                startActivityForResult(intent, START_VOID_ACTIVITY);
                break;
            case 6:
                System.out.println("Going to close batch...");
                Context context = getApplicationContext();
                //Create a new transaction driver class
                transaction_Driver td = new transaction_Driver(this, context);
                td.execute(6);
                break;
            default:
                break;
        }
    }

  /*  public void endLookUp(int ecr, int ref){
        Context context = getApplicationContext();
        PosLink pLink = POSLinkCreator.createPoslink(context); //Create a new PosLink Object
        //Create a new transaction driver class
        transaction_Driver td = new transaction_Driver(this,context);

        int e = ecr;
        int r = ref;
        td.execute(2, e, r); //execute the transaction driver thread
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Bundle bundle = data.getExtras();
            Context context = getApplicationContext();
            //Create Instance of Transaction Driver class
            transaction_Driver td = new transaction_Driver(this,context);
            if (resultCode == Activity.RESULT_OK){
                switch(requestCode){
                    case 1:
                        //SALE
                        System.out.println("REQUEST CODE: "+requestCode);
                        System.out.println("RESULT CODE: "+resultCode);
                        if(!bundle.getString("grandTotal").isEmpty()){
                            String total = bundle.getString("grandTotal");
                            double doublet = Double.parseDouble(total)*100;
                            int t =(int) doublet ;
                            System.out.println("Got sale total results: "+total);
                            td.execute(1,t); //execute the PaymentEngine thread
                        }
                        break;
                    case 2:
                        //LOOKUP
                        System.out.println("REQUEST CODE: "+requestCode);
                        System.out.println("RESULT CODE: "+resultCode);
                        //int ecr = bundle.getInt("ECR");
                        int ref = bundle.getInt("REF");
                        System.out.println("Got ECR: "+" REF: "+ref);
                        //endLookUp(ecr, ref);
                        //Create a new transaction driver class
                        td = new transaction_Driver(this,context);
//                int e = ecr;
//                int r = ref;
                        //td.execute(2, e, ref); //execute the transaction driver thread
                        td.execute(2, ref); //execute the transaction driver thread

                        break;
                    case 4:
                        //REFUND
                        System.out.println("REQUEST CODE: "+requestCode);
                        System.out.println("RESULT CODE: "+resultCode);
                        String rAmount = bundle.getString("refundAmt");
                        System.out.println("Got REFUND AMOUNT: "+rAmount);
                        System.out.print("Working with transaction driver to fin REFUND -----");
                        //transaction_Driver td = new transaction_Driver(this,context);
                        double refundD = Double.parseDouble(rAmount)*100;
                        int rt = (int) refundD;
                        td.execute(4,rt);
                        break;
                    case 5:
                        //VOID
                        System.out.println("REQUEST CODE: "+requestCode);
                        System.out.println("RESULT CODE: "+resultCode);
                        int ecrV =  bundle.getInt("voidECR");
                        System.out.print("Working with transaction driver to fin VOID -----");
                        //Create a new transaction driver class
                        td.execute(5,ecrV);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception");
        }





    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }


}