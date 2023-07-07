package exercise.article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import exercise.worker.WorkerImpl;

public class WorkerImplTest {

    @Mock
    private Library library;
    private WorkerImpl worker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        worker = new WorkerImpl(library);
    }

    @Test
    public void addNewArticlesCallsLibraryStore() {
        List<Article> art = new ArrayList<>();
        art.add(new Article("Статья 1", "Содержимое 1", "Автор 1", LocalDate.of(2023, 7, 10)));
        art.add(new Article("Статья 2", "Содержимое 2", "Автор 2", LocalDate.of(2023, 7, 10)));
        worker.addNewArticles(art);
        verify(library).store(2023, art);
    }

    @Test
    public void getCatalogReturnsNonEmptyStringWhenLibraryHasNoArticles() {
        when(library.getAllTitles()).thenReturn(new ArrayList<>());
        String catalog = worker.getCatalog();
        assertFalse(catalog.isEmpty());
    }

    @Test
    public void getCatalogReturnsStringWithArticleTitlesWhenLibraryHasArticles() {
        List<String> titles = List.of("Статья 1", "Статья 2", "Статья 3");
        when(library.getAllTitles()).thenReturn(titles);
        String catalog = worker.getCatalog();
        for (String title : titles) {
            assertEquals(true, catalog.contains(title));
        }
    }

    @Test
    public void prepareArticlesRemovesArticlesWithoutTitle() {
        List<Article> art = new ArrayList<>();
        art.add(new Article(null, "Содержимое 1", "Автор 1", LocalDate.of(2023, 7, 10)));
        art.add(new Article("Статья 2", "Содержимое 2", "Автор 2", LocalDate.of(2023, 7, 10)));
        List<Article> preparedArticles = worker.prepareArticles(art);
        assertEquals(1, preparedArticles.size());
        assertEquals("Статья 2", preparedArticles.get(0).getTitle());
    }

    @Test
    public void prepareArticlesRemovesArticlesWithoutContent() {
        List<Article> art = new ArrayList<>();
        art.add(new Article("Статья 1", null, "Автор 1", LocalDate.of(2023, 7, 10)));
        art.add(new Article("Статья 2", "Содержимое 2", "Автор 2", LocalDate.of(2023, 7, 10)));
        List<Article> preparedArticles = worker.prepareArticles(art);
        assertEquals(1, preparedArticles.size());
        assertEquals("Статья 2", preparedArticles.get(0).getTitle());
    }

    @Test
    public void prepareArticlesSetsDefaultAuthor() {
        List<Article> art = new ArrayList<>();
        art.add(new Article("Статья 1", "Содержимое 1", "Автор 1", LocalDate.of(2023, 7, 10)));
        art.add(new Article("Статья 2", "Содержимое 2", "Автор 2", LocalDate.of(2023, 7, 10)));
        List<Article> preparedArticles = worker.prepareArticles(art);
        assertEquals(2, preparedArticles.size());
        assertEquals("Автор 1", preparedArticles.get(0).getAuthor());
        assertEquals("Автор 2", preparedArticles.get(1).getAuthor());
    }

    @Test
    public void prepareArticlesSetsDefaultCreationDateWhenNotSpecified() {
        List<Article> art = new ArrayList<>();
        art.add(new Article("Статья 1", "Содержимое 1", "Автор 1", null));
        art.add(new Article("Статья 2", "Содержимое 2", "Автор 2", LocalDate.of(2023, 7, 10)));
        List<Article> preparedArticles = worker.prepareArticles(art);
        assertEquals(2, preparedArticles.size());
        assertEquals(LocalDate.now(), preparedArticles.get(0).getCreationDate());
        assertEquals(LocalDate.of(2023, 7, 10), preparedArticles.get(1).getCreationDate());
    }
    @Test

    void checkSameArticle() {
        List<Article> art = new ArrayList<>();
        art.add(new Article("Книга 1", "Содержимое 1, ",
                "Автор 1", LocalDate.of(2023, 7, 10)));
        art.add(new Article("Книга 1", "Содержимое 1, ",
                "Автор 1", LocalDate.of(2023, 7, 10)));

        assertEquals(1, worker.prepareArticles(art).size());
    }
}