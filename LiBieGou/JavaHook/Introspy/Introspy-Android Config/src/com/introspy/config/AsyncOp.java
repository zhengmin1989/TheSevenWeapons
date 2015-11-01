package com.introspy.config;

import android.content.Context;
import android.os.AsyncTask;

@SuppressWarnings("rawtypes")
public class AsyncOp extends AsyncTask {
	public AsyncOp(Context _context) {
		// TODO Auto-generated constructor stub
	}

	// will be used to not freeze the GUI while executing commands
	@Override
	protected Object doInBackground(Object... params) {
		//if (!InjectConfig.getInstance().execute((String) params[0])) {
			//Toast.makeText(getActivity(), "This app. needs root!", 
			//		Toast.LENGTH_LONG).show();
		//}
		return null;
	}
}
