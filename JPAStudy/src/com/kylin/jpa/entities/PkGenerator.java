package com.kylin.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "PK_GENERATOR")
@Entity
public class PkGenerator {

	private int id;
	private String columnName;
	private int pkValue;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "PK_COLUMN_NAME")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	@Column(name = "PK_VALUE")
	public int getPkValue() {
		return pkValue;
	}

	public void setPkValue(int pkValue) {
		this.pkValue = pkValue;
	}

	

	public PkGenerator(String columnName, int pkValue) {
		super();
		this.columnName = columnName;
		this.pkValue = pkValue;
	}

	public PkGenerator() {
	}

}
