/**
*
* @author Peter Ghawi C1640608
*
**/

import java.util.*;

public class State {
    private List<Integer> order = new ArrayList<>();
    private int cost;

    public State(List<Integer> order) {
        this.order = order;

        int total = 0;
        for (int i=0; i < order.size(); i++) {
            for (int j=0; j < i; j++) {
                total += SA.graph[i][j];
            }
        }
        cost = total;
    }

    public State(State r) {
        order = new ArrayList<Integer>(r.order);
        cost = r.cost;
    }

    public List get_Order() {
        return order;
    }

    public int get_Cost() {
        return cost;
    }

    public State get_Neighbour() {
        Random rand = new Random();
        int pos = rand.nextInt(order.size()-1);

        State r = new State(this);
        r.order.set(pos, order.get(pos+1));
        r.order.set(pos+1, order.get(pos));

        int pos1 = order.get(pos)-1;
        int pos2 = order.get(pos+1)-1;
        r.cost = r.cost - SA.graph[pos2][pos1] + SA.graph[pos1][pos2];

        return r;
    }
}
