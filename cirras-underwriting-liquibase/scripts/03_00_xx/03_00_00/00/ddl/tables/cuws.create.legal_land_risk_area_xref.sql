


CREATE TABLE cuws.legal_land_risk_area_xref(
    legal_land_id            numeric(10, 0)    NOT NULL,
    risk_area_id             numeric(10, 0)    NOT NULL,
    active_from_crop_year    numeric(4, 0)     NOT NULL,
    active_to_crop_year      numeric(4, 0),
    create_user              varchar(64)       NOT NULL,
    create_date              timestamp(0)      NOT NULL,
    update_user              varchar(64)       NOT NULL,
    update_date              timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.legal_land_risk_area_xref.legal_land_id IS 'Legal Land Id is a unique key of a legal land from LEGAL_LAND'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref.risk_area_id IS 'Risk Area Id is a unique identifier for a RISK AREA generated from a surrogate key'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref.active_from_crop_year IS 'Active From Crop Year is the first year the risk area is active for the legal land'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref.active_to_crop_year IS 'Active To Crop Year is the last year the risk area was active for the legal land'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.legal_land_risk_area_xref IS 'The table associates risk areas with legal lands.'
;

CREATE INDEX ix_llrax_ll ON cuws.legal_land_risk_area_xref(legal_land_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_llrax_ra ON cuws.legal_land_risk_area_xref(risk_area_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.legal_land_risk_area_xref ADD 
    CONSTRAINT pk_llrax PRIMARY KEY (legal_land_id, risk_area_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.legal_land_risk_area_xref ADD CONSTRAINT fk_llrax_ll 
    FOREIGN KEY (legal_land_id)
    REFERENCES cuws.legal_land(legal_land_id)
;

ALTER TABLE cuws.legal_land_risk_area_xref ADD CONSTRAINT fk_llrax_ra 
    FOREIGN KEY (risk_area_id)
    REFERENCES cuws.risk_area(risk_area_id)
;
