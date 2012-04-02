package cn.edu.shu.apps.qrreader.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileIO {
    String externalStoragePath;

    /**
     * Default external storage Path, /sdcard/
     */
    public FileIO() {
        this.externalStoragePath = Environment.getExternalStorageDirectory().
            getAbsolutePath() + File.separator;
    }
    
    /**
     * Set external storage directory to save files
     */
    public FileIO(String externalStorageDirectoryPath) {
        this.externalStoragePath = externalStorageDirectoryPath + File.separator;
    }

    /**
     * Read file into memory
     */
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    /**
     * Write data into a file from memory
     */
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }
}

