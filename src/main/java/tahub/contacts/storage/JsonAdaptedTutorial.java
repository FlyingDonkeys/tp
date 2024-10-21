package tahub.contacts.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import tahub.contacts.commons.exceptions.IllegalValueException;
import tahub.contacts.model.course.Course;
import tahub.contacts.model.tutorial.Tutorial;

/**
 * Represents a JSON-adapted Tutorial object.
 * This class serves as a wrapper around Tutorial to enable JSON serialization/deserialization.
 */
class JsonAdaptedTutorial {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Tutorial's %s field is missing!";
    private final String tutorialId;
    private final JsonAdaptedCourse course;

    /**
     * Constructs a JsonAdaptedTutorial object with the given tutorial ID and course.
     *
     * @param tutorialId the tutorial ID of the tutorial
     * @param course the Course associated with the tutorial
     */
    @JsonCreator
    public JsonAdaptedTutorial(@JsonProperty("tutorialId") String tutorialId,
                             @JsonProperty("course") JsonAdaptedCourse course) {
        this.tutorialId = tutorialId;
        this.course = course;
    }

    /**
     * Constructs a JSON-adapted Tutorial object based on the provided Tutorial source.
     *
     * @param source the original Tutorial object to be adapted
     */
    public JsonAdaptedTutorial(Tutorial source) {
        tutorialId = source.getTutorialId();
        course = new JsonAdaptedCourse(source.getCourse());
    }

    /**
     * Converts a JSON-adapted Tutorial object to a model Tutorial object.
     * Validates the fields of the JSON-adapted Tutorial object before conversion.
     *
     * @return a model Tutorial object representing the JSON-adapted Tutorial
     * @throws IllegalValueException if the data in the JSON-adapted Tutorial object does not fulfill constraints
     */
    public Tutorial toModelType() throws IllegalValueException {
        if (tutorialId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Tutorial.class.getSimpleName()));
        }

        final String modelTutorialId = tutorialId;

        if (course == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Course.class.getSimpleName()));
        }

        final Course courseModel = this.course.toModelType();
        if (!Course.isValidCourseCode(courseModel.courseCode)) {
            throw new IllegalValueException(Course.COURSE_CODE_MESSAGE_CONSTRAINTS);
        } else if (!Course.isValidCourseName(courseModel.courseName)) {
            throw new IllegalValueException(Course.COURSE_NAME_MESSAGE_CONSTRAINTS);
        }

        return new Tutorial(modelTutorialId, courseModel);
    }
}