package database.example.com.gaad_job_schedule;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private int jobId = 0;
    private ComponentName componentName;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent jobServiceIntent = new Intent(this, GaadJobService.class);
        Message message = new Message();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setJobOnUserDemand() {
        long jobDelay = 1, jobDuration = 1, jobDeadline = 1;

        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putLong(WORK_DURATION_KEY, (jobDuration * 1000));

        JobInfo.Builder builder = new JobInfo.Builder(jobId++, componentName);
        builder.setMinimumLatency((jobDelay * 1000)).setOverrideDeadline((jobDeadline * 1000))
                .setExtras(persistableBundle);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null)
            return;
        jobScheduler.schedule(builder.build());
    }

    class handleJobService extends Handler {

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            switch (message.what) {
                case JobType.JOB_DEFAULT:
                    break;

                case JobType.JOB_START:

                    break;

                case JobType.JOB_STOP:
                    break;
            }
        }
    }
}
