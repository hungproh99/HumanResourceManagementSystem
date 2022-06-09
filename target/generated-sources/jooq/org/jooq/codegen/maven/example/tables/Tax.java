/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables;


import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.codegen.maven.example.HumanResourceManagement;
import org.jooq.codegen.maven.example.Keys;
import org.jooq.codegen.maven.example.tables.records.TaxRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tax extends TableImpl<TaxRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>human_resource_management.tax</code>
     */
    public static final Tax TAX = new Tax();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TaxRecord> getRecordType() {
        return TaxRecord.class;
    }

    /**
     * The column <code>human_resource_management.tax.tax_id</code>.
     */
    public final TableField<TaxRecord, Long> TAX_ID = createField(DSL.name("tax_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>human_resource_management.tax.description</code>.
     */
    public final TableField<TaxRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.tax.tax_name</code>.
     */
    public final TableField<TaxRecord, String> TAX_NAME = createField(DSL.name("tax_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.tax.percent</code>.
     */
    public final TableField<TaxRecord, String> PERCENT = createField(DSL.name("percent"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.tax.title</code>.
     */
    public final TableField<TaxRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.tax.employee_id</code>.
     */
    public final TableField<TaxRecord, String> EMPLOYEE_ID = createField(DSL.name("employee_id"), SQLDataType.VARCHAR(255), this, "");

    private Tax(Name alias, Table<TaxRecord> aliased) {
        this(alias, aliased, null);
    }

    private Tax(Name alias, Table<TaxRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>human_resource_management.tax</code> table
     * reference
     */
    public Tax(String alias) {
        this(DSL.name(alias), TAX);
    }

    /**
     * Create an aliased <code>human_resource_management.tax</code> table
     * reference
     */
    public Tax(Name alias) {
        this(alias, TAX);
    }

    /**
     * Create a <code>human_resource_management.tax</code> table reference
     */
    public Tax() {
        this(DSL.name("tax"), null);
    }

    public <O extends Record> Tax(Table<O> child, ForeignKey<O, TaxRecord> key) {
        super(child, key, TAX);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public Identity<TaxRecord, Long> getIdentity() {
        return (Identity<TaxRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<TaxRecord> getPrimaryKey() {
        return Keys.KEY_TAX_PRIMARY;
    }

    @Override
    public List<ForeignKey<TaxRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FKEI7SCUKYXJXRWM8J14RXCGFPT);
    }

    private transient Employee _employee;

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.employee</code> table.
     */
    public Employee employee() {
        if (_employee == null)
            _employee = new Employee(this, Keys.FKEI7SCUKYXJXRWM8J14RXCGFPT);

        return _employee;
    }

    @Override
    public Tax as(String alias) {
        return new Tax(DSL.name(alias), this);
    }

    @Override
    public Tax as(Name alias) {
        return new Tax(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Tax rename(String name) {
        return new Tax(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Tax rename(Name name) {
        return new Tax(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, String, String, String, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}