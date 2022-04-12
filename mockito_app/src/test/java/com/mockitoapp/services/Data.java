package com.mockitoapp.services;

import com.mockitoapp.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {
    public static final List<Exam> EXAMS = Arrays.asList(new Exam(5L, "Math"), new Exam(6l, "Languages"), new Exam(7l, "History"));

    public final static List<String> QUESTIONS = Arrays.asList("Math", "Algebra", "Combinatorics", "Geometry");

    public final static Exam EXAM = new Exam(null, "Biology");
}
