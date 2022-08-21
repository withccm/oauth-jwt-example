package com.withccm.oauthapi.component.user.refreshtoken;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class UserRefreshToken {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenSeq;

    @NotNull
    private Long userNo;

    @Column(length = 256)
    @NotNull
    @Size(max = 256)
    private String refreshToken;

    private LocalDateTime refreshTokenExpires;

    @Column(length = 256)
    @NotNull
    @Size(max = 256)
    private String accessToken;

    private LocalDateTime accessTokenExpires;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
