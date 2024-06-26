package com.capstone.csdrms.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tblstudent")
public class StudentEntity {
	
	@Id
	@Column(name = "studentID")
	private String sid;
	
	private String firstname;
	private String middlename;
	private String lastname;
	private int grade;
	private String section;
	private String schoolYear;
	private Long adviser_id;
	
	@Column(name = "contact_number")
	private String con_num;

	public StudentEntity() {
		super();
	}

	public StudentEntity(String sid, String firstname, String middlename, String lastname, int grade, String section,
			String schoolYear, Long adviser_id, String con_num) {
		super();
		this.sid = sid;
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.grade = grade;
		this.section = section;
		this.schoolYear = schoolYear;
		this.adviser_id = adviser_id;
		this.con_num = con_num;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Long getAdviser_id() {
		return adviser_id;
	}

	public void setAdviser_id(Long adviser_id) {
		this.adviser_id = adviser_id;
	}

	public String getCon_num() {
		return con_num;
	}

	public void setCon_num(String con_num) {
		this.con_num = con_num;
	}

	
	
}