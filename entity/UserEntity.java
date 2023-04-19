package com.go.tokenverification.entity;

import com.go.tokenverification.enums.EncryptionAlgorithm;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USER")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column(name="USER_NAME")
    private String username;

    @Column(name="PASSWORD")
    private String password;

    /*
    Algorithm used for encrypt user password
     */
    @Column(name = "PASSWORD_HASH_ALGORITHM")
    @Enumerated(EnumType.STRING)
    private EncryptionAlgorithm encryptionAlgorithm;

    /*
    This part is token for sending email
     */
    @OneToMany(mappedBy = "user")
    private List<EmailConfirmationTokenEntity> emailConfirmationTokens;

    /*
    This part is token for password reminder
     */
    @OneToMany(mappedBy = "user")
    private List<PasswordReminderTokenEntity> passwordReminderTokens;

    /*
    This check whether user is active or not
     */
    @Column(name = "IS_ACTIVE")
    private boolean isActive ;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<AuthorityEntity> authorities;

}
