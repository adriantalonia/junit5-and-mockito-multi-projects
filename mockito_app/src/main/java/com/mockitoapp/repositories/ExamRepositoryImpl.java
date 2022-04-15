package com.mockitoapp.repositories;

import com.mockitoapp.Data;
import com.mockitoapp.models.Exam;

import java.util.Arrays;
import java.util.List;

public class ExamRepositoryImpl implements ExamRepository {
    @Override
    public List<Exam> findALl() {
        System.out.println("ExamRepositoryImpl findALl");
        return Data.EXAMS;
    }

    @Override
    public Exam save(Exam exam) {
        System.out.println("ExamRepositoryImpl save");
        return Data.EXAM;
    }
}
