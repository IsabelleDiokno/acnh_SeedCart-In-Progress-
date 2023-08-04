package com.example.testcoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;


import java.lang.ref.WeakReference;
@SuppressWarnings("deprecation")
public class Payment extends AppCompatActivity {
    String ECR_ref;
    String acctNum;
    String apprvdAmt;
    String cardType;
    String transType;
    String AuthCode;
    String HostCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
/*        //Set commSetting
        CommSetting commSetting = new CommSetting();
        commSetting.setType(CommSetting.AIDL);
        // Create POSLink object
        PosLink posLink = POSLinkCreator.createPoslink(getApplicationContext());
        posLink.SetCommSetting(commSetting);
        //Create payment request with the TransType as AUTH and TenderType CREDIT
        PaymentRequest request = new PaymentRequest();*/

    }

    private void startAsyncTask (View v){
        PaymentTask task = new PaymentTask();
        task.execute(10);
    }

    private static class PaymentTask extends AsyncTask<Integer, Integer, String>{

        private WeakReference<MainActivity> activityWeakReference;
        PaymentTask(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        public PaymentTask() {
            super();
        }

        @Override
        protected String doInBackground(Integer... integers) {


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        //Use to publish results
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}