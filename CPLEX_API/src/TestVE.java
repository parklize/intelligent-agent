import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.cplex.IloCplex;


public class TestVE {
	
	public static void cplexSolvoer(){
		
		try {
			IloCplex cplex = new IloCplex();
			
			IloNumVar[] x = cplex.numVarArray(3, 0.0, 100.0);
		
			
			IloNumExpr expr = cplex.sum(x[0],cplex.prod(2.0, x[1]),cplex.prod(3.0, x[2]));
			IloObjective obj = cplex.maximize(expr);
			cplex.add(obj);
			
			cplex.addLe(cplex.sum(cplex.prod(-1.0, x[0]),cplex.prod(1.0, x[1]),cplex.prod(1.0, x[2])), 20);
			cplex.addLe(cplex.sum(cplex.prod(1.0, x[0]),cplex.prod(-3.0, x[1]),cplex.prod(1.0, x[2])), 30.0);
		

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
