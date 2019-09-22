
import java.io.*; 
import java.util.*;

/**
 *
 * @author Adnan Ahmed
 */
class EightPuzzleSearchAgent
{
    EightPuzzleProblem problem;
    private final Queue<Node<EightPuzzleBoard, EightPuzzleAction>> frontier;
    private LinkedList<Node<EightPuzzleBoard, EightPuzzleAction>> starFrontier;
    private int numNodes = 0;
    private LinkedList<Integer> bfsNodeList = new LinkedList();
    private LinkedList<Integer> aStarH1NodeList = new LinkedList();
    private LinkedList<Integer> aStarH2NodeList = new LinkedList();

    public EightPuzzleSearchAgent(EightPuzzleProblem aProblem) {
        this.problem = aProblem;
        this.frontier = new LinkedList();
        this.starFrontier = new LinkedList();
    }

    public static void main(String[] args) {
        String inputFile = args[0];
        String line = null;
        int[][] initial = new int[3][3];
        int[][] goal = new int[3][3];

        //opening file and parsing
        try {
            FileReader fileReader = new FileReader(args[0]);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int col = 0;
            int row = 0;

            int count = 0;
            while((line = bufferedReader.readLine()) != null) {
                if (count != 0 && count != 4) {
                    //System.out.println(line);
                    StringTokenizer tempString = new StringTokenizer(line);
                    while (tempString.hasMoreTokens()) {
                        //System.out.println(tempString.nextToken());
                        if (col==3) { col = 0; }
                        if (row==3) { row = 0; }
                        if (count>4) {
                           goal[row][col] = Integer.parseInt(tempString.nextToken());
                        } else {
                            initial[row][col] = Integer.parseInt(tempString.nextToken());
                        }
                        col++;
                    }
                    row++;
                }
                count++;
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Could not open file: " + inputFile);
        }
        catch(IOException ex) {
            System.out.println("Error reading file: " + inputFile);
        }

        EightPuzzleBoard initialBoard = new EightPuzzleBoard(initial, 0, 0);
        EightPuzzleBoard goalBoard = new EightPuzzleBoard(goal, 0, 0);
        EightPuzzleProblem eightPuzzleProblem = new EightPuzzleProblem(initialBoard, goalBoard);
        EightPuzzleSearchAgent searchAgent = new EightPuzzleSearchAgent(eightPuzzleProblem);

        System.out.println("Initial State");
        initialBoard.printState();
        System.out.println("Goal State");
        goalBoard.printState();

        Node<EightPuzzleBoard, EightPuzzleAction> BFSsolutionNode = breadthFirstSearch(searchAgent);
        printSolution(BFSsolutionNode, false, searchAgent.numNodes);

        searchAgent.numNodes = 0;
        Node<EightPuzzleBoard, EightPuzzleAction> aStarH1solutionNode = aStarSearchH1(searchAgent);
        printSolution(aStarH1solutionNode, true, searchAgent.numNodes);

        searchAgent.numNodes = 0;
        searchAgent.starFrontier.clear();
        Node<EightPuzzleBoard, EightPuzzleAction> aStarH2solutionNode = aStarSearchH2(searchAgent);
        printSolution(aStarH2solutionNode, true, searchAgent.numNodes);

        System.out.println("");
        System.out.println("Results:");
        System.out.println("");
        System.out.println("Depth |    Search cost (nodes generated)     |");
        System.out.println("      | A* h1 | A* h2 | Breadth First Search |");
        int printLength = 0;
        if (searchAgent.bfsNodeList.size() >= searchAgent.aStarH1NodeList.size() && searchAgent.bfsNodeList.size() >= searchAgent.aStarH2NodeList.size()) {
            printLength = searchAgent.bfsNodeList.size();
        } else if (searchAgent.aStarH1NodeList.size() >= searchAgent.bfsNodeList.size() && searchAgent.aStarH1NodeList.size() >= searchAgent.aStarH2NodeList.size()) {
            printLength = searchAgent.aStarH1NodeList.size();
        } else if (searchAgent.aStarH2NodeList.size() > searchAgent.bfsNodeList.size() && searchAgent.aStarH2NodeList.size() > searchAgent.aStarH1NodeList.size()) {
            printLength = searchAgent.aStarH2NodeList.size();
        }

        int bfsVal = 0;
        int aStarH1Val = 0;
        int aStarH2Val = 0;
        for (int i = 0; i < printLength; i++) {
            bfsVal = 0;
            aStarH1Val = 0;
            aStarH2Val = 0;
            if (i < searchAgent.aStarH1NodeList.size()) { aStarH1Val = searchAgent.aStarH1NodeList.get(i); }
            if (i < searchAgent.aStarH2NodeList.size()) { aStarH2Val = searchAgent.aStarH2NodeList.get(i); }
            if (i < searchAgent.bfsNodeList.size()) { bfsVal = searchAgent.bfsNodeList.get(i); }
            System.out.println("  " + (i+1) + "   |   " + aStarH1Val + "   |   " + aStarH2Val + "   |          " + bfsVal + "          |");
        }

    }

    public static Node<EightPuzzleBoard, EightPuzzleAction> breadthFirstSearch(EightPuzzleSearchAgent searchAgent) {
        System.out.println();
        System.out.println("Breadth First Search:");

        Node<EightPuzzleBoard, EightPuzzleAction> node = new Node<EightPuzzleBoard, EightPuzzleAction>(searchAgent.problem.getInitialState(), null, null, 0.0);
        node.getState().printState();
        System.out.print("Action = ");
        List<EightPuzzleAction> actionList = searchAgent.problem.getActions(node.getState());
        for (EightPuzzleAction action: actionList) { action.printAction(); System.out.print(", "); }
        System.out.println("");
        System.out.println("g = " + node.getPathCost());
        System.out.println("-------------------------");
        if (searchAgent.problem.goalTest(node.getState())) {
            searchAgent.numNodes = 1;
            return node;
        }
        searchAgent.frontier.add(node);
        Queue<Node<EightPuzzleBoard, EightPuzzleAction>> explored = new LinkedList();
        while(true) {
            if (searchAgent.frontier.isEmpty()) { return null; }
            node = searchAgent.frontier.remove();
            explored.add(node);
            actionList = searchAgent.problem.getActions(node.getState());
            searchAgent.bfsNodeList.add(actionList.size());
            if (searchAgent.problem.goalTest(node.getState())) { 
                searchAgent.numNodes = explored.size() + searchAgent.frontier.size();
                return node;
            }
            for (EightPuzzleAction action: actionList) {
                int[][] parentBoard = new int[3][3]; //copy parent state -> not the direct reference
                parentBoard = node.getState().copyBoard(parentBoard);
                EightPuzzleBoard parentState = new EightPuzzleBoard(parentBoard, 0, 0);
                Node<EightPuzzleBoard, EightPuzzleAction> child = new Node<EightPuzzleBoard, EightPuzzleAction>(searchAgent.problem.getResult(parentState, action), node, action, node.getPathCost()+1.0);
                child.getState().printState();
                System.out.print("Action = ");
                List<EightPuzzleAction> childActions = searchAgent.problem.getActions(child.getState());
                for (EightPuzzleAction childAction: childActions) { childAction.printAction(); System.out.print(", "); }
                System.out.println("");
                System.out.println("g = " + child.getPathCost());
                System.out.println("-------------------------");
                if (!searchAgent.frontier.contains(child) || !explored.contains(child)) {
                    searchAgent.frontier.add(child);
                }
            }
        }
    }

    public static Node<EightPuzzleBoard, EightPuzzleAction> aStarSearchH1(EightPuzzleSearchAgent searchAgent) {
        System.out.println();
        System.out.println("A* Search with h1:");

        Node<EightPuzzleBoard, EightPuzzleAction> node = new Node<EightPuzzleBoard, EightPuzzleAction>(searchAgent.problem.getInitialState(), null, null, 0.0);
        node.getState().printState();
        System.out.print("Action = ");
        List<EightPuzzleAction> actionList = searchAgent.problem.getActions(node.getState());
        for (EightPuzzleAction action: actionList) { action.printAction(); System.out.print(", "); }
        System.out.println("");
        double gValue = node.getPathCost();
        System.out.print("g = " + gValue);
        int hValue = searchAgent.problem.setHvalue(node.getState());
        System.out.print(", h = " + hValue);
        node.getState().setFvalue(calculateFvalue(gValue, hValue));
        int fValue = node.getState().getFvalue();
        System.out.println(", f = " + fValue);
        System.out.println("-------------------------");
        searchAgent.numNodes = 1;
        searchAgent.starFrontier.add(node);
        Queue<Node<EightPuzzleBoard, EightPuzzleAction>> explored = new LinkedList();
        while(true) {
            if (searchAgent.starFrontier.isEmpty()) { return null; }
            node = searchAgent.starFrontier.remove();
            explored.add(node);
            actionList = searchAgent.problem.getActions(node.getState());
            searchAgent.aStarH1NodeList.add(actionList.size());
            if (searchAgent.problem.goalTest(node.getState())) { 
                searchAgent.numNodes = explored.size() + searchAgent.starFrontier.size();
                return node;
            }
            for (EightPuzzleAction action: actionList) {
                int[][] parentBoard = new int[3][3]; //copy parent state -> not the direct reference
                parentBoard = node.getState().copyBoard(parentBoard);
                EightPuzzleBoard parentState = new EightPuzzleBoard(parentBoard, 0, 0);
                Node<EightPuzzleBoard, EightPuzzleAction> child = new Node<EightPuzzleBoard, EightPuzzleAction>(searchAgent.problem.getResult(parentState, action), node, action, node.getPathCost()+1.0);
                child.getState().printState();
                System.out.print("Action = ");
                List<EightPuzzleAction> childActions = searchAgent.problem.getActions(child.getState());
                for (EightPuzzleAction childAction: childActions) { childAction.printAction(); System.out.print(", "); }
                System.out.println("");
                gValue = child.getPathCost();
                System.out.print("g = " + gValue);
                hValue = searchAgent.problem.setHvalue(node.getState());
                System.out.print(", h = " + hValue);
                node.getState().setFvalue(calculateFvalue(gValue, hValue));
                fValue = node.getState().getFvalue();
                System.out.println(", f = " + fValue);
                System.out.println("-------------------------");
                if (!searchAgent.starFrontier.contains(child) || !explored.contains(child)) {
                    gValue = child.getPathCost();
                    hValue = searchAgent.problem.setHvalue(child.getState());
                    child.getState().setFvalue(calculateFvalue(gValue, hValue));
                    searchAgent.starFrontier.add(child);
                }
            }
            Comparator<Node<EightPuzzleBoard, EightPuzzleAction>> compareByF = Comparator.comparing(e -> e.getState().getFvalue());
            Collections.sort(searchAgent.starFrontier, compareByF);
        }
    }

    public static Node<EightPuzzleBoard, EightPuzzleAction> aStarSearchH2(EightPuzzleSearchAgent searchAgent) {
        System.out.println();
        System.out.println("A* Search with h2:");

        Node<EightPuzzleBoard, EightPuzzleAction> node = new Node<EightPuzzleBoard, EightPuzzleAction>(searchAgent.problem.getInitialState(), null, null, 0.0);
        node.getState().printState();
        System.out.print("Action = ");
        List<EightPuzzleAction> actionList = searchAgent.problem.getActions(node.getState());
        for (EightPuzzleAction action: actionList) { action.printAction(); System.out.print(", "); }
        System.out.println("");
        double gValue = node.getPathCost();
        System.out.print("g = " + gValue);
        int hValue = searchAgent.problem.setHvalue(node.getState());
        System.out.print(", h = " + hValue);
        node.getState().setFvalue(calculateFvalue(gValue, hValue));
        int fValue = node.getState().getFvalue();
        System.out.println(", f = " + fValue);
        System.out.println("-------------------------");
        searchAgent.numNodes = 1;
        searchAgent.starFrontier.add(node);
        Queue<Node<EightPuzzleBoard, EightPuzzleAction>> explored = new LinkedList();
        while(true) {
            if (searchAgent.starFrontier.isEmpty()) { return null; }
            node = searchAgent.starFrontier.remove();
            explored.add(node);
            actionList = searchAgent.problem.getActions(node.getState());
            searchAgent.aStarH2NodeList.add(actionList.size());
            if (searchAgent.problem.goalTest(node.getState())) { 
                searchAgent.numNodes = explored.size() + searchAgent.starFrontier.size();
                return node;
            }
            for (EightPuzzleAction action: actionList) {
                int[][] parentBoard = new int[3][3]; //copy parent state -> not the direct reference
                parentBoard = node.getState().copyBoard(parentBoard);
                EightPuzzleBoard parentState = new EightPuzzleBoard(parentBoard, 0, 0);
                Node<EightPuzzleBoard, EightPuzzleAction> child = new Node<EightPuzzleBoard, EightPuzzleAction>(searchAgent.problem.getResult(parentState, action), node, action, node.getPathCost()+1.0);
                child.getState().printState();
                System.out.print("Action = ");
                List<EightPuzzleAction> childActions = searchAgent.problem.getActions(child.getState());
                for (EightPuzzleAction childAction: childActions) { childAction.printAction(); System.out.print(", "); }
                System.out.println("");
                gValue = child.getPathCost();
                System.out.print("g = " + gValue);
                hValue = searchAgent.problem.setHvalue(node.getState());
                System.out.print(", h = " + hValue);
                node.getState().setFvalue(calculateFvalue(gValue, hValue));
                fValue = node.getState().getFvalue();
                System.out.println(", f = " + fValue);
                System.out.println("-------------------------");
                if (!searchAgent.starFrontier.contains(child) || !explored.contains(child)) {
                    gValue = child.getPathCost();
                    hValue = searchAgent.problem.setHvalueH2(child.getState());
                    child.getState().setFvalue(calculateFvalue(gValue, hValue));
                    searchAgent.starFrontier.add(child);
                }
            }
            Comparator<Node<EightPuzzleBoard, EightPuzzleAction>> compareByF = Comparator.comparing(e -> e.getState().getFvalue());
            Collections.sort(searchAgent.starFrontier, compareByF);
        }

    }


    public static int calculateFvalue(double gValue, int hValue) {
        int fValue = (int) gValue + hValue;
        return fValue;
    }

    public static void printSolution(Node<EightPuzzleBoard, EightPuzzleAction> solutionNode, boolean isAstar, int numNodes) {
        Stack<Node<EightPuzzleBoard, EightPuzzleAction>> solutionPath = new Stack();
        if (solutionNode != null) {
            System.out.println("Number of nodes on the tree = " + numNodes);
            System.out.println("");
            solutionPath.push(solutionNode);
            while (solutionNode.getParent() != null) {
                solutionNode = solutionNode.getParent();
                solutionPath.push(solutionNode);
            }
        }
        Node<EightPuzzleBoard, EightPuzzleAction> displayNode = solutionPath.pop();
        System.out.println("Solution: ");
        System.out.println("");
        System.out.println("Initial State");
        double g = 0.0;
        int h = 0; int f = 0;
        while(!solutionPath.isEmpty()) {
            displayNode.getState().printState();
            g = displayNode.getPathCost();
            h = displayNode.getState().getHvalue();
            f = displayNode.getState().getFvalue();
            displayNode = solutionPath.pop();
            System.out.print("Action = ");
            displayNode.getAction().printAction();
            System.out.println("");
            System.out.print("g = " + g);
            if (isAstar) {
                System.out.print(", h = " + h);
                System.out.println(", f = " + f);
            } else { System.out.println(""); }
            System.out.println("-------------------------");
        }
        displayNode.getState().printState();
        g = displayNode.getPathCost();
        h = displayNode.getState().getHvalue();
        f = displayNode.getState().getFvalue();
        System.out.print("g = " + g);
        if (isAstar) {
                System.out.print(", h = " + h);
                System.out.println(", f = " + f);
        } else { System.out.println(""); }
        System.out.println("Number of nodes on the tree = " + numNodes);
    }

}