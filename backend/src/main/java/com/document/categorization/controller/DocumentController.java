package com.document.categorization.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.document.categorization.service.DocumentService;

import model.Document;
import processing.Categorizer;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(
            DocumentService documentService) {

        this.documentService =
                documentService;
    }

    // =========================================================
    // CATEGORIZE DOCUMENT
    // =========================================================

    @PostMapping("/categorize")
    public Map<String, Object> categorize(
            @RequestBody Map<String, String> request) {

        String text =
                request.get("text");

        Map<String, Object> response =
                new HashMap<>();

        if (text == null
                || text.trim().isEmpty()) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Document text cannot be empty");

            return response;
        }

        String predictedCategory =
                Categorizer.categorize(text);

        response.put(
                "success",
                true);

        response.put(
                "predictedCategory",
                predictedCategory);

        return response;
    }

    // =========================================================
    // ADD NEW DOCUMENT
    // =========================================================

    @PostMapping("/add")
    public Map<String, Object> addDocument(
            @RequestBody Map<String, String> request) {

        Map<String, Object> response =
                new HashMap<>();

        String text =
                request.get("text");

        if (text == null
                || text.trim().isEmpty()) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Document text cannot be empty");

            return response;
        }

        Document document =
                documentService.addDocument(text);

        if (document == null) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Unable to add document");

            return response;
        }

        int documentId =
                documentService.getDocumentCount();

        response.put(
                "success",
                true);

        response.put(
                "message",
                "Document added successfully");

        response.put(
                "id",
                documentId);

        response.put(
                "news",
                document.getNews());

        response.put(
                "predictedCategory",
                document.getPredictedCategory());

        response.put(
                "actualCategory",
                document.getActualCategory());

        response.put(
                "userAdded",
                document.isUserAdded());

        return response;
    }

    // =========================================================
    // GET ALL DOCUMENTS
    // =========================================================

    @GetMapping
    public Map<String, Object> getAllDocuments() {

        Map<String, Object> response =
                new HashMap<>();

        List<Document> documents =
                documentService.getAllDocuments();

        response.put(
                "success",
                true);

        response.put(
                "totalDocuments",
                documents.size());

        response.put(
                "originalDatasetSize",
                documentService
                        .getOriginalDatasetSize());

        response.put(
                "documents",
                documents);

        return response;
    }

    // =========================================================
    // GET DOCUMENT COUNT
    // =========================================================

    @GetMapping("/count")
    public Map<String, Object> getCount() {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "success",
                true);

        response.put(
                "totalDocuments",
                documentService
                        .getDocumentCount());

        response.put(
                "originalDatasetSize",
                documentService
                        .getOriginalDatasetSize());

        return response;
    }

    // =========================================================
    // GET CATEGORY STATISTICS
    // =========================================================

    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "success",
                true);

        response.put(
                "categories",
                documentService
                        .getStatistics());

        return response;
    }

    // =========================================================
    // HASHMAP KEYWORD SEARCH
    // =========================================================

    @GetMapping("/search")
    public Map<String, Object> searchByKeyword(
            @RequestParam String keyword) {

        Map<String, Object> response =
                new HashMap<>();

        if (keyword == null
                || keyword.trim().isEmpty()) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Keyword cannot be empty");

            return response;
        }

        List<Document> results =
                documentService
                        .searchByKeyword(keyword);

        response.put(
                "success",
                true);

        response.put(
                "keyword",
                keyword);

        response.put(
                "totalResults",
                results.size());

        response.put(
                "documents",
                results);

        return response;
    }

    // =========================================================
    // TRIE EXACT SEARCH
    // =========================================================

    @GetMapping("/trie/search")
    public Map<String, Object> trieSearch(
            @RequestParam String keyword) {

        Map<String, Object> response =
                new HashMap<>();

        if (keyword == null
                || keyword.trim().isEmpty()) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Keyword cannot be empty");

            return response;
        }

        boolean found =
                documentService
                        .trieSearch(keyword);

        response.put(
                "success",
                true);

        response.put(
                "keyword",
                keyword);

        response.put(
                "found",
                found);

        return response;
    }

    // =========================================================
    // TRIE PREFIX SEARCH
    // =========================================================

    @GetMapping("/trie/prefix")
    public Map<String, Object> triePrefixSearch(
            @RequestParam String prefix) {

        Map<String, Object> response =
                new HashMap<>();

        if (prefix == null
                || prefix.trim().isEmpty()) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Prefix cannot be empty");

            return response;
        }

        boolean found =
                documentService
                        .triePrefixSearch(prefix);

        response.put(
                "success",
                true);

        response.put(
                "prefix",
                prefix);

        response.put(
                "found",
                found);

        return response;
    }

    // =========================================================
    // BINARY SEARCH
    // =========================================================

    @GetMapping("/binary-search")
    public Map<String, Object> binarySearch(
            @RequestParam int id) {

        Map<String, Object> response =
                new HashMap<>();

        int index =
                documentService
                        .binarySearchDocumentId(id);

        if (index == -1) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Document not found");

            return response;
        }

        Document document =
                documentService
                        .getDocumentById(id);

        response.put(
                "success",
                true);

        response.put(
                "searchMethod",
                "Binary Search");

        response.put(
                "documentId",
                id);

        response.put(
                "index",
                index);

        response.put(
                "news",
                document.getNews());

        response.put(
                "actualCategory",
                document.getActualCategory());

        response.put(
                "predictedCategory",
                document.getPredictedCategory());

        response.put(
                "userAdded",
                document.isUserAdded());

        return response;
    }

    // =========================================================
    // CATEGORY RANKING
    // =========================================================

    @PostMapping("/ranking")
    public Map<String, Object> getCategoryRanking(
            @RequestBody Map<String, String> request) {

        Map<String, Object> response =
                new HashMap<>();

        String text =
                request.get("text");

        if (text == null
                || text.trim().isEmpty()) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Document text cannot be empty");

            return response;
        }

        Map<String, Object> ranking =
                documentService
                        .getCategoryRanking(text);

        response.put(
                "success",
                true);

        response.put(
                "scores",
                ranking.get("scores"));

        response.put(
                "ranking",
                ranking.get("ranking"));

        response.put(
                "predictedCategory",
                ranking.get(
                        "predictedCategory"));

        return response;
    }

    // =========================================================
    // EVALUATION
    // =========================================================

    @GetMapping("/evaluation")
    public Map<String, Object> getEvaluation() {

        Map<String, Object> response =
                new HashMap<>();

        Map<String, Object> evaluation =
                documentService
                        .getEvaluation();

        response.put(
                "success",
                true);

        response.put(
                "evaluation",
                evaluation);

        return response;
    }

    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public Map<String, Object> getDocument(
            @PathVariable int id) {

        Map<String, Object> response =
                new HashMap<>();

        Document document =
                documentService
                        .getDocumentById(id);

        if (document == null) {

            response.put(
                    "success",
                    false);

            response.put(
                    "message",
                    "Document not found");

            return response;
        }

        response.put(
                "success",
                true);

        response.put(
                "id",
                id);

        response.put(
                "news",
                document.getNews());

        response.put(
                "actualCategory",
                document.getActualCategory());

        response.put(
                "predictedCategory",
                document.getPredictedCategory());

        response.put(
                "userAdded",
                document.isUserAdded());

        return response;
    }
}