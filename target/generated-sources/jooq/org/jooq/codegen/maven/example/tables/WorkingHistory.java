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
import org.jooq.codegen.maven.example.tables.records.WorkingHistoryRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WorkingHistory extends TableImpl<WorkingHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>human_resource_management.working_history</code>
     */
    public static final WorkingHistory WORKING_HISTORY = new WorkingHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<WorkingHistoryRecord> getRecordType() {
        return WorkingHistoryRecord.class;
    }

    /**
     * The column
     * <code>human_resource_management.working_history.working_history_id</code>.
     */
    public final TableField<WorkingHistoryRecord, Long> WORKING_HISTORY_ID = createField(DSL.name("working_history_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column
     * <code>human_resource_management.working_history.company_name</code>.
     */
    public final TableField<WorkingHistoryRecord, String> COMPANY_NAME = createField(DSL.name("company_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.working_history.end_date</code>.
     */
    public final TableField<WorkingHistoryRecord, LocalDate> END_DATE = createField(DSL.name("end_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>human_resource_management.working_history.start_date</code>.
     */
    public final TableField<WorkingHistoryRecord, LocalDate> START_DATE = createField(DSL.name("start_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>human_resource_management.working_history.type_id</code>.
     */
    public final TableField<WorkingHistoryRecord, Long> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column
     * <code>human_resource_management.working_history.employee_id</code>.
     */
    public final TableField<WorkingHistoryRecord, String> EMPLOYEE_ID = createField(DSL.name("employee_id"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.working_history.position</code>.
     */
    public final TableField<WorkingHistoryRecord, String> POSITION = createField(DSL.name("position"), SQLDataType.VARCHAR(255), this, "");

    private WorkingHistory(Name alias, Table<WorkingHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private WorkingHistory(Name alias, Table<WorkingHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>human_resource_management.working_history</code>
     * table reference
     */
    public WorkingHistory(String alias) {
        this(DSL.name(alias), WORKING_HISTORY);
    }

    /**
     * Create an aliased <code>human_resource_management.working_history</code>
     * table reference
     */
    public WorkingHistory(Name alias) {
        this(alias, WORKING_HISTORY);
    }

    /**
     * Create a <code>human_resource_management.working_history</code> table
     * reference
     */
    public WorkingHistory() {
        this(DSL.name("working_history"), null);
    }

    public <O extends Record> WorkingHistory(Table<O> child, ForeignKey<O, WorkingHistoryRecord> key) {
        super(child, key, WORKING_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public Identity<WorkingHistoryRecord, Long> getIdentity() {
        return (Identity<WorkingHistoryRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<WorkingHistoryRecord> getPrimaryKey() {
        return Keys.KEY_WORKING_HISTORY_PRIMARY;
    }

    @Override
    public List<ForeignKey<WorkingHistoryRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK6QAYSC7F06G54A5XUYD150B6V, Keys.FKIFVK8C7ONM47VH7RBDM6R7KUN);
    }

    private transient ContractType _contractType;
    private transient Employee _employee;

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.contract_type</code> table.
     */
    public ContractType contractType() {
        if (_contractType == null)
            _contractType = new ContractType(this, Keys.FK6QAYSC7F06G54A5XUYD150B6V);

        return _contractType;
    }

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.employee</code> table.
     */
    public Employee employee() {
        if (_employee == null)
            _employee = new Employee(this, Keys.FKIFVK8C7ONM47VH7RBDM6R7KUN);

        return _employee;
    }

    @Override
    public WorkingHistory as(String alias) {
        return new WorkingHistory(DSL.name(alias), this);
    }

    @Override
    public WorkingHistory as(Name alias) {
        return new WorkingHistory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkingHistory rename(String name) {
        return new WorkingHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkingHistory rename(Name name) {
        return new WorkingHistory(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, String, LocalDate, LocalDate, Long, String, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
