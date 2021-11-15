package org.alabourd.honors_contract;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

public class FileInformation {

    private final Date date;
    private String fileName;
    private String absoluteFilePath;
    private String relativeFilePath;
    private long fileSize, lastModified;
    private final DecimalFormat format;

    /**
     * Constructor for FileInformation
     * 
     * @param fileName   name of the file
     * @param filePath   path of the file
     * @param fileSize   size of the file
     * @param lastOpened last time the file was opened
     */
    public FileInformation(String fileName, String filePath, long fileSize, long lastOpened) {
        this.fileName = fileName;
        this.absoluteFilePath = filePath;
        this.relativeFilePath = filePath;
        this.fileSize = fileSize;
        lastModified = lastOpened;
        date = new Date(lastModified);
        format = new DecimalFormat();
        format.applyLocalizedPattern("#,##0.00");
    }

    /**
     * Constructor for FileInformation
     * 
     * @param file File to get information from
     */
    public FileInformation(File file) {
        fileName = file.getName();
        absoluteFilePath = file.getAbsolutePath();
        relativeFilePath = file.getPath();
        fileSize = file.length();
        lastModified = file.lastModified();
        date = new Date(lastModified);
        format = new DecimalFormat();
        format.applyLocalizedPattern("#,##0.00");
    }

    /**
     * Get the name of the file
     * 
     * @return name of the file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the absolute path of the file
     * 
     * @return absolute path of the file
     */
    public String getAbsoluteFilePath() {
        return absoluteFilePath;
    }

    /**
     * Get the relative path of the file
     * 
     * @return relative path of the file
     */
    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    /**
     * Get the size of the file
     * 
     * @return size of the file
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * Get the size of the file in a formatted string
     * 
     * @return size of the file in a formatted string
     */
    public String getFileSizeAsString() {
        if (fileSize > 1e+9) {
            return format.format(fileSize * 1e-9) + " GB";
        } else {
            return format.format(fileSize * 1e-6) + " MB";
        }
    }

    /**
     * Get the last modified time of the file
     * 
     * @return last modified time of the file
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Get the last modified time of the file in a formatted string
     * 
     * @return last modified time of the file in a formatted string
     */
    public String getLastModifiedAsString() {
        return date.toString();
    }

    /**
     * Set the name of the file
     * 
     * @param fileName name of the file
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Set the absolute path of the file
     * 
     * @param absoluteFilePath absolute path of the file
     */
    public void setAbsoluteFilePathFilePath(String filePath) {
        this.absoluteFilePath = filePath;
    }

    /**
     * Set the size of the file
     * 
     * @param fileSize size of the file
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * Set the last modified time of the file
     * 
     * @param lastModified last modified time of the file
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
