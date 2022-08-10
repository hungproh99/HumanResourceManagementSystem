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
import org.jooq.codegen.maven.example.tables.records.RequestStatusRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RequestStatus extends TableImpl<RequestStatusRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>human_resource_management.request_status</code>
     */
    public static final RequestStatus REQUEST_STATUS = new RequestStatus();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RequestStatusRecord> getRecordType() {
        return RequestStatusRecord.class;
    }

    /**
     * The column <code>human_resource_management.request_status.type_id</code>.
     */
    public final TableField<RequestStatusRecord, Long> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>human_resource_management.request_status.name</code>.
     */
    public final TableField<RequestStatusRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255), this, "");

    private RequestStatus(Name alias, Table<RequestStatusRecord> aliased) {
        this(alias, aliased, null);
    }

    private RequestStatus(Name alias, Table<RequestStatusRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>human_resource_management.request_status</code>
     * table reference
     */
    public RequestStatus(String alias) {
        this(DSL.name(alias), REQUEST_STATUS);
    }

    /**
     * Create an aliased <code>human_resource_management.request_status</code>
     * table reference
     */
    public RequestStatus(Name alias) {
        this(alias, REQUEST_STATUS);
    }

    /**
     * Create a <code>human_resource_management.request_status</code> table
     * reference
     */
    public RequestStatus() {
        this(DSL.name("request_status"), null);
    }

    public <O extends Record> RequestStatus(Table<O> child, ForeignKey<O, RequestStatusRecord> key) {
        super(child, key, REQUEST_STATUS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : HumanResourceManagement.HUMAN_RESOURCE_MANAGEMENT;
    }

    @Override
    public Identity<RequestStatusRecord, Long> getIdentity() {
        return (Identity<RequestStatusRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<RequestStatusRecord> getPrimaryKey() {
        return Keys.KEY_REQUEST_STATUS_PRIMARY;
    }

    @Override
    public RequestStatus as(String alias) {
        return new RequestStatus(DSL.name(alias), this);
    }

    @Override
    public RequestStatus as(Name alias) {
        return new RequestStatus(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RequestStatus rename(String name) {
        return new RequestStatus(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RequestStatus rename(Name name) {
        return new RequestStatus(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
