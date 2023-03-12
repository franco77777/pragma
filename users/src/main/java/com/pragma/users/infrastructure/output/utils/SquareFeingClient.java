package com.pragma.users.infrastructure.output.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

//, url = "http://localhost:8081"
@FeignClient(name = "square-service")
public interface SquareFeingClient {

    @PostMapping("/square/users/{OwnerId}/{restaurantId}/{employeeId}")
    Boolean createEmployee(@PathVariable("OwnerId") Long OwnerId, @PathVariable("restaurantId") Long restaurantId, @PathVariable("employeeId") Long employeeId);

    }

