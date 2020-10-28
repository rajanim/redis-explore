package redis.io;

import datasets.io.NewsGroupsArticle;
import datasets.io.NewsGroupsParser;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;

/**
 * Write news articles metadata to redis
 * fetch top authors
 * fetch top terms per group
 * intersect groups to find common terms between groups
 */
public class NewsGroupsToRedis {

   static String jedisHost;
    static int jedisPort;

    public NewsGroupsToRedis(){
        this.jedisPort = 6379;
        this.jedisHost = "localhost";
    }
    public NewsGroupsToRedis(String host){
        this.jedisHost = host;
        this.jedisPort = 6379;
    }

    public NewsGroupsToRedis(String host, int port){
        this.jedisHost = host;
        this.jedisPort = port;
    }
    public static void main(String[] args) throws IOException {
        NewsGroupsToRedis newsGroupsToRedis = new NewsGroupsToRedis("localhost");
        Jedis conn = new Jedis(jedisHost, jedisPort);
        System.out.println( conn.echo("ping"));
        NewsGroupsParser parser = new NewsGroupsParser("src/main/resources/20_news_groups_subset");

        parser.parse();
        parser.getArticles().forEach((key, articles) -> {
            System.out.println(key + ": " + articles.size());
            articles.forEach(a -> {
                System.out.println("\t" +"Writing article payload to Redis : " + a.getArticleId());
                    newsGroupsToRedis.postArticle(conn, a);
                    newsGroupsToRedis.addAuthorToTopAuthorsSet(conn, a.getAuthor());
            });
        });

        System.out.println("look up redis for article's group :  \"alt.atheism:51188\"");
        System.out.println(newsGroupsToRedis.getArticleGroup(conn, "alt.atheism:51188"));
        System.out.println(newsGroupsToRedis.getTopAuthors(conn, "top_authors", 2));


    }

    String getArticleGroup(Jedis conn, String key){
        return conn.hget(key, "group");
    }

    Set<String> getTopAuthors(Jedis conn, String key, long num){
       long max = conn.zcard(key);
        return conn.zrevrange(key,0, num);
    }

    Set<String> getCommonTerms(Jedis conn, String key, String[] articleIds){
        conn.zinterstore("common_terms",articleIds);
        return conn.zrevrange(key,0, -1);
    }

    Set<String> getTopNCommonTerms(Jedis conn, String key, String[] articleIds, long n){
        conn.zinterstore("common_terms",articleIds);
        return conn.zrevrange(key,0, n);
    }
    void postArticle(Jedis conn, NewsGroupsArticle article){
        String key = article.getArticleId();
        conn.hmset(key, article.getArticleMetadata());
    }

    void addAuthorToTopAuthorsSet(Jedis conn, String author){
        conn.zincrby("top_authors", 1, author);
    }

    void recordTFPerGroup(Jedis conn, String group, String terms){
        String hashKey = "tf_per_group:" + group;
        String term[]  = terms.split(" ");
        for(String s : term)
        conn.zincrby(hashKey, 1, s);
    }

}
