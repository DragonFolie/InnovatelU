import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DocumentManagerRunner {
    public static void main(String[] args) {
        DocumentManager documentManager = new DocumentManager();

        // Create author
        DocumentManager.Author author1 = DocumentManager.Author.builder().id("author1").name("Author One").build();
        DocumentManager.Author author2 = DocumentManager.Author.builder().id("author2").name("Author Two").build();

        // Create document
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

        // Save
        documentManager.save(doc1);
        documentManager.save(doc2);
        documentManager.save(doc3);

        // Find by prefix
        DocumentManager.SearchRequest searchRequest1 = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Arrays.asList("First", "Second"))
                .build();
        List<DocumentManager.Document> searchResults1 = documentManager.search(searchRequest1);
        System.out.println("Search results for title prefixes 'First' and 'Second':");
        searchResults1.forEach(doc -> System.out.println(doc.getTitle()));

        // Find by content
        DocumentManager.SearchRequest searchRequest2 = DocumentManager.SearchRequest.builder()
                .containsContents(Arrays.asList("different"))
                .build();
        List<DocumentManager.Document> searchResults2 = documentManager.search(searchRequest2);
        System.out.println("Search results for content containing 'different':");
        searchResults2.forEach(doc -> System.out.println(doc.getTitle()));

        // Find by id author
        DocumentManager.SearchRequest searchRequest3 = DocumentManager.SearchRequest.builder()
                .authorIds(Arrays.asList("author1"))
                .build();
        List<DocumentManager.Document> searchResults3 = documentManager.search(searchRequest3);
        System.out.println("Search results for author ID 'author1':");
        searchResults3.forEach(doc -> System.out.println(doc.getTitle()));

        // Find by id
        Optional<DocumentManager.Document> foundDoc = documentManager.findById(doc1.getId());
        System.out.println("Found document by ID: " + (foundDoc.isPresent() ? foundDoc.get().getTitle() : "Not found"));
    }
}
