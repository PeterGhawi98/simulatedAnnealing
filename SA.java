/**
*
* @author Peter Ghawi C1640608
*
**/

import java.io.*;
import java.util.*;

public class SA {
    public static int[][] graph;
    public static Map<Integer, String> candidates = new HashMap<Integer, String>();
    public static int uphill_Moves = 0;


// The Simulated Annealing Algorithm provided in the lecture 9 slide 13
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static State SimulatedAnnealingAlgorithm(State initial_Solution, double cooling_Ratio, int stopping_Criterion, int ti, int tl) {
//Constructing initial solution
State current_Solution = initial_Solution; 
//setting initial temperature
        double t = ti;
        int num_Non_Improve = 0;
        Random rand = new Random();
//Repeat until condition is met 
        while (num_Non_Improve < stopping_Criterion) {
            for (int i=0; i < tl; i++) {
                //generating a random neighbouring solution
                State new_Solution = current_Solution.get_Neighbour();
                // calculating the difference in cost
                int change_of_Cost = new_Solution.get_Cost() - current_Solution.get_Cost();
                // if the change in temperature is less than 0 the it accepts the new state
                if (change_of_Cost <= 0) {
                    current_Solution = new_Solution;
                } else {
                    //generating q
                    double q = rand.nextDouble();
                    /*the new state is accepted if the the random q generated is greater 
                    than the probability of an increase in cost*/
                    if (q < Math.exp((-change_of_Cost)/t)) {
                        current_Solution = new_Solution;
                        //keeping track of uphill moves by counting them.
                        uphill_Moves += 1;
                    } else {
                        // num_non_improves is increased until it reaches the stopping criterion 
                        num_Non_Improve += 1;
                    }
                }
            }
            //setting new temperature
            t *= cooling_Ratio;
        }
        return current_Solution;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));

        int num_Candidates = Integer.parseInt(in.readLine().trim());
        graph = new int[num_Candidates][num_Candidates];

        for (int i=0; i < num_Candidates; i++) {
            String[] doc = in.readLine().split(",");
            candidates.put(Integer.parseInt(doc[0].trim()), doc[1].trim());
        }

        in.readLine(); // discard one irrelevant doc

        String d = in.readLine();
        while (d != null) {
            String[] doc = d.split(",");
            int start = Integer.parseInt(doc[1].trim());
            int finish = Integer.parseInt(doc[2].trim());
            int weight = Integer.parseInt(doc[0].trim());
            graph[start-1][finish-1] = weight;

            d = in.readLine();
        }

        in.close();

        List<Integer> initial_Order = new ArrayList<>();
        for (int i=1; i < num_Candidates+1; i++) {
            initial_Order.add(i);
        }

        Long begin_Timer = System.currentTimeMillis();
        State sa_Solution = SimulatedAnnealingAlgorithm(new State(initial_Order), 0.97, 4000,100, 70);
        Long time_Taken = System.currentTimeMillis() - begin_Timer;

        System.out.println("State:");
        System.out.print("Pos..  "); System.out.print("Candidate #  "); System.out.println("Candidate Name");
        for (int i=0; i < num_Candidates; i++) {
            System.out.print(String.format("%-7s", i+1));
            System.out.print(String.format("%-13s", sa_Solution.get_Order().get(i)));
            System.out.println(candidates.get(sa_Solution.get_Order().get(i)));
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Kemeny Score: " + sa_Solution.get_Cost());
        System.out.println("Runtime (ms): " + time_Taken);
        System.out.println("Uphill moves: " + uphill_Moves);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
