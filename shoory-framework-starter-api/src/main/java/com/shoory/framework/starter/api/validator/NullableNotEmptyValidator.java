package com.shoory.framework.starter.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.shoory.framework.starter.api.validator.annotation.NullableNotEmpty;

public class NullableNotEmptyValidator implements ConstraintValidator<NullableNotEmpty, Object> {
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return value == null || 
				value.getClass().isArray() && ((Object[])value).length > 0;
	}
}