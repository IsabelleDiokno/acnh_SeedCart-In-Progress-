package com.example.testcoffee;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


@SuppressWarnings("deprecation")

public class FirstFragment extends Fragment {

    Button btn_tran_sale;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v=inflater.inflate(R.layout.fragment_first,container,false);
        //fragment_sale salefrag = new fragment_sale();
        //btn_tran_sale=v.findViewById(R.id.btn_tran_sale);


        //Init POSLink for Android
        Context context = getActivity();
        //Check if context exists here

     //   POSLinkAndroid.init(context);
        FirstFragment ff = new FirstFragment();
        //if(!doesExist(this)){System.out.println("FIRST FRGMNT THIS DOES NOT EXIST" ); }
        //retrieveInfo(ff, context);

/*        btn_tran_sale=v.findViewById(R.id.btn_tran_sale);

        //Click listener for Sale Button
        btn_tran_sale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.content_main,new fragment_sale())
                        .addToBackStack(null).commit();
            }
        }); */
        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public boolean doesExist(Fragment frg){
/*       if(frg!=null && frg != null && frg.getActivity() != null &&
                frg
                        .isVisible()
                && !frg.isRemoving())*/
        Fragment fragmentA = frg.getFragmentManager().findFragmentById(R.id.fr);
        if(fragmentA !=null ){
            System.out.println("Fragment exists");
            return true;
        }
        System.out.println("Fragment does NOT exist");
        return false;
    }
    //Static method to get the BroadPOS app info
    //Calls payment engine thread
//    public static void retrieveInfo(Fragment fragmentNm,Context context){
//        PosLink pLink = POSLinkCreator.createPoslink(context); //Create a new PosLink Object
//        transaction_Driver td = new transaction_Driver(context); //Create a new payment engine class
//        td.execute(0); //execute the PaymentEngine thread
//    }
}