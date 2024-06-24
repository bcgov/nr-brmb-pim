CREATE TABLE cuws.crop_variety_insurability(
    crop_variety_insurability_guid    varchar(32)      NOT NULL,
    crop_variety_id                   numeric(9, 0)    NOT NULL,
    is_quantity_insurable_ind         varchar(1)       NOT NULL,
    is_unseeded_insurable_ind         varchar(1)       NOT NULL,
    is_plant_insurable_ind            varchar(1)       NOT NULL,
    is_awp_eligible_ind               varchar(1)       NOT NULL,
    is_underseeding_eligible_ind      varchar(1)       NOT NULL,
    create_user                       varchar(64)      NOT NULL,
    create_date                       timestamp(0)     NOT NULL,
    update_user                       varchar(64)      NOT NULL,
    update_date                       timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.crop_variety_insurability.crop_variety_insurability_guid IS 'Crop Variety Insurability GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.is_quantity_insurable_ind IS 'Is Quantity Insurable Ind determines if the specified variety is eligible for Quantity Coverage in CIRRAS (Y) or not (N). '
;
COMMENT ON COLUMN cuws.crop_variety_insurability.is_unseeded_insurable_ind IS 'Is Unseeded Insurable Ind determines if the specified variety is eligible for Unseeded Coverage in CIRRAS (Y) or not (N).'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.is_plant_insurable_ind IS 'Is Plant Insurable Ind determines if the specified variety is eligible for Plant Coverage in CIRRAS (Y) or not (N).'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.is_awp_eligible_ind IS 'Is AWP Eligible Ind determines if the specified variety is eligible for AWP in CIRRAS (Y) or not (N).'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.is_underseeding_eligible_ind IS 'is underseeding eligible determines if a Forage Variety can be used in inventory_field.underseeded_variety_id (Y)'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.crop_variety_insurability.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.crop_variety_insurability IS 'The table defines insurance eligibility for a variety.'
;

ALTER TABLE cuws.crop_variety_insurability ADD 
    CONSTRAINT pk_cvi PRIMARY KEY (crop_variety_insurability_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.crop_variety_insurability ADD 
    CONSTRAINT uk_cvi UNIQUE (crop_variety_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.crop_variety_insurability ADD CONSTRAINT fk_cvi_cva 
    FOREIGN KEY (crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;


