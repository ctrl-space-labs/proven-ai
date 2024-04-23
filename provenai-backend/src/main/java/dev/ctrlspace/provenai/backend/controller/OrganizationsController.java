package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.controller.specs.OrganizationsControllerSpec;
import dev.ctrlspace.provenai.backend.model.Organization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrganizationsController implements OrganizationsControllerSpec {

    @GetMapping("/organizations")
    public String getOrganizations() {
        return "Organizations";
    }

    @PostMapping("/organizations/registration")
    public Organization registerOrganization(Organization organization) {
        return Organization.builder().name("Ctrl+Space Labs").build();
    }
}
