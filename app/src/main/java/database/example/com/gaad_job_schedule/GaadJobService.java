package database.example.com.gaad_job_schedule;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GaadJobService extends JobService {

    private Messenger messages;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("MIMO_SAHA:", "On Create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("MIMO_SAHA:", "On Destroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        messages = intent.getParcelableExtra(MainActivity.MESSENGER_INTENT_KEY);
        return START_REDELIVER_INTENT;
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        handleStartService(JobType.JOB_START, jobParameters.getJobId());
        long duration = jobParameters.getExtras().getLong(MainActivity.WORK_DURATION_KEY);

        Log.v("MIMO_SAHA:", "Duration: " + duration);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleStartService(JobType.JOB_STOP, jobParameters.getJobId());
                jobFinished(jobParameters, false);
            }
        }, duration);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        handleStartService(JobType.JOB_STOP, jobParameters.getJobId());
        return false;
    }

    private void handleStartService(int jobType, Object params) {

        if (messages == null)
            return;

        Message message = Message.obtain();
        message.what = jobType;
        message.obj = params;
        try {
            messages.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
