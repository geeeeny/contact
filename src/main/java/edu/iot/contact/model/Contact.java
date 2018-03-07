package edu.iot.contact.model;

import java.util.Date;

public class Contact {
	private int contactId;
	private String owner;
	private String name;
	private String email;
	private String cellPhone;
	private String address;
	private Date regDate;
	private Date updateDate;
	
	public Contact() {
		
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "Contact [contactId=" + contactId + ", owner=" + owner + ", name=" + name + ", email=" + email
				+ ", cellPhone=" + cellPhone + ", address=" + address + ", regDate=" + regDate + ", updateDate="
				+ updateDate + "]";
	}
	
	
}
