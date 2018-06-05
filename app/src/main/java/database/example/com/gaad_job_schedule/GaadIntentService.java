package database.example.com.gaad_job_schedule;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class GaadIntentService extends IntentService {

    private String TAG = "GAAD_INTENT";
    private static IntentUICallBack intentUICallBack;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "On Create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "On Destroy");
    }

    public interface IntentUICallBack {
        void uiUpdate(String updateString);
    }

    public GaadIntentService() {
        super("GaadIntentService");
    }

    public static void startIntentService(Context context, String name, IntentUICallBack intentCallBack) {
        intentUICallBack = intentCallBack;
        Intent intent = new Intent(context, GaadIntentService.class);
        intent.putExtra(GaadIntentService.class.getSimpleName(), name);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null)
            return;
        String name = intent.getStringExtra(GaadIntentService.class.getSimpleName());

        try {
            Thread.sleep(1000);
            if (intentUICallBack != null) {
                intentUICallBack.uiUpdate(name + "_" + System.currentTimeMillis());
            }
            Thread.sleep(2000);
            if (intentUICallBack != null) {
                intentUICallBack.uiUpdate(name + "_" + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
