import java.io.*;
import java.util.*;
/*PROBLEM: CHECK Why State 10 and 11 are happening in state 10 itself like given in the sample output states
 * Things to do: id-- check for >0 to stop underflowing in backtracking*/
public class Kenken {
	static int count = 0;
    public static class Puzzle{
	    static int N;
	    static String printedBoard;
	    static long noOfStates;
	    static int time;
	    static float rate;
	    static int[][] result; // Maximum numbers required 5x5 puzzle = 25 digits
	    static int[][] operations; // Maximum numbers required 5x5 puzzle = 25 digits
	    static String[] constraint;
	    static String constraints;
	    static ArrayList<LinkedList<Integer>> openLists;
	    static ArrayList<LinkedList<Integer>> closedLists;
	    static boolean[][] constants;
	    static int[][] uniques;
	    static int[][] computes;
	    Puzzle(String puzzle)
	    {
	    	N = puzzle.charAt(0) - '0';
	    	LinkedList<Integer> object = new LinkedList<Integer>();
	    	openLists = new ArrayList<LinkedList<Integer>>();
	    	closedLists = new ArrayList<LinkedList<Integer>>();
		    noOfStates = 0;
			result = new int[N][N];
	    	operations = new int[N][N];
	    	constants = new boolean[N][N];
		    uniques = new int[N][N];
		    computes = new int[N][N];
	    	
	    	for(int i = 0; i< N; i++)
	    		object.add(i+1);
	    	
	    	for(int i = 0; i<N; i++)
	    	{
	    		for(int j = 0; j<N; j++)
   			 	{
	    			openLists.add(object);
	    			closedLists.add(new LinkedList<Integer>());
	    			result[i][j] = 0;
	    			constants[i][j] = false;
	    			uniques[i][j] = 0;
	    			computes[i][j] = 0;
	    	 	}
	    	}
	    	constraints = puzzle.substring(3);
	    	constraint = constraints.split("\n");
	    	//System.out.println("\nPuzzle created:\n"+ constraints);
	    	mapOperations();	
	    }
	    public static void printOpenLists()
	    {
	    	for(int i = 0; i<N; i++)
	    		for(int j =0; j<N; j++)
	    		{
	    			System.out.print("i="+i+",j="+j+":");
	    			System.out.print("Open: " + openLists.get(i*N+j) + " Closed: " + closedLists.get(i*N+j) + "\n");
	    		}
	    }
	    public void solve0(boolean debug)
	    {
	    	noOfStates++;
	    	int[][] mockSolution = {{2,4,3,1},{1,3,4,2},{4,1,2,3},{3,2,1,4}};
	    	for(int i = 0; i<N; i++)
    			for(int j=0; j<N;j++)
    				result[i][j] = mockSolution[i][j];
	    	if(isSolved(false,-1)==0) 
    			solve0(debug);
	    	else if(debug)
    			printBoard(1);
	    }
	    public void solve1(boolean debug)
	    {
	    	while(isSolved(false,-1)==0)
	    	{
	    		noOfStates++;
	    		boolean flag = false;
	    		for(int i = N-1; i>=0; i--)
	    			{
		    			for(int j = N-1; j>=0; j--)
		    				if(result[i][j]<N)
		    				{
	    						result[i][j]++;
	    						flag = true;
	    						break;
		    				}
		    				else
		    				{
		    					result[i][j] = 1;
		    				}
		    			if(flag) 
		    				break;
	    			}
	    	}
	    	if(debug)
	    		printBoard(1);
	    }
	    public void solve2(boolean debug)
	    {
	    	int resultFlag = 0;
	    	int [][] ordering = new int[2][N*N];
	    	for(int i = 0; i < N*N; i++)
	    		{
	    			ordering[0][i] = i/N;
	    			if(debug)System.out.print(ordering[0][i]);
	    		}
	    	if(debug) System.out.println();	    	
	    	for(int i = 0; i < N*N; i++)
	    		{
	    			ordering[1][i] = i%N;
	    			if(debug) System.out.print(ordering[1][i]);
	    		}
	    	
	    	int id = 0;
	    	int flag = 0;
	    	while(resultFlag != 1)
	    	{
    			if(flag==1 && id!=N*N-1)
    				id++;
    			if(result[ordering[0][id]][ordering[1][id]]>=N) //backtracking
    				{
    				
					  result[ordering[0][id]][ordering[1][id]] = 0;
					  id--;
					  flag = 0;
    				}
    			else
    				{
	    				result[ordering[0][id]][ordering[1][id]]++;
	    				
			    		if(checkUniquenessConstraint(ordering[0][id]+1,ordering[1][id]+1)==1)
		    				{
			    				flag = isPartlySolved(-1);
			    				if(debug) System.out.println("flag = " + flag);
		    				}
		    			else
		    				flag = 0;
			    		noOfStates++;	
			    		if(debug) 
			    			{
				    			printBoard(flag);
				    			System.out.println(ordering[0][id]);
				    			System.out.println(ordering[1][id]);
			    			}
			    			
		    			if(id==N*N-1)
		    				resultFlag = isSolved(debug,-1);	
			    		//promptEnterKey();
    				}
	    	}
    			    	
	    }
	    public void solve3(boolean debug)
	    {
	    	int resultFlag = 0;
	    	int [][] ordering = new int[2][N*N];
	    	boolean backtrack = false;
	    	for(int i = 0; i < N*N; i++)
	    		{
	    			ordering[0][i] = i/N;
	    			if(debug)System.out.print(ordering[0][i]);
	    		}
	    	if(debug) System.out.println();	    	
	    	for(int i = 0; i < N*N; i++)
	    		{
	    			ordering[1][i] = i%N;
	    			if(debug) System.out.print(ordering[1][i]);
	    		}
	    	
	    	int id = 0,flag = 0;
	    	while(resultFlag != 1)
	    	{
	    		if(flag==1 && id!=N*N-1)
					id++;
	    		int temp = getNumber(ordering[0][id], ordering[1][id]);
	    		if(debug)
	    		{
	    			System.out.println("---------------------------------------- ");
	    			System.out.println("id: "+ id);
	    			System.out.println("ordering: "+ ordering[0][id] +"," +ordering[1][id]);
	    			System.out.println("backtrack: "+ backtrack +",temp: " + temp);
	    		}
				if(temp != -1 || backtrack)
				{
					if(backtrack)
					{
						if(result[ordering[0][id]][ordering[1][id]]<N)
						{
							LinkedList<Integer> object = getOpenList(ordering[0][id], ordering[1][id]);
							int location = object.indexOf(new Integer(result[ordering[0][id]][ordering[1][id]]));
							if(object.size()-1>location)
							{   
								int oldValue = result[ordering[0][id]][ordering[1][id]];
								int number = object.get(object.indexOf(new Integer(oldValue))+1);
							    result[ordering[0][id]][ordering[1][id]] = number;
								updateOpenLists(ordering[0][id], ordering[1][id],result[ordering[0][id]][ordering[1][id]],oldValue);
								if(checkUniquenessConstraint(ordering[0][id]+1,ordering[1][id]+1)==1)
			    				{
				    				flag = isPartlySolved(-1);
				    				if(debug) System.out.println("flag = " + flag);
			    				}
								else
									flag = 0;
					    		noOfStates++;	
					    		if(id==N*N-1)
				    				resultFlag = isSolved(debug,-1);
					    		if(flag == 1 || openLists.get(ordering[0][id]*N+ordering[1][id]).size()<2) 
					    		{
					    			LinkedList<Integer> object1 = new LinkedList<Integer>();
					    			openLists.set(ordering[0][id]*N+ordering[1][id], object1);
					    		}
					    		if(flag == 1)
					    			backtrack = false;
							}
							else
							{
							  int oldValue = result[ordering[0][id]][ordering[1][id]];
							  result[ordering[0][id]][ordering[1][id]] = 0;
							  updateOpenLists(ordering[0][id], ordering[1][id],0, oldValue);
							  id--;
							  flag = 0;
							  backtrack = true;
							}
							
						}
						else
							backtrack = false;
						
					}
					else
					{
						int oldValue = result[ordering[0][id]][ordering[1][id]];
						result[ordering[0][id]][ordering[1][id]] = temp;
						updateOpenLists(ordering[0][id],ordering[1][id],result[ordering[0][id]][ordering[1][id]],oldValue);
						if(checkUniquenessConstraint(ordering[0][id]+1,ordering[1][id]+1)==1)
	    				{
		    				flag = isPartlySolved(-1);
		    				if(debug) System.out.println("flag = " + flag);
	    				}
						else
							flag = 0;
			    		noOfStates++;	
			    		if(id==N*N-1)
		    				resultFlag = isSolved(debug,-1);
			    		if(flag == 1) //openLists.get(ordering[0][id]*N+ordering[1][id]).size()<2)
			    		{
			    		   LinkedList<Integer> object = new LinkedList<Integer>();
			 			   openLists.set(ordering[0][id]*N+ordering[1][id], object);
			    		}
			    		else
			    		{
			    			LinkedList<Integer> object = new LinkedList<Integer>(openLists.get(ordering[0][id]*N+ordering[1][id]));
			    			object.remove(new Integer(result[ordering[0][id]][ordering[1][id]]));
			    	 		openLists.set(ordering[0][id]*N+ordering[1][id], object);
			    		}
			    			
					}
				}
				else
				{
				  int oldValue = result[ordering[0][id]][ordering[1][id]]; 
				  result[ordering[0][id]][ordering[1][id]] = 0;
				  updateOpenLists(ordering[0][id], ordering[1][id],0,oldValue);
				  id--;
				  flag = 0;
				  backtrack = true;
				}	
				if(debug) 
	    		{
				  printBoard(flag);
				  printOpenLists();
				 if(noOfStates < 0) promptEnterKey();
				}
		    }   	
	    }
	    public void solve4(boolean debug)
	    {
	    	int resultFlag = 0;
	    	int [][] ordering = new int[2][N*N];
	    	boolean backtrack = false, multipleTriesFlag = false;
	    	int multipleTriesFlagID = -1;
	    	for(int i = 0; i < N*N; i++)
	    		ordering[0][i] = i/N;
	    	for(int i = 0; i < N*N; i++)
	    		ordering[1][i] = i%N;
	    	
	    	int id = 0,flag = 0;
	    	
	    	fillConstants();
	    	
	    	while(resultFlag != 1)
	    	{
	    		if(debug)
	    			System.out.println("---------------------------------------- ");
	    		if(flag==1 && id!=N*N-1)
					{
	    				id++; //Check the logic, you need to increment atleast once and it should choose the next unfilled cell.
	    				while(result[ordering[0][id]][ordering[1][id]]!=0 && id<N*N-1) //Go to the next zero element on the board
	    					id++;
					}
	    		int temp = getNumberNew(ordering[0][id], ordering[1][id],backtrack);
	    		if(debug)
	    		{
	    			System.out.println("id: "+ id);
	    			System.out.println("ordering: "+ ordering[0][id] +"," +ordering[1][id]);
	    			System.out.println("backtrack: "+ backtrack +",temp: " + temp);
	    			System.out.println("While entering flag = " + flag);
	    		}
				if(temp != -1 || backtrack)
				{
					if(backtrack)
					{
						if(result[ordering[0][id]][ordering[1][id]]<N)
						{
							resetUniqueValues(id);
							LinkedList<Integer> object = getOpenListNew(ordering[0][id], ordering[1][id],true);
							int location = object.indexOf(new Integer(result[ordering[0][id]][ordering[1][id]]));
							if(object.size()-1>location) //If the number is not in the end of the sorted openlist.
							{   
								int number = object.get(object.indexOf(new Integer(result[ordering[0][id]][ordering[1][id]]))+1);
							    result[ordering[0][id]][ordering[1][id]] = number;
								updateOpenListsNew(ordering[0][id], ordering[1][id]);
								if(checkUniquenessConstraint(ordering[0][id]+1,ordering[1][id]+1)==1)
			    				{
				    				flag = isPartlySolved(-1);
				    				if(debug) System.out.println("Updated flag0 = " + flag);
			    				}
								else
									flag = 0;
					    		noOfStates++;	
					    		if(flag == 1)
					    			{
					    				LinkedList<Integer> oldOpenList1 = openLists.get(ordering[0][id]*N+ordering[1][id]);
					    				resetUniqueValues(id);
					    				fillUniqueValues(ordering[0][id],ordering[1][id],id);
					    				openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList1);
					    				flag = isPartlySolved(-1);
					    				if(debug) System.out.println("Updated flag1 = " + flag);
					    				if(flag == 1 || openLists.get(ordering[0][id]*N+ordering[1][id]).size()<2) 
							    		{
							    			LinkedList<Integer> object1 = new LinkedList<Integer>();
							    			openLists.set(ordering[0][id]*N+ordering[1][id], object1);
							    		}
					    				if(flag == 1) backtrack = false;
					    				/*else
					    				{
					    					oldOpenList1.remove(new Integer(result[ordering[0][id]][ordering[1][id]]));
					    					openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList1);
					    				}*/
						    			
					    			}
					    		resultFlag = isSolved(false,-1);
							}
							else
							{
							  id = doBacktrack(ordering,id);
							  flag = 0;
							  backtrack = true;
							}
							
						}
						else
							backtrack = false;
						
					}
					else
					{
						if(multipleTriesFlagID != id) //Reset the flag once the tries have been completed and trial for next number/id has started
						{
							multipleTriesFlagID = -1;
							multipleTriesFlag = false;
						}
						result[ordering[0][id]][ordering[1][id]] = temp;
						resetUniqueValues(id);
						LinkedList<Integer> oldOpenList = openLists.get(ordering[0][id]*N+ordering[1][id]);
						updateOpenListsNew(ordering[0][id],ordering[1][id]);
						if(multipleTriesFlag)
							openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList); //Retain the old open list in which already tried values have been deleted.
						else
							openLists.set(ordering[0][id]*N+ordering[1][id], getOpenListNew(ordering[0][id], ordering[1][id], true)); //previous update would have deleted the open list of current element, so restore it.
						if(checkUniquenessConstraint(ordering[0][id]+1,ordering[1][id]+1)==1)
	    				{
		    				flag = isPartlySolved(-1);
		    				if(debug) System.out.println("Updated flag2 = " + flag);
	    				}
						else
							flag = 0;
			    		noOfStates++;	
			    		if(flag == 1) 
			    		{
			    		   LinkedList<Integer> oldOpenList1 = openLists.get(ordering[0][id]*N+ordering[1][id]);
			    		   resetUniqueValues(id);
		    			   fillUniqueValues(ordering[0][id],ordering[1][id],id);		    				   
		    			   openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList1);
		    			   flag = isPartlySolved(-1);
		    			   if(flag==1)
		    				   {
		    				   	LinkedList<Integer> object = new LinkedList<Integer>();
		    				   	openLists.set(ordering[0][id]*N+ordering[1][id], object);
		    				   }
		    			   else
				    		{ //If the number doesn't solve the problem, remove from the openlist so it is not tried again
				    			LinkedList<Integer> object = new LinkedList<Integer>(openLists.get(ordering[0][id]*N+ordering[1][id]));
				    			LinkedList<Integer> otherObject = new LinkedList<Integer>(closedLists.get(ordering[0][id]*N+ordering[1][id]));
				    			otherObject.add(new Integer(result[ordering[0][id]][ordering[1][id]]));
				    			object.remove(new Integer(result[ordering[0][id]][ordering[1][id]]));
				    			multipleTriesFlag = true;
				    			multipleTriesFlagID = id;
				    	 		openLists.set(ordering[0][id]*N+ordering[1][id], object);
				    	 		closedLists.set(ordering[0][id]*N+ordering[1][id], otherObject);
				    		}  
		    			   if(debug) System.out.println("Updated flag3 = " + flag);
			    		}
			    		else
			    		{ //If the number doesn't solve the problem, remove from the openlist so it is not tried again
			    			LinkedList<Integer> object = new LinkedList<Integer>(openLists.get(ordering[0][id]*N+ordering[1][id]));
			    			LinkedList<Integer> otherObject = new LinkedList<Integer>(closedLists.get(ordering[0][id]*N+ordering[1][id]));
			    			otherObject.add(new Integer(result[ordering[0][id]][ordering[1][id]]));
			    			object.remove(new Integer(result[ordering[0][id]][ordering[1][id]]));
			    			multipleTriesFlag = true;
			    			multipleTriesFlagID = id;
			    	 		openLists.set(ordering[0][id]*N+ordering[1][id], object);
			    	 		closedLists.set(ordering[0][id]*N+ordering[1][id], otherObject);
			    		}
			    		resultFlag = isSolved(false,-1);
					}
				}
				else
				{
				  id = doBacktrack(ordering,id);
				  flag = 0;
				  backtrack = true;
				}	
				if(debug) 
	    		{
				  printBoard(resultFlag);
				  printOpenLists();
				  if(noOfStates < 0) promptEnterKey();
				}
		    }   	
	    }
	    public void solve5(boolean debug)
	    {
	    	int resultFlag = 0;
	    	int [][] ordering = new int[2][N*N];
	    	boolean backtrack = false, multipleTriesFlag = false;
	    	int multipleTriesFlagID = -1;
	    	LinkedList<Integer> oldOpenList = new LinkedList<Integer>();
	    	for(int i = 0; i < N*N; i++)
	    		ordering[0][i] = i/N;
	    	for(int i = 0; i < N*N; i++)
	    		ordering[1][i] = i%N;
	    	
	    	int id = 0,flag = 0;
	    	
	    	fillConstants();
	    	
	    	while(resultFlag != 1)
	    	{
	    		if(debug)
	    			System.out.println("---------------------------------------- ");
	    		if(flag==1 && id!=N*N-1)
					{
	    				id++; //Check the logic, you need to increment atleast once and it should choose the next unfilled cell.
	    				while(result[ordering[0][id]][ordering[1][id]]!=0 && id<N*N-1) //Go to the next zero element on the board
	    					id++;
					}
	    		int temp = getNumberNew(ordering[0][id], ordering[1][id],backtrack);
	    		if(debug)
	    		{
	    			System.out.println("id: "+ id);
	    			System.out.println("ordering: "+ ordering[0][id] +"," +ordering[1][id]);
	    			System.out.println("backtrack: "+ backtrack +",temp: " + temp);
	    			System.out.println("While entering flag = " + flag);
	    		}
				if(temp != -1 || backtrack)
				{
					if(backtrack)
					{
						if(result[ordering[0][id]][ordering[1][id]]<N)
						{
							resetUniqueValues(id);
							resetComputeValues(id);
							LinkedList<Integer> object = getOpenListNew(ordering[0][id], ordering[1][id],true);
							int location = object.indexOf(new Integer(result[ordering[0][id]][ordering[1][id]]));
							if(object.size()-1>location) //If the number is not in the end of the sorted openlist.
							{   
								int number = object.get(object.indexOf(new Integer(result[ordering[0][id]][ordering[1][id]]))+1);
							    if(result[ordering[0][id]][ordering[1][id]]!=0)
							    	emptyClosedLists(ordering,id);
								result[ordering[0][id]][ordering[1][id]] = number;
							    oldOpenList = openLists.get(ordering[0][id]*N+ordering[1][id]);
								updateOpenListsNew(ordering[0][id], ordering[1][id]);
								openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList);
								if(checkUniquenessConstraint(ordering[0][id]+1,ordering[1][id]+1)==1)
			    				{
									oldOpenList = openLists.get(ordering[0][id]*N+ordering[1][id]);
									flag = isPartlySolved(id);
									openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList);
				    				if(debug) System.out.println("Updated flag0 = " + flag);
			    				}
								else
									flag = 0;
					    		noOfStates++;	
					    		if(flag == 1)
					    			{
					    				oldOpenList = openLists.get(ordering[0][id]*N+ordering[1][id]);
					    				resetUniqueValues(id);
					    				resetComputeValues(id);
					    				fillUniqueValues(ordering[0][id],ordering[1][id],id);
					    				flag = isPartlySolved(id);
					    				openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList);
					    				if(debug) System.out.println("Updated flag1 = " + flag);
					    				if(flag == 1 || openLists.get(ordering[0][id]*N+ordering[1][id]).size()<2) 
							    		{
							    			LinkedList<Integer> object1 = new LinkedList<Integer>();
							    			openLists.set(ordering[0][id]*N+ordering[1][id], object1);
							    		}
					    				if(flag == 1) backtrack = false;
					    			}
					    		resultFlag = isSolved(false,id);
							}
							else
							{
							  id = doBacktrack(ordering,id);
							  flag = 0;
							  backtrack = true;
							}
							
						}
						else
							backtrack = false;
						
					}
					else
					{
						if(multipleTriesFlagID != id) //Reset the flag once the tries have been completed and trial for next number/id has started
						{
							multipleTriesFlagID = -1;
							multipleTriesFlag = false;
						}
						result[ordering[0][id]][ordering[1][id]] = temp;
						resetUniqueValues(id);
						resetComputeValues(id);
						oldOpenList = openLists.get(ordering[0][id]*N+ordering[1][id]);
						if(multipleTriesFlag)
							emptyClosedLists(ordering,id);
						updateOpenListsNew(ordering[0][id],ordering[1][id]);
						if(multipleTriesFlag)
								openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList); //Retain the old open list in which already tried values have been deleted.
						else
							openLists.set(ordering[0][id]*N+ordering[1][id], getOpenListNew(ordering[0][id], ordering[1][id], true)); //previous update would have deleted the open list of current element, so restore it.
			    	    if(checkUniquenessConstraint(ordering[0][id]+1,ordering[1][id]+1)==1)
	    				{
							oldOpenList = openLists.get(ordering[0][id]*N+ordering[1][id]);
		    				flag = isPartlySolved(id);
		    				openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList);
		    				if(debug) System.out.println("Updated flag2 = " + flag);
	    				}
						else
							flag = 0;
			    		noOfStates++;	
			    		if(flag == 1) 
			    		{
			    		   oldOpenList = openLists.get(ordering[0][id]*N+ordering[1][id]);
			    		   resetUniqueValues(id);
			    		   resetComputeValues(id);
		    			   fillUniqueValues(ordering[0][id],ordering[1][id],id);		    				   
		    			   openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList);
		    			   oldOpenList = openLists.get(ordering[0][id]*N+ordering[1][id]);
		    			   flag = isPartlySolved(id);
		    			   openLists.set(ordering[0][id]*N+ordering[1][id],oldOpenList);
		    			   
		    			   if(flag==1)
		    				   {
		    				   	LinkedList<Integer> object = new LinkedList<Integer>();
		    				   	openLists.set(ordering[0][id]*N+ordering[1][id], object);
		    				   }
		    			   else
				    		{ //If the number doesn't solve the problem, remove from the openlist so it is not tried again
		    					
				    			LinkedList<Integer> object = new LinkedList<Integer>(openLists.get(ordering[0][id]*N+ordering[1][id]));
				    			LinkedList<Integer> otherObject = new LinkedList<Integer>(closedLists.get(ordering[0][id]*N+ordering[1][id]));
				    			if(!otherObject.contains(result[ordering[0][id]][ordering[1][id]])) otherObject.add(new Integer(result[ordering[0][id]][ordering[1][id]]));
				    			object.remove(new Integer(result[ordering[0][id]][ordering[1][id]]));
				    			multipleTriesFlag = true;
				    			multipleTriesFlagID = id;
				    	 		openLists.set(ordering[0][id]*N+ordering[1][id], object);
				    	 		closedLists.set(ordering[0][id]*N+ordering[1][id], otherObject);
				    		}  
		    			   if(debug) System.out.println("Updated flag3 = " + flag);
			    		}
			    		else
			    		{ //If the number doesn't solve the problem, remove from the openlist so it is not tried again
			    			LinkedList<Integer> object = new LinkedList<Integer>(openLists.get(ordering[0][id]*N+ordering[1][id]));
			    			LinkedList<Integer> otherObject = new LinkedList<Integer>(closedLists.get(ordering[0][id]*N+ordering[1][id]));
			    			if(!otherObject.contains(result[ordering[0][id]][ordering[1][id]])) otherObject.add(new Integer(result[ordering[0][id]][ordering[1][id]]));
			    			object.remove(new Integer(result[ordering[0][id]][ordering[1][id]]));
			    			multipleTriesFlag = true;
			    			multipleTriesFlagID = id;
			    	 		openLists.set(ordering[0][id]*N+ordering[1][id], object);
			    	 		closedLists.set(ordering[0][id]*N+ordering[1][id], otherObject);
			    		}
			    		resultFlag = isSolved(false,id);
					}
				}
				else
				{
				  id = doBacktrack(ordering,id);
				  flag = 0;
				  backtrack = true;
				}	
				if(debug) 
	    		{
				  printBoard(resultFlag);
				  printOpenLists();
				  if(noOfStates > 9) promptEnterKey();
				}
		    }   	
	    }
	    public boolean fillRemainingValue(int i, int j, int[] numbers,int operationResult,String operation, int id)
	    {
	    	LinkedList<Integer> estimates = new LinkedList<Integer>();
	    	boolean success = false;
	    	if(operation.equals("Add"))
	    	{
	    		int k = operationResult - Arrays.stream(numbers).sum();
	    		if(k>=1 && k<=N) estimates.add(k);
	    	}
	    	else if (operation.equals("Multiply"))
	    	{
	    		for(int k = 1; k<=N; k++)
	    			if(product(numbers)*k == operationResult) 
	    			{
	    				estimates.add(k);
	    				break;
	    			}
	    	}
	    	else if(operation.equals("Subtract"))
	    	{
	    		int number = 0;
	    		if(numbers[0]!= 0) number = numbers[0];
	    		else if(numbers[1]!=0) number = numbers[1];
	    		int k1 = number - operationResult,k2 = number + operationResult;
	    		if(k1>=1 && k1<=N) estimates.add(k1);
	    		if(k2>=1 && k2<=N) estimates.add(k2);
	    	}
	    	else if(operation.equals("Divide"))
	    	{
	    		int number = 0;
	    		if(numbers[0]!= 0) number = numbers[0];
	    		else if(numbers[1]!=0) number = numbers[1];
	    		int k1 = number*operationResult, k2 = number/operationResult;
	    		if(k1>=1 && k1<=N) estimates.add(k1);
	    		if(k2>=1 && k2<=N && k2*operationResult == numbers[0]) estimates.add(k2);
	    	}
	    	int n = estimates.size(); 	
	    	if(n>0)
	    	{	
	    		LinkedList<Integer> object = new LinkedList<Integer>(getOpenListNew(i,j,true));
				LinkedList<Integer> otherObject = new LinkedList<Integer>(closedLists.get(i*N+j));
				if(n==1)
					{
						if(!otherObject.contains(estimates.get(0)) && object.contains(estimates.get(0))) 
					  		{
								result[i][j] = estimates.get(0);
								computes[i][j] = id+1;
								success = true;
								updateOpenListsNew(i,j);
								openLists.set(i*N+j, new LinkedList<Integer>()); //Empty the list either ways. even if the number is not there, u need to remove the ones present already in the open list
					  		}
						else
							{
							for(int k = 0; k < openLists.get(i*N+j).size(); k++)
								if(!closedLists.get(i*N+j).contains(openLists.get(i*N+j).get(k)))
									closedLists.get(i*N+j).add(openLists.get(i*N+j).get(k));
							}
						
						
					}	
				if(n==2)
					{
						for(int k = 1; k<=N; k++)
							if(k!=estimates.get(0) && k!=estimates.get(1))
								{
									LinkedList<Integer> object1= openLists.get(i*N+j); 
									object1.remove(new Integer(k));
									openLists.set(i*N+j, object1);
									if(!closedLists.get(i*N+j).contains(new Integer(k)))
										closedLists.get(i*N+j).add(new Integer(k));
								}
						success = true;
						
					}
	    	}
	    	else
	    	{
				for(int k = 0; k < openLists.get(i*N+j).size(); k++)
					if(!closedLists.get(i*N+j).contains(openLists.get(i*N+j).get(k)))
						closedLists.get(i*N+j).add(openLists.get(i*N+j).get(k));
			}
	    	
	    	return success;
	    }
	    public static boolean resetUniqueValues(int id)
	    {
	    	boolean success = false;
	    	for(int i = 0; i < N; i++)
	    		for(int j = 0; j < N; j++)
	    			if(uniques[i][j]==id+1)
	    			{
	    				result[i][j] = 0;
	    				uniques[i][j] = 0;
	    				success = true;
	    			}
	        return success;
	    }
	    public static boolean resetComputeValues(int id)
	    {
	    	boolean success = false;
	    	for(int i = 0; i < N; i++)
	    		for(int j = 0; j < N; j++)
	    			if(computes[i][j]==id+1)
	    			{
	    				result[i][j] = 0;
	    				computes[i][j] = 0;
	    				success = true;
	    			}
	        return success;
	    }	
	    public static boolean fillUniqueValues(int y, int x, int id)
	    {
	    	boolean noUniqueValues = false, success=false;
	    	while(!noUniqueValues)
	    	{
	    		noUniqueValues = true;
	    		for(int i = 0; i< N; i++)
	    			for(int j = 0; j< N; j++)
	    			{	if(openLists.get(i*N+j).size()==1 && !(i==y && j==x)) //Don't fill the current id as unique value. That will be taken care outside this function
	    				{
	    					if(!closedLists.get(i*N+j).contains(openLists.get(i*N+j).get(0)))
		    					{
	    							result[i][j] = openLists.get(i*N+j).get(0);
	    							updateOpenListsNew(i,j);
	    							noUniqueValues = false;
	    							uniques[i][j] = id+1;
	    							success = true;
		    					}
	    				}
	    			}
	    	}
	    	return success;
	    }
        public static int doBacktrack(int[][] ordering, int id) 
	    {
			  ArrayList<Integer> index = new ArrayList<Integer>();
			  index.add(ordering[0][id]);
			  index.add(ordering[1][id]);
			  if(!constants[ordering[0][id]][ordering[1][id]]) //&& uniques[ordering[0][id]][ordering[1][id]]==0) Dont know why i had this logic here
				  {
				  	result[ordering[0][id]][ordering[1][id]] = 0;
				  	uniques[ordering[0][id]][ordering[1][id]]= 0;
				    computes[ordering[0][id]][ordering[1][id]]= 0;
				  	emptyClosedLists(ordering, id);
				  	resetUniqueValues(id);
				  	resetComputeValues(id);
				  }
			  updateOpenListsNew(ordering[0][id], ordering[1][id]);
			  if(id>0) id--;
			  while(constants[ordering[0][id]][ordering[1][id]] && id > 0) 
				  {
				    id--; // Don't touch constant values while backtracking
				  	index.add(ordering[0][id]);
					index.add(ordering[1][id]);
				  }
			   
			  return id;
	    }
  	    public void fillConstants()
	    {
	    	for(int i = 0; i < constraint.length; i++)
	    	{
	    		String temp = constraint[i].trim();
	    		String[] fields = temp.split(" ");
		    	String operation = fields[0].trim();
		    	if(operation.equals("Constant"))
		    		{
		    			int operationResult = Integer.parseInt(fields[1]);
		    	    	int y = fields[2].charAt(0) - '0'-1, x = fields[2].charAt(2)-'0'-1; // Interchanged only in this place to account for swap in row-column convention
		    	    	result[y][x] = operationResult;
		    			updateOpenListsNew(y,x);
		    	    	constants[y][x] = true;
		    		}	
	    	}
	    }
	    public static int getNumber(int i, int j)
	   {
		   int number = -1;
		   if(openLists.get(i*N+j).size()>0)
		   {
			   LinkedList<Integer> object = new LinkedList<Integer>();
			   object = openLists.get(i*N+j);
			   Collections.sort(object);
			   number = object.get(0);
		   }
		    return number;
	   }
	    public static int getNumberNew(int i, int j, boolean backtrack)
		   {
			   int number = -1;
			   if(computes[i][j]==0)
			   {
				   if(openLists.get(i*N+j).size()>0)
				   {
					   LinkedList<Integer> object = new LinkedList<Integer>();
					   object = openLists.get(i*N+j);
					   Collections.sort(object);
					   number = object.get(0);
				   }
				   else if(backtrack)
				   {
					   LinkedList<Integer> object = new LinkedList<Integer>(getOpenListNew(i,j,true));
					   LinkedList<Integer> otherObject = new LinkedList<Integer>(closedLists.get(i*N+j));
					   for(int k = 0; k < object.size(); k++)
						   if(!otherObject.contains(object.get(k)))
							   {
							   	number = object.get(k);
							   	break;
							   }
				   }
			   }
			   return number;
		   }
	   public static void updateOpenLists(int i, int j, int newValue, int oldValue)
	   {
		   for(int k = j+1; k<N; k++)
		      openLists.set(i*N+k, getOpenList(i,k));
		   for(int k = i+1; k<N; k++)
			      openLists.set(k*N+j, getOpenList(k,j));
		   if(newValue==0)
			   openLists.set(i*N+j, getOpenList(i,j));
	   }
	   public static LinkedList<Integer> getOpenList(int i, int j)
	   {
		   LinkedList<Integer> object = new LinkedList<Integer>();
	       for(int k = 0; k<N; k++) object.add(k+1);
		   for(int k = 0; k<j; k++) if(result[i][k]!=0) object.remove(new Integer(result[i][k]));
		   for(int k = 0; k<i; k++) if(result[k][j]!=0) object.remove(new Integer(result[k][j]));
		   return object;
	   }
	   public static void updateOpenListsNew(int i, int j)
	   {
		   for(int p = 0; p<N; p++)
			 for(int q = 0; q<N; q++)
				  openLists.set(p*N+q, getOpenListNew(p,q, false));
	   }
	   public static void emptyClosedLists(int [][] ordering, int id)
	   {
		   for(int i = id; i<N*N; i++)
		   {
			LinkedList<Integer> otherObject = new LinkedList<Integer>();
			closedLists.set(ordering[0][i]*N+ordering[1][i], otherObject); //closedLists.set(ordering[0][id]*N+ordering[1][id], otherObject);
		   }
	   }
	   public static LinkedList<Integer> getOpenListNew(int i, int j, boolean exception) //exception is during the backtracking
	   {
		   LinkedList<Integer> object = new LinkedList<Integer>();
		   if(computes[i][j]==0)
		   {
			   if(result[i][j]==0 || exception)
			   {
				   for(int k = 0; k<N; k++) if(!closedLists.get(i*N+j).contains(k+1)) object.add(k+1);
				   for(int k = 0; k<N; k++) if(result[i][k]!=0) object.remove(new Integer(result[i][k]));
				   for(int k = 0; k<N; k++) if(result[k][j]!=0) object.remove(new Integer(result[k][j]));
			   }
			   if(exception)
			   {
				   object.add(result[i][j]); //The above loops would have removed the element at i,j itself 
				   Collections.sort(object); // Add that element and sort the array.
			   }
		   }
		   return object;
	   }
	   public int product(int array[])
	    {
	        int result = 1;
	        for (int i = 0; i < array.length; i++)
	            if(array[i]!=0) result = result * array[i];
	        return result;
	    }
	   public void mapOperations()
	    {	
	    	int noOfConstraints = constraint.length;
	    	for(int i = 0; i<noOfConstraints; i++)
	    	{
	    		String[] fields = constraint[i].split(" ");
		    	for(int k = 0; k < fields.length-2; k++)
		    	{
		    		int y = fields[k+2].charAt(0) - '0' - 1, x = fields[k+2].charAt(2)-'0'-1;
		    		operations[y][x] = i;
		    	}
	    	}	
	    }
	   public int checkConstraint(String constraint, int id)
	    {
	    	int flag = 1;
	    	String[] fields = constraint.split(" ");
	    	int[] numbers = new int[fields.length-2];
	    	String operation = fields[0].trim();
	    	int operationResult = Integer.parseInt(fields[1]);
	    	int noOfZeros = 0, yZero = -1, xZero = -1;
	    	for(int k = 0; k < fields.length-2; k++)
	    	{
	    		int y = fields[k+2].charAt(0) - '0' - 1, x = fields[k+2].charAt(2)-'0' - 1; // Interchanged only in this place to account for swap in row-column convention
	    		if(result[y][x]==0)
	    			{
	    				yZero = y;
	    				xZero = x;
	    				noOfZeros++;
	    				//break;
	    			}
	    		else
	    			numbers[k] = result[y][x];
	    	}
	    	if(noOfZeros!=0)
	    		{
	    			if(noOfZeros==1)
	    				{
	    					if(fillRemainingValue(yZero,xZero,numbers,operationResult,operation,id))
	    						flag = 1;
	    					else
	    						flag = 0;
	    				}
	    			else
	    				flag = 2;
	    		}
	    	else
	    	{
	    		if(operation.equals("Add"))
		    		if(Arrays.stream(numbers).sum()!= operationResult) flag = 0;
		    	if(operation.equals("Subtract"))
		    		if(Math.abs(numbers[0]-numbers[1])!= operationResult) flag = 0;
		    	if(operation.equals("Constant"))
		    		if(numbers[0]!= operationResult) flag = 0;
		    	if(operation.equals("Multiply"))
		    		if(product(numbers)!= operationResult) flag = 0;
		    	if(operation.equals("Divide"))
		    		if(numbers[0]*operationResult!=numbers[1] && numbers[1]*operationResult!=numbers[0]) flag = 0;
	    	}
	    	
	    	return flag;
	    }
	   public static int checkUniquenessConstraint(int y, int x)
	    {
	    	//System.out.println("\nChecking uniqueness for "+ x  +","+  y + " (world coordinates)");
	    	
	    	for(int j=0; j<x-1; j++)
	    		{
	    		//System.out.println(result[y-1][x-1]+"=="+result[y-1][j]);
	    		if(result[y-1][x-1]==result[y-1][j])
	    			return 0;
	    		}
    		
	    	for(int i=0; i<y-1; i++)
	    		{
	    		//System.out.println(result[y-1][x-1]+"=="+result[i][x-1]);
	    		if(result[y-1][x-1]==result[i][x-1])
	    			return 0;
	    		}
		
	    	return 1;
	    }
 	   public int checkOtherConstraints()
	    {
	    	int[] set = new int[N];
	    	for(int k = 0; k<N; k++)
    			set[k] = k;
    		
	    	for(int i = 0; i<N; i++)
	    	{
	    		int [] newSet = set.clone();
	    		for(int j=0; j<N; j++)
	    			{
	    				if(result[i][j]>0 && result[i][j]<=N)
	    					if(newSet[result[i][j]-1]==-1)
	    						return 0;
	    					else
	    						newSet[result[i][j]-1]=-1;
	    				else
	    					return 0;
	    			}
	    	}
	    	for(int j = 0; j<N; j++)
	    	{
	    		int [] newSet = set.clone(); 
	    		for(int i=0; i<N; i++)
	    			{
	    				if(result[i][j]>0 && result[i][j]<=N)
	    					if(newSet[result[i][j]-1]==-1)
	    						return 0;
	    					else
	    						newSet[result[i][j]-1]=-1;
	    				else
	    					return 0;
	    			}
	    	}
	    	return 1;
	    }
	   public int isSolved(boolean debugMode, int id)
	    {
	    	int flag = 1;
	    	if(debugMode)
	    		System.out.println();
	    	for(int i = 0; i < constraint.length; i++)
	    	{
	    		flag = checkConstraint(constraint[i].trim(),id);
	    		if(debugMode)
	    			System.out.print("Checking constraint: " + constraint[i].trim() + " - " + flag + "\n");
	    		if(flag==0) break;
	    	}
	    	if(flag==1)
	    		{
	    			flag = checkOtherConstraints();
	    			if(debugMode)
	    				System.out.print("Checking other constraints" + " - " + flag + "\n");
	    		}
	    	return flag;
	    }
	   public int isPartlySolved(int id)
	    {
	    	int resultFlag = 1;
	    	int flag = -1; // 0 - unsolved, 1 - solved, 2 - cannot evaluate
	    	int prevOperation = -1;
	    	for(int i = 0;i<N;i++)
	    	{
	    		for(int j = 0;j<N;j++)
	    		{
	    			if(result[i][j]!=0) //Until the iteration reaches unfilled values in the board
	    			{	
	    				if(prevOperation != operations[i][j]) //Don't check constraint for the same operation
	    				{ 
	    					prevOperation = operations[i][j];
	    					if(checkConstraint(constraint[prevOperation],id)==0)
		    					{
		    						resultFlag = 0;
		    						break;
		    					}
	    				}
	    			}
	    			else
	    				{
	    					flag = 0;
	    					break;
	    				}
	    		}
	    		if(resultFlag==0 || flag==0)
	    			break;
	    	}	    	
	    	return resultFlag;
	    }
	   public static void printBoard(int flag)
	    {
		   printedBoard = "";
	    	if(flag==1)
	    		printedBoard = "Solution:\n";
	    	else if(flag!=2)
	    		printedBoard = "\nPuzzle Unsolved: State - " + noOfStates;
	    	for(int i = 0; i < N; i++)
	    		{
		    		for(int j = 0; j< N; j++)
		    			printedBoard = printedBoard + result[i][j] + "\t";
		    		printedBoard = printedBoard + "\n"; 
	    		}
	    	if(flag!=2) System.out.print(printedBoard);
	    	/*
	    	System.out.println("Uniques: ");
	    	for(int i = 0; i < N; i++)
    		{
	    		for(int j = 0; j< N; j++)
	    			System.out.print(uniques[i][j] + " ");
	    		System.out.println();
    		}
	    	System.out.println("Computes: ");
	    	for(int i = 0; i < N; i++)
    		{
	    		for(int j = 0; j< N; j++)
	    			System.out.print(computes[i][j] + " ");
	    		System.out.println();
    		}
	    		
	    	System.out.println("Operations: ");
	    	for(int i = 0; i < N; i++)
    		{
	    		for(int j = 0; j< N; j++)
	    			System.out.print(operations[i][j] + " ");
	    		System.out.println();
    		}
    		 */
	    }    
    }
    
	public static void main(String[] args) throws IOException {
		boolean writeFlag = true, production = true;
		String path = ""; //"G:/University of Michigan/Fall 2018/EECS 592/Assignments/modified ";
		File file = new File(path + "input.txt");
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("(?=\\n\\d)");
		
		String contents = "";
		
		while(scanner.hasNext()) //Change this to while
		{
			 Puzzle latestPuzzle = new Puzzle(scanner.next().trim());
			 contents = contents  + "\nPuzzle " + ++count + ":" + "\n";
			 contents = contents  + "Approach 5:"+ "\n";	
			 long startTime = System.nanoTime();
		     latestPuzzle.solve5(false);
		     float estimatedTime = (System.nanoTime() - startTime)/1000;
		     if(production) 
		    	 latestPuzzle.printBoard(2);
		     else
		    	 latestPuzzle.printBoard(1);
		     contents = contents + latestPuzzle.printedBoard;
		     contents = contents  + "States generated: " + latestPuzzle.noOfStates+ "\n";
		     contents = contents  + "Time in ms: " + estimatedTime+ "\n";
		     contents = contents  + "States/microseconds: " + latestPuzzle.noOfStates/estimatedTime+ "\n";
		     if(!production) System.out.println(contents);
		     if(writeFlag)
				{
					FileWriter fw = new FileWriter(path + "output.txt");
		        	fw.write(contents);
		        	fw.close();
				}
		}
		scanner.close();
		
	}
	public static void promptEnterKey(){
		   System.out.println("Press \"ENTER\" to continue...");
		   Scanner scanner = new Scanner(System.in);
		   scanner.nextLine();
		}

}

