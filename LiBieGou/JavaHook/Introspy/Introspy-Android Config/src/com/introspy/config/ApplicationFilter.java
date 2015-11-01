package com.introspy.config;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ApplicationFilter extends UpdateAppList {

	private Boolean _alwaysOverwriteConfig = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
		_context = this.getActivity().getApplicationContext();
		
		updateAppList();
		
		ListAdapter myListAdapter = new ArrayAdapter<String>(
				getActivity(),
				R.layout.list_item,
				_completeAppList);
		
		setListAdapter(myListAdapter);
		} catch (Exception e) {
			Log.w("Introspy", "error: " + e);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listfragment, container, false);
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		try {
			_lastItemSelected = _appList.get(position);
			int count = getListView().getCount();			
			SparseBooleanArray sparseBooleanArray = getListView().getCheckedItemPositions();
			_lastItemChecked = false;
			for (int i = 0; i < count; i++){
				if (sparseBooleanArray.get(i) && i == position) {
						_lastItemChecked = true;
				}
			}
			// commit the change to preferences
			_sp.edit().putBoolean(_lastItemSelected, _lastItemChecked).commit();
			
		} catch (Exception e) {
			Log.w("IntrospyConfig", "Error:onListItemClick:" + e + 
					"\n SP: "+ _sp);
		}
		InjectConfig.getInstance().writeConfig(_lastItemChecked, 
				_lastItemSelected, _context);
		InjectConfig.getInstance().commit();
	}
	
    @Override
    public void onStart() {
            super.onStart();
            try {
	            _sp = _context.getSharedPreferences("ApplicationFilter", 0);
	            Boolean setup = _sp.getBoolean("Enabled", false);
	            
	            if (setup == false) {
	        		_sp.edit().putBoolean("Enabled", true).commit();
	            	for(String item : _appList) {
	            		_sp.edit().putBoolean(item, false).commit();
	            	}
	            	
		        	final ListView list = getListView();
		        	for (int i = 0; i < getListAdapter().getCount(); i++) {
		        	        list.setItemChecked(i, false);
		        	}
	            }
	            // create preferences with the appDir as key
	            // TODO: uninstalled apps are never cleaned
	            // it does not create a bug because installed apps
	            // are compared against what is in the preferences 
	            // but it keeps an history of all apps ever installed
	            // ..no time to change this behavior
	            else {
	            	final ListView list = getListView();
	            	for (int i = 0; i < _appList.size(); i++) {
	            		String appDir = _appList.get(i);
	            		Boolean checked = _sp.getBoolean(appDir, false);
	            		list.setItemChecked(i, checked);
	            		// overwrite / remove the config file even if preferences
	            		// state otherwise because the tester may remove/add it
	            		if (_alwaysOverwriteConfig) {
	            			InjectConfig.getInstance().writeConfig(
	            					checked, appDir, _context);
	            		}
	            	}
	            	if (_alwaysOverwriteConfig)
	            		InjectConfig.getInstance().commit();
	            }
            }
            catch (Exception e) {
            	Log.w("IntrospyConfig", "Error: " + e);
            }
    }
}
