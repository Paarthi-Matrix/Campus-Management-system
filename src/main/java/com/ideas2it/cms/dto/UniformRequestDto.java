package com.ideas2it.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UniformRequestDto {
    private String shirtSize;
    private int shoeSize;
    private int pantSize;
    private String rollNumber;
}
