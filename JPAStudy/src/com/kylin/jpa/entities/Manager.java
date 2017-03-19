package com.kylin.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "MANAGER")
@Entity
public class Manager {
	private Integer id;
	private String name;
	private Department department;

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToOne(mappedBy = "manager")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department departmetn) {
		this.department = departmetn;
	}

	@Transient
	@Override
	public String toString() {
		return "Manager [id=" + id + ", name=" + name + "]";
	}

}
