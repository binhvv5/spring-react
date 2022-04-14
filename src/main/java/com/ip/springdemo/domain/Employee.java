package com.ip.springdemo.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "emp_name")
    private String empName;

    @Column(name = "emp_birthday")
    private Instant empBirthday;

    @Column(name = "emp_code")
    private String empCode;

    @Column(name = "emp_gender")
    private Boolean empGender;

    @Column(name = "emp_email")
    private String empEmail;

    @Column(name = "emp_join_date")
    private Instant empJoinDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpName() {
        return this.empName;
    }

    public Employee empName(String empName) {
        this.setEmpName(empName);
        return this;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Instant getEmpBirthday() {
        return this.empBirthday;
    }

    public Employee empBirthday(Instant empBirthday) {
        this.setEmpBirthday(empBirthday);
        return this;
    }

    public void setEmpBirthday(Instant empBirthday) {
        this.empBirthday = empBirthday;
    }

    public String getEmpCode() {
        return this.empCode;
    }

    public Employee empCode(String empCode) {
        this.setEmpCode(empCode);
        return this;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public Boolean getEmpGender() {
        return this.empGender;
    }

    public Employee empGender(Boolean empGender) {
        this.setEmpGender(empGender);
        return this;
    }

    public void setEmpGender(Boolean empGender) {
        this.empGender = empGender;
    }

    public String getEmpEmail() {
        return this.empEmail;
    }

    public Employee empEmail(String empEmail) {
        this.setEmpEmail(empEmail);
        return this;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public Instant getEmpJoinDate() {
        return this.empJoinDate;
    }

    public Employee empJoinDate(Instant empJoinDate) {
        this.setEmpJoinDate(empJoinDate);
        return this;
    }

    public void setEmpJoinDate(Instant empJoinDate) {
        this.empJoinDate = empJoinDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
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
