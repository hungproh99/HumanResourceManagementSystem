/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.codegen.maven.example.HumanResourceManagement;
import org.jooq.codegen.maven.example.Keys;
import org.jooq.codegen.maven.example.tables.records.AdvancesSalaryRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AdvancesSalary extends TableImpl<AdvancesSalaryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>human_resource_management.advances_salary</code>
     */
    public static final AdvancesSalary ADVANCES_SALARY = new AdvancesSalary();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AdvancesSalaryRecord> getRecordType() {
        return AdvancesSalaryRecord.class;
    }

    /**
     * The column
     * <code>human_resource_management.advances_salary.advances_id</code>.
     */
    public final TableField<AdvancesSalaryRecord, Long> ADVANCES_ID = createField(DSL.name("advances_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>human_resource_management.advances_salary.date</code>.
     */
    public final TableField<AdvancesSalaryRecord, LocalDate> DATE = createField(DSL.name("date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>human_resource_management.advances_salary.description</code>.
     */
    public final TableField<AdvancesSalaryRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.advances_salary.value</code>.
     */
    public final TableField<AdvancesSalaryRecord, BigDecimal> VALUE = createField(DSL.name("value"), SQLDataType.DECIMAL(19, 2), this, "");

    /**
     * The column
     * <code>human_resource_management.advances_salary.salary_id</code>.
     */
    public final TableField<AdvancesSalaryRecord, Long> SALARY_ID = createField(DSL.name("salary_id"), SQLDataType.BIGINT, this, "");

    private AdvancesSalary(Name alias, Table<AdvancesSalaryRecord> aliased) {
        this(alias, aliased, null);
    }

    private AdvancesSalary(Name alias, Table<AdvancesSalaryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>human_resource_management.advances_salary</code>
     * table reference
     */
    public AdvancesSalary(String alias) {
        this(DSL.name(alias), ADVANCES_SALARY);
    }

    /**
     * Create an aliased <code>human_resource_management.advances_salary</code>
     * table reference
     */
    public AdvancesSalary(Name alias) {
        this(alias, ADVANCES_SALARY);
    }

    /**
     * Create a <code>human_resource_management.advances_salary</code> table
     * reference
     */
    public AdvancesSalary() {
        this(DSL.name("advances_salary"), null);
    }

    public <O extends Record> AdvancesSalary(Table<O> child, ForeignKey<O, AdvancesSalaryRecord> key) {
        super(child, key, ADVANCES_SALARY);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public Identity<AdvancesSalaryRecord, Long> getIdentity() {
        return (Identity<AdvancesSalaryRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<AdvancesSalaryRecord> getPrimaryKey() {
        return Keys.KEY_ADVANCES_SALARY_PRIMARY;
    }

    @Override
    public List<ForeignKey<AdvancesSalaryRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FKPM5ACHJSHMBHOGWBR5QVY5HLM);
    }

    private transient SalaryMonthly _salaryMonthly;

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.salary_monthly</code> table.
     */
    public SalaryMonthly salaryMonthly() {
        if (_salaryMonthly == null)
            _salaryMonthly = new SalaryMonthly(this, Keys.FKPM5ACHJSHMBHOGWBR5QVY5HLM);

        return _salaryMonthly;
    }

    @Override
    public AdvancesSalary as(String alias) {
        return new AdvancesSalary(DSL.name(alias), this);
    }

    @Override
    public AdvancesSalary as(Name alias) {
        return new AdvancesSalary(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AdvancesSalary rename(String name) {
        return new AdvancesSalary(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AdvancesSalary rename(Name name) {
        return new AdvancesSalary(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, LocalDate, String, BigDecimal, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
