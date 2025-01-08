CREATE TABLE cuws.user_setting(
    underwriting_user_setting_guid     varchar(32)      NOT NULL,
    login_user_guid                    varchar(32)      NOT NULL,
    login_user_id                      varchar(64)      NOT NULL,
    login_user_type                    varchar(64)      NOT NULL,
    given_name                         varchar(50),
    family_name                        varchar(50),
    policy_search_crop_year            numeric(4, 0),
    policy_search_insurance_plan_id    numeric(9, 0),
    policy_search_office_id            numeric(9, 0),
    create_user                        varchar(64)      NOT NULL,
    create_date                        timestamp(0)     NOT NULL,
    update_user                        varchar(64)      NOT NULL,
    update_date                        timestamp(0)     NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.user_setting.underwriting_user_setting_guid IS 'Underwriting User Guid is a unique key of an underwriting user '
;
COMMENT ON COLUMN cuws.user_setting.login_user_guid IS 'Login User Guid is a unique key from the authentication for a user.'
;
COMMENT ON COLUMN cuws.user_setting.login_user_id IS 'Login User Id is the user id from the authentication for a user '
;
COMMENT ON COLUMN cuws.user_setting.login_user_type IS 'Login User Type is the user type from the authentication for a user '
;
COMMENT ON COLUMN cuws.user_setting.given_name IS 'Given Name is the given name from the authentication for a user '
;
COMMENT ON COLUMN cuws.user_setting.family_name IS 'Family Name is the family name from the authentication for a user '
;
COMMENT ON COLUMN cuws.user_setting.policy_search_crop_year IS 'Policy Search Crop Year is the user''s preferred crop year to search for on the policy screen'
;
COMMENT ON COLUMN cuws.user_setting.policy_search_insurance_plan_id IS 'Policy Search Insurance Plan Id is the user''s  preferred insurance plan to search for on the policy screen.'
;
COMMENT ON COLUMN cuws.user_setting.policy_search_office_id IS 'Policy Search Office Id is the user''s preferred office to search for on the policy screen.'
;
COMMENT ON COLUMN cuws.user_setting.create_user IS 'Create User is the user id of the user that created the record '
;
COMMENT ON COLUMN cuws.user_setting.create_date IS 'Create Date is the date when the record was created '
;
COMMENT ON COLUMN cuws.user_setting.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.user_setting.update_date IS 'Update Date is the date when the record was updated last '
;
COMMENT ON TABLE cuws.user_setting IS 'The table contains user''s preferred settings for search '
;

CREATE INDEX ix_us_lug ON cuws.user_setting(login_user_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_us_ip ON cuws.user_setting(policy_search_insurance_plan_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_us_of ON cuws.user_setting(policy_search_office_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.user_setting ADD 
    CONSTRAINT pk_us PRIMARY KEY (underwriting_user_setting_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.user_setting ADD 
    CONSTRAINT uk_us UNIQUE (login_user_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.user_setting ADD CONSTRAINT fk_us_ip 
    FOREIGN KEY (policy_search_insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;

ALTER TABLE cuws.user_setting ADD CONSTRAINT fk_us_of 
    FOREIGN KEY (policy_search_office_id)
    REFERENCES cuws.office(office_id)
;
