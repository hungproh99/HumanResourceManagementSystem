/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables;


import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.codegen.maven.example.HumanResourceManagement;
import org.jooq.codegen.maven.example.Keys;
import org.jooq.codegen.maven.example.tables.records.DeductionTypeRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DeductionType extends TableImpl<DeductionTypeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>human_resource_management.deduction_type</code>
     */
    public static final DeductionType DEDUCTION_TYPE = new DeductionType();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DeductionTypeRecord> getRecordType() {
        return DeductionTypeRecord.class;
    }

    /**
     * The column
     * <code>human_resource_management.deduction_type.deduction_type_id</code>.
     */
    public final TableField<DeductionTypeRecord, Long> DEDUCTION_TYPE_ID = createField(DSL.name("deduction_type_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column
     * <code>human_resource_management.deduction_type.deduction_type</code>.
     */
    public final TableField<DeductionTypeRecord, String> DEDUCTION_TYPE_ = createField(DSL.name("deduction_type"), SQLDataType.VARCHAR(255), this, "");

    private DeductionType(Name alias, Table<DeductionTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private DeductionType(Name alias, Table<DeductionTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>human_resource_management.deduction_type</code>
     * table reference
     */
    public DeductionType(String alias) {
        this(DSL.name(alias), DEDUCTION_TYPE);
    }

    /**
     * Create an aliased <code>human_resource_management.deduction_type</code>
     * table reference
     */
    public DeductionType(Name alias) {
        this(alias, DEDUCTION_TYPE);
    }

    /**
     * Create a <code>human_resource_management.deduction_type</code> table
     * reference
     */
    public DeductionType() {
        this(DSL.name("deduction_type"), null);
    }

    public <O extends Record> DeductionType(Table<O> child, ForeignKey<O, DeductionTypeRecord> key) {
        super(child, key, DEDUCTION_TYPE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public Identity<DeductionTypeRecord, Long> getIdentity() {
        return (Identity<DeductionTypeRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<DeductionTypeRecord> getPrimaryKey() {
        return Keys.KEY_DEDUCTION_TYPE_PRIMARY;
    }

    @Override
    public DeductionType as(String alias) {
        return new DeductionType(DSL.name(alias), this);
    }

    @Override
    public DeductionType as(Name alias) {
        return new DeductionType(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public DeductionType rename(String name) {
        return new DeductionType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public DeductionType rename(Name name) {
        return new DeductionType(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
