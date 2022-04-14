package com.ip.springdemo.service;

import com.ip.springdemo.domain.*; // for static metamodels
import com.ip.springdemo.domain.Employee;
import com.ip.springdemo.repository.EmployeeRepository;
import com.ip.springdemo.service.criteria.EmployeeCriteria;
import com.ip.springdemo.service.dto.EmployeeDTO;
import com.ip.springdemo.service.mapper.EmployeeMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Employee} entities in the database.
 * The main input is a {@link EmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeDTO} or a {@link Page} of {@link EmployeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeQueryService extends QueryService<Employee> {

    private final Logger log = LoggerFactory.getLogger(EmployeeQueryService.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeQueryService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Return a {@link List} of {@link EmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findByCriteria(EmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeMapper.toDto(employeeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.findAll(specification, page).map(employeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Employee> createSpecification(EmployeeCriteria criteria) {
        Specification<Employee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            //            if (criteria.getDistinct() != null) {
            //                specification = specification.and(distinct(criteria.getDistinct()));
            //            }
            //            if (criteria.getId() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getId(), Employee_.id));
            //            }
            //            if (criteria.getEmpName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getEmpName(), Employee_.empName));
            //            }
            //            if (criteria.getEmpBirthday() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getEmpBirthday(), Employee_.empBirthday));
            //            }
            //            if (criteria.getEmpCode() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getEmpCode(), Employee_.empCode));
            //            }
            //            if (criteria.getEmpGender() != null) {
            //                specification = specification.and(buildSpecification(criteria.getEmpGender(), Employee_.empGender));
            //            }
            //            if (criteria.getEmpEmail() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getEmpEmail(), Employee_.empEmail));
            //            }
            //            if (criteria.getEmpJoinDate() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getEmpJoinDate(), Employee_.empJoinDate));
            //            }
        }
        return specification;
    }
}
