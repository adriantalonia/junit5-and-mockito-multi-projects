package com.mockitoapp;

import com.mockitoapp.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {
    public static final List<Exam> EXAMS = Arrays.asList(new Exam(5L, "Math"), new Exam(6l, "Languages"), new Exam(7l, "History"));

    public static final List<Exam> EXAMS_ID_NULL = Arrays.asList(new Exam(null, "Math"), new Exam(null, "Languages"), new Exam(null, "History"));

    public static final List<Exam> EXAMS_ID_NEGATIVE = Arrays.asList(new Exam(-5l, "Math"), new Exam(-6l, "Languages"), new Exam(-7l, "History"));

    public final static List<String> QUESTIONS = Arrays.asList("Math", "Algebra", "Combinatorics", "Geometry");

    public final static Exam EXAM = new Exam(null, "Biology");
}
