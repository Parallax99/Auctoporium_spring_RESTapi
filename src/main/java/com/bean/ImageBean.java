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
@Data
@Table(name = "images")
public class ImageBean {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer imageId;
	private String imagePath;
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserBean users;

}
