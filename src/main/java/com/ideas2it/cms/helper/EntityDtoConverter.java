package com.ideas2it.cms.helper;

import com.ideas2it.cms.dto.StudentRequestDto;
import com.ideas2it.cms.model.UniformMeasurement;

public class EntityDtoConverter {

    public static UniformMeasurement toUniformMeasurement(StudentRequestDto studentRequestDto) {
        UniformMeasurement uniformMeasurement = new UniformMeasurement();
        uniformMeasurement.setId(studentRequestDto.getUniformRequestDto().getRollNumber());
        uniformMeasurement.setPantSize(studentRequestDto.getUniformRequestDto().getPantSize());
        uniformMeasurement.setShirtSize(studentRequestDto.getUniformRequestDto().getShirtSize());
        uniformMeasurement.setShoeSize(studentRequestDto.getUniformRequestDto().getShoeSize());
        return uniformMeasurement;
    }
}
