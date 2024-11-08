CREATE TABLE cuws.verified_yield_amendment(
    verified_yield_amendment_guid    varchar(32)       NOT NULL,
    verified_yield_amendment_code    varchar(10)       NOT NULL,
    verified_yield_contract_guid     varchar(32)       NOT NULL,
    crop_commodity_id                numeric(9, 0)     NOT NULL,
    field_id                         numeric(9, 0),
    yield_per_acre                   numeric(14, 4)    NOT NULL,
    acres                            numeric(14, 4)    NOT NULL,
    rationale                        varchar(200)      NOT NULL,
    create_user                      varchar(64)       NOT NULL,
    create_date                      timestamp(0)      NOT NULL,
    update_user                      varchar(64)       NOT NULL,
    update_date                      timestamp(0)      NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.verified_yield_amendment.verified_yield_amendment_guid IS 'Verified Yield Amendment GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.verified_yield_amendment_code IS 'Verified Yield Amendment Code is a code value that uniquely identifies a record.'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.yield_per_acre IS 'Yield Per Acre is a user entered value for yield per acre by commodity and field'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.acres IS 'Acres is the user entered value for acres by commodity and field'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.rationale IS 'Rationale is the explanation for the amendement like source or reason'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.verified_yield_amendment.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_amendment IS 'The table contains verified yield amendment data by contract and commodity.'
;

CREATE INDEX ix_vya_vyc ON cuws.verified_yield_amendment(verified_yield_contract_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_vya_vyac ON cuws.verified_yield_amendment(verified_yield_amendment_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_vya_fld ON cuws.verified_yield_amendment(field_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_vya_cco ON cuws.verified_yield_amendment(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.verified_yield_amendment ADD 
    CONSTRAINT pk_vya PRIMARY KEY (verified_yield_amendment_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_amendment ADD CONSTRAINT fk_vya_vyac 
    FOREIGN KEY (verified_yield_amendment_code)
    REFERENCES cuws.verified_yield_amendment_code(verified_yield_amendment_code)
;

ALTER TABLE cuws.verified_yield_amendment ADD CONSTRAINT fk_vya_vyc 
    FOREIGN KEY (verified_yield_contract_guid)
    REFERENCES cuws.verified_yield_contract(verified_yield_contract_guid)
;

ALTER TABLE cuws.verified_yield_amendment ADD CONSTRAINT fk_vya_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;

ALTER TABLE cuws.verified_yield_amendment ADD CONSTRAINT fk_vya_fld 
    FOREIGN KEY (field_id)
    REFERENCES cuws.field(field_id)
;
