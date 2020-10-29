package datasets.io;

/**
 * forked from
 * https://github.com/rajanim/selective-search project and
 * https://github.com/wojtuch/20newsgroups-parser
 * per sub dir, per news, group (sub-dir-name) extract from: subject: news_id(dir_name_file_name), emails, telephones
 * //mentions
 * <p>
 * id: group_name_file_name
 * title:subject
 * author : from
 * mentions : email ids
 * topic : group
 * <p>
 * //use cluster model to choose top cluster labels per doc
 * top authors
 * top mentions
 * //loop through dir
 * //per file, extract subject:, from:, id
 * //write it to redis
 * //top authors, top mentions
 * //news as hash
 * //top_authors
 * //top_mentions zset (name, times)
 * //unique users set
 * // top terms per topic -- each new sub dir is new topic, pick subject split by space, sorted set term, tf, df (terms statistics)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;


public class NewsGroupsParser {

    public static void main(String[] args) throws IOException {
        NewsGroupsParser parser = new NewsGroupsParser("src/main/resources/20_news_groups_subset");
        parser.parse();

        parser.getArticles().forEach((key, articles) -> {
          //  System.out.println(key + ": " + articles.size());
            articles.forEach(a -> {
                for (Map.Entry<String, String> stringStringEntry : a.getArticleMetadata().entrySet()) {
                    System.out.println(stringStringEntry.getKey() + "," + stringStringEntry.getValue());
                }
            });
        });

    }

    private final Path folder;
    private final Map<String, List<NewsGroupsArticle>> articles = new HashMap<>();

    public NewsGroupsParser(Path folder) {
        this.folder = folder.toAbsolutePath();
    }

    public NewsGroupsParser(String folder) {
        this.folder = Paths.get(folder).toAbsolutePath();
    }

    public Map<String, List<NewsGroupsArticle>> getArticles() {
        return articles;
    }

    public void parse() throws IOException {
        try (DirectoryStream<Path> categoriesDirs = Files.newDirectoryStream(folder)) {
            for (Path category : categoriesDirs) {
                String newsgroups = category.getFileName().toString();
                articles.put(newsgroups, new ArrayList<>());

                try (DirectoryStream<Path> articleFiles = Files.newDirectoryStream(category)) {
                    for (Path articleFile : articleFiles) {
                        try (BufferedReader br = new BufferedReader(new FileReader(articleFile.toString()))) {
                            NewsGroupsArticle article = new NewsGroupsArticle();
                            article.setArticleId(newsgroups + ":" + articleFile.getFileName().toString());
                            article.setLabel(newsgroups);
                            StringBuffer content = new StringBuffer();
                            String line;
                            boolean inContent = false;
                            while ((line = br.readLine()) != null) {
                                if (!inContent) {
                                    if (line.trim().length() == 0) {
                                        inContent = true;
                                        continue;
                                    }

                                    String[] split = line.split(": ", 2);
                                    if (split.length == 2) {
                                        article.addHeader(split[0], split[1]);
                                    }

                                } else {
                                    content.append(line);
                                    content.append(" ");
                                }
                            }
                            article.setAuthor(getAuthor(article.getHeader("From")));
                            article.setRawText(content.toString().trim());
                            Map<String, String> articleMetaData = new HashMap<>();
                            articleMetaData.putAll(article.getHeaders());
                            articleMetaData.put("author", article.getAuthor());
                            articleMetaData.put("articleId", article.getArticleId());
                            articleMetaData.put("group", article.getLabel());
                            article.setArticleMetadata(articleMetaData);
                            articles.get(newsgroups).add(article);
                        }
                    }
                }catch (NotDirectoryException nde){
                    System.err.println("not a directory" + nde.toString());
                }
            }
        }
    }

    static String getAuthor(String text){
        int startIndex = text.indexOf('(');
        int endIndex = text.indexOf(')');
        int len = text.length();
        if(startIndex>=0 && startIndex<len && endIndex>=0 && endIndex<len)
            return text.substring(startIndex+1, endIndex);
        else
            return "NA";
    }
}

