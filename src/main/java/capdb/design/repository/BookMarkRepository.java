package capdb.design.repository;

import capdb.design.domain.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark,Integer> {
}
