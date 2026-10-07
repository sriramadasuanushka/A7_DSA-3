package processing;

import java.util.ArrayList;
import java.util.HashMap;

import dsa.HashMapProcessor;
import dsa.Sorting;

public class Categorizer {

    public static String categorize(String text) {

        ArrayList<String> words =
                TextProcessing.processText(text);

        HashMap<String, String[]> categories =
                HashMapProcessor.createCategoryKeywords();

        HashMap<String, Integer> scores =
                HashMapProcessor.calculateScores(
                        words,
                        categories);

        ArrayList<String> categoryList =
                new ArrayList<>(scores.keySet());

        ArrayList<String> rankedCategories =
                Sorting.mergeSort(
                        categoryList,
                        scores);

        String bestCategory =
                rankedCategories.get(0);

        return bestCategory;
    }


    public static HashMap<String, Integer> getCategoryScores(
            String text) {

        ArrayList<String> words =
                TextProcessing.processText(text);

        HashMap<String, String[]> categories =
                HashMapProcessor.createCategoryKeywords();

        return HashMapProcessor.calculateScores(
                words,
                categories);
    }
}