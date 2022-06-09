/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.records;


import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.codegen.maven.example.tables.RelativeInformation;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RelativeInformationRecord extends UpdatableRecordImpl<RelativeInformationRecord> implements Record7<Long, LocalDate, String, String, String, Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>human_resource_management.relative_information.relative_id</code>.
     */
    public RelativeInformationRecord setRelativeId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.relative_information.relative_id</code>.
     */
    public Long getRelativeId() {
        return (Long) get(0);
    }

    /**
     * Setter for
     * <code>human_resource_management.relative_information.birth_date</code>.
     */
    public RelativeInformationRecord setBirthDate(LocalDate value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.relative_information.birth_date</code>.
     */
    public LocalDate getBirthDate() {
        return (LocalDate) get(1);
    }

    /**
     * Setter for
     * <code>human_resource_management.relative_information.parent_name</code>.
     */
    public RelativeInformationRecord setParentName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.relative_information.parent_name</code>.
     */
    public String getParentName() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>human_resource_management.relative_information.status</code>.
     */
    public RelativeInformationRecord setStatus(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.relative_information.status</code>.
     */
    public String getStatus() {
        return (String) get(3);
    }

    /**
     * Setter for
     * <code>human_resource_management.relative_information.employee_id</code>.
     */
    public RelativeInformationRecord setEmployeeId(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.relative_information.employee_id</code>.
     */
    public String getEmployeeId() {
        return (String) get(4);
    }

    /**
     * Setter for
     * <code>human_resource_management.relative_information.relative_type</code>.
     */
    public RelativeInformationRecord setRelativeType(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.relative_information.relative_type</code>.
     */
    public Long getRelativeType() {
        return (Long) get(5);
    }

    /**
     * Setter for
     * <code>human_resource_management.relative_information.contact</code>.
     */
    public RelativeInformationRecord setContact(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.relative_information.contact</code>.
     */
    public String getContact() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, LocalDate, String, String, String, Long, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, LocalDate, String, String, String, Long, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return RelativeInformation.RELATIVE_INFORMATION.RELATIVE_ID;
    }

    @Override
    public Field<LocalDate> field2() {
        return RelativeInformation.RELATIVE_INFORMATION.BIRTH_DATE;
    }

    @Override
    public Field<String> field3() {
        return RelativeInformation.RELATIVE_INFORMATION.PARENT_NAME;
    }

    @Override
    public Field<String> field4() {
        return RelativeInformation.RELATIVE_INFORMATION.STATUS;
    }

    @Override
    public Field<String> field5() {
        return RelativeInformation.RELATIVE_INFORMATION.EMPLOYEE_ID;
    }

    @Override
    public Field<Long> field6() {
        return RelativeInformation.RELATIVE_INFORMATION.RELATIVE_TYPE;
    }

    @Override
    public Field<String> field7() {
        return RelativeInformation.RELATIVE_INFORMATION.CONTACT;
    }

    @Override
    public Long component1() {
        return getRelativeId();
    }

    @Override
    public LocalDate component2() {
        return getBirthDate();
    }

    @Override
    public String component3() {
        return getParentName();
    }

    @Override
    public String component4() {
        return getStatus();
    }

    @Override
    public String component5() {
        return getEmployeeId();
    }

    @Override
    public Long component6() {
        return getRelativeType();
    }

    @Override
    public String component7() {
        return getContact();
    }

    @Override
    public Long value1() {
        return getRelativeId();
    }

    @Override
    public LocalDate value2() {
        return getBirthDate();
    }

    @Override
    public String value3() {
        return getParentName();
    }

    @Override
    public String value4() {
        return getStatus();
    }

    @Override
    public String value5() {
        return getEmployeeId();
    }

    @Override
    public Long value6() {
        return getRelativeType();
    }

    @Override
    public String value7() {
        return getContact();
    }

    @Override
    public RelativeInformationRecord value1(Long value) {
        setRelativeId(value);
        return this;
    }

    @Override
    public RelativeInformationRecord value2(LocalDate value) {
        setBirthDate(value);
        return this;
    }

    @Override
    public RelativeInformationRecord value3(String value) {
        setParentName(value);
        return this;
    }

    @Override
    public RelativeInformationRecord value4(String value) {
        setStatus(value);
        return this;
    }

    @Override
    public RelativeInformationRecord value5(String value) {
        setEmployeeId(value);
        return this;
    }

    @Override
    public RelativeInformationRecord value6(Long value) {
        setRelativeType(value);
        return this;
    }

    @Override
    public RelativeInformationRecord value7(String value) {
        setContact(value);
        return this;
    }

    @Override
    public RelativeInformationRecord values(Long value1, LocalDate value2, String value3, String value4, String value5, Long value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RelativeInformationRecord
     */
    public RelativeInformationRecord() {
        super(RelativeInformation.RELATIVE_INFORMATION);
    }

    /**
     * Create a detached, initialised RelativeInformationRecord
     */
    public RelativeInformationRecord(Long relativeId, LocalDate birthDate, String parentName, String status, String employeeId, Long relativeType, String contact) {
        super(RelativeInformation.RELATIVE_INFORMATION);

        setRelativeId(relativeId);
        setBirthDate(birthDate);
        setParentName(parentName);
        setStatus(status);
        setEmployeeId(employeeId);
        setRelativeType(relativeType);
        setContact(contact);
    }

    /**
     * Create a detached, initialised RelativeInformationRecord
     */
    public RelativeInformationRecord(org.jooq.codegen.maven.example.tables.pojos.RelativeInformation value) {
        super(RelativeInformation.RELATIVE_INFORMATION);

        if (value != null) {
            setRelativeId(value.getRelativeId());
            setBirthDate(value.getBirthDate());
            setParentName(value.getParentName());
            setStatus(value.getStatus());
            setEmployeeId(value.getEmployeeId());
            setRelativeType(value.getRelativeType());
            setContact(value.getContact());
        }
    }
}