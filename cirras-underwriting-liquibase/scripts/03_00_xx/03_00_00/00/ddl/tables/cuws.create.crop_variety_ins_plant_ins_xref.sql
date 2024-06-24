CREATE TABLE cuws.crop_variety_ins_plant_ins_xref(
    crop_variety_insurability_guid    varchar(32)    NOT NULL,
    plant_insurability_type_code      varchar(10)    NOT NULL,
    create_user                       varchar(64)    NOT NULL,
    create_date                       timestamp(0)   NOT NULL,
    update_user                       varchar(64)    NOT NULL,
    update_date                       timestamp(0)   NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.crop_variety_ins_plant_ins_xref.crop_variety_insurability_guid IS 'Crop Variety Insurability GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.crop_variety_ins_plant_ins_xref.plant_insurability_type_code IS 'Plant Insurability Type Code is a unique record identifier for plant insurability type records.'
;
COMMENT ON COLUMN cuws.crop_variety_ins_plant_ins_xref.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.crop_variety_ins_plant_ins_xref.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.crop_variety_ins_plant_ins_xref.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.crop_variety_ins_plant_ins_xref.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.crop_variety_ins_plant_ins_xref IS 'The table defines eligible plant insurability types for any varieties for which is_plant_insurable_ind = Y in crop_variety_insurability'
;

CREATE INDEX ix_cvipix_cvi ON cuws.crop_variety_ins_plant_ins_xref(crop_variety_insurability_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_cvipix_pitc ON cuws.crop_variety_ins_plant_ins_xref(plant_insurability_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.crop_variety_ins_plant_ins_xref ADD 
    CONSTRAINT pk_cvipix PRIMARY KEY (crop_variety_insurability_guid, plant_insurability_type_code) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.crop_variety_ins_plant_ins_xref ADD CONSTRAINT fk_cvipix_cvi 
    FOREIGN KEY (crop_variety_insurability_guid)
    REFERENCES cuws.crop_variety_insurability(crop_variety_insurability_guid)
;

ALTER TABLE cuws.crop_variety_ins_plant_ins_xref ADD CONSTRAINT fk_cvipix_pitc 
    FOREIGN KEY (plant_insurability_type_code)
    REFERENCES cuws.plant_insurability_type_code(plant_insurability_type_code)
;
