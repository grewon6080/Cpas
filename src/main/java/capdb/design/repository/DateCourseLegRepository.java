package capdb.design.repository;

import capdb.design.domain.DateCourseLeg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateCourseLegRepository extends JpaRepository<DateCourseLeg, Long> {
    List<DateCourseLeg> findByDateCourseId(Long courseId);
}
