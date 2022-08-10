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
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.codegen.maven.example.HumanResourceManagement;
import org.jooq.codegen.maven.example.Keys;
import org.jooq.codegen.maven.example.tables.records.TimekeepingRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Timekeeping extends TableImpl<TimekeepingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>human_resource_management.timekeeping</code>
     */
    public static final Timekeeping TIMEKEEPING = new Timekeeping();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TimekeepingRecord> getRecordType() {
        return TimekeepingRecord.class;
    }

    /**
     * The column
     * <code>human_resource_management.timekeeping.timekeeping_id</code>.
     */
    public final TableField<TimekeepingRecord, Long> TIMEKEEPING_ID = createField(DSL.name("timekeeping_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>human_resource_management.timekeeping.date</code>.
     */
    public final TableField<TimekeepingRecord, LocalDate> DATE = createField(DSL.name("date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>human_resource_management.timekeeping.point_ot_day</code>.
     */
    public final TableField<TimekeepingRecord, Double> POINT_OT_DAY = createField(DSL.name("point_ot_day"), SQLDataType.DOUBLE, this, "");

    /**
     * The column
     * <code>human_resource_management.timekeeping.point_work_day</code>.
     */
    public final TableField<TimekeepingRecord, Double> POINT_WORK_DAY = createField(DSL.name("point_work_day"), SQLDataType.DOUBLE, this, "");

    /**
     * The column
     * <code>human_resource_management.timekeeping.employee_id</code>.
     */
    public final TableField<TimekeepingRecord, String> EMPLOYEE_ID = createField(DSL.name("employee_id"), SQLDataType.VARCHAR(255), this, "");

    private Timekeeping(Name alias, Table<TimekeepingRecord> aliased) {
        this(alias, aliased, null);
    }

    private Timekeeping(Name alias, Table<TimekeepingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>human_resource_management.timekeeping</code>
     * table reference
     */
    public Timekeeping(String alias) {
        this(DSL.name(alias), TIMEKEEPING);
    }

    /**
     * Create an aliased <code>human_resource_management.timekeeping</code>
     * table reference
     */
    public Timekeeping(Name alias) {
        this(alias, TIMEKEEPING);
    }

    /**
     * Create a <code>human_resource_management.timekeeping</code> table
     * reference
     */
    public Timekeeping() {
        this(DSL.name("timekeeping"), null);
    }

    public <O extends Record> Timekeeping(Table<O> child, ForeignKey<O, TimekeepingRecord> key) {
        super(child, key, TIMEKEEPING);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public Identity<TimekeepingRecord, Long> getIdentity() {
        return (Identity<TimekeepingRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<TimekeepingRecord> getPrimaryKey() {
        return Keys.KEY_TIMEKEEPING_PRIMARY;
    }

    @Override
    public List<ForeignKey<TimekeepingRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FKO4RRBGBYXSY5828KF771VM621);
    }

    private transient Employee _employee;

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.employee</code> table.
     */
    public Employee employee() {
        if (_employee == null)
            _employee = new Employee(this, Keys.FKO4RRBGBYXSY5828KF771VM621);

        return _employee;
    }

    @Override
    public Timekeeping as(String alias) {
        return new Timekeeping(DSL.name(alias), this);
    }

    @Override
    public Timekeeping as(Name alias) {
        return new Timekeeping(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Timekeeping rename(String name) {
        return new Timekeeping(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Timekeeping rename(Name name) {
        return new Timekeeping(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, LocalDate, Double, Double, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
