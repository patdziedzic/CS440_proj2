import java.util.*;

public class Bfs {
    private static boolean[][] visited; //2D array to keep track of visited cells

    /**
     * Regular Shortest Path BFS from bot to goal
     */
    public static LinkedList<Cell> SP_BFS(Cell bot, Cell goal){
        visited = new boolean[Ship.D][Ship.D];
        Queue<Cell> Q = new LinkedList<>(); //tell us what to explore next
        HashMap<Cell, Cell> parentNodes = new HashMap<>(); //keeps track of where bot has visited
        //^ Map the previous to the next by using .put(next, previous)

        Q.add(bot);
        visited[bot.getRow()][bot.getCol()] = true;

        while (!Q.isEmpty()) {
            bot = Q.remove();

            if(bot.equals(goal)) {
                //Once path found, start from end and go back and store the path into LinkedList
                LinkedList<Cell> shortestPath = new LinkedList<>();
                Cell ptr = bot;
                while (ptr != null) {
                    shortestPath.add(ptr);
                    ptr = parentNodes.get(ptr);
                }
                Collections.reverse(shortestPath);
                return shortestPath;
            }

            if(isValid(bot.up)) {
                parentNodes.put(bot.up, bot);
                Q.add(bot.up);
                visited[bot.up.getRow()][bot.up.getCol()] = true;
            }
            if(isValid(bot.down)) {
                parentNodes.put(bot.down, bot);
                Q.add(bot.down);
                visited[bot.down.getRow()][bot.down.getCol()] = true;
            }
            if(isValid(bot.left)) {
                parentNodes.put(bot.left, bot);
                Q.add(bot.left);
                visited[bot.left.getRow()][bot.left.getCol()] = true;
            }
            if(isValid(bot.right)) {
                parentNodes.put(bot.right, bot);
                Q.add(bot.right);
                visited[bot.right.getRow()][bot.right.getCol()] = true;
            }
        }
        return null;
    }

    /**
     * Check if a neighbor is valid to search
     */
    private static boolean isValid(Cell c) {
        return (c != null && c.isOpen && !visited[c.getRow()][c.getCol()]);
    }

    /**
     * Deterministic Shortest Path BFS
     * Goes to nearest potential leak cell (noLeak = false)
     */
    public static LinkedList<Cell> detSP_BFS(Cell bot){
        visited = new boolean[Ship.D][Ship.D];
        Queue<Cell> Q = new LinkedList<>(); //tell us what to explore next
        HashMap<Cell, Cell> parentNodes = new HashMap<>(); //keeps track of where bot has visited
        //^ Map the previous to the next by using .put(next, previous)

        Q.add(bot);
        visited[bot.getRow()][bot.getCol()] = true;
        
        while (!Q.isEmpty()) {
            bot = Q.remove();

            if(!bot.noLeak) {
                //Once path found, start from end and go back and store the path into LinkedList
                LinkedList<Cell> shortestPath = new LinkedList<>();
                Cell ptr = bot;
                while (ptr != null) {
                    shortestPath.add(ptr);
                    ptr = parentNodes.get(ptr);
                }
                Collections.reverse(shortestPath);
                return shortestPath;
            }
    
            if(isValid(bot.up)) {
                parentNodes.put(bot.up, bot);
                Q.add(bot.up);
                visited[bot.up.getRow()][bot.up.getCol()] = true;
            }
            if(isValid(bot.down)) {
                parentNodes.put(bot.down, bot);
                Q.add(bot.down);
                visited[bot.down.getRow()][bot.down.getCol()] = true;
            }
            if(isValid(bot.left)) {
                parentNodes.put(bot.left, bot);
                Q.add(bot.left);
                visited[bot.left.getRow()][bot.left.getCol()] = true;
            }
            if(isValid(bot.right)) {
                parentNodes.put(bot.right, bot);
                Q.add(bot.right);
                visited[bot.right.getRow()][bot.right.getCol()] = true;
            }
        }
        return null;
    }


    /**
     * Dijkstra's Algorithm to compute and update the distances
     */
    public static void updateDistances(Cell bot){
        visited = new boolean[Ship.D][Ship.D];
        PriorityQueue<PQCell> pq = new PriorityQueue<>();

        pq.add(new PQCell(bot, 0));
        bot.distFromBot = 0;
        visited[bot.getRow()][bot.getCol()] = true;

        while (!pq.isEmpty()) {
            Cell curr = pq.remove().cell;

            for (Cell neighbor : curr.neighbors) {
                if (neighbor != null && neighbor.isOpen) {
                    int tempDist = curr.distFromBot + 1;
                    if (!visited[neighbor.getRow()][neighbor.getCol()] || tempDist < neighbor.distFromBot) {
                        neighbor.distFromBot = tempDist;
                        pq.add(new PQCell(neighbor, tempDist));
                        visited[neighbor.getRow()][neighbor.getCol()] = true;
                    }
                }
            }
        }
    }

}