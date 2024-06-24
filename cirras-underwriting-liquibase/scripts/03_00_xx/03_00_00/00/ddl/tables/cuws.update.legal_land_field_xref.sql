

--Drop COLUMNS
ALTER TABLE cuws.legal_land_field_xref DROP COLUMN data_sync_trans_date;

--Add missing indexes
CREATE INDEX ix_llfx_fld ON cuws.legal_land_field_xref(field_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_llfx_ll ON cuws.legal_land_field_xref(legal_land_id)
 TABLESPACE pg_default
;
