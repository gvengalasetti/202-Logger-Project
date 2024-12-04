package com.example;

import java.io.File;

import com.example.LoggerTypes.APMLogger;
public class App 
{
    public static void main( String[] args )
    {
        File file = new File("/home/guna/SchoolProjects/202-Logger-Project/src/main/resources/SampleLog.txt");
        // ApplicationLogger logger3 = new ApplicationLogger();
        // RequestLogger logger2 = new RequestLogger(file);
        
        APMLogger logger = new APMLogger(file);
        logger.ExtractData();
        logger.FormatJSONData();
        
        
        
        
        
        // logger.setHandlerNext(logger2);

        // logger2.setHandlerNext(logger3);

//         TestingIdeas test = new TestingIdeas();
//         test.JSONBuilder();    }
// }
    }
}
