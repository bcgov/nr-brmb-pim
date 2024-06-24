


CREATE TABLE cuws.primary_reference_type_code(
    primary_reference_type_code    varchar(10)     NOT NULL,
    description                    varchar(100)    NOT NULL,
    sort_order                     numeric(2, 0)   NOT NULL,
    effective_date                 date            NOT NULL,
    expiry_date                    date            NOT NULL,
    create_user                    varchar(64)     NOT NULL,
    create_date                    timestamp(0)    NOT NULL,
    update_user                    varchar(64)     NOT NULL,
    update_date                    timestamp(0)    NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.primary_reference_type_code.primary_reference_type_code IS 'Primary Reference Type Code is a code value that uniquely identifies a record.'
;
COMMENT ON COLUMN cuws.primary_reference_type_code.description IS 'Description is the long description associated with the code'
;
COMMENT ON COLUMN cuws.primary_reference_type_code.sort_order IS 'Sort Order is a number used to sort the primary reference type code in a specific order'
;
COMMENT ON COLUMN cuws.primary_reference_type_code.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.primary_reference_type_code.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.primary_reference_type_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.primary_reference_type_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.primary_reference_type_code.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.primary_reference_type_code.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.primary_reference_type_code IS 'The table defines options for referencing a legal land.'
;

ALTER TABLE cuws.primary_reference_type_code ADD 
    CONSTRAINT pk_prtc PRIMARY KEY (primary_reference_type_code) USING INDEX TABLESPACE pg_default 
;

-- Add codes now so a foreign-key can be created to legal_land.primary_reference_type_code.
-- SHORT and ADDRESS are deliberately set inactive until UI support is added as a future enhancement.
insert into primary_reference_type_code(primary_reference_type_code, description, sort_order, effective_date, expiry_date, create_user, create_date, update_user, update_date)
values 
('IDENTIFIER', 'Primary Identifier', 1, current_date, '9999-12-31', 'CUWS_03_00_00', now(), 'CUWS_03_00_00', now()),
('LEGAL', 'Legal Description', 2, current_date, '9999-12-31', 'CUWS_03_00_00', now(), 'CUWS_03_00_00', now()), 
('SHORT', 'Short Legal Description', 3, current_date, current_date, 'CUWS_03_00_00', now(), 'CUWS_03_00_00', now()),
('OTHER', 'Other Description', 4, current_date, '9999-12-31', 'CUWS_03_00_00', now(), 'CUWS_03_00_00', now()),
('ADDRESS', 'Address', 5, current_date, current_date, 'CUWS_03_00_00', now(), 'CUWS_03_00_00', now())
ON CONFLICT DO NOTHING
;
