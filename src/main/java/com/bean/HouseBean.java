package com.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "housing")
@Data
public class HouseBean {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer houseId;
	private String houseName;
	private String currentOwner;
	@ManyToOne
	@JoinColumn(name = "categoryId")
	private CategoryBean category;
	private String spaceDivision;
	private String Dimension;
	private long intialAmount;

}
