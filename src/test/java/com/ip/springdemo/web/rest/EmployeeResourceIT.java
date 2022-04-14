package com.ip.springdemo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ip.springdemo.IntegrationTest;
import com.ip.springdemo.domain.Employee;
import com.ip.springdemo.repository.EmployeeRepository;
import com.ip.springdemo.service.criteria.EmployeeCriteria;
import com.ip.springdemo.service.dto.EmployeeDTO;
import com.ip.springdemo.service.mapper.EmployeeMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_EMP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMP_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_EMP_BIRTHDAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EMP_BIRTHDAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EMP_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMP_GENDER = false;
    private static final Boolean UPDATED_EMP_GENDER = true;

    private static final String DEFAULT_EMP_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMP_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_EMP_JOIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EMP_JOIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .empName(DEFAULT_EMP_NAME)
            .empBirthday(DEFAULT_EMP_BIRTHDAY)
            .empCode(DEFAULT_EMP_CODE)
            .empGender(DEFAULT_EMP_GENDER)
            .empEmail(DEFAULT_EMP_EMAIL)
            .empJoinDate(DEFAULT_EMP_JOIN_DATE);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .empName(UPDATED_EMP_NAME)
            .empBirthday(UPDATED_EMP_BIRTHDAY)
            .empCode(UPDATED_EMP_CODE)
            .empGender(UPDATED_EMP_GENDER)
            .empEmail(UPDATED_EMP_EMAIL)
            .empJoinDate(UPDATED_EMP_JOIN_DATE);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmpName()).isEqualTo(DEFAULT_EMP_NAME);
        assertThat(testEmployee.getEmpBirthday()).isEqualTo(DEFAULT_EMP_BIRTHDAY);
        assertThat(testEmployee.getEmpCode()).isEqualTo(DEFAULT_EMP_CODE);
        assertThat(testEmployee.getEmpGender()).isEqualTo(DEFAULT_EMP_GENDER);
        assertThat(testEmployee.getEmpEmail()).isEqualTo(DEFAULT_EMP_EMAIL);
        assertThat(testEmployee.getEmpJoinDate()).isEqualTo(DEFAULT_EMP_JOIN_DATE);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].empName").value(hasItem(DEFAULT_EMP_NAME)))
            .andExpect(jsonPath("$.[*].empBirthday").value(hasItem(DEFAULT_EMP_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].empCode").value(hasItem(DEFAULT_EMP_CODE)))
            .andExpect(jsonPath("$.[*].empGender").value(hasItem(DEFAULT_EMP_GENDER.booleanValue())))
            .andExpect(jsonPath("$.[*].empEmail").value(hasItem(DEFAULT_EMP_EMAIL)))
            .andExpect(jsonPath("$.[*].empJoinDate").value(hasItem(DEFAULT_EMP_JOIN_DATE.toString())));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.empName").value(DEFAULT_EMP_NAME))
            .andExpect(jsonPath("$.empBirthday").value(DEFAULT_EMP_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.empCode").value(DEFAULT_EMP_CODE))
            .andExpect(jsonPath("$.empGender").value(DEFAULT_EMP_GENDER.booleanValue()))
            .andExpect(jsonPath("$.empEmail").value(DEFAULT_EMP_EMAIL))
            .andExpect(jsonPath("$.empJoinDate").value(DEFAULT_EMP_JOIN_DATE.toString()));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empName equals to DEFAULT_EMP_NAME
        defaultEmployeeShouldBeFound("empName.equals=" + DEFAULT_EMP_NAME);

        // Get all the employeeList where empName equals to UPDATED_EMP_NAME
        defaultEmployeeShouldNotBeFound("empName.equals=" + UPDATED_EMP_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empName not equals to DEFAULT_EMP_NAME
        defaultEmployeeShouldNotBeFound("empName.notEquals=" + DEFAULT_EMP_NAME);

        // Get all the employeeList where empName not equals to UPDATED_EMP_NAME
        defaultEmployeeShouldBeFound("empName.notEquals=" + UPDATED_EMP_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empName in DEFAULT_EMP_NAME or UPDATED_EMP_NAME
        defaultEmployeeShouldBeFound("empName.in=" + DEFAULT_EMP_NAME + "," + UPDATED_EMP_NAME);

        // Get all the employeeList where empName equals to UPDATED_EMP_NAME
        defaultEmployeeShouldNotBeFound("empName.in=" + UPDATED_EMP_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empName is not null
        defaultEmployeeShouldBeFound("empName.specified=true");

        // Get all the employeeList where empName is null
        defaultEmployeeShouldNotBeFound("empName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empName contains DEFAULT_EMP_NAME
        defaultEmployeeShouldBeFound("empName.contains=" + DEFAULT_EMP_NAME);

        // Get all the employeeList where empName contains UPDATED_EMP_NAME
        defaultEmployeeShouldNotBeFound("empName.contains=" + UPDATED_EMP_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empName does not contain DEFAULT_EMP_NAME
        defaultEmployeeShouldNotBeFound("empName.doesNotContain=" + DEFAULT_EMP_NAME);

        // Get all the employeeList where empName does not contain UPDATED_EMP_NAME
        defaultEmployeeShouldBeFound("empName.doesNotContain=" + UPDATED_EMP_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empBirthday equals to DEFAULT_EMP_BIRTHDAY
        defaultEmployeeShouldBeFound("empBirthday.equals=" + DEFAULT_EMP_BIRTHDAY);

        // Get all the employeeList where empBirthday equals to UPDATED_EMP_BIRTHDAY
        defaultEmployeeShouldNotBeFound("empBirthday.equals=" + UPDATED_EMP_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empBirthday not equals to DEFAULT_EMP_BIRTHDAY
        defaultEmployeeShouldNotBeFound("empBirthday.notEquals=" + DEFAULT_EMP_BIRTHDAY);

        // Get all the employeeList where empBirthday not equals to UPDATED_EMP_BIRTHDAY
        defaultEmployeeShouldBeFound("empBirthday.notEquals=" + UPDATED_EMP_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empBirthday in DEFAULT_EMP_BIRTHDAY or UPDATED_EMP_BIRTHDAY
        defaultEmployeeShouldBeFound("empBirthday.in=" + DEFAULT_EMP_BIRTHDAY + "," + UPDATED_EMP_BIRTHDAY);

        // Get all the employeeList where empBirthday equals to UPDATED_EMP_BIRTHDAY
        defaultEmployeeShouldNotBeFound("empBirthday.in=" + UPDATED_EMP_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empBirthday is not null
        defaultEmployeeShouldBeFound("empBirthday.specified=true");

        // Get all the employeeList where empBirthday is null
        defaultEmployeeShouldNotBeFound("empBirthday.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empCode equals to DEFAULT_EMP_CODE
        defaultEmployeeShouldBeFound("empCode.equals=" + DEFAULT_EMP_CODE);

        // Get all the employeeList where empCode equals to UPDATED_EMP_CODE
        defaultEmployeeShouldNotBeFound("empCode.equals=" + UPDATED_EMP_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empCode not equals to DEFAULT_EMP_CODE
        defaultEmployeeShouldNotBeFound("empCode.notEquals=" + DEFAULT_EMP_CODE);

        // Get all the employeeList where empCode not equals to UPDATED_EMP_CODE
        defaultEmployeeShouldBeFound("empCode.notEquals=" + UPDATED_EMP_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpCodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empCode in DEFAULT_EMP_CODE or UPDATED_EMP_CODE
        defaultEmployeeShouldBeFound("empCode.in=" + DEFAULT_EMP_CODE + "," + UPDATED_EMP_CODE);

        // Get all the employeeList where empCode equals to UPDATED_EMP_CODE
        defaultEmployeeShouldNotBeFound("empCode.in=" + UPDATED_EMP_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empCode is not null
        defaultEmployeeShouldBeFound("empCode.specified=true");

        // Get all the employeeList where empCode is null
        defaultEmployeeShouldNotBeFound("empCode.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpCodeContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empCode contains DEFAULT_EMP_CODE
        defaultEmployeeShouldBeFound("empCode.contains=" + DEFAULT_EMP_CODE);

        // Get all the employeeList where empCode contains UPDATED_EMP_CODE
        defaultEmployeeShouldNotBeFound("empCode.contains=" + UPDATED_EMP_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpCodeNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empCode does not contain DEFAULT_EMP_CODE
        defaultEmployeeShouldNotBeFound("empCode.doesNotContain=" + DEFAULT_EMP_CODE);

        // Get all the employeeList where empCode does not contain UPDATED_EMP_CODE
        defaultEmployeeShouldBeFound("empCode.doesNotContain=" + UPDATED_EMP_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empGender equals to DEFAULT_EMP_GENDER
        defaultEmployeeShouldBeFound("empGender.equals=" + DEFAULT_EMP_GENDER);

        // Get all the employeeList where empGender equals to UPDATED_EMP_GENDER
        defaultEmployeeShouldNotBeFound("empGender.equals=" + UPDATED_EMP_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empGender not equals to DEFAULT_EMP_GENDER
        defaultEmployeeShouldNotBeFound("empGender.notEquals=" + DEFAULT_EMP_GENDER);

        // Get all the employeeList where empGender not equals to UPDATED_EMP_GENDER
        defaultEmployeeShouldBeFound("empGender.notEquals=" + UPDATED_EMP_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpGenderIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empGender in DEFAULT_EMP_GENDER or UPDATED_EMP_GENDER
        defaultEmployeeShouldBeFound("empGender.in=" + DEFAULT_EMP_GENDER + "," + UPDATED_EMP_GENDER);

        // Get all the employeeList where empGender equals to UPDATED_EMP_GENDER
        defaultEmployeeShouldNotBeFound("empGender.in=" + UPDATED_EMP_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empGender is not null
        defaultEmployeeShouldBeFound("empGender.specified=true");

        // Get all the employeeList where empGender is null
        defaultEmployeeShouldNotBeFound("empGender.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empEmail equals to DEFAULT_EMP_EMAIL
        defaultEmployeeShouldBeFound("empEmail.equals=" + DEFAULT_EMP_EMAIL);

        // Get all the employeeList where empEmail equals to UPDATED_EMP_EMAIL
        defaultEmployeeShouldNotBeFound("empEmail.equals=" + UPDATED_EMP_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empEmail not equals to DEFAULT_EMP_EMAIL
        defaultEmployeeShouldNotBeFound("empEmail.notEquals=" + DEFAULT_EMP_EMAIL);

        // Get all the employeeList where empEmail not equals to UPDATED_EMP_EMAIL
        defaultEmployeeShouldBeFound("empEmail.notEquals=" + UPDATED_EMP_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpEmailIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empEmail in DEFAULT_EMP_EMAIL or UPDATED_EMP_EMAIL
        defaultEmployeeShouldBeFound("empEmail.in=" + DEFAULT_EMP_EMAIL + "," + UPDATED_EMP_EMAIL);

        // Get all the employeeList where empEmail equals to UPDATED_EMP_EMAIL
        defaultEmployeeShouldNotBeFound("empEmail.in=" + UPDATED_EMP_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empEmail is not null
        defaultEmployeeShouldBeFound("empEmail.specified=true");

        // Get all the employeeList where empEmail is null
        defaultEmployeeShouldNotBeFound("empEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpEmailContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empEmail contains DEFAULT_EMP_EMAIL
        defaultEmployeeShouldBeFound("empEmail.contains=" + DEFAULT_EMP_EMAIL);

        // Get all the employeeList where empEmail contains UPDATED_EMP_EMAIL
        defaultEmployeeShouldNotBeFound("empEmail.contains=" + UPDATED_EMP_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpEmailNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empEmail does not contain DEFAULT_EMP_EMAIL
        defaultEmployeeShouldNotBeFound("empEmail.doesNotContain=" + DEFAULT_EMP_EMAIL);

        // Get all the employeeList where empEmail does not contain UPDATED_EMP_EMAIL
        defaultEmployeeShouldBeFound("empEmail.doesNotContain=" + UPDATED_EMP_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpJoinDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empJoinDate equals to DEFAULT_EMP_JOIN_DATE
        defaultEmployeeShouldBeFound("empJoinDate.equals=" + DEFAULT_EMP_JOIN_DATE);

        // Get all the employeeList where empJoinDate equals to UPDATED_EMP_JOIN_DATE
        defaultEmployeeShouldNotBeFound("empJoinDate.equals=" + UPDATED_EMP_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpJoinDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empJoinDate not equals to DEFAULT_EMP_JOIN_DATE
        defaultEmployeeShouldNotBeFound("empJoinDate.notEquals=" + DEFAULT_EMP_JOIN_DATE);

        // Get all the employeeList where empJoinDate not equals to UPDATED_EMP_JOIN_DATE
        defaultEmployeeShouldBeFound("empJoinDate.notEquals=" + UPDATED_EMP_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpJoinDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empJoinDate in DEFAULT_EMP_JOIN_DATE or UPDATED_EMP_JOIN_DATE
        defaultEmployeeShouldBeFound("empJoinDate.in=" + DEFAULT_EMP_JOIN_DATE + "," + UPDATED_EMP_JOIN_DATE);

        // Get all the employeeList where empJoinDate equals to UPDATED_EMP_JOIN_DATE
        defaultEmployeeShouldNotBeFound("empJoinDate.in=" + UPDATED_EMP_JOIN_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmpJoinDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where empJoinDate is not null
        defaultEmployeeShouldBeFound("empJoinDate.specified=true");

        // Get all the employeeList where empJoinDate is null
        defaultEmployeeShouldNotBeFound("empJoinDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].empName").value(hasItem(DEFAULT_EMP_NAME)))
            .andExpect(jsonPath("$.[*].empBirthday").value(hasItem(DEFAULT_EMP_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].empCode").value(hasItem(DEFAULT_EMP_CODE)))
            .andExpect(jsonPath("$.[*].empGender").value(hasItem(DEFAULT_EMP_GENDER.booleanValue())))
            .andExpect(jsonPath("$.[*].empEmail").value(hasItem(DEFAULT_EMP_EMAIL)))
            .andExpect(jsonPath("$.[*].empJoinDate").value(hasItem(DEFAULT_EMP_JOIN_DATE.toString())));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .empName(UPDATED_EMP_NAME)
            .empBirthday(UPDATED_EMP_BIRTHDAY)
            .empCode(UPDATED_EMP_CODE)
            .empGender(UPDATED_EMP_GENDER)
            .empEmail(UPDATED_EMP_EMAIL)
            .empJoinDate(UPDATED_EMP_JOIN_DATE);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmpName()).isEqualTo(UPDATED_EMP_NAME);
        assertThat(testEmployee.getEmpBirthday()).isEqualTo(UPDATED_EMP_BIRTHDAY);
        assertThat(testEmployee.getEmpCode()).isEqualTo(UPDATED_EMP_CODE);
        assertThat(testEmployee.getEmpGender()).isEqualTo(UPDATED_EMP_GENDER);
        assertThat(testEmployee.getEmpEmail()).isEqualTo(UPDATED_EMP_EMAIL);
        assertThat(testEmployee.getEmpJoinDate()).isEqualTo(UPDATED_EMP_JOIN_DATE);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee.empBirthday(UPDATED_EMP_BIRTHDAY).empCode(UPDATED_EMP_CODE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmpName()).isEqualTo(DEFAULT_EMP_NAME);
        assertThat(testEmployee.getEmpBirthday()).isEqualTo(UPDATED_EMP_BIRTHDAY);
        assertThat(testEmployee.getEmpCode()).isEqualTo(UPDATED_EMP_CODE);
        assertThat(testEmployee.getEmpGender()).isEqualTo(DEFAULT_EMP_GENDER);
        assertThat(testEmployee.getEmpEmail()).isEqualTo(DEFAULT_EMP_EMAIL);
        assertThat(testEmployee.getEmpJoinDate()).isEqualTo(DEFAULT_EMP_JOIN_DATE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .empName(UPDATED_EMP_NAME)
            .empBirthday(UPDATED_EMP_BIRTHDAY)
            .empCode(UPDATED_EMP_CODE)
            .empGender(UPDATED_EMP_GENDER)
            .empEmail(UPDATED_EMP_EMAIL)
            .empJoinDate(UPDATED_EMP_JOIN_DATE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmpName()).isEqualTo(UPDATED_EMP_NAME);
        assertThat(testEmployee.getEmpBirthday()).isEqualTo(UPDATED_EMP_BIRTHDAY);
        assertThat(testEmployee.getEmpCode()).isEqualTo(UPDATED_EMP_CODE);
        assertThat(testEmployee.getEmpGender()).isEqualTo(UPDATED_EMP_GENDER);
        assertThat(testEmployee.getEmpEmail()).isEqualTo(UPDATED_EMP_EMAIL);
        assertThat(testEmployee.getEmpJoinDate()).isEqualTo(UPDATED_EMP_JOIN_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
