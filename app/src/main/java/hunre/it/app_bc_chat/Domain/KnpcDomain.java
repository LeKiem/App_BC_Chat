package hunre.it.app_bc_chat.Domain;

public class KnpcDomain {

    private String title ;
    private String description ;

    public KnpcDomain(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public KnpcDomain() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
