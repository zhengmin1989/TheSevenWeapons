package com.introspy.custom_hooks;

import android.content.Intent;

import com.introspy.core.IntroHook;

public class HookExampleImpl extends IntroHook {
	@Override
	public void execute(Object... args) {
		_logBasicInfo();
		_logParameter("Intent details", (Intent)args[0]);
		_logFlush_I("Example of a CUSTOM HOOK. Notes: " + _notes);
	}
}

