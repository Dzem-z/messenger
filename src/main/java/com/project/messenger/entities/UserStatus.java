package com.project.messenger.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_status")
@Getter
@Setter
@NoArgsConstructor
public class UserStatus {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isVerified = false;
}
