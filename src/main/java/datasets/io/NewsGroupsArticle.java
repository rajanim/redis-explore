package datasets.io;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsGroupsArticle implements Serializable {
    private static SimpleDateFormat[] dateFormats = new SimpleDateFormat[]{
            new SimpleDateFormat("dd MMM yyyy HH:mm:ss z"),
            new SimpleDateFormat("dd MMM yyyy HH:mm:ss"),
            new SimpleDateFormat("dd MMM yyyy HH:mm z"),
            new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z"),
            new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss"),
            new SimpleDateFormat("E, dd MMM yyyy HH:mm z"),
            new SimpleDateFormat("E, dd MMM yy HH:mm:ss z")
    };
    private Map<String, String> headers = new HashMap<>();
    private String rawText;
    private String label;
    private Date date;
    private List<String> mentions;
    private String articleId;
    private String author;
    private Map<String, String> articleMetadata;

    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return author;
    }

    public void setArticleMetadata(Map<String, String> articleMetadata){
        this.articleMetadata = articleMetadata;
    }

    public Map<String, String> getArticleMetadata(){
        return articleMetadata;
    }
    public void setArticleId(String id){
        this.articleId = id;
    }

    public String getArticleId(){
        return articleId;
    }

    public NewsGroupsArticle() {
    }

    public String getMentions() {
        return rawText;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getDate() {
        if (date == null) {
            String dateString = headers.get("Date");
            if (dateString != null) {
                dateString = dateString.trim();
                dateString = dateString.replaceFirst("UT$", "UTC");
                date = tryToParseDate(dateString);
            }
        }
        return date;
    }

    private Date tryToParseDate(String dateString) {
        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                Date parsedDate = dateFormat.parse(dateString);
                return parsedDate;
            }
            catch (ParseException e) {
            }
        }
        throw new RuntimeException("Date format of " + dateString + " unknown!");
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}