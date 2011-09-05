import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.cplex.IloCplex;


public class TestVE {
	
	public static void cplexSolvoer(){
		
		try {
			IloCplex cplex = new IloCplex();
			
			IloNumVar[] x = cplex.numVarArray(10, 0.0, Double.MAX_VALUE);
			IloNumVar[] y = cplex.numVarArray(4, 0.0, Double.MAX_VALUE);
		
			IloLinearNumExpr expr = cplex.linearNumExpr();
			expr.addTerm(30, x[0]);//x11
			expr.addTerm(40, x[1]);//x12
			expr.addTerm(50, x[2]);//x13
			expr.addTerm(60, x[3]);//x14
			expr.addTerm(30, x[4]);//x22
			expr.addTerm(40, x[5]);//x23
			expr.addTerm(50, x[6]);//x24
			expr.addTerm(30, x[7]);//x33
			expr.addTerm(40, x[8]);//x34
			expr.addTerm(50, x[9]);//x44
			
			IloObjective obj = cplex.maximize(expr);
			cplex.add(obj);
			
			cplex.addLe(cplex.sum(x[0],x[1],x[2],x[3]), 50);
			cplex.addLe(cplex.sum(x[4],x[5],x[6]), 60.0);
			cplex.addLe(cplex.sum(x[7],x[8]), 65);
			cplex.addLe(x[9], 50);
			
			cplex.addEq(cplex.sum(y[0],y[1],y[2],y[3]), 10.0);
			
			cplex.addGe(cplex.sum(x[0],y[0]), 50);
			cplex.addGe(cplex.sum(x[1],), arg1)
		

			if(cplex.solve()){
				cplex.output().println("Solution status = "+cplex.getStatus());
				cplex.output().println("Solution value = "+cplex.getObjValue());
				
				double[] val = cplex.getValues(x);
				int ncols = cplex.getNcols();
				
				for(int j=0;j<ncols;j++){
					cplex.output().println("Column: " + j + " Value = " + val[j]);
				}
				cplex.end();
			}
			
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public static void main(String[] args) {
		TestVE.cplexSolvoer();
	}
}
