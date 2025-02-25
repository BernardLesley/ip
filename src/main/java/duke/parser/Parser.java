package duke.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

import duke.commands.AddDeadlineCommand;
import duke.commands.AddEventCommand;
import duke.commands.AddTodoCommand;
import duke.commands.Command;
import duke.commands.DeleteCommand;
import duke.commands.FindByDateCommand;
import duke.commands.FindByKeywordCommand;
import duke.commands.GoodbyeCommand;
import duke.commands.IncorrectCommand;
import duke.commands.ListCommand;
import duke.commands.MarkCommand;
import duke.commands.UnmarkCommand;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;
import duke.ui.TextUi;

/**
 * The Parser class is responsible for parsing user input and file content
 * into commands or tasks. It contains methods to parse the following:
 * - user commands
 * - deadline and event tasks
 * - task indices
 * - date and date-time values
 * - file content
 */
public class Parser {
    TextUi ui = new TextUi();

    /**
     * Parses user input and returns the corresponding command object.
     *
     * @param userInput the user input to parse
     * @return the corresponding command object
     */
    public static Command parseCommand(String userInput) {
        String[] words = userInput.trim().split(" ", 2);
        if (words.length == 0) {
            return new IncorrectCommand();
        }

        final String commandWord = words[0];
        final String arguments = userInput.replaceFirst(commandWord, "").trim();

        switch (commandWord) {

        case AddDeadlineCommand.COMMAND_WORD:
            return (Command) new AddDeadlineCommand(arguments);

        case AddEventCommand.COMMAND_WORD:
            return (Command) new AddEventCommand(arguments);

        case AddTodoCommand.COMMAND_WORD:
            return (Command) new AddTodoCommand(arguments);

        case DeleteCommand.COMMAND_WORD:
            return (Command) new DeleteCommand(arguments);

        case FindByDateCommand.COMMAND_WORD:
            return (Command) new FindByDateCommand(arguments);

        case FindByKeywordCommand.COMMAND_WORD:
            return (Command) new FindByKeywordCommand(arguments);

        case GoodbyeCommand.COMMAND_WORD:
            return (Command) new GoodbyeCommand();

        case MarkCommand.COMMAND_WORD:
            return (Command) new MarkCommand(arguments);

        case ListCommand.COMMAND_WORD:
            return (Command) new ListCommand();

        case UnmarkCommand.COMMAND_WORD:
            return (Command) new UnmarkCommand(arguments);

        default:
            return (Command) new IncorrectCommand();
        }
    }

    /**
     * Parses a deadline task description and end time from a string.
     *
     * @param arguments the string to parse
     * @return an array containing the description and end time, or null if the string is in the wrong format
     */
    public String[] parseDeadline(String arguments) {
        try {
            int dividerPosition = arguments.indexOf("/by");
            String description = arguments.substring(0, dividerPosition).trim();
            String endTime = arguments.substring(dividerPosition + 3).trim();
            return new String[]{description, endTime};
        } catch (StringIndexOutOfBoundsException e) {
            ui.showIncorrectFormatMessage("deadline");
            return null;
        }

    }

    /**
     * Parses an event task description, start time, and end time from a string.
     *
     * @param arguments the string to parse
     * @return an array containing the description, start time, and end time, or null if the string is in the wrong format
     */
    public String[] parseEvent(String arguments) {
        try {
            int divider1Position = arguments.indexOf("/from");
            int divider2Position = arguments.indexOf("/to");
            String description = arguments.substring(0, divider1Position).trim();
            String startTime = arguments.substring(divider1Position + 5, divider2Position).trim();
            String endTime = arguments.substring(divider2Position + 3).trim();
            return new String[]{description, startTime, endTime};
        } catch (StringIndexOutOfBoundsException e) {
            ui.showIncorrectFormatMessage("event");
            return null;
        }

    }

    /**
     * Parses a task index from a string.
     *
     * @param arguments the string to parse
     * @return the task index, or -1 if the string is not an integer
     */
    public int parseIndex(String arguments) {
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (InputMismatchException | NumberFormatException e) {
            ui.showTaskIndexNotIntegerError();
            return -1;
        }
    }

    /**
     * Parses a date from a string.
     *
     * @param arguments the string to parse
     * @return the date as a LocalDateTime object, or null if the string is in the wrong format
     */
    public LocalDateTime parseDate(String arguments) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime date = LocalDate.parse(arguments, formatter).atStartOfDay();
            return date;
        } catch (NullPointerException e) {
            return null;
        } catch (DateTimeParseException e) {
            ui.showWrongDateFormatError();
            return null;
        }
    }

    /**
     * Parses a date-time value from a string.
     *
     * @param arguments the string to parse
     * @return the date-time value as a LocalDateTime object, or null if the string is in the wrong format
     */
    public LocalDateTime parseDateTime(String arguments) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime date = LocalDateTime.parse(arguments, formatter);
            return date;
        } catch (NullPointerException e) {
            return null;
        } catch (DateTimeParseException e) {
            ui.showWrongDateTimeFormatError();
            return null;
        }
    }

    /**
     * Parses a task from a string representing its contents in the format used in the save file.
     *
     * @param text the string to parse
     * @return the parsed task object, or null if the string is in the wrong format
     */
    public Task parseFileContent(String text) {
        Task readTask = null;
        try {
            String[] arrOfStr = text.split("[|]");
            String taskType = arrOfStr[0].trim();
            String doneString = arrOfStr[1].trim();
            String description = arrOfStr[2].trim();
            Boolean isDone;
            if (doneString.equals("0")) {
                isDone = false;
            } else if (doneString.equals("1")) {
                isDone = true;
            } else {
                isDone = null;
            }

            if (isDone != null) {

                LocalDateTime startTime;
                LocalDateTime endTime;

                switch (taskType) {
                case "T":
                    readTask = new Todo(description);
                    readTask.setStatus(isDone);
                    break;
                case "D":
                    endTime = LocalDateTime.parse(arrOfStr[3].trim());
                    readTask = new Deadline(description, endTime);
                    readTask.setStatus(isDone);
                    break;
                case "E":
                    startTime = LocalDateTime.parse(arrOfStr[3].trim());
                    endTime = LocalDateTime.parse(arrOfStr[4].trim());
                    readTask = new Event(description, startTime, endTime);
                    readTask.setStatus(isDone);
                    break;
                default:
                    readTask = null;
                }
            }
        } catch (DateTimeParseException e) {
            return readTask = null;
        }

        return readTask;
    }

    /**
     * Converts a task object into a string representing its contents in the format used in the save file.
     *
     * @param task the task to convert
     * @return the string representing the task's contents
     */
    public String parseTaskToWrite(Task task) {
        Boolean isDone = task.getStatus();
        String taskType = task.getTaskType();
        String encodedBooleanIsDone;
        String description = task.getTaskDescription();
        LocalDateTime startTime;
        LocalDateTime endTime;
        String parseResult = null;

        if (isDone) {
            encodedBooleanIsDone = "1";
        } else {
            encodedBooleanIsDone = "0";
        }

        switch (taskType) {
        case "T":
            parseResult = taskType + " | " + encodedBooleanIsDone + " | " + description;
            break;
        case "D":
            endTime = ((Deadline) task).getEndTime();
            parseResult = taskType + " | " + encodedBooleanIsDone + " | " + description + " | " + endTime;
            break;
        case "E":
            startTime = ((Event) task).getStartTime();
            endTime = ((Event) task).getEndTime();
            parseResult = taskType + " | " + encodedBooleanIsDone + " | " + description + " | " + startTime + " | " + endTime;
            break;
        default:
            parseResult = "Error";
        }

        return parseResult;
    }

}
