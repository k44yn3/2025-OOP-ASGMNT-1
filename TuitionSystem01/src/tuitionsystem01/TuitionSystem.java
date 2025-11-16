package tuitionsystem01;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TuitionSystem {

    private List<Student> students;
    private List<Tutor> tutors;
    private List<Subject> subjects;

    private Scanner scanner;
//Main system
    public TuitionSystem() {
        this.students = new ArrayList<>();
        this.tutors = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        TuitionSystem system = new TuitionSystem();
        system.initializeData();
        system.run();
    }

    public void initializeData() {
        // Create subjects
        Subject math = new Subject("MATH01", "Mathematics", 120.00, 20);
        Subject eng = new Subject("ENG01", "English", 100.00, 15);
        Subject phy = new Subject("PHY01", "Physics", 130.00, 10);

        subjects.add(math);
        subjects.add(eng);
        subjects.add(phy);

        // Create tutors
        Tutor t1 = new Tutor("T001", "Mr. Ali", "BSc Mathematics", 5);
        Tutor t2 = new Tutor("T002", "Ms. Siti", "BA English", 3);

        tutors.add(t1);
        tutors.add(t2);

        // Assign tutors to subjects (aggregation)
        t1.assignSubject(math);
        t1.assignSubject(phy);
        t2.assignSubject(eng);

        // Create students
        Student s1 = new Student("Adam", "1001", "012-3456789", "Form 4", "012-999888");
        Student s2 = new Student("Aisha", "1002", "013-4447777", "Form 5", "013-222111");

        students.add(s1);
        students.add(s2);

        // Adam in Math for example
        s1.enrollClass(math);
        // assign timetable rows
        s1.getTimetable().assignSubject("Monday", "3pm-4pm", math, t1, "Room A1");
        s2.getTimetable().assignSubject("Wednesday", "4pm-5pm", eng, t2, "Room B2");

        System.out.println("--- System Initialized (Demo Data) ---");
    }

    public void run() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n===== WELCOME TO TUITION CENTRE SYSTEM =====");
            System.out.println("Are you a:");
            System.out.println("  1. Student");
            System.out.println("  2. Tutor");
            System.out.println("  3. Exit System");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    runStudentFlow();
                    break;
                case "2":
                    runTutorFlow();
                    break;
                case "3":
                    isRunning = false;
                    System.out.println("Thank you for using Tuition System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    break;
            }
        }
        scanner.close();
    }

    private void runStudentFlow() {
        System.out.print("\nPlease enter your name to start: ");
        String name = scanner.nextLine();
        Student student = findStudentByName(name);
        if (student == null) {
            System.out.print("No record found. Create new student? (yes/no): ");
            String ans = scanner.nextLine();
            if (ans.equalsIgnoreCase("yes")) {
                System.out.print("Enter student ID: ");
                String sid = scanner.nextLine();
                System.out.print("Enter phone: ");
                String phone = scanner.nextLine();
                System.out.print("Enter level: ");
                String level = scanner.nextLine();
                System.out.print("Enter parent contact: ");
                String pc = scanner.nextLine();
                student = new Student(name, sid, phone, level, pc);
                students.add(student);
                System.out.println("Student created. Welcome, " + student.getName() + "!");
            } else {
                System.out.println("Returning to main menu.");
                return;
            }
        } else {
            System.out.println("Welcome back, " + student.getName() + "!");
        }

        boolean isStudentMenu = true;
        while (isStudentMenu) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View Schedule");
            System.out.println("2. View Fees");
            System.out.println("3. Enroll in Subject");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    student.viewSchedule();
                    break;
                case "2":
                    student.viewFees();
                    break;
                case "3":
                    studentEnrollFlow(student);
                    break;
                case "4":
                    isStudentMenu = false;
                    System.out.println("Logging out, " + student.getName() + "...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
                    break;
            }
        }
    }

    private void studentEnrollFlow(Student student) {
        System.out.println("\n--- Available Subjects ---");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + subjects.get(i).getDisplayDetails());
        }
        System.out.println("  0. Back to menu");
        System.out.print("Enter subject number to enroll (or 0 to go back): ");
        int sel = getIntInput();
        if (sel == 0) return;
        if (sel < 1 || sel > subjects.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Subject chosen = subjects.get(sel - 1);
        if (chosen.checkVacancy() <= 0) {
            System.out.println("No vacancy in selected subject.");
            return;
        }
        student.enrollClass(chosen);
        System.out.print("Would you like to assign a timetable row now? (yes/no): ");
        String ans = scanner.nextLine();
        if (ans.equalsIgnoreCase("yes")) {
            System.out.print("Enter day (e.g., Monday): ");
            String day = scanner.nextLine();
            System.out.print("Enter time (e.g., 3pm-4pm): ");
            String time = scanner.nextLine();
            System.out.print("Enter location (e.g., Room A1): ");
            String loc = scanner.nextLine();
            Tutor tutor = chosen.getTutor();
            student.getTimetable().assignSubject(day, time, chosen, tutor, loc);
        }
    }

    private void runTutorFlow() {
        System.out.print("\nEnter tutor name to login: ");
        String name = scanner.nextLine();
        Tutor tutor = findTutorByName(name);
        if (tutor == null) {
            System.out.println("Tutor not found. Returning to main menu.");
            return;
        }
        System.out.println("Welcome, " + tutor.getName() + "!");

        boolean isTutorMenu = true;
        while (isTutorMenu) {
            System.out.println("\n--- Tutor Menu ---");
            System.out.println("1. View Subjects");
            System.out.println("2. View Students");
            System.out.println("3. Add Note to Enrollment");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n--- Your Subjects ---");
                    for (Subject s : tutor.getSubjects()) {
                        System.out.println("  - " + s.getDisplayDetails());
                    }
                    break;
                case "2":
                    tutor.viewStudents();
                    break;
                case "3":
                    tutorAddNoteFlow(tutor);
                    break;
                case "4":
                    isTutorMenu = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-4.");
                    break;
            }
        }
    }

    private void tutorAddNoteFlow(Tutor tutor) {
        List<Enrollment> allEnrollments = new ArrayList<>();
        for (Subject s : tutor.getSubjects()) {
            allEnrollments.addAll(s.getEnrollments());
        }
        if (allEnrollments.isEmpty()) {
            System.out.println("No enrollments found for your subjects.");
            return;
        }
        System.out.println("\n--- Enrollments ---");
        for (int i = 0; i < allEnrollments.size(); i++) {
            Enrollment e = allEnrollments.get(i);
            System.out.println("  " + (i + 1) + ". " + e.getStudent().getName()
                    + " | " + e.getSubject().getName() + " | Status: " + e.getStatus());
        }
        System.out.print("Select enrollment number to add note (or 0 to cancel): ");
        int sel = getIntInput();
        if (sel == 0) return;
        if (sel < 1 || sel > allEnrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Enrollment chosen = allEnrollments.get(sel - 1);
        System.out.print("Enter note to add: ");
        String note = scanner.nextLine();
        tutor.updateNotes(chosen, note);
    }

    private Student findStudentByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    private Tutor findTutorByName(String name) {
        for (Tutor t : tutors) {
            if (t.getName().equalsIgnoreCase(name)) return t;
        }
        return null;
    }

    private int getIntInput() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}
