import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.cplex.IloCplex;


public class Test01 {
	public static void main(String[] args) {

		try{
			
			IloCplex cplex = new IloCplex();
			IloNumVar[] x = cplex.numVarArray(3, 0.0, 100.0);
			
			// create a model and solve it
			IloLinearNumExpr expr = cplex.linearNumExpr();
			expr.addTerm(1.0, x[0]);
			expr.addTerm(2.0, x[1]);
			expr.addTerm(3.0, x[2]);
			
			IloObjective obj = cplex.maximize(expr);
			cplex.add(obj);
			
			 cplex.addLe(cplex.sum(x[0], x[1], x[2]), 20);

			// slove the problem
			cplex.solve();
			
			// print solution
			double objval = cplex.getObjValue();
			double[] xval = cplex.getValues(x);
			
			System.out.println(xval[0]);
			System.out.println(xval[1]);
			System.out.println(xval[2]);
			
// sysout obj value
			System.out.println(objval);
			
		}catch(IloException e){
			System.err.println("Concert exception caught: " + e);
		}
	}
}
