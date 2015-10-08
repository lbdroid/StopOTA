package ml.rabidbeaver.stopota;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;
import com.stericson.RootShell.execution.Shell.ShellContext;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
	
	private Shell rshell;
	String logtag = "StopOTA-Boot";
	boolean success = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(logtag,"Running onReceive");
		if (RootShell.isAccessGiven()){
			
			Log.d(logtag,"Root access is granted");
			
			final Command command1 = new Command(0,
					"pm disable com.google.android.gms/.update.SystemUpdateService",
					"pm disable com.google.android.gms/.update.SystemUpdateService\\$SecretCodeReceiver",
					"pm disable com.google.android.gms/.update.SystemUpdateService\\$Receiver",
					"pm disable com.google.android.gms/.update.SystemUpdateService\\$ActiveReceiver",
					"pm disable com.google.android.gms/.update.SystemUpdateService\\$OtaPolicyReceiver",
					"rm /cache/update.zip"){
				@Override
				public void commandOutput(int id, String line){
					Log.d(logtag,line);
				}
			};
			
			final Command command2 = new Command(0,
					"rm /data/data/com.google.android.gms/shared_prefs/update.xml",
					"rm /data/data/com.google.android.gms/app_download/*"){
				@Override
				public void commandOutput(int id, String line){
					Log.d(logtag,line);
				}
			};
			
			try {
				rshell = RootShell.getShell(true, ShellContext.SYSTEM_APP);
				rshell.add(command1);
				Log.d(logtag,"Components disabled.");
				success=true;
			} catch (Exception e) {
				Log.d(logtag,"exception");
			}
			
			try {
				Shell rshellb = RootShell.getShell(true, ShellContext.SYSTEM_SERVER);
				rshellb.add(command2);
			} catch (Exception e) {
				Log.d(logtag,"exception");
			}

		} else {
			Log.d(logtag,"Root access not granted");
		}
		if (success){
			Log.d(logtag,"Complete, setting kill alarm for 20 seconds");
			AlarmManager mgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Intent i=new Intent(context, OnAlarmReceiver.class);
			PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
			mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+20000, pi);
		} else Log.d(logtag,"Unsuccessful");
	}
}
