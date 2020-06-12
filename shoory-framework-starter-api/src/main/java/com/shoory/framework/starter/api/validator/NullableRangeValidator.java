package com.shoory.framework.starter.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.shoory.framework.starter.api.validator.annotation.NullableRange;

public class NullableRangeValidator implements ConstraintValidator<NullableRange, Long> {
	private long min;
	private long max;
    /**
     * 初始化
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(NullableRange constraintAnnotation) {
        //获取禁止的词汇
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }
    
	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return value == null ||
				value >= this.min && value <= this.max;
	}


}