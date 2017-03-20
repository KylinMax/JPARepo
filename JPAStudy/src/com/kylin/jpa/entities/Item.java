package com.kylin.jpa.entities;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Table(name = "ITEMS")
@Entity
public class Item {

	private Integer id;
	private String itemName;
	private Set<Category> categories = new HashSet<>();

	@GeneratedValue
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "ITEM_NAME")
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	// 使用ManyToMany来映射多对多的关联关系
	// 使用@JoinTable来映射中间表
	// 1.  name指向中间表的名字
	// 2.  JoinColums映射当前表在中间表的哪一列
	// 2.1 name指定中间列的列明
	// 2.2 referencedColumnName指定外键列关联当前表的哪一列
	// 3.  inverseJoinColumn映射关联的类所在中间表的外键
	// 3.1 name指定中间列的列明
	// 3.2 referencedColumnName指定外键列关联当前表的哪一列
	@JoinTable(name = "ITEM_CATAGORY", joinColumns = {
			@JoinColumn(name = "ITEM_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "CATAGORY_ID", referencedColumnName = "ID") })
	@ManyToMany
	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@Transient
	@Override
	public String toString() {
		return "Items [id=" + id + ", itemName=" + itemName + ", categories=" + categories + "]";
	}

}
