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

	//ӳ�䵥�߶��һ�Ĺ�����ϵ
	//ʹ��@ManyToOne��ӳ����һ�Ĺ�����ϵ
	//ʹ��@JoinColumn��ӳ�����
	//����ʹ��@ManyToOne��fetch�������޸�Ĭ�ϵĹ������Եļ��ز���
	@JoinColumn(name="PERSON_ID")//ӳ������е�����
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
