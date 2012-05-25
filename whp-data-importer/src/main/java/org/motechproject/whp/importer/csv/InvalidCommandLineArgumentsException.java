package org.motechproject.whp.importer.csv;

public class InvalidCommandLineArgumentsException extends Exception {
    InvalidCommandLineArgumentsException(){
        super("Invalid request! Arguments to be passed - \"{provider/patient},filename,logFileName");
    }
}
