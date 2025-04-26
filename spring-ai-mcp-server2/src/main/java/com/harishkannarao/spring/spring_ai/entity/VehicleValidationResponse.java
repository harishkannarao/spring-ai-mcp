package com.harishkannarao.spring.spring_ai.entity;

import com.harishkannarao.spring.spring_ai.entity.VehicleValidationError;

import java.util.List;

public record VehicleValidationResponse(
	List<VehicleValidationError> errors
) {
}
