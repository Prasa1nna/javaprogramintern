import java.io.*;
import java.util.*;

public class StudentManagementSystemApp {

    // Student class
    public static class Student implements Serializable {
        private String name;
        private String rollNumber;
        private String grade;
        private int age;

        public Student(String name, String rollNumber, String grade, int age) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.grade = grade;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public String getGrade() {
            return grade;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade + ", Age: " + age;
        }
    }

    // StudentManagementSystem class
    public static class StudentManagementSystem {
        private List<Student> students = new ArrayList<>();
        private static final String FILE_NAME = "students.dat";

        public void addStudent(Student student) {
            students.add(student);
        }

        public void removeStudent(String rollNumber) {
            students.removeIf(student -> student.getRollNumber().equals(rollNumber));
        }

        public Student searchStudent(String rollNumber) {
            return students.stream()
                           .filter(student -> student.getRollNumber().equals(rollNumber))
                           .findFirst()
                           .orElse(null);
        }

        public void displayAllStudents() {
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                students.forEach(System.out::println);
            }
        }

        public void saveToFile() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(students);
            } catch (IOException e) {
                System.out.println("Error saving to file: " + e.getMessage());
            }
        }

        public void loadFromFile() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                students = (List<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading from file: " + e.getMessage());
            }
        }
    }

    // Main class with console-based UI
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManagementSystem sms = new StudentManagementSystem();
        sms.loadFromFile();

        while (true) {
            System.out.println("Student Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addStudent(scanner, sms);
                    break;
                case 2:
                    removeStudent(scanner, sms);
                    break;
                case 3:
                    searchStudent(scanner, sms);
                    break;
                case 4:
                    sms.displayAllStudents();
                    break;
                case 5:
                    sms.saveToFile();
                    System.out.println("Data saved. Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addStudent(Scanner scanner, StudentManagementSystem sms) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter roll number: ");
        String rollNumber = scanner.nextLine();
        System.out.print("Enter grade: ");
        String grade = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty() || age <= 0) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        Student student = new Student(name, rollNumber, grade, age);
        sms.addStudent(student);
        System.out.println("Student added successfully.");
    }

    private static void removeStudent(Scanner scanner, StudentManagementSystem sms) {
        System.out.print("Enter roll number of the student to remove: ");
        String rollNumber = scanner.nextLine();

        if (sms.searchStudent(rollNumber) == null) {
            System.out.println("Student not found.");
        } else {
            sms.removeStudent(rollNumber);
            System.out.println("Student removed successfully.");
        }
    }

    private static void searchStudent(Scanner scanner, StudentManagementSystem sms) {
        System.out.print("Enter roll number of the student to search: ");
        String rollNumber = scanner.nextLine();

        Student student = sms.searchStudent(rollNumber);
        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }
}
