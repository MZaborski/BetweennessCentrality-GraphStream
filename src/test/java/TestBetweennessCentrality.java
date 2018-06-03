import org.graphstream.algorithm.measure.AbstractCentrality;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class TestBetweennessCentrality {

    public static void test1NormModMax1Min0() {

        Graph graph = new SingleGraph("Betweenness Test_1");

        Node A = graph.addNode("A");
        Node B = graph.addNode("B");
        Node C = graph.addNode("C");
        Node D = graph.addNode("D");
        Node E = graph.addNode("E");
        Node F = graph.addNode("F");
        Node G = graph.addNode("G");
        Node H = graph.addNode("H");

        graph.addEdge("AB", "A", "B");
        graph.addEdge("AC", "A", "C");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("BF", "B", "F");
        graph.addEdge("CD", "C", "D");
        graph.addEdge("CE", "C", "E");
        graph.addEdge("DE", "D", "E");
        graph.addEdge("DF", "D", "F");
        graph.addEdge("EF", "E", "F");
        graph.addEdge("EG", "E", "G");
        graph.addEdge("EH", "E", "H");
        graph.addEdge("FG", "F", "G");
        graph.addEdge("FH", "F", "H");
        graph.addEdge("GH", "G", "H");

        BetweennessCentrality betweennessCentrality = new BetweennessCentrality("betweenness", AbstractCentrality.NormalizationMode.MAX_1_MIN_0);

        betweennessCentrality.init(graph);
        betweennessCentrality.computeCentrality();

        System.out.println("A = " + A.getAttribute("BCV"));
        System.out.println("B = " + B.getAttribute("BCV"));
        System.out.println("C = " + C.getAttribute("BCV"));
        System.out.println("D = " + D.getAttribute("BCV"));
        System.out.println("E = " + E.getAttribute("BCV"));
        System.out.println("F = " + F.getAttribute("BCV"));
        System.out.println("G = " + G.getAttribute("BCV"));
        System.out.println("H = " + H.getAttribute("BCV"));

        graph.display();
    }

    public static void test2NormModSumIs1() {

        Graph graph = new SingleGraph("Betweenness Test");

        Node A = graph.addNode("A");
        Node B = graph.addNode("B");
        Node C = graph.addNode("C");
        Node D = graph.addNode("D");
        Node E = graph.addNode("E");
        Node F = graph.addNode("F");

        graph.addEdge("AB", "A", "B");
        graph.addEdge("AC", "A", "C");
        graph.addEdge("AF", "A", "F");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("FC", "F", "C");
        graph.addEdge("CD", "C", "D");
        graph.addEdge("FE", "F", "E");
        graph.addEdge("ED", "E", "D");
        graph.addEdge("BD", "B", "D");

        BetweennessCentrality bcb = new BetweennessCentrality("betweenness", AbstractCentrality.NormalizationMode.SUM_IS_1);

        bcb.init(graph);
        bcb.computeCentrality();

        System.out.println("A = " + A.getAttribute("BCV"));
        System.out.println("B = " + B.getAttribute("BCV"));
        System.out.println("C = " + C.getAttribute("BCV"));
        System.out.println("D = " + D.getAttribute("BCV"));
        System.out.println("E = " + E.getAttribute("BCV"));
        System.out.println("F = " + F.getAttribute("BCV"));

        graph.display();
    }

    public static void test3NormModNone() {

        Graph graph = new SingleGraph("Betweenness Test");

        Node A = graph.addNode("A");
        Node B = graph.addNode("B");
        Node C = graph.addNode("C");
        Node D = graph.addNode("D");
        Node E = graph.addNode("E");

        graph.addEdge("AB", "A", "B");
        graph.addEdge("AC", "A", "C");
        graph.addEdge("AD", "A", "D");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("BD", "B", "D");
        graph.addEdge("CD", "C", "D");
        graph.addEdge("EC", "E", "C");
        graph.addEdge("EB", "E", "B");
        graph.addEdge("ED", "E", "D");

        BetweennessCentrality bcb = new BetweennessCentrality("betweenness", AbstractCentrality.NormalizationMode.NONE);

        bcb.init(graph);
        bcb.computeCentrality();

        System.out.println("A = " + A.getAttribute("BCV"));
        System.out.println("B = " + B.getAttribute("BCV"));
        System.out.println("C = " + C.getAttribute("BCV"));
        System.out.println("D = " + D.getAttribute("BCV"));
        System.out.println("E = " + E.getAttribute("BCV"));
        graph.display();
    }

    public static void main(String[] args) {

        System.out.println("Wyniki testu 1 (normalizacja typu MAX_1_MIN_0): ");
        test1NormModMax1Min0();

        System.out.println("Wyniki testu 2 (normalizacja typu SUM_IS_1): ");
        test2NormModSumIs1();

        System.out.println("Wyniki testu 3 (normalizacja typu NONE - brak normalizacji): ");
        test3NormModNone();
    }
}
