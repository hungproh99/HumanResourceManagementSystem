/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.codegen.maven.example.HumanResourceManagement;
import org.jooq.codegen.maven.example.Keys;
import org.jooq.codegen.maven.example.tables.records.RelativeInformationRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RelativeInformation extends TableImpl<RelativeInformationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>human_resource_management.relative_information</code>
     */
    public static final RelativeInformation RELATIVE_INFORMATION = new RelativeInformation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RelativeInformationRecord> getRecordType() {
        return RelativeInformationRecord.class;
    }

    /**
     * The column
     * <code>human_resource_management.relative_information.relative_id</code>.
     */
    public final TableField<RelativeInformationRecord, Long> RELATIVE_ID = createField(DSL.name("relative_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column
     * <code>human_resource_management.relative_information.birth_date</code>.
     */
    public final TableField<RelativeInformationRecord, LocalDate> BIRTH_DATE = createField(DSL.name("birth_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>human_resource_management.relative_information.contact</code>.
     */
    public final TableField<RelativeInformationRecord, String> CONTACT = createField(DSL.name("contact"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.relative_information.parent_name</code>.
     */
    public final TableField<RelativeInformationRecord, String> PARENT_NAME = createField(DSL.name("parent_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.relative_information.status</code>.
     */
    public final TableField<RelativeInformationRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.relative_information.employee_id</code>.
     */
    public final TableField<RelativeInformationRecord, String> EMPLOYEE_ID = createField(DSL.name("employee_id"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.relative_information.type_id</code>.
     */
    public final TableField<RelativeInformationRecord, Long> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.BIGINT, this, "");

    private RelativeInformation(Name alias, Table<RelativeInformationRecord> aliased) {
        this(alias, aliased, null);
    }

    private RelativeInformation(Name alias, Table<RelativeInformationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased
     * <code>human_resource_management.relative_information</code> table
     * reference
     */
    public RelativeInformation(String alias) {
        this(DSL.name(alias), RELATIVE_INFORMATION);
    }

    /**
     * Create an aliased
     * <code>human_resource_management.relative_information</code> table
     * reference
     */
    public RelativeInformation(Name alias) {
        this(alias, RELATIVE_INFORMATION);
    }

    /**
     * Create a <code>human_resource_management.relative_information</code>
     * table reference
     */
    public RelativeInformation() {
        this(DSL.name("relative_information"), null);
    }

    public <O extends Record> RelativeInformation(Table<O> child, ForeignKey<O, RelativeInformationRecord> key) {
        super(child, key, RELATIVE_INFORMATION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public Identity<RelativeInformationRecord, Long> getIdentity() {
        return (Identity<RelativeInformationRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<RelativeInformationRecord> getPrimaryKey() {
        return Keys.KEY_RELATIVE_INFORMATION_PRIMARY;
    }

    @Override
    public List<ForeignKey<RelativeInformationRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FKMWKD588KL8GL2RFVI52GXUT3, Keys.FKKKNO01O28GOAPEHPUDC6PVXGQ);
    }

    private transient Employee _employee;
    private transient RelativeType _relativeType;

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.employee</code> table.
     */
    public Employee employee() {
        if (_employee == null)
            _employee = new Employee(this, Keys.FKMWKD588KL8GL2RFVI52GXUT3);

        return _employee;
    }

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.relative_type</code> table.
     */
    public RelativeType relativeType() {
        if (_relativeType == null)
            _relativeType = new RelativeType(this, Keys.FKKKNO01O28GOAPEHPUDC6PVXGQ);

        return _relativeType;
    }

    @Override
    public RelativeInformation as(String alias) {
        return new RelativeInformation(DSL.name(alias), this);
    }

    @Override
    public RelativeInformation as(Name alias) {
        return new RelativeInformation(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RelativeInformation rename(String name) {
        return new RelativeInformation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RelativeInformation rename(Name name) {
        return new RelativeInformation(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, LocalDate, String, String, String, String, Long> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
