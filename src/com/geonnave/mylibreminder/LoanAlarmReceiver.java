package com.geonnave.mylibreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class LoanAlarmReceiver extends BroadcastReceiver {

	  private static final int NOTIFICATION = 3456; 
	  private NotificationManager mNM;
	  /*since you're always doing a 1-time notification, we can make this final and static, the number
	   won't change. If you want it to change, consider using SharedPreferences or similar to keep track 
	   of the number. You would have the same issue with a Service since you call stopself() and so,
	   you would delete the object every time.*/

	    @Override
	    public void onReceive(Context context,Intent intent) {

	      final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//	      builder.setSmallIcon(R.drawable.clock_alarm);
	      builder.setContentTitle("Time is up");
	      builder.setContentText("SLIMS");
	      builder.setVibrate(new long[] { 0, 200, 100, 200 });
	      final Notification notification = builder.build(); 

	      mNM.notify(NOTIFICATION, notification);
	    }

}