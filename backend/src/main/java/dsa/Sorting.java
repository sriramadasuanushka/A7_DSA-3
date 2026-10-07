package dsa;

import java.util.ArrayList;
import java.util.HashMap;

public class Sorting {

    public static ArrayList<String> mergeSort(
            ArrayList<String> categories,
            HashMap<String, Integer> scores) {

        if (categories.size() <= 1) {
            return categories;
        }

        int middle = categories.size() / 2;

        ArrayList<String> left =
                new ArrayList<>(categories.subList(0, middle));

        ArrayList<String> right =
                new ArrayList<>(categories.subList(middle, categories.size()));

        left = mergeSort(left, scores);
        right = mergeSort(right, scores);

        return merge(left, right, scores);
    }

    private static ArrayList<String> merge(
            ArrayList<String> left,
            ArrayList<String> right,
            HashMap<String, Integer> scores) {

        ArrayList<String> result = new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {

            if (scores.get(left.get(i)) >= scores.get(right.get(j))) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }

        return result;
    }
}