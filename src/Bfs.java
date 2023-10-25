import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Bfs {
    /**
     * Shortest Path BFS
     */
    public static LinkedList<Cell> shortestPathBFS(Cell bot, Cell button, Cell[][] ship){
        //make sure isVisited is false for all cells
        for (int r = 0; r < Ship.D; r++) {
            for (int c = 0; c < Ship.D; c++) {
                ship[r][c].isVisited = false;
            }
        }

        Queue<Cell> Q = new LinkedList<>(); //tell us what to explore next
        HashMap<Cell, Cell> parentNodes = new HashMap<>(); //keeps track of where bot has visited
        //^ Map the previous to the next by using .put(next, previous)

        Q.add(bot);
        bot.isVisited = true;
        
        while (!Q.isEmpty()) {
            bot = Q.remove();

            if(bot.equals(button)) {
                //Once path found, start from end and go back and store the path into LinkedList
                LinkedList<Cell> shortestPath = new LinkedList<>();
                Cell ptr = button;
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
                bot.up.isVisited = true;
            }
            if(isValid(bot.down)) {
                parentNodes.put(bot.down, bot);
                Q.add(bot.down);
                bot.down.isVisited = true;
            }
            if(isValid(bot.left)) {
                parentNodes.put(bot.left, bot);
                Q.add(bot.left);
                bot.left.isVisited = true;
            }
            if(isValid(bot.right)) {
                parentNodes.put(bot.right, bot);
                Q.add(bot.right);
                bot.right.isVisited = true;
            }
        }
        return null;
    }

    /**
     * Check if a neighbor is valid to search
     */
    private static boolean isValid(Cell c) {
        return (c != null && !c.getOnFire() && c.isOpen && !c.isVisited);
    }


    /**
     * Shortest Path BFS using isValid_Bot3()
     */
    public static LinkedList<Cell> shortestPathBFS_Bot3(Cell bot, Cell button, Cell[][] ship){
        //make sure isVisited is false for all cells
        for (int r = 0; r < Ship.D; r++) {
            for (int c = 0; c < Ship.D; c++) {
                ship[r][c].isVisited = false;
            }
        }

        Queue<Cell> Q = new LinkedList<>(); //tell us what to explore next
        HashMap<Cell, Cell> parentNodes = new HashMap<>(); //keeps track of where bot has visited
        //^ Map the previous to the next by using .put(next, prev)

        Q.add(bot);
        bot.isVisited = true;

        while (!Q.isEmpty()) {
            bot = Q.remove();

            if(bot.equals(button)) {
                //Once path found, start from end and go back and store the path into LinkedList
                LinkedList<Cell> shortestPath = new LinkedList<>();
                Cell ptr = button;
                while (ptr != null) {
                    shortestPath.add(ptr);
                    ptr = parentNodes.get(ptr);
                }
                Collections.reverse(shortestPath);
                return shortestPath;
            }

            if(isValid_Bot3(bot.up)) {
                parentNodes.put(bot.up, bot); //next, previous
                Q.add(bot.up);
                bot.up.isVisited = true;
            }
            if(isValid_Bot3(bot.down)) {
                parentNodes.put(bot.down, bot);
                Q.add(bot.down);
                bot.down.isVisited = true;
            }
            if(isValid_Bot3(bot.left)) {
                parentNodes.put(bot.left, bot);
                Q.add(bot.left);
                bot.left.isVisited = true;
            }
            if(isValid_Bot3(bot.right)) {
                parentNodes.put(bot.right, bot);
                Q.add(bot.right);
                bot.right.isVisited = true;
            }
        }
        return null;
    }

    /**
     * Check if a neighbor is valid to search for bot 3
     */
    private static boolean isValid_Bot3(Cell c) {
        //additionally check potential fire neighbors
        return (c != null && !c.getOnFire() && c.isOpen && !c.isVisited && c.k == 0);
    }
}