package com.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class CategoryBean {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int categoryId;
	private String categoryName;
	@ManyToMany(mappedBy = "categories")
	private List<ProductBean> products;
	@OneToMany(mappedBy = "category")
	private List<HouseBean> houses;
}
