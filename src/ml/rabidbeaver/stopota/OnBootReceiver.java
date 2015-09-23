package ml.rabidbeaver.stopota;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.execution.Command;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("StopOTA","Running onReceive");
		if (RootShell.isAccessGiven()){
			Log.d("StopOTA","Root access is granted");
			Command command = new Command(0,
					"pm disable com.google.android.gms/.update.SystemUpdateService",
					"pm disable com.google.android.gms/.update.SystemUpdateService$ActiveReceiver",
					"pm disable com.google.android.gms/.update.SystemUpdateService$Receiver",
					"pm disable com.google.android.gms/.update.SystemUpdateService$OtaPolicyReceiver",
					"rm /data/data/com.google.android.gms/shared_prefs/update.xml",
					"rm /data/data/com.google.android.gms/app_download/*"){
				@Override
				public void commandOutput(int id, String line){
					Log.d("StopOTA",line);
				}
			};
			try {
				RootShell.getShell(true).add(command);
			} catch (Exception e) {}
		} else {
			Log.d("StopOTA","Root access not granted");
		}
		Log.d("StopOTA","complete");
	}
}
