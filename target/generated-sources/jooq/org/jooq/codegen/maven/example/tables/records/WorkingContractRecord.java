/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.records;


import java.math.BigDecimal;
import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.codegen.maven.example.tables.WorkingContract;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WorkingContractRecord extends UpdatableRecordImpl<WorkingContractRecord> implements Record9<Long, BigDecimal, String, String, LocalDate, LocalDate, String, Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>human_resource_management.working_contract.working_contract_id</code>.
     */
    public WorkingContractRecord setWorkingContractId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.working_contract_id</code>.
     */
    public Long getWorkingContractId() {
        return (Long) get(0);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_contract.base_salary</code>.
     */
    public WorkingContractRecord setBaseSalary(BigDecimal value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.base_salary</code>.
     */
    public BigDecimal getBaseSalary() {
        return (BigDecimal) get(1);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_contract.company_name</code>.
     */
    public WorkingContractRecord setCompanyName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.company_name</code>.
     */
    public String getCompanyName() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_contract.contract_url</code>.
     */
    public WorkingContractRecord setContractUrl(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.contract_url</code>.
     */
    public String getContractUrl() {
        return (String) get(3);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_contract.end_date</code>.
     */
    public WorkingContractRecord setEndDate(LocalDate value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.end_date</code>.
     */
    public LocalDate getEndDate() {
        return (LocalDate) get(4);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_contract.start_date</code>.
     */
    public WorkingContractRecord setStartDate(LocalDate value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.start_date</code>.
     */
    public LocalDate getStartDate() {
        return (LocalDate) get(5);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_contract.status</code>.
     */
    public WorkingContractRecord setStatus(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.status</code>.
     */
    public String getStatus() {
        return (String) get(6);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_contract.type_id</code>.
     */
    public WorkingContractRecord setTypeId(Long value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.type_id</code>.
     */
    public Long getTypeId() {
        return (Long) get(7);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_contract.employee_id</code>.
     */
    public WorkingContractRecord setEmployeeId(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_contract.employee_id</code>.
     */
    public String getEmployeeId() {
        return (String) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<Long, BigDecimal, String, String, LocalDate, LocalDate, String, Long, String> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<Long, BigDecimal, String, String, LocalDate, LocalDate, String, Long, String> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return WorkingContract.WORKING_CONTRACT.WORKING_CONTRACT_ID;
    }

    @Override
    public Field<BigDecimal> field2() {
        return WorkingContract.WORKING_CONTRACT.BASE_SALARY;
    }

    @Override
    public Field<String> field3() {
        return WorkingContract.WORKING_CONTRACT.COMPANY_NAME;
    }

    @Override
    public Field<String> field4() {
        return WorkingContract.WORKING_CONTRACT.CONTRACT_URL;
    }

    @Override
    public Field<LocalDate> field5() {
        return WorkingContract.WORKING_CONTRACT.END_DATE;
    }

    @Override
    public Field<LocalDate> field6() {
        return WorkingContract.WORKING_CONTRACT.START_DATE;
    }

    @Override
    public Field<String> field7() {
        return WorkingContract.WORKING_CONTRACT.STATUS;
    }

    @Override
    public Field<Long> field8() {
        return WorkingContract.WORKING_CONTRACT.TYPE_ID;
    }

    @Override
    public Field<String> field9() {
        return WorkingContract.WORKING_CONTRACT.EMPLOYEE_ID;
    }

    @Override
    public Long component1() {
        return getWorkingContractId();
    }

    @Override
    public BigDecimal component2() {
        return getBaseSalary();
    }

    @Override
    public String component3() {
        return getCompanyName();
    }

    @Override
    public String component4() {
        return getContractUrl();
    }

    @Override
    public LocalDate component5() {
        return getEndDate();
    }

    @Override
    public LocalDate component6() {
        return getStartDate();
    }

    @Override
    public String component7() {
        return getStatus();
    }

    @Override
    public Long component8() {
        return getTypeId();
    }

    @Override
    public String component9() {
        return getEmployeeId();
    }

    @Override
    public Long value1() {
        return getWorkingContractId();
    }

    @Override
    public BigDecimal value2() {
        return getBaseSalary();
    }

    @Override
    public String value3() {
        return getCompanyName();
    }

    @Override
    public String value4() {
        return getContractUrl();
    }

    @Override
    public LocalDate value5() {
        return getEndDate();
    }

    @Override
    public LocalDate value6() {
        return getStartDate();
    }

    @Override
    public String value7() {
        return getStatus();
    }

    @Override
    public Long value8() {
        return getTypeId();
    }

    @Override
    public String value9() {
        return getEmployeeId();
    }

    @Override
    public WorkingContractRecord value1(Long value) {
        setWorkingContractId(value);
        return this;
    }

    @Override
    public WorkingContractRecord value2(BigDecimal value) {
        setBaseSalary(value);
        return this;
    }

    @Override
    public WorkingContractRecord value3(String value) {
        setCompanyName(value);
        return this;
    }

    @Override
    public WorkingContractRecord value4(String value) {
        setContractUrl(value);
        return this;
    }

    @Override
    public WorkingContractRecord value5(LocalDate value) {
        setEndDate(value);
        return this;
    }

    @Override
    public WorkingContractRecord value6(LocalDate value) {
        setStartDate(value);
        return this;
    }

    @Override
    public WorkingContractRecord value7(String value) {
        setStatus(value);
        return this;
    }

    @Override
    public WorkingContractRecord value8(Long value) {
        setTypeId(value);
        return this;
    }

    @Override
    public WorkingContractRecord value9(String value) {
        setEmployeeId(value);
        return this;
    }

    @Override
    public WorkingContractRecord values(Long value1, BigDecimal value2, String value3, String value4, LocalDate value5, LocalDate value6, String value7, Long value8, String value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WorkingContractRecord
     */
    public WorkingContractRecord() {
        super(WorkingContract.WORKING_CONTRACT);
    }

    /**
     * Create a detached, initialised WorkingContractRecord
     */
    public WorkingContractRecord(Long workingContractId, BigDecimal baseSalary, String companyName, String contractUrl, LocalDate endDate, LocalDate startDate, String status, Long typeId, String employeeId) {
        super(WorkingContract.WORKING_CONTRACT);

        setWorkingContractId(workingContractId);
        setBaseSalary(baseSalary);
        setCompanyName(companyName);
        setContractUrl(contractUrl);
        setEndDate(endDate);
        setStartDate(startDate);
        setStatus(status);
        setTypeId(typeId);
        setEmployeeId(employeeId);
    }

    /**
     * Create a detached, initialised WorkingContractRecord
     */
    public WorkingContractRecord(org.jooq.codegen.maven.example.tables.pojos.WorkingContract value) {
        super(WorkingContract.WORKING_CONTRACT);

        if (value != null) {
            setWorkingContractId(value.getWorkingContractId());
            setBaseSalary(value.getBaseSalary());
            setCompanyName(value.getCompanyName());
            setContractUrl(value.getContractUrl());
            setEndDate(value.getEndDate());
            setStartDate(value.getStartDate());
            setStatus(value.getStatus());
            setTypeId(value.getTypeId());
            setEmployeeId(value.getEmployeeId());
        }
    }
}
