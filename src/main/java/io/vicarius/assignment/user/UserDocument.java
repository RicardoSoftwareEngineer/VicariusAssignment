package io.vicarius.assignment.user;

import org.springframework.data.elasticsearch.annotations.Document;
import java.time.LocalDateTime;

@Document(indexName = "user")
public class UserDocument {
    public UserDocument() {
    }

    public UserDocument(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.lastLoginTimeUtc = userDTO.getLastLoginTimeUtc();
    }

    private String id;
    private String firstName;
    private String lastName;
    private LocalDateTime lastLoginTimeUtc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getLastLoginTimeUtc() {
        return lastLoginTimeUtc;
    }

    public void setLastLoginTimeUtc(LocalDateTime lastLoginTimeUtc) {
        this.lastLoginTimeUtc = lastLoginTimeUtc;
    }
}
