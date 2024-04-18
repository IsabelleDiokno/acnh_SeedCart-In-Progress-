package com.example.testcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class refundActivity extends AppCompatActivity {
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    TextView tv_espqty, tv_cappqty, tv_latteqty
            , tv_stotal, tv_saletx, tv_gtotal, tv_cappst, tv_espst, tv_lattest;
    Button btn_capp, btn_esp, btn_latte, btn_rfnd, btn_cancel;
    int capp_qty =0;
    int esp_qty = 0;
    int latte_qty = 0;
    double capp_st, esp_st, latte_st, sub_tot;
    double sales_tx, grd_tot;
    final double CAPP_PRICE = 2.50;
    final double ESP_PRICE = 1;
    final double LATTE_PRICE = 3.50;
    final double SALES_TAX= 0.07;
   // final int RESULT_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        //Buttons and Textviews on Layout
        btn_capp = findViewById(R.id.btn_orgseeds);
        btn_esp = findViewById(R.id.btn_turseeds);
        btn_latte = findViewById(R.id.btn_pchSeeds);
        btn_rfnd = findViewById(R.id.btn_rfnd);
        btn_cancel = findViewById(R.id.btn_cancelrefund);
        tv_espqty = findViewById(R.id.tv_espqty);
        tv_cappqty = findViewById(R.id.tv_cappqty);
        tv_latteqty = findViewById(R.id.tv_latteqty);
        tv_stotal = findViewById(R.id.tv_stotal);
        tv_saletx = findViewById(R.id.tv_salestx);
        tv_gtotal = findViewById(R.id.tv_gtotal);
        tv_cappst = findViewById(R.id.tv_cappst);
        tv_espst = findViewById(R.id.tv_espst);
        tv_lattest = findViewById(R.id.tv_lattest);

        //Set a listeners for menu items
        btn_rfnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("Pay button clicked sending grand total");
                payButtonClicked(view);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cancelButtonClicked(view);
            }
        });
        btn_capp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // gets called when clicked
                addToCart("cappucino");
            }
        });
        btn_esp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // gets called when clicked
                addToCart("espresso");
            }
        });
        btn_latte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // gets called when clicked
                addToCart("latte");
            }
        });
    }

    public void payButtonClicked(View v){
        System.out.println("----- Pay button clicked sending grand total");

        Intent data = new Intent();
        String refundAmount = Double.toString(grd_tot);
        data.putExtra("refundAmt", refundAmount);
        setResult(RESULT_OK, data);
       // setResult(RESULT_CODE,data);
        finish();
    }
    public void cancelButtonClicked(View v){
        System.out.println("Cancelling Transaction");
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }
    public String reformat(double d){

        String f = df2.format(d);
        return f;
    }
    public void addToCart(String item){

        switch (item){
            case "cappucino":
                capp_qty++;
                tv_cappqty.setText(reformat(capp_qty));
                capp_st = CAPP_PRICE * capp_qty;
                tv_cappst.setText(reformat(capp_st));
                wrapupCart();
                break;
            case "espresso":
                esp_qty++;
                tv_espqty.setText(reformat(esp_qty));
                esp_st = ESP_PRICE * esp_qty;
                tv_espst.setText(reformat(esp_st));
                wrapupCart();
                break;
            case "latte":
                latte_qty++;
                tv_latteqty.setText(reformat(latte_qty));
                latte_st = LATTE_PRICE * latte_qty;
                tv_lattest.setText(reformat(latte_st));
                wrapupCart();
                break;
            default:
                break;
        }
    }
    public void wrapupCart(){
        sub_tot = capp_st + esp_st + latte_st;
        tv_stotal.setText("Subtotal: "+reformat(sub_tot));
        sales_tx = sub_tot * SALES_TAX;
        tv_saletx.setText("Sales tax: "+reformat(sales_tx));
        grd_tot = sub_tot + sales_tx;
        tv_gtotal.setText("Grand Total: "+reformat(grd_tot));

    }

}