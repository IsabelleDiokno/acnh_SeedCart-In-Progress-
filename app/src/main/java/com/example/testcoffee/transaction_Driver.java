package com.example.testcoffee;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.pax.poscore.commsetting.AidlSetting;
import com.pax.poslinkadmin.ExecutionResult;
import com.pax.poslinkadmin.constant.EdcType;
import com.pax.poslinkadmin.constant.SafMode;
import com.pax.poslinkadmin.constant.TransType;
import com.pax.poslinkadmin.manage.GetSafParametersReq;
import com.pax.poslinkadmin.manage.GetSafParametersRsp;
import com.pax.poslinkadmin.manage.GetVariableReq;
import com.pax.poslinkadmin.manage.GetVariableRsp;
import com.pax.poslinkadmin.manage.InitRsp;
import com.pax.poslinkadmin.manage.Manage;
import com.pax.poslinkadmin.manage.SetSafParametersReq;
import com.pax.poslinkadmin.manage.SetSafParametersRsp;
import com.pax.poslinkadmin.util.AmountReq;
import com.pax.poslinksemiintegration.POSLinkSemi;
import com.pax.poslinksemiintegration.Terminal;
import com.pax.poslinksemiintegration.batch.BatchCloseReq;
import com.pax.poslinksemiintegration.batch.BatchCloseRsp;
import com.pax.poslinksemiintegration.batch.SafUploadReq;
import com.pax.poslinksemiintegration.batch.SafUploadRsp;
import com.pax.poslinksemiintegration.constant.ReceiptPrintFlag;
import com.pax.poslinksemiintegration.constant.SafIndicator;
import com.pax.poslinksemiintegration.constant.SafReportIndicator;
import com.pax.poslinksemiintegration.constant.SignatureAcquireFlag;
import com.pax.poslinksemiintegration.constant.SignatureCaptureFlag;
import com.pax.poslinksemiintegration.constant.SignatureUploadFlag;
import com.pax.poslinksemiintegration.constant.TipRequestFlag;
import com.pax.poslinksemiintegration.report.LocalDetailReportReq;
import com.pax.poslinksemiintegration.report.LocalDetailReportRsp;
import com.pax.poslinksemiintegration.report.LocalTotalReportReq;
import com.pax.poslinksemiintegration.report.LocalTotalReportRsp;
import com.pax.poslinksemiintegration.report.SafSummaryReportReq;
import com.pax.poslinksemiintegration.report.SafSummaryReportRsp;
import com.pax.poslinksemiintegration.transaction.DoCreditReq;
import com.pax.poslinksemiintegration.transaction.DoCreditRsp;
import com.pax.poslinksemiintegration.util.TraceReq;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.HandlerThread;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

/*
typeTransaction dictates what will be run:
0--> Getting Start up info
1--> Process Sale
2--> Lookup by ECR AND REF numbers
3--> Report
4--> Refund
5--> Void
6--> Close Batch
 */

@SuppressWarnings("deprecation")
public class transaction_Driver extends AsyncTask<Integer, Void, String> {
    private static Integer typeTransaction;
   // final String SERIAL_NUMBER= "1440000571";
//    Button btn_tran_sale;
//    TextView tv_version, tv_serialNm, tv_appNm,tv_model, tv_osVersion, tv_gtot;
    String  appNameandVersion, sn, model, osVersion, transDateTime,
            acctNm, cardType,transType, apprvdamt, ecrRef, authCode,
            hostCode, hostResponse, hostMessage, refNum, nameCard, transDate, transTime, cardName, status,
            entryMode, TC, TVR, AID, TSI, ATC, app, appLabel, CVM, AC, AIP, AVN, IAUTHD, stringResults, safMode = "null";
    Boolean success = true;
    boolean demoMode;


    private WeakReference<MainActivity> activityWeakReference;
    private WeakReference<Context> contextWeakReference;
    DatabaseHelper dataBaseHelper;
    public static final String EXTRA_RESULTS = "RESULTS";
    public Boolean getDemoMode() {
        return demoMode;
    }
    public void setDemoMode(Boolean demoMode) {
        System.out.println("Setting Demo Mode: "+demoMode);
        this.demoMode = demoMode;
    }
    public String getSafModeString() {
        return safMode;
    }

    public void setSafModeString(String safMode) {
        System.out.println("SAF MODE SETTER "+safMode);
        this.safMode = safMode;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    //Weak reference to activity
    transaction_Driver(MainActivity activity){
        activityWeakReference = new WeakReference<MainActivity>(activity);
    }
    //Weak reference to context
    transaction_Driver(Context context){
        contextWeakReference = new WeakReference<Context>(context);
    }

    //Weak reference to both
    transaction_Driver(MainActivity activity,Context context){
        //super.onAttach(activity);
        activityWeakReference = new WeakReference<MainActivity>(activity);
        contextWeakReference = new WeakReference<Context>(context);
    }
    @Override
    protected String doInBackground(Integer... type) {
        typeTransaction = type[0];
        Terminal terminal = setCommSetting();
        switch (typeTransaction){
            case 0:
                //Get App Information to display on Main menu
                    getAppInfo(terminal);

                break;
            case 1:
                //Sale Logic
                System.out.println("Attempting to process sale");
                if(type[1] != null){ //Check the sale amount is not null
                    processPayment(terminal, type[1]); //Pass the sale amount
                }
                break;
            case 2:
                //Look up transaction
                System.out.println("Attempting to look up transaction");
                if(type[1] != null){ //Check reference number is valid
                   processLookUp(terminal, type[1]);
                }

                break;
/*            case 3:
               //Perform Report
                System.out.println("Attempting to make up a report");
                Terminal terminal3 = setCommSetting();
                makeReport();

                break;*/
            case 4:
                //Refund
                System.out.println("Attempting to process Refund");
                if(type[1] != null){ //Check refund amount is valid
                    Terminal terminal4 = setCommSetting();
                    processRefund(terminal4, type[1]);
                }
                break;
            case 5:
                //Void Transaction
                System.out.println("Attempting to process Void");
                new Thread(()->{
                    if(type[1] != null){ //Check ref number is valid
                        Terminal terminal5 = setCommSetting();//
                        processVoid(terminal5, type[1]);

                    }
                }).start();

                break;
            case 6:
                System.out.println("Attempting to close batch");
                //Terminal terminal6 = setCommSetting();
                processCloseBatch(terminal);
                break;
            case 7:
                System.out.println("Updating SAF MODE");
                new Thread(()->{
                    getSafMode(terminal);
                    System.out.println("Initial Type: "+type[1].toString());
                    System.out.println("Get SAF Mode String: "+getSafModeString());
                    if(type[1] == 1 && getSafModeString().equals("STAY_OFFLINE")){ //If user switch on and current saf mode is offline change saf mode to online
                        System.out.println("NOT THE SAME /n INIT: "+getSafModeString()+"===== Need to change to"+type[1]);
                        setSafMode(terminal,0); //Set SAFMODE to STAY_ONLINE
                        safUpload(terminal); //Upload offline transactions
                    }
                    else if(type[1] == 0 && getSafModeString().equals("STAY_ONLINE")){ //If user switch off and current saf mode is online, change saf mode
                                                                                        // to offline
                        System.out.println("NOT THE SAME /n INIT: "+getSafModeString()+"===== Need to change to"+type[1]);
                        setSafMode(terminal,1); //Set SAFMODE to STAY OFFLINE
                    }
                }).start();

                break;
            default:
                break;
        }

        return null;
    }
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean isSuccessful) {
        success = isSuccessful;
    }
    public void processCloseBatch(Terminal terminal){
        new Thread(() -> {
        //Create a PaymentRequest with a TransType VOID and TenderType CREDIT
            BatchCloseReq doBatchCloseReq = new BatchCloseReq();
            ExecutionResult<BatchCloseRsp> executionResult= terminal.getBatch().batchClose(doBatchCloseReq);

            //Check result code of the transaction
            if (executionResult.isSuccessful()){
                System.out.println( );
                System.out.println("CLOSE BATCH Results: " );
                System.out.println("========BATCH CLOSE RESULT CODE==== : "+executionResult.response().hostInformation().hostResponseCode());
                System.out.println("========BATCH CLOSE RESULT CODE==== : "+executionResult.response().hostInformation().hostResponseMessage());
                System.out.println("");

                String tid = executionResult.response().tid();
                System.out.println("GOT A TID "+tid);

                System.out.println("Batch #: "+executionResult.response().hostInformation().batchNumber());
                System.out.println("TID: "+executionResult.response().tid()); //NA in portico
                System.out.println("MID: "+executionResult.response().mid()); //NA in portico
                System.out.println("===Transaction Count===: ");
                System.out.println("Credit: "+executionResult.response().totalCount().creditCount());
                System.out.println("Debit: "+executionResult.response().totalCount().debitCount());
                System.out.println("EBT: "+executionResult.response().totalCount().ebtCount());
                System.out.println("Gift: "+executionResult.response().totalCount().giftCount());
                System.out.println("Loyalty: "+executionResult.response().totalCount().giftCount());
                System.out.println("Cash: "+executionResult.response().totalCount().cashCount());


                System.out.println("Result Code: "+executionResult.response().hostInformation().hostResponseCode());
                System.out.println("Result Text :"+executionResult.response().hostInformation().hostResponseMessage());

                System.out.println("BATCH CLOSED :"+executionResult.response().timeStamp());


                System.out.println("");
                System.out.println("=======Close Batch Success============");
                setStatus("good");
                stringResults = transDateTime + "\n" + "Batch #: "+ executionResult.response().hostInformation().batchNumber() + "\n" + "TID: "+ executionResult.response().tid() + "\n" +
                        "MID: "+ executionResult.response().mid() + "\n" + "===Transaction Count===: " + "\n" +
                        "Credit: "+ executionResult.response().totalCount().creditCount() + "\n" + "Debit: "+ executionResult.response().totalCount().debitCount() + "\n" + "Host Resp: "+ hostResponse + "\n" +
                        "EBT: "+ executionResult.response().totalCount().ebtCount() + "\n" + "Gift: "+ executionResult.response().totalCount().giftCount() + "\n" + "Card Name: "+ nameCard + "\n" +
                        "Entry Mode: "+ entryMode + "\n" + " TC: " + TC + "\n" + "TVR: "+ TVR + "\n" +  "AID: " + AID + "\n" + "TSI: " + TSI + "\n" +
                        "ATC: " + ATC + "\n" + "App Label: " + appLabel + "\n" + "CVM: " +CVM;

            }
            else if (!executionResult.isSuccessful()){
                System.out.println("=======Close Batch Failed============");
                //Go on to display the tags that failed if emv
            }

//          runOnUiThread(()-> {
//          //Update UI stuff here
//          });
        }).start();

    }
    public void processVoid(Terminal terminal, int ecrV){
        new Thread(() -> {
            System.out.println("VOIDING TRANSACTION: "+ecrV);
            System.out.println();

            System.out.println("checkpoint F VOID");

            //Create a PaymentRequest with a TransType VOID and TenderType CREDIT
            DoCreditReq voidSale = new DoCreditReq();
            voidSale.setTransactionType(TransType.VOID);

            //Generate a randomized reference number for the transaction
            ecrRef = genRandomECR(); //
            TraceReq traceReq = new TraceReq();
            traceReq.setEcrRefNumber(ecrRef);//
            //traceReq.setEcrRefNumber("1");

            if(ecrV != 0){
                String ecrString = Integer.toString(ecrV);
                traceReq.setOriginalRefNumber(ecrString);
                voidSale.setTraceInformation(traceReq);
                System.out.println("ORIG ECR REF: "+ecrString);

            }

            //Entry mode - swipe, chip, contactless
            //  request.ExtData = "<EntryModeBitmap>01110000</EntryModeBitmap>";

            ExecutionResult<DoCreditRsp> executionResult = terminal.getTransaction().doCredit(voidSale);

            //Check result code of the transaction
            if (executionResult.isSuccessful()){
                DoCreditRsp response = executionResult.response();

                System.out.println( );
                System.out.println("VOID Results: " );
                System.out.println("========VOID RESULT CODE==== : "+response.hostInformation().hostResponseCode());
                System.out.println("========VOID RESULT CODE==== : "+response.hostInformation().hostResponseMessage());
                System.out.println("");
                System.out.println("=======Void success==========");
                System.out.println("TIME/DATE OF VOIDED TRANSACTION: "+response.traceInformation().hostTimeStamp());
                System.out.println("");

                setStatus("good");

            }
            else if (!executionResult.isSuccessful()){
                System.out.println("=======Void Failed============");
                //Go on to display the tags that failed if emv
            }

//          runOnUiThread(()-> {
//          //Update UI stuff here
//          });
        }).start();
    }
    public void processRefund(Terminal terminal, int amt){
        new Thread(() -> {
        //Create a PaymentRequest with a TransType RETURN and TenderType CREDIT
            DoCreditReq doRefundReq = new DoCreditReq();
            doRefundReq.setTransactionType(TransType.RETURN);
            //Generate a randomized reference number for the transaction
            //   ecrRef = genRandomECR();

            AmountReq amountReq = new AmountReq();
            amountReq.setTransactionAmount(String.valueOf(amt));
            doRefundReq.setAmountInformation(amountReq);

            //Generate a randomized reference number for the transaction
            ecrRef = genRandomECR(); //
            TraceReq traceReq = new TraceReq();
            traceReq.setEcrRefNumber(ecrRef);//
            doRefundReq.setTraceInformation(traceReq);
            //traceReq.setEcrRefNumber("1");

/*            ecrRef = genRandomECR();
            TraceReq traceReq = new TraceReq();
            traceReq.setEcrRefNumber(ecrRef);//*/

            ExecutionResult<DoCreditRsp> executionResult = terminal.getTransaction().doCredit(doRefundReq);
            //Check result code of the transaction
            if (executionResult.isSuccessful()) {
                System.out.print("Refund success");
                DoCreditRsp response = executionResult.response();
                setSuccess(true);
                transDateTime = response.traceInformation().hostTimeStamp();
                setStatus("good");
                System.out.println("=======Payment success==========");
                System.out.println("========PAYMENT RESULT CODE ==== : "+hostCode);
                System.out.println("========PAYMENT RESULT CODE ==== : "+hostMessage);
                System.out.println("PAYMENT REF 1: "+refNum);
                System.out.println("PAYMENT ECR 1: "+ecrRef);
                //displayTransResults(); //USE ME UNTIL FB STOPS OVERRIDING SALES
            }
//          runOnUiThread(()-> {
//          //Update UI stuff here
//          });
        }).start();
    }
    /*public void makeReport(){
        System.out.println("checkpoint D REPORT");

        //Create a ReportRequest with  TransType LOCALTOTALREPORT
        LocalTotalReportReq report = new LocalTotalReportReq();
        report.setEdcType(EdcType.ALL);

        LocalTotalReportRsp reportRsp = new LocalTotalReportRsp();


        if(reportRsp.responseCode().equals("OK")){
            System.out.println("CREATING A 'LOCALTOTALREPORT' FOR ALL EDC TYPES");

            System.out.println("EDC TYPE COUNT: ");
            System.out.println("CREDIT COUNT : "+reportRsp.totals().creditTotals());
            System.out.println("DEBIT COUNT: "+reportRsp.totals().debitTotals());
            System.out.println("EBT COUNT: "+reportRsp.totals().ebtTotals());
            System.out.println("GIFT COUNT: "+reportRsp.totals().giftTotals());
            System.out.println("LOYALTY COUNT: "+reportRsp.totals().loyaltyTotals());
            System.out.println("CASH COUNT: "+reportRsp.totals().cashTotals());
            System.out.println("CHECK COUNT: "+reportRsp.totals().checkTotals());
            System.out.println("===============================");
            System.out.println("EDC TYPE AMOUNT: ");
            System.out.println("CREDIT AMOUNT : "+reportRsp.edcType().valueOf("CREDIT"));
//            System.out.println("DEBIT AMOUNT: "+res.DebitAmount);
//            System.out.println("EBT AMOUNT : "+res.EBTAmount);
//            System.out.println("GIFT AMOUNT : "+res.GiftAmount);
//            System.out.println("LOYALTY AMOUNT : "+res.LoyaltyAmount);
//            System.out.println("CASH AMOUNT : "+res.CashAmount);
//            System.out.println("CHECK AMOUNT : "+res.CHECKAmount);

        } else {
            System.out.println("===Report Failed===");
        }
    }*/
    public void processLookUp(Terminal terminal, int ref){
        new Thread(() -> {
            LocalDetailReportReq lookUpReq = new LocalDetailReportReq();
            System.out.print(ref);
            //refNum
            lookUpReq.setOriginalRefNumber(Integer.toString(ref));
            lookUpReq.setEdcType(EdcType.ALL);

            ExecutionResult<LocalDetailReportRsp> executionResult = terminal.getReport().localDetailReport(lookUpReq);

            if (executionResult.isSuccessful()) {
                LocalDetailReportRsp response = executionResult.response();
                setSuccess(true);

                // String extData = response.;
                // tagReader(1,extData); //1 for cardholder name

                transDateTime = response.traceInformation().timeStamp();
                System.out.println("Trans Date "+transDateTime);
                acctNm = response.accountInformation().account(); //Currently returning last 4
                cardType = String.valueOf(response.accountInformation().cardType());
                transType = String.valueOf(response.transactionType()); //Change me
                apprvdamt = response.amountInformation().approveAmount();
                authCode = response.hostInformation().authCode();
                hostCode = response.hostInformation().hostResponseCode();
                hostResponse = response.hostInformation().hostDetailedMessage();//no show
                System.out.println("HOST RESPON "+hostResponse);
                hostMessage = response.hostInformation().hostResponseMessage();
                refNum = response.traceInformation().refNumber();
                nameCard = response.accountInformation().cardHolder();
                entryMode = String.valueOf(response.accountInformation().entryMode());

                stringResults = transDateTime + "\n" + "Account Number: "+ acctNm + "\n" + "Card Type: "+ cardType + "\n" +
                        "Trans Type: "+ transType + "\n" + "Approved Amt: "+ apprvdamt + "\n" +
                        "Auth Code: "+ authCode + "\n" + "Host Code: "+ hostCode + "\n" + "Host Resp: "+ hostResponse + "\n" +
                        "Host Message: "+ hostMessage + "\n" + "Ref Num: "+ refNum + "\n" + "Card Name: "+ nameCard + "\n" +
                        "Entry Mode: "+entryMode;

                System.out.println("=======Payment success==========");
                System.out.println("========PAYMENT RESULT CODE ==== : "+hostCode);
                System.out.println("========PAYMENT RESULT CODE ==== : "+hostMessage);
                System.out.println("PAYMENT REF 1: "+refNum);
                System.out.println("PAYMENT ECR 1: "+ecrRef);

                setStatus("good");
                //return status;

                //displayTransResults(1,1); //USE ME UNTIL FB STOPS OVERRIDING SALES
            }
            else if (!executionResult.isSuccessful()){
                setSuccess(false);
                System.out.println("Payment Failed");
                setStatus("bad");
                //return status;

                //displayTransResults(1,1);
            }
            //displayTransResults(1,2);
            displayTransResults();

//          runOnUiThread(()-> {
//          //Update UI stuff here
//          });
        }).start();

    }
    public void processPayment(Terminal terminal, int total){
      new Thread(() -> {
          DoCreditReq doCreditReq = new DoCreditReq();
          //Set Signature Flags
          doCreditReq.getTransactionBehavior().setSignatureAcquireFlag(SignatureAcquireFlag.REQUIRED);
          doCreditReq.getTransactionBehavior().setSignatureCaptureFlag(SignatureCaptureFlag.CAPTURE);
          doCreditReq.getTransactionBehavior().setSignatureUploadFlag(SignatureUploadFlag.TO_UPLOAD);

          //Receipt
          doCreditReq.getTransactionBehavior().setReceiptPrintFlag(ReceiptPrintFlag.BOTH_COPY);

          doCreditReq.setTransactionType(TransType.SALE);
          AmountReq amountReq = new AmountReq();
          amountReq.setTransactionAmount(String.valueOf(total));
          doCreditReq.setAmountInformation(amountReq);
          TraceReq traceReq = new TraceReq();
          traceReq.setEcrRefNumber("1");
          doCreditReq.setTraceInformation(traceReq);
          ExecutionResult<DoCreditRsp> executionResult = terminal.getTransaction().doCredit(doCreditReq);

          if (executionResult.isSuccessful()) {
              DoCreditRsp response = executionResult.response();
              setSuccess(true);

              // String extData = response.;
              // tagReader(1,extData); //1 for cardholder name

              transDateTime = response.traceInformation().hostTimeStamp();
              System.out.println("Trans Date "+transDateTime);
              acctNm = response.accountInformation().account(); //Currently returning last 4
              cardType = String.valueOf(response.accountInformation().cardType());
              transType = String.valueOf(response.transactionType()); //Change me
              apprvdamt = response.amountInformation().approveAmount();
              authCode = response.hostInformation().authCode();
              hostCode = response.hostInformation().hostResponseCode();
              hostResponse = response.hostInformation().hostDetailedMessage();//no show
              System.out.println("HOST RESPON "+hostResponse);
              hostMessage = response.hostInformation().hostResponseMessage();
              refNum = response.traceInformation().refNumber();
              nameCard = response.accountInformation().cardHolder();
              entryMode = String.valueOf(response.accountInformation().entryMode());
              TC = response.paymentEmvTag().tc();// NO SHOW
              TVR = response.paymentEmvTag().tvr();
              AID = response.paymentEmvTag().aid();
              TSI = response.paymentEmvTag().tsi();
              ATC = response.paymentEmvTag().atc();
              app = response.paymentEmvTag().appPreferName();
              appLabel = response.paymentEmvTag().appLabel();
              CVM = String.valueOf(response.paymentEmvTag().cvm());
              setStatus("good");
              System.out.println("=======Payment success==========");
              System.out.println("========PAYMENT RESULT CODE ==== : "+hostCode);
              System.out.println("========PAYMENT RESULT CODE ==== : "+hostMessage);
              System.out.println("PAYMENT REF 1: "+refNum);
              System.out.println("PAYMENT ECR 1: "+ecrRef);
              stringResults = transDateTime + "\n" + "Account Number: "+ acctNm + "\n" + "Card Type: "+ cardType + "\n" +
                      "Trans Type: "+ transType + "\n" + "Approved Amt: "+ apprvdamt + "\n" +
                      "Auth Code: "+ authCode + "\n" + "Host Code: "+ hostCode + "\n" + "Host Resp: "+ hostResponse + "\n" +
                      "Host Message: "+ hostMessage + "\n" + "Ref Num: "+ refNum + "\n" + "Card Name: "+ nameCard + "\n" +
                      "Entry Mode: "+ entryMode + "\n" + " TC: " + TC + "\n" + "TVR: "+ TVR + "\n" +  "AID: " + AID + "\n" + "TSI: " + TSI + "\n" +
                      "ATC: " + ATC + "\n" + "App Label: " + appLabel + "\n" + "CVM: " +CVM;
          }
          else if (!executionResult.isSuccessful()){
              setSuccess(false);
              System.out.println("Payment Failed");
              DoCreditRsp response = executionResult.response();
              AC = response.paymentEmvTag().ac();
              AIP = response.paymentEmvTag().aip();
              AVN = response.paymentEmvTag().avn();
              IAUTHD = response.paymentEmvTag().issuerAuthData();
              stringResults = transDateTime + "\n" + "Account Number: "+ acctNm + "\n" + "Card Type: "+ cardType + "\n" +
                      "Trans Type: "+ transType + "\n" + "Approved Amt: "+ apprvdamt + "\n" +
                      "Auth Code: "+ authCode + "\n" + "Host Code: "+ hostCode + "\n" + "Host Resp: "+ hostResponse + "\n" +
                      "Host Message: "+ hostMessage + "\n" + "Ref Num: "+ refNum + "\n" + "Card Name: "+ nameCard + "\n" +
                      "Entry Mode: "+ entryMode + "\n" + " AC: " + AC + "\n" + "AIP: "+ AIP + "\n" +  "AVN: " + AVN + "\n" + "IAUTHHD: " + IAUTHD;
          }
          //displayTransResults(1,1);
          displayTransResults();
//          runOnUiThread(()-> {
//          //Update UI stuff here
//          });
      }).start();


    }
    public String genRandomECR(){
        Random rand = new Random();
        int rand_ecr = rand.nextInt(1000);
        String rnd_ecr = Integer.toString(rand_ecr);
        return rnd_ecr;
    }
    public String getAppInfo(Terminal terminal){
       // new Thread(()->{
            ExecutionResult<InitRsp> executionResult = terminal.getManage().init();
            if (executionResult.isSuccessful()) {
                InitRsp initRsp = executionResult.response();
                //Save sn, appName, and appVersions
                appNameandVersion = initRsp.appName()+" "+initRsp.appVersion();
                sn = "SN: "+initRsp.sn();
                model = initRsp.modelName();
                osVersion = initRsp.osVersion();
                System.out.println("getAppInfo SN: "+sn);

               getSafMode(terminal);
                //isDemoMode(terminal);//
                //setDemoMode(false);
                getVar(terminal); //Use getVar method to see if in demo Mode
                setStatus("good");
                System.out.println("//////INIT SUCCESS///");
                return status;

            } else if (!executionResult.isSuccessful()) {
                System.out.println("//////BAD REQUEST///");
                setStatus("bad");
                return status;

            } else {
                System.out.println("//////ERROR///");
                setStatus("error");
                return status;

            }
        //}).start();

        //MainActivity activity = activityWeakReference.get();
        //Send Init request
        /*ExecutionResult<InitRsp> executionResult = terminal.getManage().init();
        if (executionResult.isSuccessful()) {
            InitRsp initRsp = executionResult.response();
            //Save sn, appName, and appVersions
            appNameandVersion = initRsp.appName()+" "+initRsp.appVersion();
            sn = "SN: "+initRsp.sn();
            model = initRsp.modelName();
            osVersion = initRsp.osVersion();

            getSafMode(terminal);
            //isDemoMode(terminal);//
            setDemoMode(false);
            getVar(terminal); //Use getVar method to see if in demo Mode
            setStatus("good");

            return status;


        } else if (!executionResult.isSuccessful()) {
            System.out.println("//////BAD REQUEST///");

            setStatus("bad");
            return status;

        } else {
            System.out.println("//////ERROR///");
            setStatus("error");
            return status;
        }*/
    }
    public void getVar(Terminal terminal){
        //new Thread(()->{
            MainActivity activity = activityWeakReference.get();
            GetVariableReq getVarReq = new GetVariableReq();
            getVarReq.setVariableName1("demoMode");
            getVarReq.getVariableName1();

            ExecutionResult<GetVariableRsp> executionResult = terminal.getManage().getVariable(getVarReq);
            if(executionResult.isSuccessful()){
                System.out.println("GetVar successful: demoMode: "+executionResult.response().variableValue1());
                if(executionResult.response().variableValue1().equals("Y")){
                    System.out.println("Demo Mode is on");
                    setDemoMode(true); // Demo Mode is on, set demoMode boolean to true
                }
                else if(executionResult.response().variableValue1().equals("N")){
                    System.out.println("Demo Mode is Off");
                    setDemoMode(false);
                }
                setStatus("good");

            }
            else if(!executionResult.isSuccessful()){
                setSuccess(false);
                setStatus("bad");


            }

        //}).start();
    }
    /*public void isDemoMode(Terminal terminal){
        new Thread(()->{
            MainActivity activity = activityWeakReference.get();


        }).start();
    }*/
    public void safUpload(Terminal terminal){
        new Thread(()->{
            System.out.println("Performing SAF Upload");

            SafUploadReq safUploadReq = new SafUploadReq();
            safUploadReq.setSafIndicator(SafIndicator.NEW_STORED_TRANSACTION_RECORDS);

            ExecutionResult<SafUploadRsp> executionResult = terminal.getBatch().safUpload(safUploadReq);
            if(executionResult.isSuccessful()){
                System.out.println("SAF UPLOAD SUCCESS");

                System.out.println("SAF Uploaded Cnt:  "+executionResult.response().safUploadedCount());
                System.out.println("SAF Uploaded Total Amt:  "+executionResult.response().safUploadedAmount());

                System.out.println("SAF Cnt:  "+executionResult.response().safTotalCount());
                System.out.println("SAF Total Amt:  "+executionResult.response().safTotalAmount());

                System.out.println("SAF Failed Cnt: "+executionResult.response().safFailedCount());
                System.out.println("SAF Failed Total Cnt:  "+executionResult.response().safFailedTotal());

                if(Integer.parseInt(executionResult.response().safFailedCount()) > 0 || Integer.parseInt(executionResult.response().safFailedTotal())> 0){
                    //If the saf failed count is greater than 0, or the saf failed total is greater than 0 perform saf failed report
                    //(A saf transaction failed during the upload process) or (a saf transaction failed to upload after the uploading process)
                    safReport(terminal);

                }
            }
            else if(!executionResult.isSuccessful()){
                System.out.println("SAF UPLOAD UNSUCCESSFUL");
            }
        }).start();
    }
    public void safReport(Terminal terminal){
        System.out.println("Attempting SAF Failed Report");
        SafSummaryReportReq safSummaryReportReq = new SafSummaryReportReq();
        safSummaryReportReq.setSafIndicator(SafReportIndicator.FAILED_RECORD_REPORT);

        ExecutionResult<SafSummaryReportRsp> executionResult = terminal.getReport().safSummaryReport(safSummaryReportReq);
        if(executionResult.isSuccessful()){
            System.out.println("Saf summary report completed succesfully");
            System.out.println("Saf summary report completed succesfully");
            System.out.println("Amex Cnt: "+executionResult.response().cardTotalCount().amexCount());
            System.out.println("Amex Amt: "+executionResult.response().cardTotalAmount().amexAmount());
            System.out.println("Disc Cnt: "+executionResult.response().cardTotalCount().discoverCount());
            System.out.println("Disc Amt: "+executionResult.response().cardTotalAmount().discoverAmount());
            System.out.println("Visa Cnt: "+executionResult.response().cardTotalCount().visaCount());
            System.out.println("Visa Amt: "+executionResult.response().cardTotalAmount().visaAmount());
            System.out.println("MC Cnt: "+executionResult.response().cardTotalCount().masterCardCount());
            System.out.println("MC Amt: "+executionResult.response().cardTotalAmount().masterCardAmount());

            stringResults = "SAF Report Results" + "\n" +"Amex Cnt:" + "\n" + executionResult.response().cardTotalCount().amexCount() + "Amex Amt: " +
                    executionResult.response().cardTotalAmount().amexAmount()+ "\n" + "Disc Cnt: "+ executionResult.response().cardTotalCount().discoverCount() +"\n" +
                    "Disc Amt: "+executionResult.response().cardTotalAmount().discoverAmount() + "\n" + "Visa Cnt: " + executionResult.response().cardTotalCount().visaCount() + "\n" +
                    "Visa Amt: "+executionResult.response().cardTotalAmount().visaAmount() + "\n" + "MC Cnt: "+executionResult.response().cardTotalCount().masterCardCount() + "\n" +
                    "MC Amt: "+executionResult.response().cardTotalAmount().masterCardAmount();
            setStatus("good");

        }
        else if(!executionResult.isSuccessful()){
            System.out.println("Saf summary report uncompleted; FAIL");
            setStatus("bad");
        }
        displayTransResults();
    }
    public void setSafMode(Terminal terminal, Integer mode){
        //new Thread(() -> {
            SetSafParametersReq setSafParamReq = new SetSafParametersReq();
            if (mode == 0){
                setSafParamReq.setSafMode(SafMode.STAY_ONLINE);

            }
            else if (mode == 1){
                setSafParamReq.setSafMode(SafMode.STAY_OFFLINE);

            }
            ExecutionResult<SetSafParametersRsp> executionResult = terminal.getManage().setSafParameters(setSafParamReq);

            if (executionResult.isSuccessful()) {

                if (mode == 0 && !getSafModeString().equals("STAY_ONLINE")){
                    setSafModeString("STAY_ONLINE");
                    System.out.println("1 SET SAF MODE SUCCESSFULLY TO "+getSafModeString());
                    setStatus("good");

                }
                else if (mode == 1 && !getSafModeString().equals("STAY_OFFLINE")){
                    setSafModeString("STAY_OFFLINE");
                    System.out.println("2 SET SAF MODE SUCCESSFULLY TO "+getSafModeString());

                }
                // String extData = response.;
                // tagReader(1,extData); //1 for cardholder name

                setStatus("good");
                setSuccess(true);

            }
            else if (!executionResult.isSuccessful()){
                setSuccess(false);
                setStatus("bad");

                //displayTransResults();
            }
            //displayTransResults(1,1);
//          runOnUiThread(()-> {
//          //Update UI stuff here
//          });
        //}).start();

    }
    public void getSafMode(Terminal terminal){
//
        ExecutionResult<GetSafParametersRsp> executionResult = terminal.getManage().getSafParameters();
        if (executionResult.isSuccessful()){
            GetSafParametersRsp getSafParam = executionResult.response();
            System.out.println("==== Inside getSafMODE CURRENTLY SET TO "+ getSafParam.safMode()+"===-==");

            if(getSafParam.safMode().equals(SafMode.STAY_ONLINE)){
                setSafModeString("STAY_ONLINE");
/*                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "SAF MODE is currently set to "+getSafParam.safMode().toString(), Toast.LENGTH_LONG);
                    }
                });*/
                //return getSafParam.safMode().toString();

            }
            else if (getSafParam.safMode().equals(SafMode.STAY_OFFLINE)){
                setSafModeString("STAY_OFFLINE");//Update the String Saf MODE to know the present value of SAF Modea
                //activity.sw_safMode.setChecked(false);
/*                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "SAF MODE is currently set to "+getSafParam.safMode().toString(), Toast.LENGTH_LONG);
                    }
                });*/
                //return getSafParam.safMode().toString();

            }
            setSuccess(true);
            setStatus("good");
        }
        else if (!executionResult.isSuccessful()){
            setSuccess(false);
            setStatus("bad");
            setSafModeString("ERROR");
            //displayTransResults();
        }
    }
    public Terminal setCommSetting(){
       AidlSetting aidlSetting = new AidlSetting ();
       Context contxt = contextWeakReference.get();
       Terminal terminal = POSLinkSemi.getInstance().getTerminal(contxt, aidlSetting);
       return terminal;
   }
    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        //Context ctxt = contextWeakReference.get();
        //transactionModel transactionModel;
        //dataBaseHelper = new DatabaseHelper(ctxt);
        MainActivity activity = activityWeakReference.get();
        Intent results;
        if (!doesExist(activity)){
            System.out.println("Checkpoint POST EXECUTE FAILED");
            return;} //Make sure activity exists
        System.out.println("At on POST EXECUTE///");

        //if (status == "good"){
            switch (typeTransaction){
                case 0:
                    //Get info to display on main activity screen
                    if (activity != null || !activity.isFinishing()){ //If main activity is not null or not finishing
                        //Setting TextViews for App Info on Main Activity Screen
                        System.out.println("//////SETTING TEXT///");
                         //System.out.println("------APP NAME: "+appName);
                        System.out.println("------SERIAL: "+sn);
                        //System.out.println("------APP VERSION: "+appVersion);

                        activity.tv_appNm.setText(appNameandVersion);
                        activity.tv_serialNm.setText(sn);
                        activity.tv_model.setText(model);
                        activity.tv_osVersion.setText(osVersion);


                        //if(getDemoMode() && activity.tv_demoWarning.getVisibility(View.INVISIBLE)){
                        if(getDemoMode()){
                            activity.tv_demoWarning.setVisibility(View.VISIBLE);
                            System.out.println("POST getDemoMode: "+getDemoMode());

                        }
                        //else if(!getDemoMode() && activity.tv_demoWarning.getVisibility(View.VISIBLE)){
                        else if(!getDemoMode()){
                            System.out.println("POST getDemoMode: "+getDemoMode());
                            activity.tv_demoWarning.setVisibility(View.INVISIBLE);
                        }

                        if(getSafModeString().equals("STAY_ONLINE")){
                            activity.sw_safMode.setChecked(true);
                        }
                        else if(getSafModeString().equals("STAY_OFFLINE")){
                            activity.sw_safMode.setChecked(false);
                        }


                    } //Make sure activity exists
                    else{
                        System.out.println("Checkpoint POST EXECUTE FAILED");
                        return;
                    }
                    break;
                case 1:
                    //
                    if (activity != null || !activity.isFinishing()){ //If main activity is not null or not finishing
                    //if (lookUpActivity != null || !lookUpActivity.isFinishing()){ //If lookup activity is not null or not finishing

                    } //Make sure activity exists
                    else{
                        System.out.println("Checkpoint POST EXECUTE FAILED");
                        return;
                    }

                    break;
                case 2:
                    //Set lookup activity
                    if (activity != null || !activity.isFinishing()){ //If main activity is not null or not finishing
                    //if (lookUpActivity != null || !lookUpActivity.isFinishing()){ //If lookup activity is not null or not finishing
                        System.out.println("TEST: "+transDateTime);


                    } //Make sure activity exists
                    else{
                        System.out.println("Checkpoint POST EXECUTE FAILED");
                        return;
/*
                        results = new Intent(transaction_Driver.this, tDriverResultsActivity.class);
                        results.putExtra("results",stringResults);
                        startActivity(results);
*/
                    }
                    break;
                case 7:
                    break;
            //}
        }

        /////////////////////////
/*        if (!doesExist(activity)){
            System.out.println("Checkpoint POST EXECUTE FAILED");
            return;} //Make sure activity exists
        System.out.println("At on POST EXECUTE///");
        if(typeTransaction == 0 && status == "good"){
            //Setting TextViews for App Info on Main Activity Screen
            System.out.println("//////SETTING TEXT///");
           // System.out.println("------APP NAME: "+appName);
            System.out.println("------SERIAL: "+sn);
            //System.out.println("------APP VERSION: "+appVersion);

            activity.tv_appNm.setText(appNameandVersion);
            activity.tv_serialNm.setText(sn);
            activity.tv_model.setText(model);
            activity.tv_osVersion.setText(osVersion);
        }
        else if(typeTransaction == 1 && status == "good"){
            //PROCESS SALE
            System.out.println("ECR NUM: "+ecrRef);
            System.out.println("Ref Num: "+refNum);
           // Context ctxt = contextWeakReference.get();
           // transactionModel transactionModel;
            *//*dataBaseHelper = new DatabaseHelper(ctxt);
            try {
                   // System.out.println("Inserting sale into DB");
                    transactionModel = new transactionModel( -1, transDateTime, acctNm,
                            cardType, transType, apprvdamt, ecrRef, nameCard, authCode, hostCode, hostResponse, hostMessage, refNum);
                    //System.out.println("Success adding transaction to db");
                    //displayTransResults(); //USE ME UNTIL FB STOPS OVERRIDING SALES

            }catch (Exception e) {
               // System.out.println("Cannot to insert DB");
                transactionModel = new transactionModel( -1, "error", "error",
                        "error", "error", "error", "error", "error",
                        "error", "error", "error", "error", "error");

            }

            boolean success = dataBaseHelper.addOne(transactionModel);*//*
        }
        else if (typeTransaction == 2 && status == "good"){
            //activity_lookUp
           // System.out.println("Lets check the db for a count"); **WHEN SEARCHING MADE DB IT IS OVERRIDING PER SALE PROBABLY WHERE CREAITNG DB INSTANCE
*//*            dataBaseHelper = new DatabaseHelper(ctxt);
            dataBaseHelper.isEmpty();*//*
            //displayTransResults(2);
        }*/
    }
    public boolean doesExist(MainActivity activity){
        if(activity != null || !activity.isFinishing()){
           // if(activity != null ){
                System.out.println("Activity exists");
                return true;
            }
       // }
        System.out.println("Activity does NOT exist");
        return false;
    }
    /*public void displayTransResults(int resultFormat, int transType){
       Intent results;
       MainActivity activity = activityWeakReference.get();

        switch(resultFormat){
            case 1:
             //SALE or LOOKUP
*//*                System.out.println("Transaction Date/Time: "+transDateTime); //missing with LOOKUP

                System.out.println("Acct Num: "+acctNm);
                System.out.println("Card Type: "+cardType);
                System.out.println("Trans Type: "+ transType); //MISSING WITH LOOKUP
                System.out.println("Approved Amount: "+apprvdamt);
                System.out.println("authCode: "+authCode);
                System.out.println("Host Code: "+hostCode);
                System.out.println("Host Response: "+hostResponse);
                System.out.println("Host Message: "+hostMessage);
                System.out.println("Ref Num: "+refNum);
                   //Need Transaction Num
                System.out.println("Cardholders Name: "+nameCard);


                if (transType == 1 && getSuccess()){
                   System.out.println("TC: "+TC);
                   System.out.println("TVR: "+TVR);
                   System.out.println("AID: "+AID);
                   System.out.println("TSI: "+TSI);
                   System.out.println("ATC: "+ATC);
                   System.out.println("App Label: "+appLabel);
                   System.out.println("CVM: "+CVM);
                }
                else if (transType==1 && !getSuccess()){
                  System.out.println("AIP: "+AIP);
                  System.out.println("AVN: "+AVN);
                        System.out.println("IAUTHD: "+IAUTHD);
                }*//*
                System.out.println("A"+ "/n");
                //getActivity().startActivity(new Intent(activity.getApplicationContext(), displayResultsActivity.class));
                System.out.println("TEST2:"+stringResults);
                results = new Intent(activity.getApplicationContext(), displayResultsActivity.class);
                results.putExtra(EXTRA_RESULTS,stringResults);
                System.out.println("b" + "/n");
                activity.startActivity(results);
//                        //startActivity(activity.getApplicationContext(), results,results.getBundleExtra(EXTRA_RESULTS));
                //startActivity(activity.getApplicationContext(), results, results.getBundleExtra("EXTRA_NAME"));
                System.out.println("d" + "/n");
                break;
            case 2:
            //BATCH CLOSE


               break;
            case 4:
            //REFUND

               break;
            case 5:
            //VOID

               break;
            default:
               break;
        }

    }*/
    public void displayTransResults(){
        Intent results;
        MainActivity activity = activityWeakReference.get();
        System.out.println("A"+ "/n");
        //getActivity().startActivity(new Intent(activity.getApplicationContext(), displayResultsActivity.class));
        System.out.println("TEST2:"+stringResults);
        results = new Intent(activity.getApplicationContext(), displayResultsActivity.class);
        results.putExtra(EXTRA_RESULTS,stringResults);
        System.out.println("b" + "/n");
        activity.startActivity(results);
//                        //startActivity(activity.getApplicationContext(), results,results.getBundleExtra(EXTRA_RESULTS));
        //startActivity(activity.getApplicationContext(), results, results.getBundleExtra("EXTRA_NAME"));
        System.out.println("d" + "/n");
        /*switch(resultFormat){
            case 1:
                //SALE or LOOKUP
*//*                System.out.println("Transaction Date/Time: "+transDateTime); //missing with LOOKUP

                System.out.println("Acct Num: "+acctNm);
                System.out.println("Card Type: "+cardType);
                System.out.println("Trans Type: "+ transType); //MISSING WITH LOOKUP
                System.out.println("Approved Amount: "+apprvdamt);
                System.out.println("authCode: "+authCode);
                System.out.println("Host Code: "+hostCode);
                System.out.println("Host Response: "+hostResponse);
                System.out.println("Host Message: "+hostMessage);
                System.out.println("Ref Num: "+refNum);
                   //Need Transaction Num
                System.out.println("Cardholders Name: "+nameCard);


                if (transType == 1 && getSuccess()){
                   System.out.println("TC: "+TC);
                   System.out.println("TVR: "+TVR);
                   System.out.println("AID: "+AID);
                   System.out.println("TSI: "+TSI);
                   System.out.println("ATC: "+ATC);
                   System.out.println("App Label: "+appLabel);
                   System.out.println("CVM: "+CVM);
                }
                else if (transType==1 && !getSuccess()){
                  System.out.println("AIP: "+AIP);
                  System.out.println("AVN: "+AVN);
                        System.out.println("IAUTHD: "+IAUTHD);
                }*//*
                System.out.println("A"+ "/n");
                //getActivity().startActivity(new Intent(activity.getApplicationContext(), displayResultsActivity.class));
                System.out.println("TEST2:"+stringResults);
                results = new Intent(activity.getApplicationContext(), displayResultsActivity.class);
                results.putExtra(EXTRA_RESULTS,stringResults);
                System.out.println("b" + "/n");
                activity.startActivity(results);
//                        //startActivity(activity.getApplicationContext(), results,results.getBundleExtra(EXTRA_RESULTS));
                //startActivity(activity.getApplicationContext(), results, results.getBundleExtra("EXTRA_NAME"));
                System.out.println("d" + "/n");
                break;
            case 2:
                //BATCH CLOSE


                break;
            case 4:
                //REFUND

                break;
            case 5:
                //VOID

                break;
            default:
                break;
        }*/

    }
    public void tagReader(int tag, String extData){
        //1--> cardholders name
        //2--> transaction type
        //3-->
        extData = "<root>"+extData+"</root>";

        switch (tag){
            case 1:
                try{
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    InputSource src = new InputSource();
                    src.setCharacterStream(new StringReader(extData));
                    Document doc = builder.parse(src);
                    cardName = doc.getElementsByTagName("PLNameOnCard").item(0).getTextContent();
                }
                catch(Exception e){
                    cardName = "Null Name";
                }
                break;
            case 2:
                try{
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    InputSource src = new InputSource();
                    src.setCharacterStream(new StringReader(extData));
                    Document doc = builder.parse(src);
                    transType = doc.getElementsByTagName("PLNameOnCard").item(0).getTextContent();
                }
                catch(Exception e){
                    transType = "UNKNOWN";
                }
                break;

            default:
                break;
        }
    }
}

