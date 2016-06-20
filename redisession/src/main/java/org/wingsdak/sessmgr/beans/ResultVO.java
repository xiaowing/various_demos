package org.wingsdak.sessmgr.beans;

import java.io.Serializable;

public class ResultVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8821613740938704943L;
	public static final long RESULT_OK = 0;
	public static final long RESULT_NG = 1;
	
	private long returnValue;
	private String message;
	private Object data;
	public long getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(long returnValue) {
		this.returnValue = returnValue;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "ResultVO [returnValue=" + returnValue + ", message=" + message + ", data=" + data + "]";
	}
	
	

}
