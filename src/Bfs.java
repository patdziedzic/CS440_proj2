import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
     * Get the nearest open cell
     */
    public static Cell nearestOpen_BFS(Cell cell){
        visited = new boolean[Ship.D][Ship.D];
        Queue<Cell> Q = new LinkedList<>(); //tell us what to explore next

        Q.add(cell);
        visited[cell.getRow()][cell.getCol()] = true;

        while (!Q.isEmpty()) {
            cell = Q.remove();

            if(cell.isOpen) return cell;

            if(isValid_NearestOpen(cell.up)) {
                Q.add(cell.up);
                visited[cell.up.getRow()][cell.up.getCol()] = true;
            }
            if(isValid_NearestOpen(cell.down)) {
                Q.add(cell.down);
                visited[cell.down.getRow()][cell.down.getCol()] = true;
            }
            if(isValid_NearestOpen(cell.left)) {
                Q.add(cell.left);
                visited[cell.left.getRow()][cell.left.getCol()] = true;
            }
            if(isValid_NearestOpen(cell.right)) {
                Q.add(cell.right);
                visited[cell.right.getRow()][cell.right.getCol()] = true;
            }
        }
        return null;
    }

    /**
     * Check if a neighbor is valid to search
     */
    private static boolean isValid_NearestOpen(Cell c) {
        return (c != null && !visited[c.getRow()][c.getCol()]);
    }
}