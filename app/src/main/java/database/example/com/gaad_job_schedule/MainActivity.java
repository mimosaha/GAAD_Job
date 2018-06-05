package database.example.com.gaad_job_schedule;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int jobId = 0;
    private ComponentName componentName;
    private HandleJobService handleJobService;
    private long startTime = 0;

    public static final String MESSENGER_INTENT_KEY
            = BuildConfig.APPLICATION_ID + ".MESSENGER_INTENT_KEY";
    public static final String WORK_DURATION_KEY =
            BuildConfig.APPLICATION_ID + ".WORK_DURATION_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jobId = 0;
        componentName = new ComponentName(this, GaadJobService.class);
        handleJobService = new HandleJobService();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent jobServiceIntent = new Intent(this, GaadJobService.class);
        Messenger messenger = new Messenger(handleJobService);
        jobServiceIntent.putExtra(MESSENGER_INTENT_KEY, messenger);
        startService(jobServiceIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setJobOnUserDemand(View view) {
        long jobDelay = 2, // Time need for starting job
                jobDuration = 2, // Time need to triggering on Stop call after starting job
                jobDeadline = 2; // When time limit has been exceed then call the deadline

        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putLong(WORK_DURATION_KEY, (jobDuration * 1000));

        JobInfo.Builder builder = new JobInfo.Builder(jobId++, componentName);
        builder.setMinimumLatency((jobDelay * 1000)).setOverrideDeadline((jobDeadline * 1000))
                .setExtras(persistableBundle);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null)
            return;
        jobScheduler.schedule(builder.build());
        startTime = System.currentTimeMillis();
    }

    public void startIntentService(View view) {
        GaadIntentService.startIntentService(this, "MIMO SAHA", new GaadIntentService.IntentUICallBack() {
            @Override
            public void uiUpdate(String updateString) {
                Log.v("MIMO_SAHA::", "Update String: " + updateString);
            }
        });
    }


    private class HandleJobService extends Handler {

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            switch (message.what) {
                case JobType.JOB_DEFAULT:
                    Log.v("MIMO_SAHA:", "DEFAULT_JOB");
                    break;

                case JobType.JOB_START:
                    long timeTaken = System.currentTimeMillis() - startTime;
                    Log.v("MIMO_SAHA:", "JOB START " + timeTaken);
                    break;

                case JobType.JOB_STOP:
                    Log.e("MIMO_SAHA:", "JOB STOP");
                    break;
            }
        }
    }
}
