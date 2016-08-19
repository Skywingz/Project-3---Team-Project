package immersive.android.assembly.general.yelpquest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by raymour on 8/12/16.
 */
public class Notifications extends JobService {
    private static final String TAG = "Notifications";
    AsyncTask<Void, Void, NotificationCompat.Builder> mTask;
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
     mTask = new AsyncTask<Void, Void, NotificationCompat.Builder>() {


         @Override
         protected NotificationCompat.Builder doInBackground(Void... voids) {

             builder.setSmallIcon(R.drawable.cast_ic_notification_play);
             builder.setContentTitle("GoodLuck on your Quest");
             builder.setContentText("May the Yelp be with you!");
             builder.setAutoCancel(true);
             Log.i(TAG, "doInBackground: notificationalert");
             return builder;
         }

         @Override
         protected void onPostExecute(NotificationCompat.Builder builder) {

             NotificationManagerCompat.from(Notifications.this).notify(678, builder.build());
         }

     }.execute();


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
//create a job scheduler that will run something in 30 seconds
//make sure it is in a AsyncTask
// within the Async task push a notification
