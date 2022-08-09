/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.records;


import java.math.BigDecimal;
import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.codegen.maven.example.tables.AdvancesSalary;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AdvancesSalaryRecord extends UpdatableRecordImpl<AdvancesSalaryRecord> implements Record5<Long, LocalDate, String, BigDecimal, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>human_resource_management.advances_salary.advances_id</code>.
     */
    public AdvancesSalaryRecord setAdvancesId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.advances_salary.advances_id</code>.
     */
    public Long getAdvancesId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>human_resource_management.advances_salary.date</code>.
     */
    public AdvancesSalaryRecord setDate(LocalDate value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.advances_salary.date</code>.
     */
    public LocalDate getDate() {
        return (LocalDate) get(1);
    }

    /**
     * Setter for
     * <code>human_resource_management.advances_salary.description</code>.
     */
    public AdvancesSalaryRecord setDescription(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.advances_salary.description</code>.
     */
    public String getDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>human_resource_management.advances_salary.value</code>.
     */
    public AdvancesSalaryRecord setValue(BigDecimal value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.advances_salary.value</code>.
     */
    public BigDecimal getValue() {
        return (BigDecimal) get(3);
    }

    /**
     * Setter for
     * <code>human_resource_management.advances_salary.salary_id</code>.
     */
    public AdvancesSalaryRecord setSalaryId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.advances_salary.salary_id</code>.
     */
    public Long getSalaryId() {
        return (Long) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, LocalDate, String, BigDecimal, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, LocalDate, String, BigDecimal, Long> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return AdvancesSalary.ADVANCES_SALARY.ADVANCES_ID;
    }

    @Override
    public Field<LocalDate> field2() {
        return AdvancesSalary.ADVANCES_SALARY.DATE;
    }

    @Override
    public Field<String> field3() {
        return AdvancesSalary.ADVANCES_SALARY.DESCRIPTION;
    }

    @Override
    public Field<BigDecimal> field4() {
        return AdvancesSalary.ADVANCES_SALARY.VALUE;
    }

    @Override
    public Field<Long> field5() {
        return AdvancesSalary.ADVANCES_SALARY.SALARY_ID;
    }

    @Override
    public Long component1() {
        return getAdvancesId();
    }

    @Override
    public LocalDate component2() {
        return getDate();
    }

    @Override
    public String component3() {
        return getDescription();
    }

    @Override
    public BigDecimal component4() {
        return getValue();
    }

    @Override
    public Long component5() {
        return getSalaryId();
    }

    @Override
    public Long value1() {
        return getAdvancesId();
    }

    @Override
    public LocalDate value2() {
        return getDate();
    }

    @Override
    public String value3() {
        return getDescription();
    }

    @Override
    public BigDecimal value4() {
        return getValue();
    }

    @Override
    public Long value5() {
        return getSalaryId();
    }

    @Override
    public AdvancesSalaryRecord value1(Long value) {
        setAdvancesId(value);
        return this;
    }

    @Override
    public AdvancesSalaryRecord value2(LocalDate value) {
        setDate(value);
        return this;
    }

    @Override
    public AdvancesSalaryRecord value3(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public AdvancesSalaryRecord value4(BigDecimal value) {
        setValue(value);
        return this;
    }

    @Override
    public AdvancesSalaryRecord value5(Long value) {
        setSalaryId(value);
        return this;
    }

    @Override
    public AdvancesSalaryRecord values(Long value1, LocalDate value2, String value3, BigDecimal value4, Long value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AdvancesSalaryRecord
     */
    public AdvancesSalaryRecord() {
        super(AdvancesSalary.ADVANCES_SALARY);
    }

    /**
     * Create a detached, initialised AdvancesSalaryRecord
     */
    public AdvancesSalaryRecord(Long advancesId, LocalDate date, String description, BigDecimal value, Long salaryId) {
        super(AdvancesSalary.ADVANCES_SALARY);

        setAdvancesId(advancesId);
        setDate(date);
        setDescription(description);
        setValue(value);
        setSalaryId(salaryId);
    }

    /**
     * Create a detached, initialised AdvancesSalaryRecord
     */
    public AdvancesSalaryRecord(org.jooq.codegen.maven.example.tables.pojos.AdvancesSalary value) {
        super(AdvancesSalary.ADVANCES_SALARY);

        if (value != null) {
            setAdvancesId(value.getAdvancesId());
            setDate(value.getDate());
            setDescription(value.getDescription());
            setValue(value.getValue());
            setSalaryId(value.getSalaryId());
        }
    }
}
