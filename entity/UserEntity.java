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

    @Column(name = "PASSWORD_HASH_ALGORITHM")
    @Enumerated(EnumType.STRING)
    private EncryptionAlgorithm encryptionAlgorithm;

    /*
    This part is token for sending email
     */
    @Embedded
    private EmailConfirmationTokenEntity emailConfirmationTokenEntity;

    /*
    This part is token for password reminder
     */
    @Embedded
    private PasswordReminderTokenEntity passwordReminderTokenEntity;

    @OneToMany(mappedBy = "user")
    private List<AuthorityEntity> authorities;

}
