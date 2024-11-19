package com.capstone.csdrms.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="tblstudent")
public class StudentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "studentID", nullable = false)
	private String sid;
	
	private String name;
	private int grade;
	private String section;
	private String gender;
	private String email;
	private String homeAddress;
	private String emergencyNumber;
	
	@Column(nullable = false)
	private String schoolYear;
	
	private int current;

	public StudentEntity() {
		super();
	}

	public StudentEntity(Long id, String sid, String name, int grade, String section, String gender, String email,
			String homeAddress, String emergencyNumber, String schoolYear, int current) {
		super();
		this.id = id;
		this.sid = sid;
		this.name = name;
		this.grade = grade;
		this.section = section;
		this.gender = gender;
		this.email = email;
		this.homeAddress = homeAddress;
		this.emergencyNumber = emergencyNumber;
		this.schoolYear = schoolYear;
		this.current = current;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getEmergencyNumber() {
		return emergencyNumber;
	}

	public void setEmergencyNumber(String emergencyNumber) {
		this.emergencyNumber = emergencyNumber;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	
	
}