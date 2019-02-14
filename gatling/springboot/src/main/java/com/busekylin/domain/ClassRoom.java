package com.busekylin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoom {
    private String courseName;
    private String teacherName;
    private String description;
}
