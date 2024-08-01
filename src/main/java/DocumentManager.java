import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


public class DocumentManager {

    private final Map<String, Document> storage = new HashMap<>();

    /**
     * Implementation of this method should upsert the document to your storage.
     * And generate unique id if it does not exist
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null) {
            document.setId(UUID.randomUUID().toString());
        }
        storage.put(document.getId(), document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        List<Document> results = new ArrayList<>();

        for (Document doc : storage.values()) {
            if (request.getTitlePrefixes() != null) {
                boolean matches = false;
                for (String prefix : request.getTitlePrefixes()) {
                    if (doc.getTitle().startsWith(prefix)) {
                        matches = true;
                        break;
                    }
                }
                if (!matches) continue;
            }

            if (request.getContainsContents() != null) {
                boolean matches = false;
                for (String content : request.getContainsContents()) {
                    if (doc.getContent().contains(content)) {
                        matches = true;
                        break;
                    }
                }
                if (!matches) continue;
            }

            if (request.getAuthorIds() != null) {
                if (!request.getAuthorIds().contains(doc.getAuthor().getId())) {
                    continue;
                }
            }

            if (request.getCreatedFrom() != null && doc.getCreated().isBefore(request.getCreatedFrom())) {
                continue;
            }

            if (request.getCreatedTo() != null && doc.getCreated().isAfter(request.getCreatedTo())) {
                continue;
            }

            results.add(doc);
        }

        return results;
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SearchRequest {
        List<String> titlePrefixes;
        List<String> containsContents;
        List<String> authorIds;
        Instant createdFrom;
        Instant createdTo;
    }

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Document {
        String id;
        String title;
        String content;
        Author author;
        Instant created;
    }

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Author {
        String id;
        String name;
    }
}
