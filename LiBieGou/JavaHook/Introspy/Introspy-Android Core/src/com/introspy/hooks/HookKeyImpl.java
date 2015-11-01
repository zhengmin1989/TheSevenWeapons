package com.introspy.hooks;

class Intro_CRYPTO_KEY extends Intro_CRYPTO {
	
}

class Intro_GET_KEY extends Intro_CRYPTO_KEY { 
	public void execute(Object... args) {
		byte[] key = (byte[]) args[0];
		if (key != null) {
			String skey = _getReadableByteArr(key);
			_logParameter("Key", skey);
			_logParameter("Algo", args[1]);
			_logBasicInfo();
			_logFlush_I("-> Key: ["+skey+"], algo: "+args[1]);
		}
	}
}

class Intro_CRYPTO_KEYSTORE_HOSTNAME extends Intro_CRYPTO_KEY { 
	public void execute(Object... args) {
		
		_logBasicInfo();
		// arg2 is the passcode for this trustore
		if (args[2] != null) {
			String passcode = 
					_getReadableByteArr((byte[]) args[2]);
			_logParameter("Passcode", args[2]);
			_logFlush_I("-> TrustStore passcode: " + passcode);
		}
	}
}

class Intro_CRYPTO_KEYSTORE extends Intro_CRYPTO_KEY { 
	public void execute(Object... args) {
		
		_logBasicInfo();
		// arg1 is the passcode for the trustore
		if (args[1] != null) {
			String passcode = 
					_getReadableByteArr((byte[]) args[1]);
			_logParameter("Passcode", args[1]);
			_logFlush_I("-> TrustStore passcode: " + passcode);
		}
	}
}

class Intro_CRYPTO_PBEKEY extends Intro_CRYPTO_KEY { 
	public void execute(Object... args) {
		_logBasicInfo();

		String passcode = new String((char[])args[0]);
		String salt = null;
		int iterationCount = -1;
		if (args.length >= 2 && args[1] != null) {
			salt = 
				_byteArrayToReadableStr((byte[])args[1]);
			iterationCount = (Integer)args[2];
			_logParameter("Passcode", passcode);
			_logParameter("Salt", salt);
			_logParameter("Iterations", iterationCount);
			// _logReturnValue("Key", _hookInvoke(args));
			_logFlush_I("-> Passcode: [" + passcode + "], Salt: [" + salt + 
					"], iterations: " + iterationCount + "");
		}
		else {
			_logParameter("Passcode", passcode);
			_logFlush_I("Passcode: [" + passcode + "]");
		}
	}
}
