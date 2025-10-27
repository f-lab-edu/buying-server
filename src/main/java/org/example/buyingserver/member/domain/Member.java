package org.example.buyingserver.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SocialType socialType;

    @Column(nullable = true)
    private String socialid;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Builder
    private Member(String email, String password, String nickname, Status status, SocialType socialType, String socialid) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = (status != null) ? status : Status.ACTIVE;
        this.socialType = socialType;
        this.socialid = socialid;
    }

    public static Member create(String email, String rawPassword,String nickname, PasswordEncoder encoder) {
        return Member.builder()
                .email(email)
                .password(encoder.encode(rawPassword))
                .nickname(nickname)
                .status(Status.ACTIVE)
                .build();
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
        this.status = Status.DELETED;
    }
}