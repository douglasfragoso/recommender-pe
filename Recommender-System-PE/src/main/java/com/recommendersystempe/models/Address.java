package com.recommendersystempe.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {
    @Column(name = "street", length = 40)
    private String street;
    @Column(name = "number")
    private Integer number;
    @Column(name = "complement", length = 20)
    private String complement;
    @Column(name = "neighborhood", length = 30)
    private String neighborhood;
    @Column(name = "state", length = 2)
    private String state;
    @Column(name = "country", length = 20)
    private String country;
    @Column(name = "zip_code", length = 8)
    private String zipCode;
}
