package capdb.design.dto;

import java.time.LocalDate;

public record DateCourseRequest (String title, String dateRegion, LocalDate date)
{}
