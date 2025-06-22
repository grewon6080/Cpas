package capdb.design.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String email;
    private Integer age;
    private String gender;
    private String provider;
    private String password;

//    @Builder.Default
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<DateCourse> dateCourses = new ArrayList<>();

}

