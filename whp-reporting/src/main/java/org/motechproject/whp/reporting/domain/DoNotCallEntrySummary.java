package org.motechproject.whp.reporting.domain;



public class DoNotCallEntrySummary {

	private String entityId;
	
	private Long mobileNumber;
	
	public String getEntityId() {
		return entityId;
	}
	
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
   
	@Override
	public String toString() {
		return String.valueOf(mobileNumber.longValue()) ;
	}
}
