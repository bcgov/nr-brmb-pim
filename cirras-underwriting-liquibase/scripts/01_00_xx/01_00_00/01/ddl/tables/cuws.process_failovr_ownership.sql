-- 
-- TABLE: process_failovr_ownership
--

CREATE TABLE cuws.process_failovr_ownership(
    process_failovr_ownership_guid    varchar(32)       DEFAULT gen_random_uuid() NOT NULL,
    process_name                      varchar(64)       NOT NULL,
    service_node                      varchar(64)       NOT NULL,
    revision_count                    numeric(10, 0)    DEFAULT 0 NOT NULL,
	expiry_timestamp 				  timestamp(6) 		with time zone NOT NULL,
    create_user                       varchar(64)       NOT NULL,
    create_date                       timestamp         NOT NULL,
    update_user                       varchar(64)       NOT NULL,
    update_date                       timestamp         NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.process_failovr_ownership.process_failovr_ownership_guid IS 'PROCESS_FAILOVR_OWNERSHIP_GUID is a unique identifier for the record.'
;
COMMENT ON COLUMN cuws.process_failovr_ownership.process_name IS 'Process Name is the name of the process that requires failover support.'
;
COMMENT ON COLUMN cuws.process_failovr_ownership.service_node IS 'Service Node is the name of the current active service node.'
;
COMMENT ON COLUMN cuws.process_failovr_ownership.revision_count IS 'REVISION_COUNT is the number of times that the row of data has been changed. The column is used for optimistic locking via application code.'''
;
COMMENT ON COLUMN cuws.process_failovr_ownership.expiry_timestamp IS 'Expiry Timestamp is when the current active service node expires and either the current service node renews the expiry timestamp, or another service node is promoted as the current service node and a new expiry timestamp is issued.'
;
COMMENT ON COLUMN cuws.process_failovr_ownership.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.process_failovr_ownership.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.process_failovr_ownership.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.process_failovr_ownership.update_date IS 'Update Date is the date when the record was updated last.'
;

COMMENT ON TABLE cuws.process_failovr_ownership IS 'Dispatch Synchronization Ownership is used to track the active node that will process synchronization jobs between Claims Calculator and CIRRAS. Redundant processing nodes are set up to process synchronization jobs. Only one process node is active at any time, so a single process node will be declared by this table as being the active node for processing jobs. If the active node goes down for some reason, then it will not be able to extend the expiry date in the table to show the node still has ownership of processing jobs. If node ownerships expires, another node will be promoted as the new active node for processing synchronization jobs and will be assigned a new expiry date.'
;

ALTER TABLE cuws.process_failovr_ownership ADD 
    CONSTRAINT pk_flv PRIMARY KEY (process_failovr_ownership_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.process_failovr_ownership ADD 
    CONSTRAINT uk_flv UNIQUE (process_name) USING INDEX TABLESPACE pg_default
;
