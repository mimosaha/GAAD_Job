package database.example.com.gaad_job_schedule;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class GaadBindService extends Service {

    private String TAG = GaadBindService.class.getName();
    private ServiceBinder serviceBinder = new ServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "OnCreate");
    }


    class ServiceBinder extends Binder {
        GaadBindService getService() {
            return GaadBindService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
        return serviceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }
}
