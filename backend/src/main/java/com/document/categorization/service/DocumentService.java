package com.document.categorization.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import dsa.KeywordIndex;
import dsa.Trie;
import model.Document;
import processing.Categorizer;

@Service
public class DocumentService {

    private final ArrayList<Document> documents =
            new ArrayList<>();

    private final KeywordIndex keywordIndex =
            new KeywordIndex();

    private final Trie trie =
            new Trie();

    // Number of documents originally loaded
    // from the CSV dataset.
    private int originalDatasetSize = 0;

    public DocumentService() {
        loadDataset();
    }

    // =========================================================
    // LOAD DATASET
    // =========================================================

    private void loadDataset() {

        try {

            ClassPathResource resource =
                    new ClassPathResource(
                            "dataset/dataset.csv");

            InputStream inputStream =
                    resource.getInputStream();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    inputStream,
                                    StandardCharsets.ISO_8859_1));

            // Skip CSV header
            reader.readLine();

            StringBuilder record =
                    new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                record.append(line);

                if (isCompleteCsvRecord(
                        record.toString())) {

                    processCsvRecord(
                            record.toString());

                    record.setLength(0);

                } else {

                    record.append("\n");
                }
            }

            // Handle remaining record
            if (record.length() > 0) {

                processCsvRecord(
                        record.toString());
            }

            reader.close();

            // Store original dataset size
            originalDatasetSize =
                    documents.size();

            System.out.println(
                    "Dataset loaded successfully: "
                            + documents.size()
                            + " documents");

            // Build HashMap keyword index
            keywordIndex.buildIndex(documents);

            System.out.println(
                    "Keyword index built successfully: "
                            + keywordIndex.size()
                            + " keywords");

            // Build Trie
            buildTrie();

            System.out.println(
                    "Trie built successfully");

        } catch (Exception e) {

            System.out.println(
                    "Error loading dataset: "
                            + e.getMessage());

            e.printStackTrace();
        }
    }

    // =========================================================
    // PROCESS CSV RECORD
    // =========================================================

    private void processCsvRecord(
            String record) {

        String[] fields =
                parseCsvRecord(record);

        if (fields.length >= 2) {

            String news =
                    fields[0].trim();

            String type =
                    fields[1].trim();

            if (!news.isEmpty()
                    && !type.isEmpty()) {

                Document document =
                        new Document(
                                news,
                                type);

                document.setPredictedCategory(
                        Categorizer.categorize(news));

                document.setUserAdded(false);

                documents.add(document);
            }
        }
    }

    // =========================================================
    // CHECK COMPLETE CSV RECORD
    // =========================================================

    private boolean isCompleteCsvRecord(
            String record) {

        boolean insideQuotes = false;

        for (int i = 0;
             i < record.length();
             i++) {

            char ch =
                    record.charAt(i);

            if (ch == '"') {

                if (insideQuotes
                        && i + 1 < record.length()
                        && record.charAt(i + 1) == '"') {

                    i++;

                } else {

                    insideQuotes =
                            !insideQuotes;
                }
            }
        }

        return !insideQuotes;
    }

    // =========================================================
    // PARSE CSV RECORD
    // =========================================================

    private String[] parseCsvRecord(
            String record) {

        ArrayList<String> fields =
                new ArrayList<>();

        StringBuilder current =
                new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0;
             i < record.length();
             i++) {

            char ch =
                    record.charAt(i);

            if (ch == '"') {

                if (insideQuotes
                        && i + 1 < record.length()
                        && record.charAt(i + 1) == '"') {

                    current.append('"');

                    i++;

                } else {

                    insideQuotes =
                            !insideQuotes;
                }

            } else if (ch == ','
                    && !insideQuotes) {

                fields.add(
                        current.toString());

                current.setLength(0);

            } else {

                current.append(ch);
            }
        }

        fields.add(
                current.toString());

        return fields.toArray(
                new String[0]);
    }

    // =========================================================
    // BUILD TRIE
    // =========================================================

    private void buildTrie() {

        for (Document document : documents) {

            String news =
                    document.getNews()
                            .toLowerCase();

            String cleanedText =
                    news.replaceAll(
                            "[^a-zA-Z0-9 ]",
                            " ");

            String[] words =
                    cleanedText.split("\\s+");

            for (String word : words) {

                if (!word.isEmpty()) {

                    trie.insert(word);
                }
            }
        }
    }

    // =========================================================
    // GET ALL DOCUMENTS
    // =========================================================

    public List<Document> getAllDocuments() {

        return documents;
    }

    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    public Document getDocumentById(int id) {

        if (id < 1
                || id > documents.size()) {

            return null;
        }

        return documents.get(id - 1);
    }

    // =========================================================
    // GET DOCUMENT COUNT
    // =========================================================

    public int getDocumentCount() {

        return documents.size();
    }

    // =========================================================
    // GET ORIGINAL DATASET COUNT
    // =========================================================

    public int getOriginalDatasetSize() {

        return originalDatasetSize;
    }

    // =========================================================
    // ADD NEW DOCUMENT
    // =========================================================

    public Document addDocument(
            String text) {

        if (text == null
                || text.trim().isEmpty()) {

            return null;
        }

        text = text.trim();

        // Categorize the new document
        String predictedCategory =
                Categorizer.categorize(text);

        // New user document does not have
        // a known actual category.
        Document document =
                new Document(
                        text,
                        "User Added");

        document.setPredictedCategory(
                predictedCategory);

        document.setUserAdded(true);

        // Add document to list.
        // Its ID becomes documents.size()
        documents.add(document);

        int documentId =
                documents.size();

        // Rebuild HashMap keyword index
        // so the new document can also
        // be found through keyword search.
        keywordIndex.buildIndex(documents);

        // Add new document words to Trie
        String cleanedText =
                text.toLowerCase()
                        .replaceAll(
                                "[^a-zA-Z0-9 ]",
                                " ");

        String[] words =
                cleanedText.split("\\s+");

        for (String word : words) {

            if (!word.isEmpty()) {

                trie.insert(word);
            }
        }

        System.out.println(
                "New document added. "
                        + "ID: "
                        + documentId
                        + ", Predicted Category: "
                        + predictedCategory);

        return document;
    }

    // =========================================================
    // GET CATEGORY STATISTICS
    // =========================================================

    public Map<String, Integer> getStatistics() {

        Map<String, Integer> statistics =
                new HashMap<>();

        for (Document document : documents) {

            String category =
                    document.getActualCategory();

            statistics.put(
                    category,
                    statistics.getOrDefault(
                            category,
                            0) + 1);
        }

        return statistics;
    }

    // =========================================================
    // HASHMAP KEYWORD SEARCH
    // =========================================================

    public List<Document> searchByKeyword(
            String keyword) {

        ArrayList<Integer> documentIds =
                keywordIndex.search(keyword);

        List<Document> results =
                new ArrayList<>();

        for (Integer id : documentIds) {

            Document document =
                    getDocumentById(id);

            if (document != null) {

                results.add(document);
            }
        }

        return results;
    }

    // =========================================================
    // TRIE EXACT SEARCH
    // =========================================================

    public boolean trieSearch(
            String keyword) {

        if (keyword == null
                || keyword.trim().isEmpty()) {

            return false;
        }

        return trie.search(
                keyword.toLowerCase().trim());
    }

    // =========================================================
    // TRIE PREFIX SEARCH
    // =========================================================

    public boolean triePrefixSearch(
            String prefix) {

        if (prefix == null
                || prefix.trim().isEmpty()) {

            return false;
        }

        return trie.startsWith(
                prefix.toLowerCase().trim());
    }

    // =========================================================
    // BINARY SEARCH
    // =========================================================

    public int binarySearchDocumentId(
            int target) {

        int[] documentIds =
                new int[documents.size()];

        for (int i = 0;
             i < documents.size();
             i++) {

            documentIds[i] = i + 1;
        }

        return dsa.Searching.binarySearch(
                documentIds,
                target);
    }

    // =========================================================
    // CATEGORY RANKING
    // =========================================================

    public Map<String, Object> getCategoryRanking(
            String text) {

        HashMap<String, Integer> scores =
                Categorizer.getCategoryScores(
                        text);

        ArrayList<String> categories =
                new ArrayList<>(
                        scores.keySet());

        ArrayList<String> rankedCategories =
                dsa.Sorting.mergeSort(
                        categories,
                        scores);

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "scores",
                scores);

        result.put(
                "ranking",
                rankedCategories);

        result.put(
                "predictedCategory",
                rankedCategories.get(0));

        return result;
    }

    // =========================================================
    // EVALUATION
    // =========================================================

    public Map<String, Object> getEvaluation() {

        int totalDocuments =
                originalDatasetSize;

        int correctPredictions = 0;

        Map<String, Integer> actualCounts =
                new LinkedHashMap<>();

        Map<String, Integer> correctCounts =
                new LinkedHashMap<>();

        String[] categories = {
            "business",
            "entertainment",
            "politics",
            "sport",
            "tech"
        };

        for (String category : categories) {

            actualCounts.put(
                    category,
                    0);

            correctCounts.put(
                    category,
                    0);
        }

        // Evaluate ONLY original dataset
        for (int i = 0;
             i < originalDatasetSize;
             i++) {

            Document document =
                    documents.get(i);

            String actual =
                    document.getActualCategory();

            String predicted =
                    document.getPredictedCategory();

            actualCounts.put(
                    actual,
                    actualCounts.getOrDefault(
                            actual,
                            0) + 1);

            if (actual.equals(predicted)) {

                correctPredictions++;

                correctCounts.put(
                        actual,
                        correctCounts.getOrDefault(
                                actual,
                                0) + 1);
            }
        }

        int incorrectPredictions =
                totalDocuments
                        - correctPredictions;

        double accuracy = 0.0;

        if (totalDocuments > 0) {

            accuracy =
                    (correctPredictions * 100.0)
                            / totalDocuments;
        }

        Map<String, Object> categoryAccuracy =
                new LinkedHashMap<>();

        for (String category : categories) {

            int actual =
                    actualCounts.get(category);

            int correct =
                    correctCounts.get(category);

            double categoryPercentage =
                    0.0;

            if (actual > 0) {

                categoryPercentage =
                        (correct * 100.0)
                                / actual;
            }

            Map<String, Object> categoryResult =
                    new LinkedHashMap<>();

            categoryResult.put(
                    "actual",
                    actual);

            categoryResult.put(
                    "correct",
                    correct);

            categoryResult.put(
                    "incorrect",
                    actual - correct);

            categoryResult.put(
                    "accuracy",
                    Math.round(
                            categoryPercentage * 100.0)
                            / 100.0);

            categoryAccuracy.put(
                    category,
                    categoryResult);
        }

        Map<String, Object> evaluation =
                new LinkedHashMap<>();

        evaluation.put(
                "totalDocuments",
                totalDocuments);

        evaluation.put(
                "correctPredictions",
                correctPredictions);

        evaluation.put(
                "incorrectPredictions",
                incorrectPredictions);

        evaluation.put(
                "accuracy",
                Math.round(
                        accuracy * 100.0)
                        / 100.0);

        evaluation.put(
                "categoryWise",
                categoryAccuracy);

        return evaluation;
    }
}