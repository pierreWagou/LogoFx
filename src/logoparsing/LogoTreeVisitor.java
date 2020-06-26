package logoparsing;

import java.util.ArrayList;
import java.util.Stack;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import logoparsing.LogoParser.AppelfonctionContext;
import logoparsing.LogoParser.AppelprocedureContext;
import logoparsing.LogoParser.ArgContext;
import logoparsing.LogoParser.AvContext;
import logoparsing.LogoParser.BcContext;
import logoparsing.LogoParser.BoolContext;
import logoparsing.LogoParser.CosinusContext;
import logoparsing.LogoParser.DonneContext;
import logoparsing.LogoParser.FcapContext;
import logoparsing.LogoParser.FccContext;
import logoparsing.LogoParser.FloatContext;
import logoparsing.LogoParser.FonctionContext;
import logoparsing.LogoParser.FposContext;
import logoparsing.LogoParser.HasardContext;
import logoparsing.LogoParser.IdentificateurContext;
import logoparsing.LogoParser.LcContext;
import logoparsing.LogoParser.LoopContext;
import logoparsing.LogoParser.MoveContext;
import logoparsing.LogoParser.ParentheseContext;
import logoparsing.LogoParser.ProcedureContext;
import logoparsing.LogoParser.ProduitContext;
import logoparsing.LogoParser.ReContext;
import logoparsing.LogoParser.RepeteContext;
import logoparsing.LogoParser.SiContext;
import logoparsing.LogoParser.SinusContext;
import logoparsing.LogoParser.SommeContext;
import logoparsing.LogoParser.StoreContext;
import logoparsing.LogoParser.TantqueContext;
import logoparsing.LogoParser.TdContext;
import logoparsing.LogoParser.TgContext;
import tools.ErrorTable;
import tools.Function;
import tools.FunctionTable;
import tools.Procedure;
import tools.ProcedureTable;
import tools.SymbolTable;

public class LogoTreeVisitor extends LogoBaseVisitor<Integer> {
	Traceur traceur = new Traceur();
	StringProperty log = new SimpleStringProperty();
	Stack<Double> loopStack = new Stack<Double>();
	Stack<Traceur> traceurStack = new Stack<Traceur>();
	Stack<SymbolTable> symbolStack = new Stack<SymbolTable>();
	ProcedureTable procedureTable = new ProcedureTable();
	ErrorTable errorTable = new ErrorTable();
	FunctionTable functionTable = new FunctionTable();

	public LogoTreeVisitor() {
		symbolStack.push(new SymbolTable());
		
	}
	
    public StringProperty logProperty() {
    	return log;
    }
    
	public Traceur getTraceur() {
		return traceur;
	}

	/*
	 * Map des attributs associés à chaque noeud de l'arbre 
	 * key = node, value = valeur de l'expression du node
	 */
	ParseTreeProperty<Double> atts = new ParseTreeProperty<Double>();

	public void setValue(ParseTree node, double value) {
		atts.put(node, value);
	}

	public double getValue(ParseTree node) {
		Double value = atts.get(node);
		if (value == null) {
			throw new NullPointerException();
		}
		return value;
	}

// Instructions de base	

	@Override
	public Integer visitAv(AvContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			traceur.avance(bilan._2);
			log.setValue("Avance de  " + bilan._2 + "\n");
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitRe(ReContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			traceur.avance(-bilan._2);
			log.setValue("Recule de  " + bilan._2 + "\n");
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitTd(TdContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			traceur.td(bilan._2);
			log.setValue("Tourne à droite de  " + bilan._2 + "\n");	
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1; 
	}

	@Override
	public Integer visitTg(TgContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			traceur.tg(bilan._2);
			log.setValue("Tourne à gauche de  " + bilan._2 + "\n");
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitLc(LcContext ctx) {
		traceur.setPosc(false);
		log.setValue("Lève le crayon \n");
		return 0;
	}
	
	@Override
	public Integer visitBc(BcContext ctx) {
		traceur.setPosc(true);
		log.setValue("Baisse le crayon \n");
		return 0;
	}
	
	@Override
	public Integer visitFpos(FposContext ctx) {
		Binome<Double> bilan1 = evaluate(ctx.expr(0));
		Binome<Double> bilan2 = evaluate(ctx.expr(1));
		if (bilan1._1 == 0 && bilan2._1 == 0) {
			traceur.fpos(bilan1._2, bilan2._2);
			log.setValue("Posiiton en (" + bilan1._2 + " , " + bilan2._2 + ") \n");
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitFcc(FccContext ctx) {
		Binome<Double> bilan1 = evaluate(ctx.expr(0));
		Binome<Double> bilan2 = evaluate(ctx.expr(1));
		Binome<Double> bilan3 = evaluate(ctx.expr(2));
		if (bilan1._1 == 0 && bilan2._1 == 0 && bilan3._1 == 0) {
			Color color = new Color(bilan1._2, bilan2._2, bilan3._2, 1);
			traceur.setColor(color);
			log.setValue("Change la couleur en  " + color + "\n");
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitFcap(FcapContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			traceur.setAngle(bilan._2);
			log.setValue("Oriente vers  " + bilan._2 + "\n");
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitRepete(RepeteContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			for(double i=0;i<bilan._2;i++) {
				loopStack.push(i);
				visit(ctx.liste_instructions());
				loopStack.pop();
			}
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	
	@Override
	public Integer visitStore(StoreContext ctx) {
		traceurStack.push(new Traceur(traceur));
		log.setValue("Position du traceur stockée \n");
		return 0;
	}

	@Override
	public Integer visitMove(MoveContext ctx) {
		if(traceurStack.peek()!=null) {
			Traceur head = traceurStack.pop();
			traceur.fpos(head.getPosx(), head.getPosy());
			traceur.setAngle(head.getAngle());
			log.setValue("Traceur déplacé en (" + head.getPosx() + ", " + head.getPosy() + ") avec un angle de " +  head.getAngle() + " \n");
			return 0;
		}
		log.setValue(errorTable.get(-2));
		return -2;	
	}
	
	@Override
	public Integer visitDonne(DonneContext ctx) {
		String key = ctx.IDENT().getText();
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			symbolStack.peek().put(key, bilan._2);
			log.setValue("Affectation de " + ctx.expr().getText() + " à la variable " + key + "\n");
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitSi(SiContext ctx) {
		visitBool(ctx.bool());
		if(getValue(ctx.bool())==1) {
			visit(ctx.liste_instructions(0));
		}
		else {
			if((ctx.liste_instructions()).size()>1) {
				visit(ctx.liste_instructions(1));
			}
		}
		return 0;
	}

	@Override
	public Integer visitTantque(TantqueContext ctx) {
		visitBool(ctx.bool());
		while(getValue(ctx.bool())==1) {
			visit(ctx.liste_instructions());
			visitBool(ctx.bool());
		}
		return 0;
	}
	
	
	@Override
	public Integer visitAppelprocedure(AppelprocedureContext ctx) {
		String name = ctx.IDENT().getText();
		if(procedureTable.containsKey(name)) {
			Procedure procedure = procedureTable.get(name);
			SymbolTable localSymbolTable = new SymbolTable();
			if(ctx.expr().size()==procedure.getParams().size()) {
				for(int i=0;i<ctx.expr().size();i++) {
					visit(ctx.expr(i));
					localSymbolTable.put(procedure.getParams().get(i), getValue(ctx.expr(i)));
				}
				symbolStack.push(localSymbolTable);
				visit(procedure.getInstructions());
				symbolStack.pop();
				return 0;
			}
			log.setValue(errorTable.get(-3));
			return -3;
		}
		log.setValue(errorTable.get(-4));
		return -4;
	}

	@Override
	public Integer visitAppelfonction(AppelfonctionContext ctx) {
		String name = ctx.IDENT().getText();
		if(functionTable.containsKey(name)) {
			Function function = functionTable.get(name);
			SymbolTable localSymbolTable = new SymbolTable();
			if(ctx.expr().size()==function.getParams().size()) {
				for(int i=0;i<ctx.expr().size();i++) {
					visit(ctx.expr(i));
					localSymbolTable.put(function.getParams().get(i), getValue(ctx.expr(i)));
				}
				symbolStack.push(localSymbolTable);
				if(function.getInstructions()!=null) {
					visit(function.getInstructions());
				}
				Binome<Double> bilan = evaluate(function.getRetour());
				symbolStack.pop();
				if(bilan._1 == 0) {
					setValue(ctx, bilan._2);
					return 0;
				}
				log.setValue(errorTable.get(-1));
				return -1;
			}
			log.setValue(errorTable.get(-3));
			return -3;
		}
		log.setValue(errorTable.get(-4));
		return -4;
	}
	
// Expressions

	@Override
	public Integer visitParenthese(ParentheseContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			setValue(ctx, bilan._2);
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}

	
	@Override
	public Integer visitSomme(SommeContext ctx) {
		Binome<Double> bilan1 = evaluate(ctx.expr(0));
		Binome<Double> bilan2 = evaluate(ctx.expr(1));
		String operateur = ctx.getChild(1).getText();
		if (bilan1._1 == 0 && bilan2._1 == 0) {
			if (operateur.equals("+")) {
				setValue(ctx, bilan1._2 + bilan2._2);
			}
			else if (operateur.equals("-")) {
				setValue(ctx, bilan1._2 - bilan2._2);
			}
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitProduit(ProduitContext ctx) {
		Binome<Double> bilan1 = evaluate(ctx.expr(0));
		Binome<Double> bilan2 = evaluate(ctx.expr(1));
		String operateur = ctx.getChild(1).getText();
		if (bilan1._1 == 0 && bilan2._1 == 0) {
			if (operateur.equals("*")) {
				setValue(ctx, bilan1._2 * bilan2._2);
			}
			else if (operateur.equals("/")) {
				if (bilan2._2!=0) {
					setValue(ctx, bilan1._2 / bilan2._2);
				}
				else {
					setValue(ctx, bilan1._2);
					log.setValue(errorTable.get(-5));
					return -5;
				}
			}
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}

	@Override
	public Integer visitHasard(HasardContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			setValue(ctx, Math.random()*bilan._2);
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}

	@Override
	public Integer visitCosinus(CosinusContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			setValue(ctx, Math.cos(bilan._2));
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}

	@Override
	public Integer visitSinus(SinusContext ctx) {
		Binome<Double> bilan = evaluate(ctx.expr());
		if (bilan._1 == 0) {
			setValue(ctx, Math.sin(bilan._2));
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}

	@Override
	public Integer visitLoop(LoopContext ctx) {
		if(loopStack.peek()!=null) {
			double loop = loopStack.peek();
			setValue(ctx, loop);
			return 0;
		}
		log.setValue(errorTable.get(-6));
		return -6;
	}

	@Override
	public Integer visitFloat(FloatContext ctx) {
		String floatText = ctx.FLOAT().getText();
		setValue(ctx, Double.valueOf(floatText));
		return 0;
	}
	
	@Override
	public Integer visitBool(BoolContext ctx) {
		Binome<Double> bilan1 = evaluate(ctx.expr(0));
		Binome<Double> bilan2 = evaluate(ctx.expr(1));
		String operateur = ctx.getChild(1).getText();
		if (bilan1._1 == 0 && bilan2._1==0) {
			switch(operateur) {
			case "=":
				setValue(ctx, (bilan1._2.equals(bilan2._2)) ? 1:0);
				break;
			case "!=":
				setValue(ctx, (bilan1._2!=bilan2._2) ? 1:0);
				break;
			case "<":
				setValue(ctx, (bilan1._2<bilan2._2) ? 1:0);
				break;
			case ">":
				setValue(ctx, (bilan1._2>bilan2._2) ? 1:0);
				break;
			case "<=":
				setValue(ctx, (bilan1._2<=bilan2._2) ? 1:0);
				break;
			case ">=":
				setValue(ctx, (bilan1._2>=bilan2._2) ? 1:0);
				break;
			}
			return 0;
		}
		log.setValue(errorTable.get(-1));
		return -1;
	}
	
	@Override
	public Integer visitIdentificateur(IdentificateurContext ctx) {
		String name = ctx.IDENT().getText();
		SymbolTable symbolTable = symbolStack.peek();
		if(symbolTable.containsKey(name)) {
			setValue(ctx, symbolTable.get(name));
			return 0;
		}
		log.setValue(errorTable.get(-7));
		return -7;
	}
	
	@Override
	public Integer visitProcedure(ProcedureContext ctx) {
		String nom = ctx.IDENT().getText();
		ArrayList<String> listeParams = new ArrayList<String>();
		for (ArgContext arg: ctx.arg()) {
			listeParams.add(arg.IDENT().getText());
		}
		ParseTree instructions = ctx.liste_instructions();
		Procedure procedure = new Procedure(nom, listeParams, instructions);
		procedureTable.put(nom, procedure);
		return 0;
	}

	@Override
	public Integer visitFonction(FonctionContext ctx) {
		String nom = ctx.IDENT().getText();
		ArrayList<String> listeParams = new ArrayList<String>();
		for (ArgContext arg: ctx.arg()) {
			listeParams.add(arg.IDENT().getText());
		}
		ParseTree instructions = ctx.liste_instructions();
		Function procedure = new Function(nom, listeParams, instructions, ctx.expr());
		functionTable.put(nom, procedure);
		return 0;
	}

	private Binome<Double> evaluate(ParseTree expr) {
		Binome<Double> res = new Binome<>();
		res._1 = visit(expr);
		res._2 = res.isValid() ? getValue(expr) : Double.POSITIVE_INFINITY;
		return res;
	}

	private class Binome<T> {
		public Integer _1;
		public T _2;

		public boolean isValid() {
			return _1 == 0;
		}
	}
}
