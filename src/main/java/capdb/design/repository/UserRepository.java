package capdb.design.repository;

import capdb.design.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    // 이메일로 유저 조회 (로그인 등에서 자주 사용)
    Optional<User> findByEmail(String email);

    // 이름으로 조회 (추가 필요 시)
    Optional<User> findByUserName(String userName);
}
