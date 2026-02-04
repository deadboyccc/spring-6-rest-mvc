package dev.dead.spring6restmvc.playground;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.Objects;
import java.util.UUID;

public class TestingJetbrainsNullableSafety {
    @NotNull
    private final String id;
    @NotNull
    private String username;
    @Nullable
    private String emailAddress;

    public TestingJetbrainsNullableSafety() {
        this.id = UUID.randomUUID()
                .toString();
        this.username = "default_user";
    }

    public static void main(String[] args) {
        var user = new TestingJetbrainsNullableSafety();

        // NotNull field - IDE warns if we try to assign null
        user.setUsername("john_doe");

        // Nullable field handling
        user.setEmailAddress("john@example.com");

        // Safe access patterns for nullable field
        if (user.getEmailAddress() != null) {
            System.out.println("Email domain: " + user.getEmailAddress()
                    .split("@")[1]);
        }

        // Using Elvis operator with null check
        String emailDomain = user.getEmailAddress() != null ? user.getEmailAddress()
                .split("@")[1] : "no email";

        // Using Objects utility
        System.out.println("Email length: " +
                Objects.requireNonNullElse(user.getEmailAddress(), "")
                        .length());
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = Objects.requireNonNull(username, "Username cannot be null");
    }

    @Nullable
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(@Nullable String emailAddress) {
        this.emailAddress = emailAddress;
    }
}