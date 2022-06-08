/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.codegen.maven.example.tables.Area;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AreaRecord extends UpdatableRecordImpl<AreaRecord> implements Record2<Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>human_resource_management.area.area_id</code>.
     */
    public AreaRecord setAreaId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.area.area_id</code>.
     */
    public Long getAreaId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>human_resource_management.area.name</code>.
     */
    public AreaRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>human_resource_management.area.name</code>.
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
        return Area.AREA.AREA_ID;
    }

    @Override
    public Field<String> field2() {
        return Area.AREA.NAME;
    }

    @Override
    public Long component1() {
        return getAreaId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Long value1() {
        return getAreaId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public AreaRecord value1(Long value) {
        setAreaId(value);
        return this;
    }

    @Override
    public AreaRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public AreaRecord values(Long value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AreaRecord
     */
    public AreaRecord() {
        super(Area.AREA);
    }

    /**
     * Create a detached, initialised AreaRecord
     */
    public AreaRecord(Long areaId, String name) {
        super(Area.AREA);

        setAreaId(areaId);
        setName(name);
    }

    /**
     * Create a detached, initialised AreaRecord
     */
    public AreaRecord(org.jooq.codegen.maven.example.tables.pojos.Area value) {
        super(Area.AREA);

        if (value != null) {
            setAreaId(value.getAreaId());
            setName(value.getName());
        }
    }
}
