package io.vicarius.assignment.user;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String firstName;
    private String lastName;
    private String operation;
    private int requestsRemaining;
    private LocalDateTime lastLoginTimeUtc;

    public UserDTO() {
    }

    public UserDTO(String id) {
        this.id = id;
    }

    public UserDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.lastLoginTimeUtc = userEntity.getLastLoginTimeUtc();
    }

    public UserDTO(UserDocument userDocument) {
        this.id = userDocument.getId();
        this.firstName = userDocument.getFirstName();
        this.lastName = userDocument.getLastName();
        this.lastLoginTimeUtc = userDocument.getLastLoginTimeUtc();
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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
