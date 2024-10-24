package tahub.contacts.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tahub.contacts.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static tahub.contacts.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static tahub.contacts.logic.commands.CommandTestUtil.MATRICULATION_NUMBER_DESC_AMY;
import static tahub.contacts.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static tahub.contacts.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static tahub.contacts.testutil.Assert.assertThrows;
import static tahub.contacts.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tahub.contacts.logic.commands.AddCommand;
import tahub.contacts.logic.commands.CommandResult;
import tahub.contacts.logic.commands.ListCommand;
import tahub.contacts.logic.commands.exceptions.CommandException;
import tahub.contacts.logic.parser.exceptions.ParseException;
import tahub.contacts.model.Model;
import tahub.contacts.model.ModelManager;
import tahub.contacts.model.ReadOnlyAddressBook;
import tahub.contacts.model.person.Person;
import tahub.contacts.storage.JsonAddressBookStorage;
import tahub.contacts.storage.JsonStudentCourseAssociationListStorage;
import tahub.contacts.storage.JsonUniqueCourseListStorage;
import tahub.contacts.storage.JsonUserPrefsStorage;
import tahub.contacts.storage.StorageManager;
import tahub.contacts.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        JsonUniqueCourseListStorage courseListStorage =
                new JsonUniqueCourseListStorage(temporaryFolder.resolve("courseList.json"));
        JsonStudentCourseAssociationListStorage scaListStorage = new JsonStudentCourseAssociationListStorage(
                temporaryFolder.resolve("courseList.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage,
                courseListStorage, scaListStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        JsonUniqueCourseListStorage courseListStorage =
                new JsonUniqueCourseListStorage(temporaryFolder.resolve("ExceptionCourseList.json"));
        JsonStudentCourseAssociationListStorage scaListStorage = new JsonStudentCourseAssociationListStorage(
                temporaryFolder.resolve("ExceptionCourseList.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage,
                courseListStorage, scaListStorage);
        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + MATRICULATION_NUMBER_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
