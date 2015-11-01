package com.introspy.config;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;

public class UpdateAppList  extends ListFragment {
	
	protected SharedPreferences _sp;
	protected ArrayList<String> _appList = 
			new ArrayList<String>();
	protected ArrayList<String> _completeAppList = 
			new ArrayList<String>();
	protected Context 	_context;
	
	protected String 	_lastItemSelected = null;
	protected Boolean 	_lastItemChecked = null;
		
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		try {
			_lastItemSelected = 
					getListView().getItemAtPosition(position).toString();		
			// String prompt = 
			//		"clicked item: " + _lastItemSelected + "\n\n";
			// prompt += "selected items: \n";
			
			int count = getListView().getCount();
			
			SparseBooleanArray sparseBooleanArray = getListView().getCheckedItemPositions();
			_lastItemChecked = false;
			for (int i = 0; i < count; i++){
				if (sparseBooleanArray.get(i)) {
					// prompt += getListView().getItemAtPosition(i).toString() + "\n";
					if (i == position)
						_lastItemChecked = true;
				}
			}
	
			// commit the change to preferences
			_sp.edit().putBoolean(_lastItemSelected, _lastItemChecked).commit();
			
		} catch (Exception e) {
			Log.w("IntrospyConfig", "Error:onListItemClick:" + e + 
					"\n SP: "+ _sp);
		}
	}
	
    protected void updateAppList() {
		android.content.pm.PackageManager pm = _context.getPackageManager();
		List<android.content.pm.PackageInfo> list = pm.getInstalledPackages(0);
	
		for(android.content.pm.PackageInfo pi : list) {
			try{
				android.content.pm.ApplicationInfo ai = pm.getApplicationInfo(pi.packageName, 0);
                String currAppName = pm.getApplicationLabel(pi.applicationInfo).toString();
				if ((ai.flags & 129) == 0) {
	                // one list for display and one list to keep track of app dirs
	                _completeAppList.add("[" + currAppName + "]\n" +
	                		pi.applicationInfo.packageName);
	                _appList.add(pi.applicationInfo.dataDir);
				}

			} catch (Exception e) {
				Log.w("IntrospyConfig", "Error: " + e);
			}
		}
    }
}
