package com.example.deukgeun.global.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@SuppressWarnings("rawtypes")
public class EnumValidator implements ConstraintValidator<ValidEnum, Enum>{
	private ValidEnum annotation;

	@Override
	public void initialize(ValidEnum constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	/**
	 * 주어진 Enum 값이 지정된 Enum 클래스에 속하는지 유효성을 검사합니다.
	 *
	 * @param value   검사할 Enum 값입니다.
	 * @param context 제약 조건 검사 컨텍스트입니다.
	 * @return Enum 값이 지정된 Enum 클래스에 속하는 경우 true, 그렇지 않으면 false를 반환합니다.
	 */
	@Override
	public boolean isValid(Enum value, ConstraintValidatorContext context) {
		if (value != null) {
			Object[] enumValues = this.annotation.enumClass().getEnumConstants();

			if (enumValues != null) {
				for (Object enumValue : enumValues) {
					if (value == enumValue) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
