package capdb.design.domain;


import jakarta.persistence.*;
import lombok.*;

//@EntityListeners(AuditingEntityListener.class) //엔티티 생성 및 수정 시간 자동기록, 감시
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String email;

    private String provider;

    private String password;

    public void update(String userName, String email, String provider, String password) {
        this.userName = userName;
        this.email = email;
        this.provider = provider;
        this.password = password;
    }
}
