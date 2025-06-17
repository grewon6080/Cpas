package capdb.design.repository;

import capdb.design.domain.DateCoursePlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateCoursePlaceRepository extends JpaRepository<DateCoursePlace, Long> {
    List<DateCoursePlace> findByDateCourseIdOrderByOrderIndex(Long courseId);
}
