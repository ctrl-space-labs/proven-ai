package dev.ctrlspace.provenai.backend.model.dtos.validators;

import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuditPermissionOfUseVcCriteriaValidator implements ConstraintValidator<ValidAuditPermissionOfUseVcCriteria, AuditPermissionOfUseVcCriteria> {

    @Override
    public boolean isValid(AuditPermissionOfUseVcCriteria criteria, ConstraintValidatorContext context) {
        if (criteria.getOwnerOrganizationId() == null && criteria.getProcessorOrganizationId() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Owner or Processor organization id is missing")
                    .addConstraintViolation();
            return false;
        }

        if (criteria.getFrom() == null || criteria.getTo() == null || criteria.getTimeIntervalInSeconds() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("From, To or TimeIntervalInSeconds is missing")
                    .addConstraintViolation();
            return false;
        }

        if (criteria.getFrom().isAfter(criteria.getTo())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("From is after To")
                    .addConstraintViolation();
            return false;
        }

        if (criteria.getTimeIntervalInSeconds() < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("TimeIntervalInSeconds is negative")
                    .addConstraintViolation();
            return false;
        }

        if ((criteria.getTo().toEpochMilli() - criteria.getFrom().toEpochMilli()) / (criteria.getTimeIntervalInSeconds() * 1000) > 720) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("TimeIntervalInSeconds is too small")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
