package dsa;

import java.util.HashMap;
import java.util.ArrayList;

public class HashMapProcessor {

    // Create keywords for each category
    public static HashMap<String, String[]> createCategoryKeywords() {

        HashMap<String, String[]> categories =
                new HashMap<>();

        categories.put("business",
                new String[]{
                        "company", "market", "bank",
                        "money", "business", "finance",
                        "profit", "sales", "economy",
                        "investment", "shares", "stock",
                        "price", "trade", "financial"
                });

        categories.put("entertainment",
                new String[]{
                        "film", "movie", "music",
                        "actor", "actress", "singer",
                        "album", "star", "award",
                        "cinema", "show", "celebrity",
                        "television", "tv", "song"
                });

        categories.put("politics",
                new String[]{
                        "government", "minister",
                        "election", "party", "president",
                        "political", "parliament",
                        "vote", "policy", "government",
                        "mp", "campaign", "leader",
                        "law", "senate"
                });

        categories.put("sport",
                new String[]{
                        "football", "cricket",
                        "player", "match", "team",
                        "game", "league", "coach",
                        "championship", "goal",
                        "cup", "tennis", "race",
                        "win", "winner"
                });

        categories.put("tech",
                new String[]{
                        "computer", "software",
                        "technology", "internet",
                        "digital", "mobile",
                        "website", "programming",
                        "phone", "device",
                        "online", "network",
                        "browser", "computer",
                        "data"
                });

        return categories;
    }


    // Calculate score for every category
    public static HashMap<String, Integer> calculateScores(
            ArrayList<String> words,
            HashMap<String, String[]> categories) {

        HashMap<String, Integer> scores =
                new HashMap<>();

        for (String category : categories.keySet()) {

            int score = 0;

            String[] keywords =
                    categories.get(category);

            for (String word : words) {

                for (String keyword : keywords) {

                    if (word.equals(keyword)) {
                        score++;
                    }
                }
            }

            scores.put(category, score);
        }

        return scores;
    }


    // Create document index
    // Key   = document ID
    // Value = document category
    public static HashMap<Integer, String>
            createDocumentIndex(
                    ArrayList<model.Document> documents) {

        HashMap<Integer, String> index =
                new HashMap<>();

        for (int i = 0; i < documents.size(); i++) {

            index.put(
                    i + 1,
                    documents.get(i).getActualCategory());
        }

        return index;
    }
}