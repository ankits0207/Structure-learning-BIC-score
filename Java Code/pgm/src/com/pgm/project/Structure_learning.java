package com.pgm.project;

import java.util.*;

public class Structure_learning {
	
	static ArrayList<String> initialization()
	{
		ArrayList<String> list_of_nodes = new ArrayList<String>();
		try
		{
			list_of_nodes.add("PKC");
			list_of_nodes.add("PKA");
			list_of_nodes.add("PLC_GAMMA");
			list_of_nodes.add("JNK");
			list_of_nodes.add("P38");
			list_of_nodes.add("RAF");
			list_of_nodes.add("PIP3");
			list_of_nodes.add("MEK");
			list_of_nodes.add("PIP2");
			list_of_nodes.add("AKT");
			list_of_nodes.add("ERK");
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return list_of_nodes;		
	}
	
	static HashMap<String, ArrayList<String>> create_edgeless_graph(ArrayList<String> list_of_nodes)
	{
		HashMap<String, ArrayList<String>> bayesian_network = new HashMap<String, ArrayList<String>>();
		try
		{
			for (String str : list_of_nodes) {
				ArrayList<String> adjacency_list = new ArrayList<String>();
				bayesian_network.put(str, adjacency_list);
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return bayesian_network;
	}
	
	static double calculate_score(HashMap<String, ArrayList<String>> bayesian_network, 
			int [][] data, ArrayList<String> list_of_nodes)
	{
		double score = 0;
		
		try
		{
			Map<String, Integer> mymap = new HashMap<String, Integer>();
			mymap.put("PKC", 8);
			mymap.put("PKA", 7);
			mymap.put("PLC_GAMMA", 2);
			mymap.put("JNK", 10);
			mymap.put("P38", 9);
			mymap.put("RAF", 0);
			mymap.put("PIP3", 4);
			mymap.put("MEK", 1);
			mymap.put("PIP2", 3);
			mymap.put("AKT", 6);
			mymap.put("ERK", 5);
			
			ArrayList<String> list_of_keys = initialization();
			ArrayList<String> list_of_values;
			
			int col_key, col_value;
			
			double p_hat_x = 0.0;
			double p_hat_y = 0.0; 
			double p_hat_xy = 0.0;
			
			double relative_score = 0.0;
			double entropy = 0.0;
			double penalty = 0.0;
			
			// Computing dim_G free params
			//Independent_params ip = new Independent_params();
			//int adjecency_matrix[][] = list_to_matrix(list_of_nodes, bayesian_network);
			//int dim_G = ip.countIslands(adjecency_matrix, 11, 11);
			
			//Code to find number of independent parameters
			ArrayList<String> super_set = new ArrayList<String>();
			ArrayList<String> sub_set = new ArrayList<String>();
			for(String key:list_of_keys)
			{
				list_of_values = bayesian_network.get(key);
				for (String value:list_of_values) 
				{
					sub_set.add(value);
				}
			}
			HashSet<String> hs = new HashSet<String>();
			hs.addAll(sub_set);
			sub_set.clear();
			sub_set.addAll(hs);
			super_set.removeAll(sub_set);
			int dim_G = super_set.size();
			
			
			int rows = data.length;
			
			for (String key:list_of_keys) {
				list_of_values = bayesian_network.get(key);
				for (String value:list_of_values) {
					col_key = mymap.get(key);
					col_value = mymap.get(value);
					
					for (int i = 1; i<=3; i++) {
						for (int j = 1; j<=3; j++) {
							for (int row = 0; row < rows; row++) {
								if (data[row][col_key] == i) p_hat_x++;
								if (data[row][col_value] == j) p_hat_y++;
								if (data[row][col_key] == i && data[row][col_value] == j)
									p_hat_xy++;
							}
							p_hat_x = p_hat_x/rows;
							p_hat_y = p_hat_y/rows;
							p_hat_xy = p_hat_xy/rows;
							
							relative_score = rows*(p_hat_xy*Math.log(p_hat_xy/(p_hat_x*p_hat_y)));
							entropy = rows*(p_hat_x*Math.log(p_hat_x));
							penalty = 0.5*Math.log(rows)*dim_G;
							
							score += relative_score;
							score -= entropy;
							score -= penalty;
							
							p_hat_x = 0;
							p_hat_y = 0;
							p_hat_xy = 0;
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return score;
	}
	
	static HashMap<String, ArrayList<String>> add_an_edge_randomly(ArrayList<String> list_of_nodes, 
			HashMap<String, ArrayList<String>> bayesian_network, int [][] data)
	{
		try
		{
			double old_score = calculate_score(bayesian_network, data, list_of_nodes);
			ArrayList<String> selected_nodes = new ArrayList<String>(); 
			Random rand = new Random();
			for (int i = 0; i < 2; i++)
			{
				int random_index = rand.nextInt(11);
				selected_nodes.add(list_of_nodes.get(random_index));				
			}
			int flag = 0;
			String first_node = selected_nodes.get(0);
			String second_node = selected_nodes.get(1);
			ArrayList<String> returned_list  = bayesian_network.get(first_node);
			for (String str : returned_list) {
				if(str.equals(second_node))
				{
					flag = 1;
					break;
				}
			}
			if(flag != 1)
			{
				returned_list.add(second_node);
				bayesian_network.put(first_node, returned_list);
				double new_score = calculate_score(bayesian_network, data, list_of_nodes);
				if(new_score > old_score)
				{
					Check_cycle_matrix obj = new Check_cycle_matrix();
					int [][] adj_list= list_to_matrix(list_of_nodes, bayesian_network);
					for(int source = 1; source < 12; source++)
					{
						if(obj.dfs(adj_list, source))
						{
							returned_list.remove(second_node);
							bayesian_network.put(first_node, returned_list);
							break;
						}
					}
				}
				else
				{
					returned_list.remove(second_node);
					bayesian_network.put(first_node, returned_list);					
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		System.out.println("Add");
		return bayesian_network;
	}
	
	static HashMap<String, ArrayList<String>> delete_an_edge_randomly(ArrayList<String> list_of_nodes, 
			HashMap<String, ArrayList<String>> bayesian_network, int [][] data)
	{
		try
		{
			double old_score = calculate_score(bayesian_network, data, list_of_nodes);
			ArrayList<String> selected_nodes = new ArrayList<String>(); 
			Random rand = new Random();
			for (int i = 0; i < 2; i++)
			{
				int random_index = rand.nextInt(11);
				selected_nodes.add(list_of_nodes.get(random_index));				
			}
			int flag = 0;
			String first_node = selected_nodes.get(0);
			String second_node = selected_nodes.get(1);
			ArrayList<String> returned_list  = bayesian_network.get(first_node);
			for (String str : returned_list) {
				if(str.equals(second_node))
				{
					flag = 1;
					break;
				}
			}
			if(flag == 1)
			{
				returned_list.remove(second_node);
				bayesian_network.put(first_node, returned_list);
				double new_score = calculate_score(bayesian_network, data, list_of_nodes);
				if(new_score < old_score)
				{
					returned_list.add(second_node);
					bayesian_network.put(first_node, returned_list);
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		System.out.println("Delete");
		return bayesian_network;
	}
	
	static HashMap<String, ArrayList<String>> reverse_an_edge_randomly(ArrayList<String> list_of_nodes, 
			HashMap<String, ArrayList<String>> bayesian_network, int [][] data)
	{
		
		try
		{
			double old_score = calculate_score(bayesian_network, data, list_of_nodes);
			ArrayList<String> selected_nodes = new ArrayList<String>(); 
			Random rand = new Random();
			for (int i = 0; i < 2; i++)
			{
				int random_index = rand.nextInt(11);
				selected_nodes.add(list_of_nodes.get(random_index));				
			}
			int flag = 0;
			String first_node = selected_nodes.get(0);
			String second_node = selected_nodes.get(1);
			ArrayList<String> returned_list_for_first_node_1  = bayesian_network.get(first_node);
			for (String str : returned_list_for_first_node_1) {
				if(str.equals(second_node))
				{
					flag = 1;
					break;
				}
			}
			ArrayList<String> returned_list_for_second_node_1 = bayesian_network.get(second_node);
			if(flag == 1)
			{
				returned_list_for_first_node_1.remove(second_node);
				returned_list_for_second_node_1.add(first_node);
				bayesian_network.put(first_node, returned_list_for_first_node_1);
				bayesian_network.put(second_node, returned_list_for_second_node_1);
				
				double new_score = calculate_score(bayesian_network, data, list_of_nodes);
				if(new_score > old_score)
				{
					Check_cycle_matrix obj = new Check_cycle_matrix();
					int [][] adj_list= list_to_matrix(list_of_nodes, bayesian_network);
					for(int source = 1; source < 12; source++)
					{
						if(obj.dfs(adj_list, source))
						{
							ArrayList<String> returned_list_for_first_node_2 = bayesian_network.get(first_node);
							ArrayList<String> returned_list_for_second_node_2 = bayesian_network.get(second_node);
							returned_list_for_first_node_2.add(second_node);
							returned_list_for_second_node_2.remove(first_node);							
							bayesian_network.put(first_node, returned_list_for_first_node_2);
							bayesian_network.put(second_node, returned_list_for_second_node_2);
							break;
						}
					}
				}
				else
				{
					ArrayList<String> returned_list_for_first_node_2 = bayesian_network.get(first_node);
					ArrayList<String> returned_list_for_second_node_2 = bayesian_network.get(second_node);
					returned_list_for_first_node_2.add(second_node);
					returned_list_for_second_node_2.remove(first_node);					
					bayesian_network.put(first_node, returned_list_for_first_node_2);
					bayesian_network.put(second_node, returned_list_for_second_node_2);
				}
			}
			ArrayList<String> returned_list_for_first_node_3 = bayesian_network.get(first_node);
			ArrayList<String> returned_list_for_second_node_3 = bayesian_network.get(second_node);
			HashSet<String> hs1 = new HashSet<String>();
			HashSet<String> hs2 = new HashSet<String>();
			hs1.addAll(returned_list_for_first_node_3);
			hs2.addAll(returned_list_for_second_node_3);
			returned_list_for_first_node_3.clear();
			returned_list_for_second_node_3.clear();
			returned_list_for_first_node_3.addAll(hs1);
			returned_list_for_second_node_3.addAll(hs2);	
			bayesian_network.put(first_node, returned_list_for_first_node_3);
			bayesian_network.put(second_node, returned_list_for_second_node_3);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		System.out.println("Rev");	
		return bayesian_network;		
	}
	
	static int[][] list_to_matrix(ArrayList<String> list_of_nodes, HashMap<String, ArrayList<String>> bayesian_network)
	{
		int[][] matrix_to_be_returned = new int[11][11];
		Map<Integer, String> mymap = new HashMap<Integer, String>();
		mymap.put(0, list_of_nodes.get(0));
		mymap.put(1, list_of_nodes.get(1));
		mymap.put(2, list_of_nodes.get(2));
		mymap.put(3, list_of_nodes.get(3));
		mymap.put(4, list_of_nodes.get(4));
		mymap.put(5, list_of_nodes.get(5));
		mymap.put(6, list_of_nodes.get(6));
		mymap.put(7, list_of_nodes.get(7));
		mymap.put(8, list_of_nodes.get(8));
		mymap.put(9, list_of_nodes.get(9));
		mymap.put(10, list_of_nodes.get(10));
		for(int i = 0; i < 11; i++)
		{
			for(int j = 0; j < 11; j++)
			{
				int flag = 0;
				String i_node = mymap.get(i);
				String j_node = mymap.get(j);
				ArrayList<String> returned_list_for_first_node  = bayesian_network.get(i_node);
				for (String str : returned_list_for_first_node) {
					if(str.equals(j_node))
					{
						flag = 1;
						break;
					}
				}
				if(flag == 1)
					matrix_to_be_returned[i][j] = 1;
				else
					matrix_to_be_returned[i][j] = 0;
			}
		}
		return matrix_to_be_returned;
	}
		
	public static void main(String[] args) {
		
		int[][] final_adjacency_list = new int[11][11];
		for (int iterator = 0; iterator < 10; iterator++) {
			
			ArrayList<String> list_of_paths = new ArrayList<String>();
			
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/1. cd3cd28.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/2. cd3cd28icam2.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/3. cd3cd28+aktinhib.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/4. cd3cd28+g0076.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/5. cd3cd28+psitect.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/6. cd3cd28+u0126.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/7. cd3cd28+ly.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/8. pma.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/9. b2camp.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/10. cd3cd28icam2+aktinhib.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/11. cd3cd28icam2+g0076.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/12. cd3cd28icam2+psit.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/13. cd3cd28icam2+u0126.xls");
			list_of_paths.add("E:/Academics/M. Tech (CB) - IIIT Delhi/Semester 2/PGM/Project/"
					+ "Final Implementation/MATLAB/Test_data_folder/14. cd3cd28icam2+ly.xls");
			
			for(String filepath:list_of_paths)
			{
				int data[][];
				Read_excel excel_reader = new Read_excel();
				data = excel_reader.get_data_as_matrix(filepath);
				ArrayList<String> list_of_nodes = initialization();
				HashMap<String, ArrayList<String>> bayes_net = create_edgeless_graph(list_of_nodes);
				Random rand = new Random();
				for (int i = 0; i < 500; i++)
				{
					int r = rand.nextInt(3);
					if(r == 0) bayes_net = add_an_edge_randomly(list_of_nodes, bayes_net, data);
					else if(r == 1) bayes_net = delete_an_edge_randomly(list_of_nodes, bayes_net, data);
					else bayes_net = reverse_an_edge_randomly(list_of_nodes, bayes_net, data);
				}
				int[][] adjmat = list_to_matrix(list_of_nodes, bayes_net);
				
				for(int i = 0; i < 11; i++) 
					for(int j = 0; j < 11; j++) 
						final_adjacency_list[i][j] = final_adjacency_list[i][j] + adjmat[i][j];
					
			}			
		}
		for (int i = 0; i<11; i++) {
			for (int j = 0; j<11; j++) {
				System.out.print(final_adjacency_list[i][j]+" ");
			}
			System.out.println();
		}
		
		System.out.println("\n\n\n");

		
		for (int i = 0; i<11; i++) {
			for (int j = 0; j<11; j++) {
				if ((double)final_adjacency_list[i][j]/140 > 0.45) System.out.print("1 ");
				else System.out.print("0 ");
			}
			System.out.println();
		}
		
		System.out.println("END OF CODE");
		System.out.println("Submitted By: Ankit and Divyanshu");

	}
}
