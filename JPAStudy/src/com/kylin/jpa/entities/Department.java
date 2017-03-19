package com.kylin.jpa.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "DEPARTMENT")
@Entity
public class Department {
	private Integer id;
	private String departmentName;
	private Manager manager;

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@JoinColumn(name = "MANAGER_ID", unique = true)
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@Transient
	@Override
	public String toString() {
		return "Department [id=" + id + ", departmentName=" + departmentName + ", manager=" + manager + "]";
	}

}
