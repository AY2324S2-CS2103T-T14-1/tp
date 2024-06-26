package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TASKS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Task;

/**
 * Mark tasks done by employees
 */
public class EditDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "edit_deadline";
    public static final String MESSAGE_MARK_TASK_SUCCESS = "Deadline for %1$s's task changed to %2$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Change the deadline of the task of a user.\n"
            + "Parameters: "
            + "INDEX (must be a positive integer)\n"
            + PREFIX_DEADLINE + "dd-MM-yyyy HHmm \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DEADLINE + "22-04-2024 2359 ";

    public static final String MESSAGE_TASK_DOES_NOT_EXIST = "%s does not have a Task. "
            + "Please select another Task";
    private final Index index;

    private final Deadline deadline;

    /**
     * Change the deadline of the task of a user.
     */
    public EditDeadlineCommand(Index index, Deadline deadline) {
        requireAllNonNull(index, deadline);
        this.index = index;
        this.deadline = deadline;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> personList = model.getFilteredPersonList();

        if (index.getOneBased() > personList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person taskOwner = personList.get(index.getZeroBased());

        if (!taskOwner.isBusy()) {
            throw new CommandException(String.format(MESSAGE_TASK_DOES_NOT_EXIST, taskOwner.getName()));
        }

        Task task = taskOwner.getTask();
        Task editedTask = task.editDeadline(deadline);

        Person editedPerson = new Person(taskOwner.getName(), taskOwner.getPhone(),
                taskOwner.getEmail(), taskOwner.getAddress(),
                taskOwner.getDepartment(), taskOwner.getTags(),
                taskOwner.getEfficiency(), taskOwner.getComment());
        editedPerson.setTask(editedTask);
        model.setPerson(taskOwner, editedPerson);
        model.setTask(task, editedTask);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS,
                Messages.printName(taskOwner), deadline.toString()));

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditDeadlineCommand)) {
            return false;
        }

        EditDeadlineCommand otherEditDeadlineCommand = (EditDeadlineCommand) other;
        return index.equals(otherEditDeadlineCommand.index)
                && this.deadline.equals(otherEditDeadlineCommand.deadline);
    }
}
