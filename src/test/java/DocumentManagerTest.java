import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DocumentManagerTest {

    private DocumentManager documentManager;

    @BeforeEach
    public void setUp() {
        documentManager = new DocumentManager();
    }

    @Test
    public void testSaveDocument() {
        DocumentManager.Author author = DocumentManager.Author.builder()
                .id("author1")
                .name("Author One")
                .build();

        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Test Document")
                .content("Test content")
                .author(author)
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        assertNotNull(savedDocument.getId(), "Document ID should not be null after saving");
        assertEquals("Test Document", savedDocument.getTitle(), "Document title should be 'Test Document'");
        assertEquals("Test content", savedDocument.getContent(), "Document content should be 'Test content'");
        assertEquals("author1", savedDocument.getAuthor().getId(), "Author ID should be 'author1'");
    }

    @Test
    public void testSearchByTitlePrefixes() {
        createSampleDocuments();

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Arrays.asList("First", "Second"))
                .build();

        List<DocumentManager.Document> searchResults = documentManager.search(searchRequest);

        assertEquals(2, searchResults.size(), "There should be 2 documents matching the title prefixes");
    }

    @Test
    public void testSearchByContent() {
        createSampleDocuments();

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .containsContents(Arrays.asList("different"))
                .build();

        List<DocumentManager.Document> searchResults = documentManager.search(searchRequest);

        assertEquals(1, searchResults.size(), "There should be 1 document matching the content");
        assertEquals("Another Document", searchResults.get(0).getTitle(), "The title of the matching document should be 'Another Document'");
    }

    @Test
    public void testSearchByAuthorId() {
        createSampleDocuments();

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .authorIds(Arrays.asList("author1"))
                .build();

        List<DocumentManager.Document> searchResults = documentManager.search(searchRequest);

        assertEquals(2, searchResults.size(), "There should be 2 documents matching the author ID 'author1'");
    }

    @Test
    public void testFindById() {
        DocumentManager.Document document = createSampleDocuments().get(0);

        Optional<DocumentManager.Document> foundDocument = documentManager.findById(document.getId());

        assertTrue(foundDocument.isPresent(), "Document should be found by ID");
        assertEquals(document.getTitle(), foundDocument.get().getTitle(), "The title of the found document should match the original document");
    }

    private List<DocumentManager.Document> createSampleDocuments() {
        DocumentManager.Author author1 = DocumentManager.Author.builder()
                .id("author1")
                .name("Author One")
                .build();

        DocumentManager.Author author2 = DocumentManager.Author.builder()
                .id("author2")
                .name("Author Two")
                .build();

        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .title("First Document")
                .content("This is the content of the first document")
                .author(author1)
                .created(Instant.now())
                .build();

        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Second Document")
                .content("This is the content of the second document")
                .author(author2)
                .created(Instant.now())
                .build();

        DocumentManager.Document doc3 = DocumentManager.Document.builder()
                .title("Another Document")
                .content("This document contains different content")
                .author(author1)
                .created(Instant.now())
                .build();

        documentManager.save(doc1);
        documentManager.save(doc2);
        documentManager.save(doc3);

        return Arrays.asList(doc1, doc2, doc3);
    }
}
