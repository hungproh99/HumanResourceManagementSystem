/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.records;


import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.codegen.maven.example.tables.WorkingPlace;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WorkingPlaceRecord extends UpdatableRecordImpl<WorkingPlaceRecord> implements Record8<Long, LocalDate, Boolean, Long, Long, Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>human_resource_management.working_place.working_place_id</code>.
     */
    public WorkingPlaceRecord setWorkingPlaceId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.working_place_id</code>.
     */
    public Long getWorkingPlaceId() {
        return (Long) get(0);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.start_date</code>.
     */
    public WorkingPlaceRecord setStartDate(LocalDate value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.start_date</code>.
     */
    public LocalDate getStartDate() {
        return (LocalDate) get(1);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.working_place_status</code>.
     */
    public WorkingPlaceRecord setWorkingPlaceStatus(Boolean value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.working_place_status</code>.
     */
    public Boolean getWorkingPlaceStatus() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>human_resource_management.working_place.area_id</code>.
     */
    public WorkingPlaceRecord setAreaId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.working_place.area_id</code>.
     */
    public Long getAreaId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>human_resource_management.working_place.grade_id</code>.
     */
    public WorkingPlaceRecord setGradeId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.working_place.grade_id</code>.
     */
    public Long getGradeId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>human_resource_management.working_place.job_id</code>.
     */
    public WorkingPlaceRecord setJobId(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.working_place.job_id</code>.
     */
    public Long getJobId() {
        return (Long) get(5);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.office_id</code>.
     */
    public WorkingPlaceRecord setOfficeId(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.office_id</code>.
     */
    public Long getOfficeId() {
        return (Long) get(6);
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.working_contract_id</code>.
     */
    public WorkingPlaceRecord setWorkingContractId(Long value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.working_contract_id</code>.
     */
    public Long getWorkingContractId() {
        return (Long) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, LocalDate, Boolean, Long, Long, Long, Long, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Long, LocalDate, Boolean, Long, Long, Long, Long, Long> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return WorkingPlace.WORKING_PLACE.WORKING_PLACE_ID;
    }

    @Override
    public Field<LocalDate> field2() {
        return WorkingPlace.WORKING_PLACE.START_DATE;
    }

    @Override
    public Field<Boolean> field3() {
        return WorkingPlace.WORKING_PLACE.WORKING_PLACE_STATUS;
    }

    @Override
    public Field<Long> field4() {
        return WorkingPlace.WORKING_PLACE.AREA_ID;
    }

    @Override
    public Field<Long> field5() {
        return WorkingPlace.WORKING_PLACE.GRADE_ID;
    }

    @Override
    public Field<Long> field6() {
        return WorkingPlace.WORKING_PLACE.JOB_ID;
    }

    @Override
    public Field<Long> field7() {
        return WorkingPlace.WORKING_PLACE.OFFICE_ID;
    }

    @Override
    public Field<Long> field8() {
        return WorkingPlace.WORKING_PLACE.WORKING_CONTRACT_ID;
    }

    @Override
    public Long component1() {
        return getWorkingPlaceId();
    }

    @Override
    public LocalDate component2() {
        return getStartDate();
    }

    @Override
    public Boolean component3() {
        return getWorkingPlaceStatus();
    }

    @Override
    public Long component4() {
        return getAreaId();
    }

    @Override
    public Long component5() {
        return getGradeId();
    }

    @Override
    public Long component6() {
        return getJobId();
    }

    @Override
    public Long component7() {
        return getOfficeId();
    }

    @Override
    public Long component8() {
        return getWorkingContractId();
    }

    @Override
    public Long value1() {
        return getWorkingPlaceId();
    }

    @Override
    public LocalDate value2() {
        return getStartDate();
    }

    @Override
    public Boolean value3() {
        return getWorkingPlaceStatus();
    }

    @Override
    public Long value4() {
        return getAreaId();
    }

    @Override
    public Long value5() {
        return getGradeId();
    }

    @Override
    public Long value6() {
        return getJobId();
    }

    @Override
    public Long value7() {
        return getOfficeId();
    }

    @Override
    public Long value8() {
        return getWorkingContractId();
    }

    @Override
    public WorkingPlaceRecord value1(Long value) {
        setWorkingPlaceId(value);
        return this;
    }

    @Override
    public WorkingPlaceRecord value2(LocalDate value) {
        setStartDate(value);
        return this;
    }

    @Override
    public WorkingPlaceRecord value3(Boolean value) {
        setWorkingPlaceStatus(value);
        return this;
    }

    @Override
    public WorkingPlaceRecord value4(Long value) {
        setAreaId(value);
        return this;
    }

    @Override
    public WorkingPlaceRecord value5(Long value) {
        setGradeId(value);
        return this;
    }

    @Override
    public WorkingPlaceRecord value6(Long value) {
        setJobId(value);
        return this;
    }

    @Override
    public WorkingPlaceRecord value7(Long value) {
        setOfficeId(value);
        return this;
    }

    @Override
    public WorkingPlaceRecord value8(Long value) {
        setWorkingContractId(value);
        return this;
    }

    @Override
    public WorkingPlaceRecord values(Long value1, LocalDate value2, Boolean value3, Long value4, Long value5, Long value6, Long value7, Long value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WorkingPlaceRecord
     */
    public WorkingPlaceRecord() {
        super(WorkingPlace.WORKING_PLACE);
    }

    /**
     * Create a detached, initialised WorkingPlaceRecord
     */
    public WorkingPlaceRecord(Long workingPlaceId, LocalDate startDate, Boolean workingPlaceStatus, Long areaId, Long gradeId, Long jobId, Long officeId, Long workingContractId) {
        super(WorkingPlace.WORKING_PLACE);

        setWorkingPlaceId(workingPlaceId);
        setStartDate(startDate);
        setWorkingPlaceStatus(workingPlaceStatus);
        setAreaId(areaId);
        setGradeId(gradeId);
        setJobId(jobId);
        setOfficeId(officeId);
        setWorkingContractId(workingContractId);
    }

    /**
     * Create a detached, initialised WorkingPlaceRecord
     */
    public WorkingPlaceRecord(org.jooq.codegen.maven.example.tables.pojos.WorkingPlace value) {
        super(WorkingPlace.WORKING_PLACE);

        if (value != null) {
            setWorkingPlaceId(value.getWorkingPlaceId());
            setStartDate(value.getStartDate());
            setWorkingPlaceStatus(value.getWorkingPlaceStatus());
            setAreaId(value.getAreaId());
            setGradeId(value.getGradeId());
            setJobId(value.getJobId());
            setOfficeId(value.getOfficeId());
            setWorkingContractId(value.getWorkingContractId());
        }
    }
}
