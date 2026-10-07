package dsa;

import java.util.ArrayList;
import java.util.HashMap;

import model.Document;
import processing.TextProcessing;

public class KeywordIndex {

    private HashMap<String, ArrayList<Integer>> index;


    // ============================================
    // CONSTRUCTOR
    // ============================================

    public KeywordIndex() {

        index = new HashMap<>();
    }


    // ============================================
    // BUILD INDEX
    // ============================================

    public void buildIndex(
            ArrayList<Document> documents) {

        index.clear();


        for (int i = 0;
             i < documents.size();
             i++) {

            Document document =
                    documents.get(i);


            ArrayList<String> words =
                    TextProcessing.processText(
                            document.getNews());


            for (String word : words) {

                if (!index.containsKey(word)) {

                    index.put(
                            word,
                            new ArrayList<Integer>());
                }


                index.get(word)
                        .add(i + 1);
            }
        }
    }


    // ============================================
    // SEARCH KEYWORD
    // ============================================

    public ArrayList<Integer> search(
            String keyword) {

        keyword =
                keyword.toLowerCase().trim();


        if (index.containsKey(keyword)) {

            return index.get(keyword);
        }


        return new ArrayList<Integer>();
    }


    // ============================================
    // CHECK KEYWORD
    // ============================================

    public boolean contains(
            String keyword) {

        return index.containsKey(
                keyword.toLowerCase().trim());
    }


    // ============================================
    // NUMBER OF UNIQUE KEYWORDS
    // ============================================

    public int size() {

        return index.size();
    }
}