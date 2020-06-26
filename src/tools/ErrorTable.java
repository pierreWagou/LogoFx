package tools;

import java.util.HashMap;

public class ErrorTable extends HashMap<Integer, String> {

	private static final long serialVersionUID = 1L;
	
	public ErrorTable() {
		put(0, "");
		put(-1, "Bad expression error");
		put(-2, "No postion stored");
		put(-3, "Number of arguments error");
		put(-4, "Function or procedure does not exist");
		put(-5, "Division by 0 error");
		put(-6, "No loop error");
		put(-7, "Variable does not exist");
	}

}
