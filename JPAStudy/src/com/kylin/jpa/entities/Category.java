package com.kylin.jpa.entities;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Table(name = "CATAGORY")
@Entity
public class Category {

	private Integer id;
	private String catagoryName;
	private Set<Item> items = new HashSet<>();

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "CATAGORY_NAME")
	public String getCatagoryName() {
		return catagoryName;
	}

	public void setCatagoryName(String catagoryName) {
		this.catagoryName = catagoryName;
	}

	@ManyToMany(mappedBy = "categories")
	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	@Transient
	@Override
	public String toString() {
		return "Category [id=" + id + ", catagoryName=" + catagoryName + ", items=" + items + "]";
	}

}
