package org.alabourd.honors_contract.Comparator;

import org.alabourd.honors_contract.FileInformation;

import java.util.Comparator;

public class FileSizeComparator implements Comparator<FileInformation> {
    boolean isAscending;

    public FileSizeComparator(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public int compare(FileInformation o1, FileInformation o2) {
        long size;
        if (isAscending) {
            size = o2.getFileSize() - o1.getFileSize();
        } else {
            size = o1.getFileSize() - o2.getFileSize();
        }

        if (size > 0) {
            return 1;
        } else if (size < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
