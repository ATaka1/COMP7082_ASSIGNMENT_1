package com.example.libs;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private static Logger instance = new Logger();
    private File appDirectory;
    private File logDirectory;
    private File logfile;

    private Logger() {
        this.appDirectory = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.example.comp7082_assignment_1/files");
        this.logDirectory = new File(this.appDirectory + "/logs");
        this.logfile = new File(this.logDirectory + "/logcat_" + System.currentTimeMillis() + ".txt" );
        if(!this.appDirectory.exists()) this.appDirectory.mkdir();
        if(!this.logDirectory.exists()) this.logDirectory.mkdir();

        if(!logfile.exists()) {
            try {
                this.logfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger getInstance() {
        return instance;
    }

    public void appendLog(String text)
    {
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
