package dev.ctrlspace.provenai.backend.repositories.specifications;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.util.StringUtils;
import dev.ctrlspace.provenai.backend.model.QOrganization;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

public class OrganizationPredicates {

    private static QOrganization qOrganization = QOrganization.organization;

    public static Predicate build(OrganizationCriteria criteria) {
        return ExpressionUtils.allOf(
                organizationId(criteria.getOrganizationId()),
                name(criteria.getName()),
                country(criteria.getCountry()),
                organizationIdIn(criteria.getOrganizationIdIn())
        );
    }


    private static Predicate organizationId(String organizationId) {
        if (StringUtils.isNullOrEmpty(organizationId)) {
            return null;
        }
        return qOrganization.id.eq(UUID.fromString(organizationId));
    }

    public static Predicate country(String country) {
        if (StringUtils.isNullOrEmpty(country)) {
            return null;
        }
        return QOrganization.organization.country.eq(country);
    }

    public static Predicate name(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            return null;
        }
        return QOrganization.organization.name.eq(name);
    }

    private static Predicate organizationIdIn(List<String> organizationIdIn) {
        if (organizationIdIn == null || organizationIdIn.isEmpty()) {
            return null;
        }
        return qOrganization.id.in(organizationIdIn.stream().map(UUID::fromString).toArray(UUID[]::new));
    }
}
