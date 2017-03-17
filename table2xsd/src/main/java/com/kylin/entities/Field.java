package com.kylin.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Field {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String type;
	private String minOccurs;
	private String maxOccurs;
	private String enumeration;
	private String integerRange;
	private String length;
	private String lengthRange;
	private String whiteSpace;
	private String fractionDigits;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMinOccurs() {
		return minOccurs;
	}

	public void setMinOccurs(String minOccurs) {
		this.minOccurs = minOccurs;
	}

	public String getMaxOccurs() {
		return maxOccurs;
	}

	public void setMaxOccurs(String maxOccurs) {
		this.maxOccurs = maxOccurs;
	}

	public String getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(String enumeration) {
		this.enumeration = enumeration;
	}

	public String getIntegerRange() {
		return integerRange;
	}

	public void setIntegerRange(String integerRange) {
		this.integerRange = integerRange;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getLengthRange() {
		return lengthRange;
	}

	public void setLengthRange(String lengthRange) {
		this.lengthRange = lengthRange;
	}

	public String getWhiteSpace() {
		return whiteSpace;
	}

	public void setWhiteSpace(String whiteSpace) {
		this.whiteSpace = whiteSpace;
	}

	public String getFractionDigits() {
		return fractionDigits;
	}

	public void setFractionDigits(String fractionDigits) {
		this.fractionDigits = fractionDigits;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + ", type=" + type + ", minOccurs=" + minOccurs + ", maxOccurs=" + maxOccurs
				+ ", enumeration=" + enumeration + ", integerRange=" + integerRange + "]";
	}

}
