# ctscan_migrator
Author: Kosmas Deligkaris

## Summary
A Java application for migrating ct scans to the new standardized format. 
The app does the below:
* Validates the existence and availability of folders.
* Moves the scan folder to the new ctscan folder in bucket, with the new standardized name. The name of the old folder is irrelevant.
* Adds an entry into the database in the ctscan table.

## Examples

### Dummy run
_"java -DLOGGER_LABEL="2ndBatch" -jar CTScanMigrator.jar 2ndBatch.csv 2ndBatch"_  

A dummy run will produce the output/validation files, but not do any migration or database edits.

The DLOGGER_LABEL is necessary to produce the correct logger file, and should be similar to the 2nd argument (here, it is "2ndBatch").  
The argument "2ndBatch.csv" denotes the file that contains the data. More information on this you can find on the Wiki pages.

Once the program is run, two files will be created: a log file, and a csv file. The log file contains information on the program's dummy execution, while
the csv file should contain exactly the same data as the input. This csv file can be used to check that the program has indeed read the values correctly 
(though this should not happen and was not observed during testing, it is just an additional debug aid).

If the dummy run finds errors, an errors.log file will be created detailing for each scan what needs to be fixed. Fix all of them, then run the same command again.
If the dummy run does not find errors, go on with running the migration as shown in the next section.


### Migration run
_"java -DLOGGER_LABEL="2ndBatch" -jar CTScanMigrator.jar 2ndBatch.csv 2ndBatch --do-migration"_  

In this case, add the --do-migration flag at the end. This will run the migration, in addition to running all validations.

## Recommendations
It is recommended you split your data into small batches, say 30-50 scans rather than migrating e.g. 100 scans at once.

## Config files
You need:
* config.properties
* credentials.properties  

These need to be in the same folder as the jar file. 

See the WIKI pages for more information on these.


## Technical note: validations
There are three validation groups containing various validation methods (see the validator package for details).
Below, you can see, in summary, what each validator group is doing.

### Basic field validator group (first validation)
This group of validators check that the specimen codes exist in the database,, that wet/dry combination is correct,
that folder location exists, etc...

### Standardized folders validator group (second validation)
This group of validators checks that the standardized folder found by the software is available in the database and in bucket.

### All folders unique validator group (third validation)
This group of validators checks that all standardized folders in the collection are unique.