package tools;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

public class Procedure {
	
	private String name;
	private ArrayList<String> args;
	private ParseTree instructions;
	
	public Procedure(String name, ArrayList<String> args, ParseTree instructions) {
		this.name = name;
		this.args = args;
		this.instructions = instructions;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getParams() {
		return args;
	}
	
	public ParseTree getInstructions() {
		return instructions;
	}
}
