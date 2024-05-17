package dev.ctrlspace.provenai.backend.model.dtos.criteria;

import java.util.List;

public class OrganizationCriteria {
    private String organizationId;
    private String country;
    private String name;
    private List<String> organizationIdIn;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<String> getOrganizationIdIn() {
        return organizationIdIn;
    }

    public void setOrganizationIdIn(List<String> organizationIdIn) {
        this.organizationIdIn = organizationIdIn;
    }
}
