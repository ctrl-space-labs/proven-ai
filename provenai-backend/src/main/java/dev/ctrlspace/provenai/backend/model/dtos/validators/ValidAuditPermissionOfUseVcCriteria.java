package dev.ctrlspace.provenai.backend.model.dtos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuditPermissionOfUseVcCriteriaValidator.class)
@Documented
public @interface ValidAuditPermissionOfUseVcCriteria {
    String message() default "Invalid AuditPermissionOfUseVcCriteria";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

