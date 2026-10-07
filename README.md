# Optimization Framework for Document Categorization

## Overview

The Optimization Framework for Document Categorization is a web-based application designed to automatically classify news documents into predefined categories using Data Structures and Algorithms.

The system processes textual documents and categorizes them into five categories:

- Business
- Entertainment
- Politics
- Sport
- Technology

The project uses the BBC News dataset containing 2,225 documents and achieved an overall classification accuracy of 84.00%.

## Objectives

- Automatically categorize textual documents.
- Apply Data Structures and Algorithms to document processing.
- Provide efficient document searching and retrieval.
- Compare different searching techniques.
- Provide an interactive web interface for users.
- Allow users to add and categorize new documents.

## Data Structures and Algorithms Used

| Data Structure / Algorithm | Purpose |
|---|---|
| ArrayList | Store documents and processed words |
| HashSet | Remove duplicate words |
| HashMap | Keyword storage, category scoring and indexing |
| Merge Sort | Rank category scores |
| Trie | Exact keyword and prefix searching |
| Binary Search | Search documents by ID |

## System Architecture

```text
                    BBC News Dataset
                           |
                           v
                  Text Preprocessing
                           |
                           v
                       HashSet
                Remove Duplicate Words
                           |
                           v
                       HashMap
              Keyword Matching & Scoring
                           |
                           v
                     Merge Sort
                  Category Ranking
                           |
                           v
                  Predicted Category
                           |
                           v
              Document Indexing & Searching
                    /       |       \
                   /        |        \
                  v         v         v
              HashMap      Trie    Binary Search
             Keyword      Prefix     Document ID
              Search      Search       Search
                   \        |        /
                    \       |       /
                           v
                  Spring Boot Backend
                           |
                           v
                    React Frontend
                           |
                           v
                  Evaluation & Results
