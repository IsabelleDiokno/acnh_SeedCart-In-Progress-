package com.example.testcoffee;

import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.DecimalFormat;

public class saleViewModel extends ViewModel{
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private MutableLiveData<String> totalAmount;
    int capp_qty =0;
    int esp_qty = 0;
    int latte_qty = 0;
    double capp_st, esp_st, latte_st, sub_tot;
    double sales_tx, grd_tot;
    final double CAPP_PRICE = 2.50;
    final double ESP_PRICE = 1;
    final double LATTE_PRICE = 3.50;
    final double SALES_TAX= 0.07;

    private final MutableLiveData<saleViewModel> uiState =
            new MutableLiveData<>(new saleViewModel()); //should be one val?
    public LiveData<saleViewModel> getuiState(){
        return uiState;
    }

    private MutableLiveData<String> getCurrentTotalAmount(){
        if(totalAmount == null){
            totalAmount = new MutableLiveData<String>();
        }
        return totalAmount;
    }
    public String reformat(double d){

        String f = df2.format(d);
        return f;
    }
    public void addToCart(String item){

        switch (item){
            case "cappucino":
                 capp_qty++;
                 capp_st = CAPP_PRICE * capp_qty;
                wrapupCart();
                break;
            case "espresso":
                esp_qty++;
                esp_st = ESP_PRICE * esp_qty;
                wrapupCart();
                break;
            case "latte":
                latte_qty++;
                latte_st = LATTE_PRICE * latte_qty;
                wrapupCart();
                break;
            default:
                break;
        }
    }
    public void wrapupCart(){
        sub_tot = capp_st + esp_st + latte_st;
        sales_tx = sub_tot * SALES_TAX;
        grd_tot = sub_tot + sales_tx;

    }

}

