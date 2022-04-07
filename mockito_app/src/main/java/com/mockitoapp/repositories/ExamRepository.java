package com.mockitoapp.repositories;

import com.mockitoapp.models.Exam;

import java.util.List;

public interface ExamRepository {
    List<Exam> findALl();
}
