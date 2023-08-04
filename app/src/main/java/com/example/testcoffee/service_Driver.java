package com.example.testcoffee;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.HandlerThread;

import androidx.annotation.Nullable;

public class service_Driver extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
