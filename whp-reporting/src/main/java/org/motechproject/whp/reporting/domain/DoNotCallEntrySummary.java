package org.motechproject.whp.reporting.domain;



public class DoNotCallEntrySummary {

	private String entityId;
	
	private String mobileNumber;
	
	public String getEntityId() {
		return entityId;
	}
	
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
   
	@Override
	public String toString() {
		return String.valueOf(mobileNumber) ;
	}
}
