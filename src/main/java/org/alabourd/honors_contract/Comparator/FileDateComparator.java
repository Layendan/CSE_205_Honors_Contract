package org.alabourd.honors_contract.Comparator;

import org.alabourd.honors_contract.FileInformation;

import java.util.Comparator;

public class FileDateComparator implements Comparator<FileInformation> {
    boolean isAscending;

    public FileDateComparator(boolean isAscending) {
        this.isAscending = isAscending;
    }
    @Override
    public int compare(FileInformation o1, FileInformation o2) {
        long comparison = o2.getLastModified() - o1.getLastModified();
        byte answer;
        if (comparison > 0)
            answer = -1;
        else if (comparison < 0)
            answer = 1;
        else
            answer = 0;

        if (!isAscending)
            answer *= -1;

        return  answer;
    }
}
