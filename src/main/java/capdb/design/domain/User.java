package capdb.design.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@EntityListeners(AuditingEntityListener.class) //엔티티 생성 및 수정 시간 자동기록, 감시
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Long 고려

    @Column(length = 20)
    private String username;

    @Column(columnDefinition = "TEXT")
    private String provider;

    @Column(columnDefinition = "TEXT")
    private String profileImg;
}
