package seedu.address.logic.commands;

import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Comment;

class CommentCommandTest {

    private static final String COMMENT_STUB = "Some comment";

    private CommandHistory commandHistory = new CommandHistory();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        // Choose an index outside the bounds of the list
        int invalidIndex = 100;
        Index index = Index.fromZeroBased(invalidIndex);

        // Create a comment
        Comment comment = new Comment("Test comment");

        // Execute the command and assert that it throws CommandException
        CommentCommand commentCommand = new CommentCommand(index, comment);
        assertThrows(CommandException.class, () -> commentCommand.execute(model, commandHistory));
    }
}
