/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row20;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.codegen.maven.example.HumanResourceManagement;
import org.jooq.codegen.maven.example.Keys;
import org.jooq.codegen.maven.example.tables.records.EmployeeRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Employee extends TableImpl<EmployeeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>human_resource_management.employee</code>
     */
    public static final Employee EMPLOYEE = new Employee();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EmployeeRecord> getRecordType() {
        return EmployeeRecord.class;
    }

    /**
     * The column <code>human_resource_management.employee.employee_id</code>.
     */
    public final TableField<EmployeeRecord, String> EMPLOYEE_ID = createField(DSL.name("employee_id"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>human_resource_management.employee.address</code>.
     */
    public final TableField<EmployeeRecord, String> ADDRESS = createField(DSL.name("address"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.avatar</code>.
     */
    public final TableField<EmployeeRecord, String> AVATAR = createField(DSL.name("avatar"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.birth_date</code>.
     */
    public final TableField<EmployeeRecord, LocalDate> BIRTH_DATE = createField(DSL.name("birth_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>human_resource_management.employee.company_email</code>.
     */
    public final TableField<EmployeeRecord, String> COMPANY_EMAIL = createField(DSL.name("company_email"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.employee.current_situation</code>.
     */
    public final TableField<EmployeeRecord, String> CURRENT_SITUATION = createField(DSL.name("current_situation"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.facebook</code>.
     */
    public final TableField<EmployeeRecord, String> FACEBOOK = createField(DSL.name("facebook"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.full_name</code>.
     */
    public final TableField<EmployeeRecord, String> FULL_NAME = createField(DSL.name("full_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.gender</code>.
     */
    public final TableField<EmployeeRecord, String> GENDER = createField(DSL.name("gender"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.manager_id</code>.
     */
    public final TableField<EmployeeRecord, String> MANAGER_ID = createField(DSL.name("manager_id"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.employee.marital_status</code>.
     */
    public final TableField<EmployeeRecord, String> MARITAL_STATUS = createField(DSL.name("marital_status"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.nick_name</code>.
     */
    public final TableField<EmployeeRecord, String> NICK_NAME = createField(DSL.name("nick_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.password</code>.
     */
    public final TableField<EmployeeRecord, String> PASSWORD = createField(DSL.name("password"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>human_resource_management.employee.personal_email</code>.
     */
    public final TableField<EmployeeRecord, String> PERSONAL_EMAIL = createField(DSL.name("personal_email"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.phone_number</code>.
     */
    public final TableField<EmployeeRecord, String> PHONE_NUMBER = createField(DSL.name("phone_number"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.tax_code</code>.
     */
    public final TableField<EmployeeRecord, String> TAX_CODE = createField(DSL.name("tax_code"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.work_status</code>.
     */
    public final TableField<EmployeeRecord, String> WORK_STATUS = createField(DSL.name("work_status"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.bank_id</code>.
     */
    public final TableField<EmployeeRecord, Long> BANK_ID = createField(DSL.name("bank_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>human_resource_management.employee.card_id</code>.
     */
    public final TableField<EmployeeRecord, String> CARD_ID = createField(DSL.name("card_id"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>human_resource_management.employee.role_type</code>.
     */
    public final TableField<EmployeeRecord, Long> ROLE_TYPE = createField(DSL.name("role_type"), SQLDataType.BIGINT, this, "");

    private Employee(Name alias, Table<EmployeeRecord> aliased) {
        this(alias, aliased, null);
    }

    private Employee(Name alias, Table<EmployeeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>human_resource_management.employee</code> table
     * reference
     */
    public Employee(String alias) {
        this(DSL.name(alias), EMPLOYEE);
    }

    /**
     * Create an aliased <code>human_resource_management.employee</code> table
     * reference
     */
    public Employee(Name alias) {
        this(alias, EMPLOYEE);
    }

    /**
     * Create a <code>human_resource_management.employee</code> table reference
     */
    public Employee() {
        this(DSL.name("employee"), null);
    }

    public <O extends Record> Employee(Table<O> child, ForeignKey<O, EmployeeRecord> key) {
        super(child, key, EMPLOYEE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public UniqueKey<EmployeeRecord> getPrimaryKey() {
        return Keys.KEY_EMPLOYEE_PRIMARY;
    }

    @Override
    public List<ForeignKey<EmployeeRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FKICPTSO0QH4VMWT76BGF5HM9P6, Keys.FKE4GSS4EY1FU5MXPE82EQNVAC6, Keys.FKQSQU0215R8TO2N2EJJ7EJCIMV);
    }

    private transient Bank _bank;
    private transient IdentityCard _identityCard;
    private transient RoleType _roleType;

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.bank</code> table.
     */
    public Bank bank() {
        if (_bank == null)
            _bank = new Bank(this, Keys.FKICPTSO0QH4VMWT76BGF5HM9P6);

        return _bank;
    }

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.identity_card</code> table.
     */
    public IdentityCard identityCard() {
        if (_identityCard == null)
            _identityCard = new IdentityCard(this, Keys.FKE4GSS4EY1FU5MXPE82EQNVAC6);

        return _identityCard;
    }

    /**
     * Get the implicit join path to the
     * <code>human_resource_management.role_type</code> table.
     */
    public RoleType roleType() {
        if (_roleType == null)
            _roleType = new RoleType(this, Keys.FKQSQU0215R8TO2N2EJJ7EJCIMV);

        return _roleType;
    }

    @Override
    public Employee as(String alias) {
        return new Employee(DSL.name(alias), this);
    }

    @Override
    public Employee as(Name alias) {
        return new Employee(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Employee rename(String name) {
        return new Employee(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Employee rename(Name name) {
        return new Employee(name, null);
    }

    // -------------------------------------------------------------------------
    // Row20 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row20<String, String, String, LocalDate, String, String, String, String, String, String, String, String, String, String, String, String, String, Long, String, Long> fieldsRow() {
        return (Row20) super.fieldsRow();
    }
}
