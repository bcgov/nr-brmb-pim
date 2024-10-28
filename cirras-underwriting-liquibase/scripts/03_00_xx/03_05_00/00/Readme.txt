--The database needs to be renamed.

DEV:	pituwdev
QA:		pituwqa
DLVR:	pituwdlvr
TEST:	pituwtest
PROD:	pituwprod

--Before running the rename statement, shutdown these apis:
	- UW API
	- Jasper API

--Rename sql has to be run: ALTER DATABASE old_database_name RENAME TO new_database_name;

DEV:	ALTER DATABASE cirrasdev RENAME TO pituwdev;
QA:		ALTER DATABASE cirrasqa RENAME TO pituwqa;
DLVR:	ALTER DATABASE cirrasdlvr RENAME TO pituwdlvr;
TEST:	ALTER DATABASE cirrastest RENAME TO pituwtest;
PROD:	ALTER DATABASE cirrasprod RENAME TO pituwprod;


Possible Error if there are sessions using the database:
	There are X other sessions using the database.database "cirras_dev" is being accessed by other users 
	ERROR:  database "cirrasdev" is being accessed by other users
	SQL state: 55006
	
Solution: SELECT pg_terminate_backend( pid ) FROM pg_stat_activity WHERE pid <> pg_backend_pid( ) AND datname = 'cirrasdev'; //WHERE cirrasdev is the old database name
