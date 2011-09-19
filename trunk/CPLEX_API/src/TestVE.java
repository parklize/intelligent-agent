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

			/* 
			 * Objective part 
			 */
			IloLinearNumExpr expr = cplex.linearNumExpr();
			
			// produce week 1,
			expr.addTerm(30, x[0]);//x11
			expr.addTerm(40, x[1]);//x12
			expr.addTerm(50, x[2]);//x13
			expr.addTerm(60, x[3]);//x14
			
			// produce week 2
			expr.addTerm(30, x[4]);//x22
			expr.addTerm(40, x[5]);//x23
			expr.addTerm(50, x[6]);//x24
			
			// produce week 3
			expr.addTerm(30, x[7]);//x33
			expr.addTerm(40, x[8]);//x34
			
			// produce week 4
			expr.addTerm(30, x[9]);//x44
			
			expr.addTerm(10, y[0]);//y1
			expr.addTerm(20, y[1]);//y2
			expr.addTerm(30, y[2]);//y3
			expr.addTerm(40, y[3]);//y4
			
			IloObjective obj = cplex.minimize(expr);
			cplex.add(obj);
			
			/*
			 * Subject to part
			 */
			cplex.addLe(cplex.sum(x[0],x[1],x[2],x[3]), 50);
			cplex.addLe(cplex.sum(x[4],x[5],x[6]), 60.0);
			cplex.addLe(cplex.sum(x[7],x[8]), 65);
			cplex.addLe(x[9], 50);
			
			cplex.addEq(cplex.sum(y[0],y[1],y[2],y[3]), 10.0);
			
			cplex.addGe(cplex.sum(x[0],y[0]), 50);
			cplex.addGe(cplex.sum(x[1],x[4],y[1]), 60);
			cplex.addGe(cplex.sum(x[2],x[5],x[7]), 65);
			cplex.addGe(cplex.sum(x[3],x[6],x[8],x[9]), 60);
System.out.println(cplex.toString());	
			/*
			 * Solution part
			 */
			if(cplex.solve()){
				cplex.output().println("Solution status = "+cplex.getStatus());
				cplex.output().println("Solution value = "+cplex.getObjValue());
				
				double[] val = cplex.getValues(x);
				int ncols = cplex.getNcols();
				
				double[] val1 = cplex.getValues(y);
				int ncols1 = cplex.getNcols();
				
				
				for(int j=0;j<x.length;j++){
					cplex.output().println("Column: " + j + " Value = " + val[j]);
				}
				
				for(int k=0;k<y.length;k++){
					cplex.output().println("Column1: " + k + " Value = " + val1[k]);
				}
				cplex.end();
			}
			
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * main method
	 */
	public static void main(String[] args) {
		TestVE.cplexSolvoer();
	}
}
