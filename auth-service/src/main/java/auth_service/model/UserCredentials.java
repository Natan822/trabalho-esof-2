package auth_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_credentials")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCredentials {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "password")
    private String password;
}
