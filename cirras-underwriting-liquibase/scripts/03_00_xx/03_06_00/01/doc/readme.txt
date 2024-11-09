Application:          CIRRAS Underwriting System
Repository:           
Version:              3.6.0-01
Author:               Vivid Solutions

Description
-------------------------------------------------------------------------------
Import Product data from CIRRAS into CUWS.

Prerequisites
-------------------------------------------------------------------------------
The 03.06.00-00 scripts must have been run prior to running this script.

-------------------------------------------------------------------------------
1. Create Database objects.
-------------------------------------------------------------------------------

1.1 Change to the scripts directory. 

    cd ../

1.2 Connect to the target PostgreSQL host server as the app_cuws user for the 
    target environment using a psql terminal (run it one folder up of this readme)
	

1.3 Import data from CIRRAS 

     Make sure the csv file was added to this folder in a previous step (Deploy CIRRAS 7.0.16-00 database scripts)
     One folder up of this readme: data_export_import

     If the csv file is there, run the following script in psql:

	\i cirras.cuws.03_06_00_01.import_cirras_data.dml.sql

    
1.4 Release completed.

-------------------------------------------------------------------------------
2. NOTIFICATION
-------------------------------------------------------------------------------
2.1 Update RFD Sub-Tasks to mark release as completed.      

