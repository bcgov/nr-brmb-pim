CREATE TABLE cuws.underwriting_comment(
    underwriting_comment_guid         varchar(32)      NOT NULL,
    inventory_field_guid              varchar(32),
    underwriting_comment_type_code    varchar(10)      NOT NULL,
    underwriting_comment              varchar(2000)    NOT NULL,
    create_user                       varchar(64)      NOT NULL,
    create_date                       timestamp(0)     NOT NULL,
    update_user                       varchar(64)      NOT NULL,
    update_date                       timestamp(0)     NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.underwriting_comment.underwriting_comment_guid IS 'Underwriting Comment GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.underwriting_comment.inventory_field_guid IS 'Inventory Field GUID links to a record in INVENTORY_FIELD table'
;
COMMENT ON COLUMN cuws.underwriting_comment.underwriting_comment_type_code IS 'Underwriting Comment Type Code is a unique record identifier for underwriting comment type records.'
;
COMMENT ON COLUMN cuws.underwriting_comment.underwriting_comment IS 'Underwriting Comment is a comment created by a representative regarding a particular aspect of the underwriting process.'
;
COMMENT ON COLUMN cuws.underwriting_comment.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.underwriting_comment.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.underwriting_comment.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.underwriting_comment.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.underwriting_comment IS 'The table contains comments for inventory and yield underwriting'
;

CREATE INDEX ix_uc_if ON cuws.underwriting_comment(inventory_field_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_uc_uctc ON cuws.underwriting_comment(underwriting_comment_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.underwriting_comment ADD 
    CONSTRAINT pk_uc PRIMARY KEY (underwriting_comment_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.underwriting_comment ADD CONSTRAINT fk_uc_if 
    FOREIGN KEY (inventory_field_guid)
    REFERENCES cuws.inventory_field(inventory_field_guid)
;

ALTER TABLE cuws.underwriting_comment ADD CONSTRAINT fk_uc_uctc 
    FOREIGN KEY (underwriting_comment_type_code)
    REFERENCES cuws.underwriting_comment_type_code(underwriting_comment_type_code)
;


