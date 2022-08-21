package com.withccm.oauthapi.component.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.withccm.oauthapi.component.user.enums.OAuthAttributes;
import com.withccm.oauthapi.component.user.enums.Role;

import lombok.Data;

@Entity
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -7973388962315647462L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Enumerated(EnumType.STRING)
    private OAuthAttributes oauthType;
    private String oauthId;

    private String name;

    private String email;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected User() {
    }

    public User(OAuthAttributes oauthType, String oauthId, String name, String email, String imageUrl, Role role) {
        this(null, oauthType, oauthId, name, email, imageUrl, role);
    }

    public User(Long userNo, OAuthAttributes oauthType, String oauthId, String name, String email, String imageUrl, Role role) {
        this.userNo = userNo;
        this.oauthType = oauthType;
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
