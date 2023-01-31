package com.example.deukgeun.trainer.entity;

public enum Gender {
	M(0, "남자") , 
	F(1, "여자");

	private int type;
	private String name;

	Gender(int type, String name) {
		this.name = name;
		this.type = type;
	}
}
