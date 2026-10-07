import { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";

const API = "http://localhost:9000/api/documents";

function App() {
  const [active, setActive] = useState("dashboard");

  const [text, setText] = useState("");
  const [result, setResult] = useState(null);

  const [documents, setDocuments] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);

  const [keyword, setKeyword] = useState("");
  const [searchResults, setSearchResults] = useState([]);

  const [prefix, setPrefix] = useState("");
  const [trieResult, setTrieResult] = useState(null);

  const [id, setId] = useState("");
  const [binaryResult, setBinaryResult] = useState(null);

  const [ranking, setRanking] = useState(null);
  const [evaluation, setEvaluation] = useState(null);

  const [addResult, setAddResult] = useState(null);

  const documentsPerPage = 20;

  // =====================================================
  // LOAD DOCUMENTS
  // =====================================================

  const loadDocuments = async () => {
    try {
      const response = await axios.get(API);

      setDocuments(response.data.documents || []);
    } catch (error) {
      console.error("Unable to load documents:", error);
    }
  };

  // Load documents when application starts
  useEffect(() => {
    loadDocuments();
  }, []);

  // =====================================================
  // NAVIGATION
  // =====================================================

  const changePage = (page) => {
    setActive(page);

    if (page === "documents") {
      loadDocuments();
      setCurrentPage(1);
    }

    if (page === "evaluation") {
      getEvaluation();
    }
  };

  // =====================================================
  // CATEGORIZE DOCUMENT
  // =====================================================

  const categorize = async () => {
    if (!text.trim()) {
      setResult({
        success: false,
        message: "Please enter document text."
      });
      return;
    }

    try {
      const response = await axios.post(
        `${API}/categorize`,
        {
          text: text
        }
      );

      setResult(response.data);
    } catch (error) {
      console.error(error);

      setResult({
        success: false,
        message:
          "Unable to connect to Spring Boot backend."
      });
    }
  };

  // =====================================================
  // ADD NEW DOCUMENT
  // =====================================================

  const addDocument = async () => {
    if (!text.trim()) {
      setAddResult({
        success: false,
        message: "Please enter document text."
      });
      return;
    }

    try {
      const response = await axios.post(
        `${API}/add`,
        {
          text: text
        }
      );

      setAddResult(response.data);

      if (response.data.success) {
        setText("");

        await loadDocuments();

        setCurrentPage(
          Math.ceil(
            response.data.id / documentsPerPage
          )
        );
      }
    } catch (error) {
      console.error(error);

      setAddResult({
        success: false,
        message:
          "Unable to add document. Make sure Spring Boot is running."
      });
    }
  };

  // =====================================================
  // HASHMAP KEYWORD SEARCH
  // =====================================================

  const searchKeyword = async () => {
    if (!keyword.trim()) {
      return;
    }

    try {
      const response = await axios.get(
        `${API}/search?keyword=${encodeURIComponent(
          keyword
        )}`
      );

      setSearchResults(
        response.data.documents || []
      );
    } catch (error) {
      console.error(error);
      setSearchResults([]);
    }
  };

  // =====================================================
  // TRIE EXACT SEARCH
  // =====================================================

  const trieSearch = async () => {
    if (!keyword.trim()) {
      return;
    }

    try {
      const response = await axios.get(
        `${API}/trie/search?keyword=${encodeURIComponent(
          keyword
        )}`
      );

      setTrieResult(response.data);
    } catch (error) {
      console.error(error);
      setTrieResult(null);
    }
  };

  // =====================================================
  // TRIE PREFIX SEARCH
  // =====================================================

  const triePrefixSearch = async () => {
    if (!prefix.trim()) {
      return;
    }

    try {
      const response = await axios.get(
        `${API}/trie/prefix?prefix=${encodeURIComponent(
          prefix
        )}`
      );

      setTrieResult(response.data);
    } catch (error) {
      console.error(error);
      setTrieResult(null);
    }
  };

  // =====================================================
  // BINARY SEARCH
  // =====================================================

  const binarySearch = async () => {
    if (!id) {
      return;
    }

    try {
      const response = await axios.get(
        `${API}/binary-search?id=${id}`
      );

      setBinaryResult(response.data);
    } catch (error) {
      console.error(error);

      setBinaryResult({
        success: false,
        message: "Document not found."
      });
    }
  };

  // =====================================================
  // CATEGORY RANKING
  // =====================================================

  const getRanking = async () => {
    if (!text.trim()) {
      return;
    }

    try {
      const response = await axios.post(
        `${API}/ranking`,
        {
          text: text
        }
      );

      setRanking(response.data);
    } catch (error) {
      console.error(error);
      setRanking(null);
    }
  };

  // =====================================================
  // EVALUATION
  // =====================================================

  const getEvaluation = async () => {
    try {
      const response = await axios.get(
        `${API}/evaluation`
      );

      setEvaluation(
        response.data.evaluation
      );
    } catch (error) {
      console.error(error);
      setEvaluation(null);
    }
  };

  // =====================================================
  // PAGINATION
  // =====================================================

  const totalPages = Math.ceil(
    documents.length / documentsPerPage
  );

  const startIndex =
    (currentPage - 1) *
    documentsPerPage;

  const currentDocuments =
    documents.slice(
      startIndex,
      startIndex + documentsPerPage
    );

  const nextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const previousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  // =====================================================
  // PAGE
  // =====================================================

  return (
    <div className="app">

      {/* =================================================
          SIDEBAR
      ================================================= */}

      <aside className="sidebar">

        <div className="logo">
          <h1>DocuClass</h1>
          <p>Document Categorization</p>
        </div>

        <nav className="menu">

          <button
            type="button"
            className={
              active === "dashboard"
                ? "menu-button active"
                : "menu-button"
            }
            onClick={() =>
              changePage("dashboard")
            }
          >
            📊 Dashboard
          </button>

          <button
            type="button"
            className={
              active === "documents"
                ? "menu-button active"
                : "menu-button"
            }
            onClick={() =>
              changePage("documents")
            }
          >
            📚 Documents
          </button>

          <button
            type="button"
            className={
              active === "categorize"
                ? "menu-button active"
                : "menu-button"
            }
            onClick={() =>
              changePage("categorize")
            }
          >
            📝 Categorize
          </button>

          <button
            type="button"
            className={
              active === "add"
                ? "menu-button active"
                : "menu-button"
            }
            onClick={() =>
              changePage("add")
            }
          >
            ➕ Add Document
          </button>

          <button
            type="button"
            className={
              active === "search"
                ? "menu-button active"
                : "menu-button"
            }
            onClick={() =>
              changePage("search")
            }
          >
            🔎 Search
          </button>

          <button
            type="button"
            className={
              active === "ranking"
                ? "menu-button active"
                : "menu-button"
            }
            onClick={() =>
              changePage("ranking")
            }
          >
            📈 Category Ranking
          </button>

          <button
            type="button"
            className={
              active === "evaluation"
                ? "menu-button active"
                : "menu-button"
            }
            onClick={() =>
              changePage("evaluation")
            }
          >
            📊 Evaluation
          </button>

        </nav>

        <div className="sidebar-bottom">
          <strong>DSA Project</strong>
          <span>
            Java + Spring Boot + React
          </span>
        </div>

      </aside>

      {/* =================================================
          MAIN
      ================================================= */}

      <main className="main">

        <header className="header">

          <div>

            <h2>

              {active === "dashboard" &&
                "Dashboard"}

              {active === "documents" &&
                "Available Documents"}

              {active === "categorize" &&
                "Document Categorization"}

              {active === "add" &&
                "Add New Document"}

              {active === "search" &&
                "Document Search"}

              {active === "ranking" &&
                "Category Ranking"}

              {active === "evaluation" &&
                "Evaluation Results"}

            </h2>

            <p>
              Optimization Framework for
              Document Categorization
            </p>

          </div>

        </header>

        {/* =================================================
            DASHBOARD
        ================================================= */}

        {active === "dashboard" && (
          <section>

            <div className="hero">

              <div className="hero-content">

                <h1>
                  Document Categorization System
                </h1>

                <p>
                  Categorize documents using
                  keyword matching, HashMap,
                  Trie, Binary Search and
                  Merge Sort.
                </p>

                <button
                  type="button"
                  className="hero-button"
                  onClick={() =>
                    changePage("categorize")
                  }
                >
                  Start Categorizing →
                </button>

              </div>

              <div className="hero-icon">
                📚
              </div>

            </div>

            <div className="cards">

              <div className="card">
                <h3>📄 Documents</h3>

                <strong>
                  {documents.length}
                </strong>

                <p>
                  Currently available
                </p>
              </div>

              <div className="card">
                <h3>🎯 Accuracy</h3>

                <strong>
                  84.00%
                </strong>

                <p>
                  Dataset classification
                </p>
              </div>

              <div className="card">
                <h3>⚡ HashMap</h3>

                <strong>
                  29,457
                </strong>

                <p>
                  Indexed keywords
                </p>
              </div>

              <div className="card">
                <h3>🌳 Trie</h3>

                <strong>
                  Active
                </strong>

                <p>
                  Keyword & prefix search
                </p>
              </div>

            </div>

            <div className="info-section">

              <h3>
                Data Structures Used
              </h3>

              <div className="tags">

                <span>ArrayList</span>
                <span>HashSet</span>
                <span>HashMap</span>
                <span>Merge Sort</span>
                <span>Trie</span>
                <span>Binary Search</span>

              </div>

            </div>

          </section>
        )}

        {/* =================================================
            AVAILABLE DOCUMENTS
        ================================================= */}

        {active === "documents" && (
          <section className="panel">

            <div className="documents-header">

              <div>
                <h3>
                  Available Documents
                </h3>

                <p>
                  Browse all documents and
                  their assigned IDs.
                </p>
              </div>

              <div className="document-count">
                {documents.length} Documents
              </div>

            </div>

            <div className="table-wrapper">

              <table className="documents-table">

                <thead>

                  <tr>
                    <th>ID</th>
                    <th>Predicted Category</th>
                    <th>Actual Category</th>
                    <th>Document</th>
                  </tr>

                </thead>

                <tbody>

                  {currentDocuments.map(
                    (document, index) => {

                      const documentId =
                        startIndex + index + 1;

                      return (
                        <tr key={documentId}>

                          <td>
                            <strong className="document-id">
                              {documentId}
                            </strong>
                          </td>

                          <td>
                            <span
                              className={
                                "category-badge " +
                                document.predictedCategory
                              }
                            >
                              {
                                document.predictedCategory
                              }
                            </span>
                          </td>

                          <td>

                            {document.userAdded ? (
                              <span className="user-added">
                                User Added
                              </span>
                            ) : (
                              <span
                                className={
                                  "category-badge " +
                                  document.actualCategory
                                }
                              >
                                {
                                  document.actualCategory
                                }
                              </span>
                            )}

                          </td>

                          <td className="document-preview">
                            {document.news}
                          </td>

                        </tr>
                      );
                    }
                  )}

                </tbody>

              </table>

            </div>

            {documents.length > 0 && (
              <div className="pagination">

                <button
                  type="button"
                  className="pagination-button"
                  disabled={currentPage === 1}
                  onClick={previousPage}
                >
                  ← Previous
                </button>

                <span>
                  Showing{" "}
                  {startIndex + 1}
                  {" - "}
                  {Math.min(
                    startIndex +
                      documentsPerPage,
                    documents.length
                  )}
                  {" of "}
                  {documents.length}
                </span>

                <button
                  type="button"
                  className="pagination-button"
                  disabled={
                    currentPage === totalPages
                  }
                  onClick={nextPage}
                >
                  Next →
                </button>

              </div>
            )}

          </section>
        )}

        {/* =================================================
            CATEGORIZE
        ================================================= */}

        {active === "categorize" && (
          <section className="panel">

            <h3>
              Categorize a Document
            </h3>

            <p className="description">
              Enter document text to find
              its predicted category.
            </p>

            <textarea
              className="text-area"
              placeholder="Paste or type your document text here..."
              value={text}
              onChange={(event) =>
                setText(event.target.value)
              }
            />

            <button
              type="button"
              className="primary-button"
              onClick={categorize}
            >
              Categorize Document
            </button>

            {result && (
              <div
                className={
                  result.success
                    ? "result success-result"
                    : "result error-result"
                }
              >

                {result.predictedCategory && (
                  <>
                    <p>
                      Predicted Category
                    </p>

                    <h2>
                      {result.predictedCategory}
                    </h2>
                  </>
                )}

                {result.message && (
                  <p>
                    {result.message}
                  </p>
                )}

              </div>
            )}

          </section>
        )}

        {/* =================================================
            ADD DOCUMENT
        ================================================= */}

        {active === "add" && (
          <section className="panel">

            <div className="add-header">

              <div className="add-icon">
                ➕
              </div>

              <div>
                <h3>
                  Add a New Document
                </h3>

                <p className="description">
                  Enter a new document. The
                  system will automatically
                  categorize it and assign the
                  next available ID.
                </p>
              </div>

            </div>

            <textarea
              className="text-area"
              placeholder="Enter the new document text here..."
              value={text}
              onChange={(event) =>
                setText(event.target.value)
              }
            />

            <button
              type="button"
              className="primary-button"
              onClick={addDocument}
            >
              Add & Categorize Document
            </button>

            {addResult && (
              <div
                className={
                  addResult.success
                    ? "result success-result"
                    : "result error-result"
                }
              >

                {addResult.success ? (
                  <>
                    <p>
                      Document Added Successfully!
                    </p>

                    <h2>
                      Document ID:{" "}
                      {addResult.id}
                    </h2>

                    <div className="added-details">

                      <div>
                        <strong>
                          Predicted Category
                        </strong>

                        <span>
                          {
                            addResult.predictedCategory
                          }
                        </span>
                      </div>

                      <div>
                        <strong>
                          Status
                        </strong>

                        <span>
                          Available for search
                        </span>
                      </div>

                    </div>
                  </>
                ) : (
                  <p>
                    {addResult.message}
                  </p>
                )}

              </div>
            )}

          </section>
        )}

        {/* =================================================
            SEARCH
        ================================================= */}

        {active === "search" && (
          <section>

            {/* HASHMAP */}

            <div className="panel">

              <h3>
                HashMap Keyword Search
              </h3>

              <p className="description">
                Search documents using an indexed
                keyword.
              </p>

              <div className="row">

                <input
                  type="text"
                  placeholder="Enter keyword e.g. football"
                  value={keyword}
                  onChange={(event) =>
                    setKeyword(
                      event.target.value
                    )
                  }
                />

                <button
                  type="button"
                  className="primary-button"
                  onClick={searchKeyword}
                >
                  Search
                </button>

              </div>

              {searchResults.length > 0 && (
                <div className="results">

                  <h4>
                    {searchResults.length}
                    {" "}
                    documents found
                  </h4>

                  {searchResults
                    .slice(0, 10)
                    .map(
                      (document, index) => {

                        const foundId =
                          documents.findIndex(
                            (item) =>
                              item.news ===
                              document.news
                          ) + 1;

                        return (
                          <div
                            className="document"
                            key={index}
                          >

                            <div className="search-document-header">

                              <strong>
                                Document ID:{" "}
                                {foundId}
                              </strong>

                              <span className="category-badge">
                                {
                                  document.actualCategory
                                }
                              </span>

                            </div>

                            <p>
                              {document.news}
                            </p>

                          </div>
                        );
                      }
                    )}

                </div>
              )}

              {searchResults.length === 0 &&
                keyword.trim() !== "" && (
                  <p className="muted">
                    No documents found.
                  </p>
                )}

            </div>

            {/* TRIE EXACT */}

            <div className="panel">

              <h3>
                Trie Exact Search
              </h3>

              <div className="row">

                <input
                  type="text"
                  placeholder="Enter keyword e.g. computer"
                  value={keyword}
                  onChange={(event) =>
                    setKeyword(
                      event.target.value
                    )
                  }
                />

                <button
                  type="button"
                  className="secondary-button"
                  onClick={trieSearch}
                >
                  Trie Search
                </button>

              </div>

              {trieResult &&
                trieResult.keyword && (
                  <div className="small-result">

                    Keyword "
                    {trieResult.keyword}
                    ":

                    <strong>
                      {trieResult.found
                        ? " Found ✓"
                        : " Not Found ✗"}
                    </strong>

                  </div>
                )}

            </div>

            {/* TRIE PREFIX */}

            <div className="panel">

              <h3>
                Trie Prefix Search
              </h3>

              <div className="row">

                <input
                  type="text"
                  placeholder="Enter prefix e.g. comp"
                  value={prefix}
                  onChange={(event) =>
                    setPrefix(
                      event.target.value
                    )
                  }
                />

                <button
                  type="button"
                  className="secondary-button"
                  onClick={triePrefixSearch}
                >
                  Prefix Search
                </button>

              </div>

              {trieResult &&
                trieResult.prefix && (
                  <div className="small-result">

                    Prefix "
                    {trieResult.prefix}
                    ":

                    <strong>
                      {trieResult.found
                        ? " Exists ✓"
                        : " Not Found ✗"}
                    </strong>

                  </div>
                )}

            </div>

            {/* BINARY SEARCH */}

            <div className="panel">

              <h3>
                Binary Search by Document ID
              </h3>

              <p className="description">
                Use a Document ID from the
                Available Documents page.
              </p>

              <div className="row">

                <input
                  type="number"
                  placeholder="Enter document ID"
                  value={id}
                  onChange={(event) =>
                    setId(
                      event.target.value
                    )
                  }
                />

                <button
                  type="button"
                  className="secondary-button"
                  onClick={binarySearch}
                >
                  Binary Search
                </button>

              </div>

              {binaryResult && (
                <div className="document">

                  {binaryResult.success ? (
                    <>
                      <p>
                        <b>
                          Document ID:
                        </b>{" "}
                        {
                          binaryResult.documentId
                        }
                      </p>

                      <p>
                        <b>
                          Actual Category:
                        </b>{" "}
                        {
                          binaryResult.actualCategory
                        }
                      </p>

                      <p>
                        <b>
                          Predicted Category:
                        </b>{" "}
                        {
                          binaryResult.predictedCategory
                        }
                      </p>

                      <p>
                        {binaryResult.news}
                      </p>
                    </>
                  ) : (
                    <p>
                      {binaryResult.message}
                    </p>
                  )}

                </div>
              )}

            </div>

          </section>
        )}

        {/* =================================================
            RANKING
        ================================================= */}

        {active === "ranking" && (
          <section className="panel">

            <h3>
              Category Score Ranking
            </h3>

            <p className="description">
              See how the document scores
              against each category.
            </p>

            <textarea
              className="text-area"
              placeholder="Enter document text..."
              value={text}
              onChange={(event) =>
                setText(
                  event.target.value
                )
              }
            />

            <button
              type="button"
              className="primary-button"
              onClick={getRanking}
            >
              Calculate Ranking
            </button>

            {ranking && (
              <div className="ranking">

                <h3>
                  Predicted Category:
                  <span>
                    {" "}
                    {
                      ranking.predictedCategory
                    }
                  </span>
                </h3>

                {ranking.ranking.map(
                  (category, index) => (

                    <div
                      className="rank"
                      key={category}
                    >

                      <b>
                        #{index + 1}
                      </b>

                      <span>
                        {category}
                      </span>

                      <strong>
                        {
                          ranking.scores[
                            category
                          ]
                        }
                      </strong>

                    </div>

                  )
                )}

              </div>
            )}

          </section>
        )}

        {/* =================================================
            EVALUATION
        ================================================= */}

        {active === "evaluation" && (
          <section>

            {!evaluation && (
              <div className="panel">

                <button
                  type="button"
                  className="primary-button"
                  onClick={getEvaluation}
                >
                  Load Evaluation
                </button>

              </div>
            )}

            {evaluation && (
              <>

                <div className="cards">

                  <div className="card">
                    <h3>
                      Dataset Documents
                    </h3>

                    <strong>
                      {
                        evaluation.totalDocuments
                      }
                    </strong>
                  </div>

                  <div className="card">
                    <h3>
                      Correct
                    </h3>

                    <strong>
                      {
                        evaluation.correctPredictions
                      }
                    </strong>
                  </div>

                  <div className="card">
                    <h3>
                      Incorrect
                    </h3>

                    <strong>
                      {
                        evaluation.incorrectPredictions
                      }
                    </strong>
                  </div>

                  <div className="card">
                    <h3>
                      Accuracy
                    </h3>

                    <strong>
                      {evaluation.accuracy}%
                    </strong>
                  </div>

                </div>

                <div className="panel">

                  <h3>
                    Category-wise Accuracy
                  </h3>

                  <table>

                    <thead>

                      <tr>
                        <th>
                          Category
                        </th>

                        <th>
                          Actual
                        </th>

                        <th>
                          Correct
                        </th>

                        <th>
                          Incorrect
                        </th>

                        <th>
                          Accuracy
                        </th>
                      </tr>

                    </thead>

                    <tbody>

                      {Object.entries(
                        evaluation.categoryWise
                      ).map(
                        ([category, data]) => (

                          <tr key={category}>

                            <td>
                              {category}
                            </td>

                            <td>
                              {data.actual}
                            </td>

                            <td>
                              {data.correct}
                            </td>

                            <td>
                              {data.incorrect}
                            </td>

                            <td>
                              {data.accuracy}%
                            </td>

                          </tr>

                        )
                      )}

                    </tbody>

                  </table>

                </div>

              </>
            )}

          </section>
        )}

      </main>

    </div>
  );
}

export default App;