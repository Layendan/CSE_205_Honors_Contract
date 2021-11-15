package org.alabourd.honors_contract.Comparator;

import org.alabourd.honors_contract.FileInformation;

import java.util.Comparator;

public class FileNameComparator implements Comparator<FileInformation> {
    boolean isAscending;

    public FileNameComparator(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public int compare(FileInformation o1, FileInformation o2) {
        if (isAscending) {
            return o1.getFileName().compareTo(o2.getFileName());
        } else {
            return o2.getFileName().compareTo(o1.getFileName());
        }
    }
}
