package org.example.buyingserver.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
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
    @Builder.Default
    @Column(nullable = false)
    private Status status = Status.RUNNING;

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

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
        this.status = Status.DELETED;
    }
}