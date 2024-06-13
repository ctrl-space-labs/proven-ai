package dev.ctrlspace.provenai.backend.model.authentication;

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
    private String userTypeId;
    private String name;
    private List<OrganizationUserDTO> organizations;

}
