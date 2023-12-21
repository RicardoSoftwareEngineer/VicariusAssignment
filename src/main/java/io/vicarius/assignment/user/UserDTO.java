package io.vicarius.assignment.user;

import java.time.LocalDateTime;

public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private int requestsRemaining;
    private LocalDateTime lastLoginTimeUtc;

    public UserDTO() {
    }

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.lastLoginTimeUtc = userEntity.getLastLoginTimeUtc();
    }

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

    public int getRequestsRemaining() {
        return requestsRemaining;
    }

    public void setRequestsRemaining(int requestsRemaining) {
        this.requestsRemaining = requestsRemaining;
    }

    public LocalDateTime getLastLoginTimeUtc() {
        return lastLoginTimeUtc;
    }

    public void setLastLoginTimeUtc(LocalDateTime lastLoginTimeUtc) {
        this.lastLoginTimeUtc = lastLoginTimeUtc;
    }
}
