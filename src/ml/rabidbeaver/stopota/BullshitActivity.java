package ml.rabidbeaver.stopota;

import com.stericson.RootShell.RootShell;

import android.app.Activity;
import android.os.Bundle;

public class BullshitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (RootShell.isAccessGiven()){
			this.finish();
		}
	}
}
