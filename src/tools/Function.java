package tools;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

import logoparsing.LogoParser.ExprContext;

public class Function extends Procedure {

	private ExprContext retour;
	
	public Function(String name, ArrayList<String> args, ParseTree instructions, ExprContext retour) {
		super(name, args, instructions);
		this.retour = retour; 
	}
	
	public ExprContext getRetour() {
		return retour;
	}
	

}
