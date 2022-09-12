package com.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class ProductBean {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int productId;
	private String productName;
	private String productDesc;
	@ManyToMany
	@JoinTable(name = "product_categories", joinColumns = { @JoinColumn(name = "productId") }, inverseJoinColumns = {
			@JoinColumn(name = "categoryId") })
	private List<CategoryBean> categories;
}
