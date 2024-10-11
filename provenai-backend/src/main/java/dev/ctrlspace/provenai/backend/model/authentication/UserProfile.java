package dev.ctrlspace.provenai.backend.model.authentication;

import dev.ctrlspace.provenai.backend.model.Types;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserProfile {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String userName;
    private String phone;
    private Types globalUserRoleType;
    private String name;
    private List<OrganizationUserDTO> organizations;

}
