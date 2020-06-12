package com.shoory.framework.starter.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.shoory.framework.starter.api.validator.annotation.NullableNotBlank;

public class NullableNotBlankValidator implements ConstraintValidator<NullableNotBlank, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || value.trim().length() > 0;
	}
}