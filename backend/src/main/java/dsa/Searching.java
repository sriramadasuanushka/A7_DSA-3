package dsa;

import java.util.HashMap;

public class Searching {

    // =====================================
    // HASHMAP SEARCH
    // =====================================

    public static String searchById(
            HashMap<Integer, String> documentIndex,
            int documentId) {

        if (documentIndex.containsKey(documentId)) {

            return documentIndex.get(documentId);

        } else {

            return "Document not found";
        }
    }


    // =====================================
    // BINARY SEARCH
    // =====================================

    public static int binarySearch(
            int[] documentIds,
            int target) {

        int left = 0;
        int right = documentIds.length - 1;

        while (left <= right) {

            int middle =
                    left + (right - left) / 2;

            if (documentIds[middle] == target) {

                return middle;
            }

            if (documentIds[middle] < target) {

                left = middle + 1;

            } else {

                right = middle - 1;
            }
        }

        return -1;
    }
}