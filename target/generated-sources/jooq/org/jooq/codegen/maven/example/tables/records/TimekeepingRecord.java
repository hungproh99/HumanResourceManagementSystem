/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.records;


import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.codegen.maven.example.tables.Timekeeping;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TimekeepingRecord extends UpdatableRecordImpl<TimekeepingRecord> implements Record5<Long, LocalDate, Double, Double, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>human_resource_management.timekeeping.timekeeping_id</code>.
     */
    public TimekeepingRecord setTimekeepingId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.timekeeping.timekeeping_id</code>.
     */
    public Long getTimekeepingId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>human_resource_management.timekeeping.date</code>.
     */
    public TimekeepingRecord setDate(LocalDate value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.timekeeping.date</code>.
     */
    public LocalDate getDate() {
        return (LocalDate) get(1);
    }

    /**
     * Setter for
     * <code>human_resource_management.timekeeping.point_ot_day</code>.
     */
    public TimekeepingRecord setPointOtDay(Double value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.timekeeping.point_ot_day</code>.
     */
    public Double getPointOtDay() {
        return (Double) get(2);
    }

    /**
     * Setter for
     * <code>human_resource_management.timekeeping.point_work_day</code>.
     */
    public TimekeepingRecord setPointWorkDay(Double value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.timekeeping.point_work_day</code>.
     */
    public Double getPointWorkDay() {
        return (Double) get(3);
    }

    /**
     * Setter for
     * <code>human_resource_management.timekeeping.employee_id</code>.
     */
    public TimekeepingRecord setEmployeeId(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.timekeeping.employee_id</code>.
     */
    public String getEmployeeId() {
        return (String) get(4);
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
    public Row5<Long, LocalDate, Double, Double, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, LocalDate, Double, Double, String> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Timekeeping.TIMEKEEPING.TIMEKEEPING_ID;
    }

    @Override
    public Field<LocalDate> field2() {
        return Timekeeping.TIMEKEEPING.DATE;
    }

    @Override
    public Field<Double> field3() {
        return Timekeeping.TIMEKEEPING.POINT_OT_DAY;
    }

    @Override
    public Field<Double> field4() {
        return Timekeeping.TIMEKEEPING.POINT_WORK_DAY;
    }

    @Override
    public Field<String> field5() {
        return Timekeeping.TIMEKEEPING.EMPLOYEE_ID;
    }

    @Override
    public Long component1() {
        return getTimekeepingId();
    }

    @Override
    public LocalDate component2() {
        return getDate();
    }

    @Override
    public Double component3() {
        return getPointOtDay();
    }

    @Override
    public Double component4() {
        return getPointWorkDay();
    }

    @Override
    public String component5() {
        return getEmployeeId();
    }

    @Override
    public Long value1() {
        return getTimekeepingId();
    }

    @Override
    public LocalDate value2() {
        return getDate();
    }

    @Override
    public Double value3() {
        return getPointOtDay();
    }

    @Override
    public Double value4() {
        return getPointWorkDay();
    }

    @Override
    public String value5() {
        return getEmployeeId();
    }

    @Override
    public TimekeepingRecord value1(Long value) {
        setTimekeepingId(value);
        return this;
    }

    @Override
    public TimekeepingRecord value2(LocalDate value) {
        setDate(value);
        return this;
    }

    @Override
    public TimekeepingRecord value3(Double value) {
        setPointOtDay(value);
        return this;
    }

    @Override
    public TimekeepingRecord value4(Double value) {
        setPointWorkDay(value);
        return this;
    }

    @Override
    public TimekeepingRecord value5(String value) {
        setEmployeeId(value);
        return this;
    }

    @Override
    public TimekeepingRecord values(Long value1, LocalDate value2, Double value3, Double value4, String value5) {
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
     * Create a detached TimekeepingRecord
     */
    public TimekeepingRecord() {
        super(Timekeeping.TIMEKEEPING);
    }

    /**
     * Create a detached, initialised TimekeepingRecord
     */
    public TimekeepingRecord(Long timekeepingId, LocalDate date, Double pointOtDay, Double pointWorkDay, String employeeId) {
        super(Timekeeping.TIMEKEEPING);

        setTimekeepingId(timekeepingId);
        setDate(date);
        setPointOtDay(pointOtDay);
        setPointWorkDay(pointWorkDay);
        setEmployeeId(employeeId);
    }

    /**
     * Create a detached, initialised TimekeepingRecord
     */
    public TimekeepingRecord(org.jooq.codegen.maven.example.tables.pojos.Timekeeping value) {
        super(Timekeeping.TIMEKEEPING);

        if (value != null) {
            setTimekeepingId(value.getTimekeepingId());
            setDate(value.getDate());
            setPointOtDay(value.getPointOtDay());
            setPointWorkDay(value.getPointWorkDay());
            setEmployeeId(value.getEmployeeId());
        }
    }
}
