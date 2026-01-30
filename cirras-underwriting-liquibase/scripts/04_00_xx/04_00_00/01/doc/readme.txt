Application:          CIRRAS Underwriting System
Repository:           
Version:              4.0.0-01
Author:               Vivid Solutions
 
Description
-------------------------------------------------------------------------------
Import Inventory Berries data.

Prerequisites
-------------------------------------------------------------------------------
The 04.00.00-00 scripts must have been run prior to running this script.

-------------------------------------------------------------------------------
1. Create Database objects.
-------------------------------------------------------------------------------

1.1 Change to the scripts directory. 

    cd ../

1.2 Connect to the target PostgreSQL host server as the app_cuws user for the 
    target environment using a psql terminal (run it one folder up of this readme)
	
1.3 Export data from CUWS 

    Run the following script in psql and attach the file to the ticket. 
	The output file is one folder up of this readme: data_export_import:

    \i cuws.04_00_00_01.export_berries_fields.sql


1.4 Create the database objects

	begin;
	\i cuws.04_00_00_01.ddl.sql
	commit;


1.5  Run the DML updates to delete previous Berries Inventory.

	begin;
	\i cuws.04_00_00_01.delete_berries_inv.dml.sql
	commit;

1.6  Run the DML updates to prepare the new Berries Inventory data for insert.
     IMPORTANT: This script performs two validation steps that require manual review.
     
     1. Review the csv files generated in the sql folder.
     2. Review the result of each validation query in cuws.04_00_00_01.prepare_berries_inv.dml.ENV.log, where ENV is NON_PROD or PROD. 
     If test_result=PASS for all, then validation has passed. 
     If test_result=FAIL for any, then validation has failed. Rollback the current transaction and review the error.

     This step is ENVIRONMENT SPECIFIC.
     
     If this is DLVR:

       The script will prompt for the contract number for a test BERRIES policy for 2026 that has been 
       created specifically for this upload. This will be used in place of actual contract numbers in the upload 
       that do not exist in DLVR.

	begin;
	\i cuws.04_00_00_01.prepare_berries_inv.dml.NON_PROD.sql
	commit;

     Else If this is TEST:

       The script will prompt for the contract number for a test BERRIES policy for 2026 that has been 
       created specifically for this upload. This will be used in place of actual contract numbers in the upload 
       that do not exist in TEST.
     
	begin;
	\i cuws.04_00_00_01.prepare_berries_inv.dml.NON_PROD.sql
	commit;
     
     Else If this is PROD:
     
	begin;
	\i cuws.04_00_00_01.prepare_berries_inv.dml.PROD.sql
	commit;     
     

1.7  Run the DML updates to insert the new Berries Inventory.

	begin;
	\i cuws.04_00_00_01.insert_berries_inv.dml.sql
	commit;


1.8 Drop database objects if previous steps were successful

	begin;
	\i cuws.04_00_00_01.drop.ddl.sql
	commit;


1.9 Release completed. Attach all log files to RFD-subtask ticket.


-------------------------------------------------------------------------------
2. NOTIFICATION
-------------------------------------------------------------------------------
2.1 Update RFD Sub-Tasks to mark release as completed.
