package pl.patronage.rest.RestClient.model;

import java.util.List;

/**
 * Created by gohilukk on 17.03.14.
 */
public class Collection {

    private String id;
    private String name;
    private String description;
    private List<String> tags;
    private String owner;
    private String items_number;
    private String created_date;
    private String modified_date;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItems_number() {
        return items_number;
    }

    public void setItems_number(String items_number) {
        this.items_number = items_number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection () {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(id + ", ")
                .append(name + ", ")
                .append(description + ", ")
                .append(tags + ", ")
                .append(owner + ", ")
                .append(items_number + ", ")
                .append(created_date + ", ")
                .append(modified_date);
        return sb.toString();
    }
}
