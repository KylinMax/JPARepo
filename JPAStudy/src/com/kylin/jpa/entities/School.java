package com.kylin.jpa.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "SCHOOL")
@Entity
public class School {

	private int id;
	private String schoolName;
	private Set<Student> students = new HashSet<>();

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "SCHOOL_NAME")
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	// 单项一对多的映射
	// 使用@OneToMany来映射一对多的关联关系
	// 使用@JoinColumn来映射外键列的名称
	// 可以使用@OneToMany的fetch的懒加载属性修改默认的加载策略
	//使用mappedBy就不能使用JoinColumn
//	@JoinColumn(name = "SCHOOL_ID") 
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.REMOVE}, mappedBy="school")
	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	public School(String schoolName, Set<Student> students) {
		super();
		this.schoolName = schoolName;
		this.students = students;
	}

	public School() {
	}



	@Transient
	@Override
	public String toString() {
		return "School [id=" + id + ", schoolName=" + schoolName + "]";
	}

}
