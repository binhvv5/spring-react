package com.ip.springdemo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ip.springdemo.domain.Employee} entity.
 */
public class EmployeeDTO implements Serializable {

    private Long id;

    private String empName;

    private Instant empBirthday;

    private String empCode;

    private Boolean empGender;

    private String empEmail;

    private Instant empJoinDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Instant getEmpBirthday() {
        return empBirthday;
    }

    public void setEmpBirthday(Instant empBirthday) {
        this.empBirthday = empBirthday;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public Boolean getEmpGender() {
        return empGender;
    }

    public void setEmpGender(Boolean empGender) {
        this.empGender = empGender;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public Instant getEmpJoinDate() {
        return empJoinDate;
    }

    public void setEmpJoinDate(Instant empJoinDate) {
        this.empJoinDate = empJoinDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", empName='" + getEmpName() + "'" +
            ", empBirthday='" + getEmpBirthday() + "'" +
            ", empCode='" + getEmpCode() + "'" +
            ", empGender='" + getEmpGender() + "'" +
            ", empEmail='" + getEmpEmail() + "'" +
            ", empJoinDate='" + getEmpJoinDate() + "'" +
            "}";
    }
}
