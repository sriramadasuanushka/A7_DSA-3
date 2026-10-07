package processing;

import java.util.ArrayList;
import java.util.HashSet;

public class TextProcessing {

    public static ArrayList<String> processText(String text) {

        text = text.toLowerCase();

        text = text.replaceAll("[^a-zA-Z0-9 ]", " ");

        String[] words = text.split("\\s+");

        ArrayList<String> wordList = new ArrayList<>();

        for (String word : words) {
            if (!word.isEmpty()) {
                wordList.add(word);
            }
        }

        HashSet<String> uniqueWords = new HashSet<>(wordList);

        return new ArrayList<>(uniqueWords);
    }
}