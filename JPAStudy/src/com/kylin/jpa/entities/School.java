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

	// ����һ�Զ��ӳ��
	// ʹ��@OneToMany��ӳ��һ�Զ�Ĺ�����ϵ
	// ʹ��@JoinColumn��ӳ������е�����
	// ����ʹ��@OneToMany��fetch�������������޸�Ĭ�ϵļ��ز���
	//ʹ��mappedBy�Ͳ���ʹ��JoinColumn
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
