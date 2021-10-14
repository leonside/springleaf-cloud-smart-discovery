package com.springleaf.cloud.discovery.config.datasource.converter;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * Verifies configured rule conditions
 * @author leon
 */
public class Validations {

    private static  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Validations(){}

    public static Validations getInstance(){
        return new Validations();
    }

    /**
     * Validates a single object
     * @param instance
     */
    public void validate(Object instance){
        Set<ConstraintViolation<Object>> validate = validator.validate(instance, new Class[0]);
        if(!CollectionUtils.isEmpty(validate)){
            String message = validate.stream().map(ConstraintViolation::getMessage).reduce((m1, m2) -> m1 + ";" + m2).orElse("configuration validate exception");
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates the collection object
     * @param instance
     */
    public void validateList(List<?> instance){
        if(CollectionUtils.isEmpty(instance)){
            return;
        }

        instance.stream().forEach(it->{
            validate(it);
        });
    }

}
