package com.go.tokenverification.entity;

import com.go.tokenverification.enums.Authority;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="AUTHORITY")
@Data
public class AuthorityEntity {

    @Id
    @GeneratedValue
    @Column(name="AUTHORITY_ID")
    private Long id;

    @Column(name = "AUTHORITY_TYPE")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private UserEntity user;
}
