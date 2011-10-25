package com.silvermongoose.ecc_ff;

public class Tech {
	
	private int techID;
	private String techFirstName;
	private String techLastName;
	private String techEmail;
	private String techPhone;
	private String techActive;
	private String techPermission;
	
	public Tech()
	{
		//None
	}

	public int getTechID() {
		return techID;
	}

	public String getTechFirstName() {
		return techFirstName;
	}

	public String getTechLastName() {
		return techLastName;
	}

	public String getTechEmail() {
		return techEmail;
	}

	public String getTechPhone() {
		return techPhone;
	}

	public String getTechActive() {
		return techActive;
	}

	public String getTechPermission() {
		return techPermission;
	}
	
	public String toString() {
		return techFirstName + " " + techLastName;
	}
}
