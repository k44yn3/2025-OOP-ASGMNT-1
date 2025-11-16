package tuitionsystem01;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private String subjectID;
    private String name;
    private double fee;
    private int capacity;

    // Aggregation: tutor may be null (subject can exist without tutor)
    private Tutor tutor;

    // Track enrollments for vacancy and reporting
    private List<Enrollment> enrollments;

    public Subject(String subjectID, String name, double fee, int capacity) {
        this.subjectID = subjectID;
        this.name = name;
        this.fee = fee;
        this.capacity = capacity;
        this.enrollments = new ArrayList<>();
        this.tutor = null;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public String getName() {
        return name;
    }

    public double getFee() {
        return fee;
    }

    public int getCapacity() {
        return capacity;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    // Adds a enrollment record to the subject
    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    // checkVacancy: returns number of available seats, capacity  is active enrollments
    public int checkVacancy() {
        int activeCount = 0;
        for (Enrollment e : enrollments) {
            if (e.getStatus().equalsIgnoreCase("Active")) activeCount++;
        }
        int vacancy = capacity - activeCount;
        return Math.max(vacancy, 0);
    }

    // Returns a brief tutor detail string; can be null if no tutor assigned
    public String getTutorDetails() {
        if (tutor == null) return "No tutor assigned.";
        return tutor.getName() + " (" + tutor.getQualification() + ")";
    }

    public String getDisplayDetails() {
        return String.format("%s - %s | RM%.2f | Capacity: %d | Vacancy: %d | Tutor: %s",
                subjectID, name, fee, capacity, checkVacancy(), (tutor == null ? "None" : tutor.getName()));
    }
}
