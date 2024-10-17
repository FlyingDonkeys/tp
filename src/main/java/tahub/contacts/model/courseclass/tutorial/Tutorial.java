package tahub.contacts.model.courseclass.tutorial;

import tahub.contacts.model.course.Course;
import tahub.contacts.model.courseclass.CourseClass;

import java.util.Objects;

/**
 * Represents a Tutorial in the system.
 */
public class Tutorial extends CourseClass {
    private String tutorialId;
    private Course course;

    public Tutorial(String tutorialId, Course course) {
        this.tutorialId = tutorialId;
        this.course = course;
    }

    /**
     * Returns the tutorial ID of this Tutorial.
     *
     * @return the tutorial ID of this Tutorial
     */
    public String getTutorialId() {
        return this.tutorialId;
    }

    /**
     * Returns the Course associated with this Tutorial.
     *
     * @return the Course associated with this Tutorial
     */
    public Course getCourse() {
        return this.course;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tutorial)) {
            return false;
        }

        Tutorial otherTutorial = (Tutorial) other;
        return this.tutorialId.equals(otherTutorial.tutorialId) &&
               this.course.equals(otherTutorial.course);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(this.tutorialId, this.course);
    }
}