package tahub.contacts.model.studentcourseassociation;

import seedu.address.model.GradingSystem;
import tahub.contacts.model.course.Course;
import tahub.contacts.model.courseclass.CourseClass;
import tahub.contacts.model.person.Name;
import tahub.contacts.model.person.Person;
import tahub.contacts.model.courseclass.recitation.Recitation;
import tahub.contacts.model.courseclass.tutorial.Tutorial;

public class StudentCourseAssociation {
    private Person student;
    private Course course;
    /**
     * Represents a Tutorial associated with a Course.
     * May be null, if this TA is not the student's tutorial TA.
     */
    private Tutorial tutorial = null;
    /**
     * Represents a Recitation
     * May be null, if this TA is not the student's recitation TA.
     * */
    private Recitation recitation = null;
    private GradingSystem grades;

    /**
     * Represents an association between a student, course, grading system, and tutorial.
     * The TA will view this object in TAHub.
     * This constructor is to be used if the TA is this student's Tutorial TA.
     *
     * @param student the student associated with this association
     * @param course the course associated with this association
     * @param grades the grading system to manage student grades for the associated course
     * @param tutorial the tutorial associated with this association
     */
    public StudentCourseAssociation(Person student, Course course, GradingSystem grades, Tutorial tutorial) {
        this.student = student;
        this.course = course;
        this.grades = grades;
        this.tutorial = tutorial;
        this.grades = new GradingSystem();
    }

    /**
     * Represents an association between a student, course, grading system, and recitation.
     * The TA will view this object in TAHub.
     * This constructor is to be used if the TA is this student's Recitation TA.
     *
     * @param student the student associated with this association
     * @param course the course associated with this association
     * @param grades the grading system to manage student grades for the associated course
     * @param recitation the recitation associated with this association
     */
    public StudentCourseAssociation(Person student, Course course, Tutorial tutorial, GradingSystem grades, Recitation recitation) {
        this.student = student;
        this.course = course;
        this.grades = grades;
        this.recitation = recitation;
        this.grades = new GradingSystem();
    }

    /**
     * Get the student associated with this StudentCourseAssociation.
     *
     * @return the student associated with this StudentCourseAssociation
     */
    public Person getStudent() {
        return student;
    }

    /**
     * Retrieve the Course associated with this StudentCourseAssociation.
     *
     * @return the Course object associated with this StudentCourseAssociation
     */
    public Course getCourse() {
        return course;
    }


    /**
     * Get the CourseClass associated with this StudentCourseAssociation.
     * If a Tutorial is associated, return the Tutorial; otherwise, return the Recitation.
     * Note that this association cannot simultaneously have a Tutorial
     * and a Recitation by design.
     *
     * @return the CourseClass associated with this StudentCourseAssociation
     */
    public CourseClass getCourseClass() {
        if (tutorial != null) {
            return this.tutorial;
        } else {
            return this.recitation;
        }
    }

    /**
     * Retrieves the grading system associated with this StudentCourseAssociation.
     *
     * @return the grading system used to manage student grades for the associated course
     */
    public GradingSystem getGrades() {
        return grades;
    }

    /**
     * Compares this StudentCourseAssociation with the specified object for equality.
     * First checks if both StudentCourseAssociations have the same Student and Course
     * If they are the same, check if the tutorials match
     *
     * @param other the object to compare this StudentCourseAssociation with
     * @return true if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentCourseAssociation)) {
            return false;
        }

        StudentCourseAssociation otherSCA = (StudentCourseAssociation) other;
        boolean checkStudentAndCourse = this.student.equals(otherSCA.student) &&
                         this.course.equals(otherSCA.course);

        if (!checkStudentAndCourse) {
            return false;
        }

        if (this.tutorial != null) {
            return this.tutorial.equals(otherSCA.tutorial);
        } else {
            return this.recitation.equals(otherSCA.recitation);
        }
    }
}