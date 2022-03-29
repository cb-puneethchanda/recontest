package com.example.recontest.models;

public class Transactions {
	String txn_id;
	String pg_id;
	String type;
	public String getTxn_id() {
		return txn_id;
	}
	public void setTxn_id(String txn_id) {
		this.txn_id = txn_id;
	}
	public String getPg_id() {
		return pg_id;
	}
	public void setPg_id(String pg_id) {
		this.pg_id = pg_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Transactions(String txn_id, String pg_id, String type) {
		super();
		this.txn_id = txn_id;
		this.pg_id = pg_id;
		this.type = type;
	}
	
}
