package com.recommendersystempe.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    private String firstName;
    private String lastName;
    private String token;
}
