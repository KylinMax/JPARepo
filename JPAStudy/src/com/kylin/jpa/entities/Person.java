package com.kylin.jpa.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.transaction.Transactional;

@Table(name = "PERSON_TABLE")
@Entity
public class Person {
	private int id;
	private String lastName;
	private int age;
	private String email;
	private Date createTime;
	private Date Birthday;

	// @TableGenerator(name="PK_GENERATOR",
	// table="PK_GENERATOR",
	// pkColumnName="PK_COLUMN_NAME",
	// pkColumnValue="PERSON_ID",
	// valueColumnName="PK_VALUE",
	// allocationSize=100)
	// @GeneratedValue(strategy = GenerationType.TABLE,
	// generator="PK_GENERATOR")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return Birthday;
	}

	public void setBirthday(Date birthday) {
		Birthday = birthday;
	}

	@Transient
	public String getInfo() {
		return "LastName: " + lastName + "Email: " + email;
	}

	public Person(String lastName, int age, String email, Date createTime, Date birthday) {
		super();
		this.lastName = lastName;
		this.age = age;
		this.email = email;
		this.createTime = createTime;
		Birthday = birthday;
	}

	public Person() {
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", lastName=" + lastName + ", age=" + age + ", email=" + email + ", createTime="
				+ createTime + ", Birthday=" + Birthday + "]";
	}

	
}
