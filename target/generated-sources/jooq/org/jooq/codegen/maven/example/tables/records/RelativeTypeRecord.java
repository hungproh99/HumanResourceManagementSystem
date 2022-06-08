/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.codegen.maven.example.tables.RelativeType;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RelativeTypeRecord extends UpdatableRecordImpl<RelativeTypeRecord> implements Record2<Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>human_resource_management.relative_type.type_id</code>.
     */
    public RelativeTypeRecord setTypeId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.relative_type.type_id</code>.
     */
    public Long getTypeId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>human_resource_management.relative_type.name</code>.
     */
    public RelativeTypeRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.relative_type.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return RelativeType.RELATIVE_TYPE.TYPE_ID;
    }

    @Override
    public Field<String> field2() {
        return RelativeType.RELATIVE_TYPE.NAME;
    }

    @Override
    public Long component1() {
        return getTypeId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Long value1() {
        return getTypeId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public RelativeTypeRecord value1(Long value) {
        setTypeId(value);
        return this;
    }

    @Override
    public RelativeTypeRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public RelativeTypeRecord values(Long value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RelativeTypeRecord
     */
    public RelativeTypeRecord() {
        super(RelativeType.RELATIVE_TYPE);
    }

    /**
     * Create a detached, initialised RelativeTypeRecord
     */
    public RelativeTypeRecord(Long typeId, String name) {
        super(RelativeType.RELATIVE_TYPE);

        setTypeId(typeId);
        setName(name);
    }

    /**
     * Create a detached, initialised RelativeTypeRecord
     */
    public RelativeTypeRecord(org.jooq.codegen.maven.example.tables.pojos.RelativeType value) {
        super(RelativeType.RELATIVE_TYPE);

        if (value != null) {
            setTypeId(value.getTypeId());
            setName(value.getName());
        }
    }
}
