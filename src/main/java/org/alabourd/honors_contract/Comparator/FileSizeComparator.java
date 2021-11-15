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
        int size;
        if (isAscending) {
            size = (int) (o2.getFileSize() - o1.getFileSize());
        } else {
            size = (int) (o1.getFileSize() - o2.getFileSize());
        }

        return Integer.compare(size, 0);
    }
}
