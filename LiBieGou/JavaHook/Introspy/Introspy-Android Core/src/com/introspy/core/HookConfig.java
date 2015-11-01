package com.introspy.core;

public class HookConfig {
	private String 		_className;
	private String 		_methodName;
	private Class<?>[] 	_parameters;
	private boolean 	_active;
	private String		_type;
	private IntroHook	_IntroHook;
	private String		_notes;
	private String 		_subType;
	
	// getters
	public String getClassName() 	{ return _className; }
	public String getMethodName() 	{ return _methodName; }
	public Class<?>[] getParameters() { return _parameters; }
	public boolean isActive() 	{ return _active; }
	public IntroHook getFunc() { return _IntroHook; }
	public String getNotes() 	{ return _notes; }

	public String getType() 	{ return _type; }
	public String getSubType() 	{ return _subType; }
	
	public String getCategory() { 
		if (_subType.isEmpty())
			return _type; 
		return _subType;
	}
	
	// constructor
	public HookConfig(boolean active,
			String type,
			String subType,
			String className, 
			String methodName,
			IntroHook IntroHook,
			Class<?>[] parameters,
			String notes) {
		
		_IntroHook = IntroHook;
		_className = className;
		_active = active;
		_methodName = methodName;
		_parameters = parameters;
		_type = type;
		_subType = subType;
		_notes = notes;
	}
	
	// constructor for custom hooks
	public HookConfig(boolean active,
			String className, 
			String methodName,
			Class<?>[] parameters,
			IntroHook IntroHook,
			String notes) {
		
		_IntroHook = IntroHook;
		_className = className;
		_active = active;
		_methodName = methodName;
		_parameters = parameters;
		_type = "CUSTOM HOOK";
		_subType = "CUSTOM HOOK";
		_notes = notes;
	}
	
	// constructor for custom hooks without notes
	public HookConfig(boolean active,
			String className, 
			String methodName,
			Class<?>[] parameters,
			IntroHook IntroHook) {
		
		_IntroHook = IntroHook;
		_className = className;
		_active = active;
		_methodName = methodName;
		_parameters = parameters;
		_type = "CUSTOM HOOK";
		_subType = "CUSTOM HOOK";
		_notes = "";
	}
}
