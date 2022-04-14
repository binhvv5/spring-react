package com.ip.springdemo.service.mapper;

import com.ip.springdemo.domain.Employee;
import com.ip.springdemo.service.dto.EmployeeDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
@Component
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {}
