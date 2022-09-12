package com.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "antique")
public class AntiqueBean {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer antiqueId;
	private String antiqueName;
	private String antiqueDesc;
	private Long intialAmmount;
}
