
CREATE TABLE cuws.application_release(
    application_version        varchar(15)      NOT NULL,
    revision_number            varchar(3)       NOT NULL,
    deployment_date            date             NOT NULL,
    release_description        varchar(2000)    NOT NULL,
    deployment_complete_ind    varchar(1)       NOT NULL,
    create_user                varchar(64)      NOT NULL,
    create_date                timestamp(0)     NOT NULL,
    update_user                varchar(64)      NOT NULL,
    update_date                timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.application_release.application_version IS 'Application Version is the version of the application that has been deployed to the environment.'
;
COMMENT ON COLUMN cuws.application_release.revision_number IS 'Revision Number is the revision of the application version that has been deployed to the environment.'
;
COMMENT ON COLUMN cuws.application_release.deployment_date IS 'Deployment Date is the date the application release was deployed to the environment.'
;
COMMENT ON COLUMN cuws.application_release.release_description IS 'Release Description is a short description of the contents of the release.'
;
COMMENT ON COLUMN cuws.application_release.deployment_complete_ind IS 'Deployment Complete Ind indicates whether the application release deployment was completed (Y) or not'
;
COMMENT ON COLUMN cuws.application_release.create_user IS 'Create User is the user id of the user that created the record.'
;
COMMENT ON COLUMN cuws.application_release.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.application_release.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.application_release.update_date IS 'Update Date is the date when the record was updated last'
;
COMMENT ON TABLE cuws.application_release IS 'Application Release table tracks release versions.'
;

CREATE INDEX ix_ar_av_rn ON cuws.application_release(application_version, revision_number)
 TABLESPACE pg_default
;
CREATE INDEX ix_ar_dd ON cuws.application_release(deployment_date)
 TABLESPACE pg_default
;
