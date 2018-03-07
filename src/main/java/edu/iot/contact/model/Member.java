package edu.iot.contact.model;

import java.util.Date;

public class Member {
	private String userId;
	private String name;
	private String password;
	private String cellPhone;
	private String email;
	private String address;
	private int grade;
	private Date regDate;
	private Date updateDate;
	
	//디폴트 생성자
	public Member() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Date getUpgradeDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "Member [userId=" + userId + ", name=" + name + ", password=" + password + ", cellPhone=" + cellPhone
				+ ", email=" + email + ", address=" + address + ", grade=" + grade + ", regDate=" + regDate
				+ ", upgradeDate=" + updateDate + "]";
	}
	
	
}
