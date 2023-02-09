public class Task {
    private String name;
    private boolean isDone;
    private static int numTask;
    public Task(String name) {
        this.name = name;
        this.isDone = false;
        numTask++;
    }

    public String getName() {
        return name;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void setStatus(boolean status) {
        this.isDone = status;
    }
    
    public String toString() {
        return "["+getStatusIcon()+"] "+getName();
    }
    
    public void addMessage() {
        System.out.println("\t____________________________________________________________\r\n"
        		+ "\t Got it. I've added this task:\r\n"
        		+ "\t  " + toString() +"\r\n"
        	    + "\t Now you have " +numTask+ " tasks in the list.\r\n"
                + "\t____________________________________________________________\r\n");
    }

    public void markMessage() {
    	if(isDone) {
    		System.out.println("\t____________________________________________________________\r\n"
                    + "\t Nice! I've marked this task as done:\r\n"
                    + "\t " + toString() + "\r\n"
                    + "\t____________________________________________________________\r\n");
    	}else {
    		System.out.println("\t____________________________________________________________\r\n"
                    + "\t OK, I've marked this task as not done yet:\r\n"
                    + "\t " + toString() + "\r\n"
                    + "\t____________________________________________________________\r\n");
    	}   
    }
}
