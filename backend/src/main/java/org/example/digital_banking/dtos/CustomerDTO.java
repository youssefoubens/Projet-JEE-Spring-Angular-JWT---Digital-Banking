package org.example.digital_banking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private Long customer_id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String city;
}