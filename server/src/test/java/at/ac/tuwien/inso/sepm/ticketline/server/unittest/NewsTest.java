package at.ac.tuwien.inso.sepm.ticketline.server.unittest;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.NewsImage;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ServerServiceValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleNewsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class NewsTest {

    private static final long NEWS_ID1 = 1L;
    private static final long NEWS_ID2 = 2L;
    private static final String NEWS_TITLE1 = "Headline1";
    private static final String NEWS_TITLE2 = "Headline2";
    private static final String NEWS_TEXT = "This is a very long text containing all the contents of the news" +
        " and a lot of other more or less useful information.";
    private static final String NEWS_SUMMARY = "This is a very long text containing all the conten...";
    private static final LocalDateTime NEWS_PUBLISHED_AT =
        LocalDateTime.of(2016, 1, 1, 12, 0, 0, 0);


    @MockBean
    private NewsRepository newsRepository;

    //@Autowired
    private NewsService newsService;

    private List<News> newsList;
    private News news1, news2;
    private List<NewsImage> newsImageList;
    private Map<Long, News> newsMap;

    @Before
    public void setUp(){
        newsService = new SimpleNewsService(newsRepository);
        newsList = new ArrayList<>();
        newsImageList = new ArrayList<>();
        newsMap = new HashMap<>();

        for (long i = newsImageList.size(); i < 4; i++) {
            newsImageList.add(NewsImage.builder().id(i).build());
        }

        news1 = News.builder()
            .id(NEWS_ID1)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title(NEWS_TITLE1)
            .text(NEWS_TEXT)
            .images(newsImageList)
            .build();

        news2 = News.builder()
            .id(NEWS_ID2)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title(NEWS_TITLE2)
            .text(NEWS_TEXT)
            .images(newsImageList)
            .build();

        newsList.add(news1);
        newsList.add(news2);

        newsMap.put(NEWS_ID1, news1);
        newsMap.put(NEWS_ID2, news2);

        BDDMockito.doAnswer(inv -> {
            News saveNews = inv.getArgument(0);
            if (saveNews.getId() == null){
                saveNews.setId(1L + newsMap.size());
            }
            newsMap.put(saveNews.getId(), saveNews);
            return saveNews;
        }).when(newsRepository).save(any(News.class));
    }

    @Test
    public void findAllNews(){
        BDDMockito.
            given(newsRepository.findAllByOrderByPublishedAtDesc()).
            willReturn(newsList);

        List<News> news = newsService.findAll();

        assertThat(news).isEqualTo(newsList);
    }

    @Test
    public void findOneNews(){
        BDDMockito.
            given(newsRepository.findOneById(NEWS_ID1)).
            willReturn(Optional.of(news1));

        News news = newsService.findOne(NEWS_ID1);

        assertThat(news).isEqualTo(news1);
    }

    @Test
    public void publishNews(){
        News newsExpected = News.builder()
            .publishedAt(NEWS_PUBLISHED_AT)
            .title("Title")
            .text("Text")
            .images(newsImageList)
            .build();

        News newsActual = newsService.publishNews(newsExpected);

        assertThat(newsExpected).isEqualTo(newsActual);
    }

    @Test
    public void findUnreadNews(){
        long userId = 1L;
        BDDMockito.
            given(newsRepository.findUnreadNews(userId)).
            willReturn(newsList);

        List<News> news = newsService.findUnreadNews(userId);

        assertThat(news).isEqualTo(newsList);
    }

    @Test(expected = ServerServiceValidationException.class)
    public void publishNewsWithId(){
        News news = News.builder()
            .id(NEWS_ID1)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title("Title")
            .text(NEWS_TEXT)
            .images(newsImageList)
            .build();

        News news3 = newsService.publishNews(news);

        assertThat(news).isEqualTo(news3);
    }

    @Test(expected = ServerServiceValidationException.class)
    public void publishNewsWithEmptyTitle(){
        News news = News.builder()
            .id(NEWS_ID1)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title("")
            .text(NEWS_TEXT)
            .images(newsImageList)
            .build();

        News news3 = newsService.publishNews(news);

        assertThat(news).isEqualTo(news3);
    }

    @Test(expected = ServerServiceValidationException.class)
    public void publishNewsWithEmptyText(){
        News news = News.builder()
            .id(NEWS_ID1)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title(NEWS_TITLE1)
            .text("")
            .images(newsImageList)
            .build();

        News news3 = newsService.publishNews(news);

        assertThat(news).isEqualTo(news3);
    }
}
