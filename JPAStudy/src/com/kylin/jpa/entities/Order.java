package com.kylin.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "ORDERS")
@Entity
public class Order {

	private int id;
	private String orderName;
	private Person person;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "ORDER_NAME")
	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	//映射单线多对一的关联关系
	//使用@ManyToOne来映射多对一的关联关系
	//使用@JoinColumn来映射外键
	//可以使用@ManyToOne的fetch属性来修改默认的关联属性的加载策略
	@JoinColumn(name="PERSON_ID")//映射外键列的列名
	@ManyToOne(fetch=FetchType.LAZY)
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	
	@Transient
	@Override
	public String toString() {
		return "Order [id=" + id + ", orderName=" + orderName + ", person=" + person + "]";
	}

	public Order(String orderName, Person person) {
		super();
		this.orderName = orderName;
		this.person = person;
	}

	public Order() {
	}

}
