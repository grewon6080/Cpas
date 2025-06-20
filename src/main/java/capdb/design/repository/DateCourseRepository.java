package capdb.design.repository;

import capdb.design.domain.DateCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateCourseRepository extends JpaRepository<DateCourse, Long> {

    List<DateCourse> findByUserId(Long userId);
}
