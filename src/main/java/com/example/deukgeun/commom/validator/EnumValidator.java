package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@SuppressWarnings("rawtypes")
public class EnumValidator implements ConstraintValidator<ValidEnum, Enum>{
	private ValidEnum annotation;

	@Override
	public void initialize(ValidEnum constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Enum value, ConstraintValidatorContext context) {
		boolean flag = false;
		
		Object[] enumValues = this.annotation.enumClass().getEnumConstants();
		
		if (enumValues != null) {
			for (Object enumValue : enumValues) {
				if (value == enumValue) {
					flag = true;
					break;
				}
			}
		}
		
		return flag;
	}


}
