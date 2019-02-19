package com.pgm.project;

import java.util.Stack;

public class Check_cycle_matrix {
	
    public Stack<Integer> stack;
    public int adjacencyMatrix[][];
    
    public Check_cycle_matrix() 
    {
        stack = new Stack<Integer>();
    }
    public boolean dfs(int adjacency_matrix[][], int source)
    {
    	int number_of_nodes = 11;
    	adjacencyMatrix = new int[number_of_nodes + 1][number_of_nodes + 1];
    	for (int sourcevertex = 0; sourcevertex < number_of_nodes; sourcevertex++)
        {
            for (int destinationvertex = 0; destinationvertex < number_of_nodes; destinationvertex++)
            {
                adjacencyMatrix[sourcevertex][destinationvertex] =
                   adjacency_matrix[sourcevertex][destinationvertex];
            }
        }
    	int visited[] = new int[number_of_nodes + 1];		
        int element = source;		
        int destination = source;			
        visited[source] = 1;		
        stack.push(source);
 
        while (!stack.isEmpty())
        {
            element = stack.peek();
            destination = element;	
		    while (destination <= number_of_nodes)
		    {
	                if (adjacencyMatrix[element][destination] == 1 && visited[destination] == 1)
	                {
	                    if (stack.contains(destination))
	                    {	
	                        return true;	
	                    }
	                }
	 
	              	if (adjacencyMatrix[element][destination] == 1 && visited[destination] == 0)
	              	{
	                    stack.push(destination);
	                    visited[destination] = 1;
	                    adjacencyMatrix[element][destination] = 0;
	                    element = destination;
	                    destination = 1;
		            continue;
	                }
	                destination++;
		    }
            stack.pop();	
        }
        return false;
    }
}
