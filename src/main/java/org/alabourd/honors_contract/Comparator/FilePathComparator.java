package org.alabourd.honors_contract.Comparator;

import org.alabourd.honors_contract.FileInformation;

import java.util.Comparator;

public class FilePathComparator implements Comparator<FileInformation> {
    boolean isAscending;

    public FilePathComparator(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public int compare(FileInformation o1, FileInformation o2) {
        if (isAscending) {
            return o1.getRelativeFilePath().compareTo(o2.getRelativeFilePath());
        } else {
            return o2.getRelativeFilePath().compareTo(o1.getRelativeFilePath());
        }
    }
}