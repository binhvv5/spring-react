package com.ip.springdemo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ip.springdemo.domain.Employee} entity. This class is used
 * in {@link com.ip.springdemo.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter empName;

    private InstantFilter empBirthday;

    private StringFilter empCode;

    private BooleanFilter empGender;

    private StringFilter empEmail;

    private InstantFilter empJoinDate;

    private Boolean distinct;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.empName = other.empName == null ? null : other.empName.copy();
        this.empBirthday = other.empBirthday == null ? null : other.empBirthday.copy();
        this.empCode = other.empCode == null ? null : other.empCode.copy();
        this.empGender = other.empGender == null ? null : other.empGender.copy();
        this.empEmail = other.empEmail == null ? null : other.empEmail.copy();
        this.empJoinDate = other.empJoinDate == null ? null : other.empJoinDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmpName() {
        return empName;
    }

    public StringFilter empName() {
        if (empName == null) {
            empName = new StringFilter();
        }
        return empName;
    }

    public void setEmpName(StringFilter empName) {
        this.empName = empName;
    }

    public InstantFilter getEmpBirthday() {
        return empBirthday;
    }

    public InstantFilter empBirthday() {
        if (empBirthday == null) {
            empBirthday = new InstantFilter();
        }
        return empBirthday;
    }

    public void setEmpBirthday(InstantFilter empBirthday) {
        this.empBirthday = empBirthday;
    }

    public StringFilter getEmpCode() {
        return empCode;
    }

    public StringFilter empCode() {
        if (empCode == null) {
            empCode = new StringFilter();
        }
        return empCode;
    }

    public void setEmpCode(StringFilter empCode) {
        this.empCode = empCode;
    }

    public BooleanFilter getEmpGender() {
        return empGender;
    }

    public BooleanFilter empGender() {
        if (empGender == null) {
            empGender = new BooleanFilter();
        }
        return empGender;
    }

    public void setEmpGender(BooleanFilter empGender) {
        this.empGender = empGender;
    }

    public StringFilter getEmpEmail() {
        return empEmail;
    }

    public StringFilter empEmail() {
        if (empEmail == null) {
            empEmail = new StringFilter();
        }
        return empEmail;
    }

    public void setEmpEmail(StringFilter empEmail) {
        this.empEmail = empEmail;
    }

    public InstantFilter getEmpJoinDate() {
        return empJoinDate;
    }

    public InstantFilter empJoinDate() {
        if (empJoinDate == null) {
            empJoinDate = new InstantFilter();
        }
        return empJoinDate;
    }

    public void setEmpJoinDate(InstantFilter empJoinDate) {
        this.empJoinDate = empJoinDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(empName, that.empName) &&
            Objects.equals(empBirthday, that.empBirthday) &&
            Objects.equals(empCode, that.empCode) &&
            Objects.equals(empGender, that.empGender) &&
            Objects.equals(empEmail, that.empEmail) &&
            Objects.equals(empJoinDate, that.empJoinDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, empName, empBirthday, empCode, empGender, empEmail, empJoinDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (empName != null ? "empName=" + empName + ", " : "") +
            (empBirthday != null ? "empBirthday=" + empBirthday + ", " : "") +
            (empCode != null ? "empCode=" + empCode + ", " : "") +
            (empGender != null ? "empGender=" + empGender + ", " : "") +
            (empEmail != null ? "empEmail=" + empEmail + ", " : "") +
            (empJoinDate != null ? "empJoinDate=" + empJoinDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
