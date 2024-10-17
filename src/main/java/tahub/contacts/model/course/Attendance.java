package tahub.contacts.model.course;

import java.util.ArrayList;
import java.util.List;

import tahub.contacts.model.course.exceptions.AttendanceOperationException;

/**
 * Represents the attendance of a student in the address book.
 */
public class Attendance {
    private final ArrayList<Boolean> attendanceList;

    /**
     * Constructs an {@code Attendance} object.
     */
    public Attendance() {
        attendanceList = new ArrayList<>();
    }

    /**
     * Constructs an {@code Attendance} object from an attendance list.
     *
     * @param attendanceList Preexisting attendance list as a {@link List} of {@link Boolean}.
     */
    public Attendance(List<Boolean> attendanceList) {
        this.attendanceList = new ArrayList<>(attendanceList);
    }

    /**
     * Adds a new lesson marked as attended.
     */
    public void addAttendedLesson() {
        attendanceList.add(true);
    }

    /**
     * Adds a new lesson marked as absent.
     */
    public void addAbsentLesson() {
        attendanceList.add(false);
    }

    /**
     * Removes the last session marked in this {@link Attendance} object.
     *
     * @throws AttendanceOperationException If this {@link Attendance} has no sessions - i.e. trying to remove the last
     *      session when there are no sessions to remove.
     */
    public void removeLast() throws AttendanceOperationException {
        if (attendanceList.isEmpty()) {
            throw new AttendanceOperationException("No attendance sessions to remove.");
        }

        int lastIndex = attendanceList.size() - 1;
        attendanceList.remove(lastIndex);
    }

    /**
     * Clears all sessions from this {@link Attendance} object, i.e. resets it.
     */
    public void clear() {
        attendanceList.clear();
    }

    /**
     * Gets the total number of attended sessions in this `Attendance` object.
     *
     * @return Number of attended sessions.
     */
    public int getAttendanceAttendedCount() {
        return (int) attendanceList.stream().filter(x -> x).count();
    }

    /**
     * Gets the total number of sessions in this `Attendance` object.
     *
     * @return Number of sessions.
     */
    public int getAttendanceTotalCount() {
        return attendanceList.size();
    }

    /**
     * Checks whether this {@link Attendance} is equal to another {@link Attendance} object.
     *
     * @param other Object to be compared against.
     * @return {@code true} if the other object is another {@link Attendance} object with the same
     *      {@code attendanceList}.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Attendance otherAttendance)) {
            return false;
        }

        return attendanceList.equals(otherAttendance.attendanceList);
    }

    @Override
    public int hashCode() {
        return attendanceList.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return String.format("[%d/%d]", getAttendanceAttendedCount(), getAttendanceTotalCount());
    }
}
