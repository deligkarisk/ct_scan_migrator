# ctscan_migrator

## Summary
A Java application for migrating ct scans to the new standardized format. 
The app, does the below, among others:
* Validates the existence and availability of folders.
* Moves the scan folder to the new ctscan folder in bucket, with the new standardized name. The name of the old folder is irrelevant.
* Adds an entry into the database in the ctscan table.

## Examples

### Dummy run
_"java -DLOGGER_LABEL="2ndBatch" -jar CTScanMigrator.jar 2ndBatch.csv 2ndBatch"_  

The DLOGGER_LABEL is necessary to produce the correct logger file, and should be similar to the 2nd argument (here, is "2ndBatch").  
The argument "2ndBatch.csv" denotes the file that contains the data. More information on this you can find on the Wiki pages.
A dummy run will produce the output/validation files, but not do any migration or database edits.
Do this first, and if all checks out in your data, run the migration.


### Migration run
_"java -DLOGGER_LABEL="2ndBatch" -jar CTScanMigrator.jar 2ndBatch.csv 2ndBatch --do-migration"_  

In this case, add the --do-migration flag at the end. This will run the migration, in addition to running all validations.

## Recommendations
It is recommended you split your data into small batches, say 30-50 scans rather than migrating e.g. 100 scans at once.

## Config files
You need:
* config.properties
* credentials.properties  

See the WIKI pages for more information on these.




