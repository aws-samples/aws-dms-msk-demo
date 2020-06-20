package com.amazonaws.samples.msks3feeder.entity;

public class RecordPojo {

	private int sno;
	private String Lastname;
	private String Firstname;
	private String city;

	private String timestamp;
	private String operation;
	private long transactionId;

	public RecordPojo() {

	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getLastname() {
		return Lastname;
	}

	public void setLastname(String lastname) {
		Lastname = lastname;
	}

	public String getFirstname() {
		return Firstname;
	}

	public void setFirstname(String firstname) {
		Firstname = firstname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}


	
	 public RecordPojo(int sno, String Lastname, String Firstname, String city,String timestamp,
	            long transactionId) {
	        this.sno = sno;
	        this.Lastname = Lastname;
	        this.Firstname = Firstname;
	        this.city = city;
	        this.timestamp = timestamp;
	        this.transactionId = transactionId;
	    }
	 
	@Override
	public String toString() {
		return "RecordPojo [sno=" + sno + ", Lastname=" + Lastname + ", Firstname=" + Firstname + ", city=" + city
				+ ", timestamp=" + timestamp + ", operation=" + operation + ", transactionId=" + transactionId + "]";
	}

}
