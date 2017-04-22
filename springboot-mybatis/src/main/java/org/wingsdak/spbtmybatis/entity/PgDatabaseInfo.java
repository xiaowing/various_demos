package org.wingsdak.spbtmybatis.entity;

import java.io.Serializable;

public class PgDatabaseInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8815134005388694082L;
	
	private String databaseName;
	private String databaseDbaName;
	private String databaseCtype;
	private String databaseEncode;
	
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getDatabaseDbaName() {
		return databaseDbaName;
	}
	public void setDatabaseDbaName(String databaseDbaName) {
		this.databaseDbaName = databaseDbaName;
	}
	public String getDatabaseCtype() {
		return databaseCtype;
	}
	public void setDatabaseCtype(String databaseCtype) {
		this.databaseCtype = databaseCtype;
	}
	public String getDatabaseEncode() {
		return databaseEncode;
	}
	public void setDatabaseEncode(String databaseEncode) {
		this.databaseEncode = databaseEncode;
	}

}
