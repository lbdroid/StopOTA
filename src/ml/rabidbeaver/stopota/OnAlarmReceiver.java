package ml.rabidbeaver.stopota;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;
import com.stericson.RootShell.execution.Shell.ShellContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {

	String logtag = "StopOTA-Alarm";
	private Shell rshell;
	private String pid="";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(logtag,"Running onReceive");
		if (RootShell.isAccessGiven()){
			try {
				rshell = RootShell.getShell(true, ShellContext.SYSTEM_SERVER);
			} catch (Exception e1) {}
			Log.d(logtag,"Root access is granted");
			
			Command command = new Command(0,"dumpsys power | grep SystemUpdateService"){
				@Override
				public void commandOutput(int id, String line){
					Log.d(logtag,line);
					if (line.contains("SystemUpdateService")){
						String s1 = line.substring(line.indexOf("pid="));
						pid = s1.substring(s1.indexOf("=")+1,s1.indexOf(","));
						Command killcmd = new Command(0,"kill -9 "+pid);
						try {
							Log.d(logtag,"Killing com.google.android.gms to clear SystemUpdateService wakelock");
							rshell.add(killcmd);
						} catch (Exception e) {}
					}
				}
			};

			try {
				rshell.add(command);
			} catch (Exception e) {
				Log.d(logtag,"exception");
			}

		} else {
			Log.d(logtag,"Root access not granted");
		}
	}
}
