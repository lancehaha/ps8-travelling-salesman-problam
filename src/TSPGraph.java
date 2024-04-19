import java.util.ArrayList;
import java.util.Stack;

public class TSPGraph implements IApproximateTSP {

    @Override
    public void MST(TSPMap map) {
        Integer Counts = map.getCount();
        TreeMapPriorityQueue<Double, Integer> PQ = new TreeMapPriorityQueue<>();
        Integer[] parent = new Integer[Counts];
        parent[0] = -1;
        for (int i = 0; i < Counts; i++) {
            if (i == 0) {
                PQ.add(i, 0.0);
            }
            PQ.add(i, Double.MAX_VALUE);
        }

        while (!PQ.isEmpty()) {
            Integer currentNode = PQ.extractMin();
            for (int i = 0; i < Counts; i++) {
                if (PQ.lookup(i) != null) {
                    if (map.pointDistance(currentNode, i) < PQ.lookup(i)) {
                        parent[i] = currentNode;
                        PQ.decreasePriority(i, map.pointDistance(currentNode, i));
                    }
                }
            }
        }

        for (int i = 0; i < Counts; i++) {
            if (parent[i] != -1) {
                map.setLink(i, parent[i], false);
            }
        }
        map.redraw();
    }


    @Override
    public void TSP(TSPMap map) {
        MST(map);
        Integer counts = map.getCount();
        ArrayList<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[counts];
        Stack<Integer> stack = new Stack<>();

        stack.push(0);
        while (!stack.isEmpty()) {
            Integer curret = stack.pop();
            if (!visited[curret]) {
                tour.add(curret);
                visited[curret] = true;
                for (int i = 0; i < counts; i++) {
                    if (!visited[i] && map.getPoint(i).getLink() == curret) {
                        stack.push(i);
                    }
                }
            }
        }

        for (int i =0; i<counts; i++) {
            map.eraseLink(i, false);
        }

        for (int i = 0; i < tour.size()-1; i++) {
            map.setLink(tour.get(i), tour.get(i+1), false);
        }

        map.setLink(tour.get(tour.size()-1), tour.get(0));

    }
    @Override
    public boolean isValidTour(TSPMap map) {
        Integer counts = map.getCount();
        boolean[] visited = new boolean[counts];
        Stack<Integer> stack = new Stack<>();
        Integer current = 0;
        int counter = 0;
        for(int i = 0; i< counts; i++) {
            if (current < 0 || visited[current]) {
                return false;
            }
            visited[current] = true;
            counter += 1;
            current = map.getPoint(current).getLink();
        }
        return current == 0 && counter == counts;
    }

    @Override
    public double tourDistance(TSPMap map) {
        double rn = 0;
        Integer counts = map.getCount();
        Integer current = 0;
        if (isValidTour(map)) {
            for (int i = 0; i<counts; i++) {
                rn += map.pointDistance(current, map.getPoint(current).getLink());
                current = map.getPoint(current).getLink();
            }
            return rn;
        }
        return -1;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "tenpoints.txt");
        TSPGraph graph = new TSPGraph();

        //graph.MST(map);
        //graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
    }
}
