package com.track.db.util;

public class ExceptionUtil {
	
	public static enum Result{
    	OK,
    	ERROR
    }
	
	private Result Status;

	private String ClassName;
	
	private String ErrorMessage;
	
	private Object ResultData;

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}

	public Result getStatus() {
		return Status;
	}

	public void setStatus(Result ok) {
		Status = ok;
	}

	public Object getResultData() {
		return ResultData;
	}

	public void setResultData(Object resultData) {
		ResultData = resultData;
	}

	
	
	
}
