import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Duke {
    public static void main(String[] args) throws ArgumentCannotBeNullException {
        ArrayList<Task> taskList = new ArrayList<Task>();
        String taskName = "";
        Scanner sc = new Scanner(System.in);
        System.out.println("\t____________________________________________________________\r\n"
                + "\t Hello! I'm Duke\r\n"
                + "\t What can I do for you?\r\n"
                + "\t____________________________________________________________\r\n");

        while (!taskName.equals("bye")) {
            taskName = sc.next();
            if (!taskName.equals("bye")) {
                int indexTask;
                Task newTask;
                String prompt;
                String description;
                try {
                	switch (taskName) {
                    case "list":
                        System.out.println("\t____________________________________________________________\r\n"
                                + "\t Here are the tasks in your list:");
                        for (int i = 0; i < taskList.size(); i++) {
                            System.out.println("\t " + (i + 1) + ". " + taskList.get(i).toString());
                        }
                        System.out.println("\n\t____________________________________________________________\r\n");
                        break;

                    case "todo":
                    	try {
                    		prompt = sc.nextLine();
                    		if(prompt=="") {
                    			throw new PromptCannotBeEmptyException();
                    		}
                    		
                    		newTask = new Todo(prompt.trim());
                            newTask.addMessage();
                            taskList.add(newTask);
                    	}catch(PromptCannotBeEmptyException e) {
                    		System.out.println("\t____________________________________________________________\r\n"
                                    + "\t OOPS!!! The description of a todo cannot be empty.\r\n"
                                    + "\t____________________________________________________________");
                    	}
                        break;

                    case "deadline":
                    	try {
                    		prompt = sc.nextLine();
                    		if(prompt=="") {
                    			throw new PromptCannotBeEmptyException();
                    		}
                    		
                    		int dividerPosition = prompt.indexOf("/by");
                            description = prompt.substring(0, dividerPosition).trim();
                            String by = prompt.substring(dividerPosition + 3).trim();
                            newTask = new Deadline(description, by);
                            newTask.addMessage();
                            taskList.add(newTask);
                    	}catch(PromptCannotBeEmptyException e) {
                    		System.out.println("\t____________________________________________________________\r\n"
                                    + "\t OOPS!!! The description of a deadline cannot be empty.\r\n"
                                    + "\t____________________________________________________________");
                    	}catch(StringIndexOutOfBoundsException e) {
                    		System.out.println("\t____________________________________________________________\r\n"
                                    + "\t OOPS!!! Please enter the correct deadline format (deadline xxx /by xxx).\r\n"
                                    + "\t____________________________________________________________");
                    	}
                        break;

                    case "event":
                    	try {
                    		prompt = sc.nextLine();
                    		if(prompt=="") {
                    			throw new PromptCannotBeEmptyException();
                    		}
                    		prompt = sc.nextLine();
                            int divider1Position = prompt.indexOf("/from");
                            int divider2Position = prompt.indexOf("/to");
                            description = prompt.substring(0, divider1Position).trim();
                            String from = prompt.substring(divider1Position + 5, divider2Position).trim();
                            String to = prompt.substring(divider2Position + 3).trim();
                            newTask = new Event(description, from, to);
                            newTask.addMessage();
                            taskList.add(newTask);
                    	}catch(PromptCannotBeEmptyException e) {
                    		System.out.println("\t____________________________________________________________\r\n"
                                    + "\t OOPS!!! The description of a event cannot be empty.\r\n"
                                    + "\t____________________________________________________________");
                    	}catch(StringIndexOutOfBoundsException e) {
                    		System.out.println("\t____________________________________________________________\r\n"
                                    + "\t OOPS!!! Please enter the correct deadline format (event xxx /from xxx /to xxx).\r\n"
                                    + "\t____________________________________________________________");
                    	}
                        break;

                    case "mark":
                    	try {
                    		indexTask = sc.nextInt() - 1;
                            taskList.get(indexTask).setStatus(true);
                            taskList.get(indexTask).markMessage();
                    	}catch (InputMismatchException e) {
                    		System.out.println("\t____________________________________________________________\r\n"
                                    + "\t OOPS!!! The task index must be an integer.\r\n"
                                    + "\t____________________________________________________________");
                    		sc.nextLine();
                    	}catch (IndexOutOfBoundsException e) {
                    		System.out.println("\t____________________________________________________________\r\n"
                                    + "\t OOPS!!! The task index is not found.\r\n"
                                    + "\t____________________________________________________________");
                    	}
                        
                        break;

                    case "unmark":
                        indexTask = sc.nextInt() - 1;
                        taskList.get(indexTask).setStatus(false);
                        taskList.get(indexTask).markMessage();
                        break;
                        
                    default:
                    	throw new ArgumentCannotBeNullException();
                    }
                	
                }catch(ArgumentCannotBeNullException e){
                	System.out.println("\t____________________________________________________________\r\n"
                            + "\t OOPS!!! I'm sorry, but I don't know what that means :-(\r\n"
                            + "\t____________________________________________________________");
                }
                
            }
        }
        System.out.println("\t____________________________________________________________\r\n"
                + "\t Bye. Hope to see you again soon!\r\n"
                + "\t____________________________________________________________");
    }
}
