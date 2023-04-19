package com.go.tokenverification.entity;

import com.go.tokenverification.enums.Authority;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name="AUTHORITY")
@Data
public class AuthorityEntity implements GrantedAuthority {

    @Id
    @GeneratedValue
    @Column(name="AUTHORITY_ID")
    private Long id;

    @Column(name = "AUTHORITY_TYPE")
    private String authority;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private UserEntity user;

}
