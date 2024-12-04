package com.example.LoggerTypes;

import java.io.File;

public abstract class GeneralLogger {
    public GeneralLogger Logger;
    public File Inputfile;
    public GeneralLogger (File Inputfile){
        this.Inputfile=Inputfile;
    }
    public void setHandlerNext(GeneralLogger Logger){
        this.Logger=Logger;
    }
    public void handleRequest(String request){};
    public void ExtractData(){};
    public void FormatJSONData(){};
    
}
