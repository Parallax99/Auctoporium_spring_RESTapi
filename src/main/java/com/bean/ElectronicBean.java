package com.bean;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "electronics")
public class ElectronicBean {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer electronicId;
	private String deviceName;
	private String deviceDescription;
	private String condition;
	private Integer intialAmmount;
	private Date manufacturedDate;
}
