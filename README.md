\# Optimization Framework for Document Categorization



\## Overview



The Optimization Framework for Document Categorization is a web-based application designed to automatically classify news documents into predefined categories using Data Structures and Algorithms.



The system processes textual documents and categorizes them into:



\- Business

\- Entertainment

\- Politics

\- Sport

\- Technology



The project uses the BBC News dataset containing 2,225 documents and achieved an overall classification accuracy of 84.00%.



\## Objectives



\- Automatically categorize textual documents.

\- Apply Data Structures and Algorithms to document processing.

\- Provide efficient document searching and retrieval.

\- Compare different searching techniques.

\- Provide an interactive web interface for users.

\- Allow users to add and categorize new documents.



\## Data Structures and Algorithms Used



| Data Structure / Algorithm | Purpose |

|---|---|

| ArrayList | Store documents and processed words |

| HashSet | Remove duplicate words |

| HashMap | Keyword storage, category scoring and indexing |

| Merge Sort | Rank category scores |

| Trie | Exact keyword and prefix searching |

| Binary Search | Search documents by ID |



\## System Architecture



```text

BBC News Dataset

&#x20;      ↓

Text Preprocessing

&#x20;      ↓

HashSet

(Remove Duplicate Words)

&#x20;      ↓

HashMap

(Keyword Matching \& Scoring)

&#x20;      ↓

Merge Sort

(Category Ranking)

&#x20;      ↓

Predicted Category

&#x20;      ↓

Document Indexing \& Searching

&#x20;  ┌──────────┬──────────┬──────────────┐

&#x20;  ↓          ↓          ↓

&#x20;HashMap     Trie    Binary Search

&#x20;Keyword     Prefix      Document

&#x20;Search      Search        ID

&#x20;  └──────────┴──────────┴──────────────┘

&#x20;               ↓

&#x20;        Spring Boot Backend

&#x20;               ↓

&#x20;          React Frontend

&#x20;               ↓

&#x20;      Evaluation \& Results



\## Main Features



\- Document categorization

\- Add new documents

\- Keyword search

\- Trie exact and prefix search

\- Binary Search by document ID

\- Category score ranking

\- Evaluation and accuracy analysis

\- Interactive React web interface





