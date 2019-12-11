package com.movienights.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import java.util.Set;
import java.util.UUID;

public class DbUser {
    @BsonId
    private ObjectId id;
    private String username;
    private String password;
    private String favoriteGenre;
    private Set<String> roles;
    private String accessToken;
    private String refreshToken;
    private Long expiresAt;
    private UUID jwtSalt;

    public DbUser() {
        jwtSalt = UUID.randomUUID();
    }

    public DbUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = Set.of("USER");
        jwtSalt = UUID.randomUUID();
    }

    public DbUser(String username, String password, String accessToken, String refreshToken, long expiresAt) {
        this.username = username;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.jwtSalt = UUID.randomUUID();
        this.expiresAt = expiresAt;
        this.roles = Set.of("USER");
    }

    @JsonIgnore
    public ObjectId getObjectId(){
        return id;
    }

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(String favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }

    public void setId(String hexId) {
        this.id = new ObjectId(hexId);
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UUID getJwtSalt() {
        return jwtSalt;
    }

    public void setJwtSalt(UUID jwtSalt) {
        this.jwtSalt = jwtSalt;
    }
}

