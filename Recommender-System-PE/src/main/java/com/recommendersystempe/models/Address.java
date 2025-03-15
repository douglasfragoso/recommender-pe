package com.recommendersystempe.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {
    @Column(name = "street", length = 40)
    @NotBlank(message = "The field street is required")
    @Size(max = 40, message = "Street must be at most 40 characters")
    private String street;

    @Column(name = "house_number")
    @NotNull(message = "The field number is required")
    @Min(value = 1, message = "House number must be at least 1")
    private Integer number;

    @Column(name = "complement", length = 20)
    @Size(max = 20, message = "Complement must be at most 20 characters")
    private String complement;

    @Column(name = "neighborhood", length = 30)
    @NotBlank(message = "The field neighborhood is required")
    @Size(max = 30, message = "Neighborhood must be at most 30 characters")
    private String neighborhood;

    @Column(name = "city", length = 30)
    @NotBlank(message = "The field city is required")
    @Size(max = 30, message = "city must be at most 30 characters")
    private String city;

    @Column(name = "states", length = 2)
    @NotBlank(message = "The field state is required")
    @Size(min = 2, max = 2, message = "State must be exactly 2 characters")
    private String state;

    @Column(name = "country", length = 20)
    @NotBlank(message = "The field country is required")
    @Size(max = 20, message = "Country must be at most 20 characters")
    private String country;

    @Column(name = "zip_code", length = 8)
    @NotBlank(message = "The field zipCode is required")
    @Size(min = 8, max = 8, message = "Zip code must be exactly 8 characters")
    private String zipCode;
}