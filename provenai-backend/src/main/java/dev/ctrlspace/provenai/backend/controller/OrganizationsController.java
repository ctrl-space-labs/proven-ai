package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.controller.specs.OrganizationsControllerSpec;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrganizationsController implements OrganizationsControllerSpec {

    @GetMapping("/organizations")
    public String getOrganizations() {
        return "Organizations";
    }
}
