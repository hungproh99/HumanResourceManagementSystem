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
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row14;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.codegen.maven.example.HumanResourceManagement;
import org.jooq.codegen.maven.example.Indexes;
import org.jooq.codegen.maven.example.Keys;
import org.jooq.codegen.maven.example.tables.records.WorkingContractRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WorkingContract extends TableImpl<WorkingContractRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>human_resource_management.working_contract</code>
     */
    public static final WorkingContract WORKING_CONTRACT = new WorkingContract();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<WorkingContractRecord> getRecordType() {
        return WorkingContractRecord.class;
    }

    /**
     * The column
     * <code>human_resource_management.working_contract.working_contract_id</code>.
     */
    public final TableField<WorkingContractRecord, Long> WORKING_CONTRACT_ID = createField(DSL.name("working_contract_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.base_salary</code>.
     */
    public final TableField<WorkingContractRecord, BigDecimal> BASE_SALARY = createField(DSL.name("base_salary"), SQLDataType.DECIMAL(19, 2), this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.company_name</code>.
     */
    public final TableField<WorkingContractRecord, String> COMPANY_NAME = createField(DSL.name("company_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.contract_url</code>.
     */
    public final TableField<WorkingContractRecord, String> CONTRACT_URL = createField(DSL.name("contract_url"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.end_date</code>.
     */
    public final TableField<WorkingContractRecord, LocalDate> END_DATE = createField(DSL.name("end_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.start_date</code>.
     */
    public final TableField<WorkingContractRecord, LocalDate> START_DATE = createField(DSL.name("start_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.status</code>.
     */
    public final TableField<WorkingContractRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.type_id</code>.
     */
    public final TableField<WorkingContractRecord, Long> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.employee_id</code>.
     */
    public final TableField<WorkingContractRecord, String> EMPLOYEE_ID = createField(DSL.name("employee_id"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.contract_status</code>.
     */
    public final TableField<WorkingContractRecord, Boolean> CONTRACT_STATUS = createField(DSL.name("contract_status"), SQLDataType.BIT, this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.area_id</code>.
     */
    public final TableField<WorkingContractRecord, Long> AREA_ID = createField(DSL.name("area_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.contract_type</code>.
     */
    public final TableField<WorkingContractRecord, Long> CONTRACT_TYPE = createField(DSL.name("contract_type"), SQLDataType.BIGINT, this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.job_id</code>.
     */
    public final TableField<WorkingContractRecord, Long> JOB_ID = createField(DSL.name("job_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column
     * <code>human_resource_management.working_contract.office_id</code>.
     */
    public final TableField<WorkingContractRecord, Long> OFFICE_ID = createField(DSL.name("office_id"), SQLDataType.BIGINT, this, "");

    private WorkingContract(Name alias, Table<WorkingContractRecord> aliased) {
        this(alias, aliased, null);
    }

    private WorkingContract(Name alias, Table<WorkingContractRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>human_resource_management.working_contract</code>
     * table reference
     */
    public WorkingContract(String alias) {
        this(DSL.name(alias), WORKING_CONTRACT);
    }

    /**
     * Create an aliased <code>human_resource_management.working_contract</code>
     * table reference
     */
    public WorkingContract(Name alias) {
        this(alias, WORKING_CONTRACT);
    }

    /**
     * Create a <code>human_resource_management.working_contract</code> table
     * reference
     */
    public WorkingContract() {
        this(DSL.name("working_contract"), null);
    }

    public <O extends Record> WorkingContract(Table<O> child, ForeignKey<O, WorkingContractRecord> key) {
        super(child, key, WORKING_CONTRACT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.WORKING_CONTRACT_FKSEX4WS8S50GFAMJ8LGMWEQGWR);
    }

    @Override
    public Identity<WorkingContractRecord, Long> getIdentity() {
        return (Identity<WorkingContractRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<WorkingContractRecord> getPrimaryKey() {
        return Keys.KEY_WORKING_CONTRACT_PRIMARY;
    }

    @Override
    public List<ForeignKey<WorkingContractRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FKM8LGM02FO7LBYK17TNGSGW3H5, Keys.FKM4VP3IHAOYHR5QRSOVQWIPBP6, Keys.FKC334JINAJFM1LFSEDUXLJ82PT, Keys.FK3CFMGM415JS09EAVMIUGYXE6R, Keys.FKOD4LOT1MBTXVMKUI2RALP7435);
    }

    private transient ContractType _fkm8lgm02fo7lbyk17tngsgw3h5;
    private transient Area _area;
    private transient ContractType _fkc334jinajfm1lfseduxlj82pt;
    private transient Job _job;
    private transient Office _office;

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.contract_type</code> table, via the
     * <code>FKm8lgm02fo7lbyk17tngsgw3h5</code> key.
     */
    public ContractType fkm8lgm02fo7lbyk17tngsgw3h5() {
        if (_fkm8lgm02fo7lbyk17tngsgw3h5 == null)
            _fkm8lgm02fo7lbyk17tngsgw3h5 = new ContractType(this, Keys.FKM8LGM02FO7LBYK17TNGSGW3H5);

        return _fkm8lgm02fo7lbyk17tngsgw3h5;
    }

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.area</code> table.
     */
    public Area area() {
        if (_area == null)
            _area = new Area(this, Keys.FKM4VP3IHAOYHR5QRSOVQWIPBP6);

        return _area;
    }

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.contract_type</code> table, via the
     * <code>FKc334jinajfm1lfseduxlj82pt</code> key.
     */
    public ContractType fkc334jinajfm1lfseduxlj82pt() {
        if (_fkc334jinajfm1lfseduxlj82pt == null)
            _fkc334jinajfm1lfseduxlj82pt = new ContractType(this, Keys.FKC334JINAJFM1LFSEDUXLJ82PT);

        return _fkc334jinajfm1lfseduxlj82pt;
    }

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.job</code> table.
     */
    public Job job() {
        if (_job == null)
            _job = new Job(this, Keys.FK3CFMGM415JS09EAVMIUGYXE6R);

        return _job;
    }

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.office</code> table.
     */
    public Office office() {
        if (_office == null)
            _office = new Office(this, Keys.FKOD4LOT1MBTXVMKUI2RALP7435);

        return _office;
    }

    @Override
    public WorkingContract as(String alias) {
        return new WorkingContract(DSL.name(alias), this);
    }

    @Override
    public WorkingContract as(Name alias) {
        return new WorkingContract(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkingContract rename(String name) {
        return new WorkingContract(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkingContract rename(Name name) {
        return new WorkingContract(name, null);
    }

    // -------------------------------------------------------------------------
    // Row14 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row14<Long, BigDecimal, String, String, LocalDate, LocalDate, String, Long, String, Boolean, Long, Long, Long, Long> fieldsRow() {
        return (Row14) super.fieldsRow();
    }
}