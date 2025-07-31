package com.recommendersystempe.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Schema(description = "Represents the Address in the system")
public class Address {
    
    @Column(name = "street", length = 40)
    @Schema(description = "Street name", example = "Rua Exemplo", required = true)
    @NotBlank(message = "The field street is required")
    @Size(max = 40, message = "Street must be at most 40 characters")
    private String street;

    
    @Column(name = "house_number")
    @Schema(description = "House number", example = "100", required = true)
    @Min(value = 1, message = "House number must be at least 1")
    private Integer number;

    @Column(name = "complement", length = 50)
    @Schema(description = "Complement of the address", example = "Apto 202")
    @Size(max = 50, message = "Complement must be at most 50 characters")
    private String complement;

    @Column(name = "neighborhood", length = 30)
    @Schema(description = "Neighborhood name", example = "Boa Viagem", required = true)
    @Size(max = 30, message = "Neighborhood must be at most 30 characters")
    private String neighborhood;

    @Column(name = "city", length = 30)
    @Schema(description = "City name", example = "Recife", required = true)
    @NotBlank(message = "The field city is required")
    @Size(max = 30, message = "city must be at most 30 characters")
    private String city;

    @Column(name = "states", length = 2)
    @Schema(description = "State name", example = "PE", required = true)
    @NotBlank(message = "The field state is required")
    @Size(min = 2, max = 2, message = "State must be exactly 2 characters")
    private String state;

    @Column(name = "country", length = 20)
    @Schema(description = "Country name", example = "Brasil", required = true)
    @NotBlank(message = "The field country is required")
    @Size(max = 20, message = "Country must be at most 20 characters")
    private String country;

    @Column(name = "zip_code", length = 8)
    @Schema(description = "Zip code", example = "50000000", required = true)
    @NotBlank(message = "The field zipCode is required")
    @Size(min = 8, max = 8, message = "Zip code must be exactly 8 characters")
    @Pattern(regexp = "^\\d{8}$", message = "Zip code must be in the format 50000000")
    private String zipCode;
    
}