package com.bean;

import lombok.Data;

@Data
public class ResponseBean<T> {
	private T data;
	private String msg;
	private Integer errorCode;
}
