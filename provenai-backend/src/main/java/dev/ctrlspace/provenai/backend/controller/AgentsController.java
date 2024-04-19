package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.controller.specs.AgentsControllerSpec;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AgentsController implements AgentsControllerSpec {


    @GetMapping("/agents")
    public String getAgents(Authentication authentication ) {
        return "Agents";
    }
}