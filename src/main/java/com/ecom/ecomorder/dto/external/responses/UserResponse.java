package com.ecom.ecomorder.dto.external.responses;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String fName;
    private String lName;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private AddressDTO address;
}
