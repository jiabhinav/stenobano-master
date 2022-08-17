package abhi.example.hp.stenobano.model;

public class Chat {
    private String title;
    private String subject;
    private String description;
    private String date,key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Chat() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }



    public Chat(String title, String subject, String description,String date) {
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.date=date;
    }
}
