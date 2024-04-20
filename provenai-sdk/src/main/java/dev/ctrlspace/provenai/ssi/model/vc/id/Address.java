package dev.ctrlspace.provenai.ssi.model.vc.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Address class for places of birth and current address
/**
 * Represents an address.
 * <p>
 *     An address is a collection of properties that describe a location.
 *     It can be used to represent a place of birth or a current address.
 * </p>

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
class Address {
    private String addressCountry;
    private String addressRegion;
    private String addressLocality;
    private String postalCode;
    private String streetAddress;
    private String fullAddress;

}
