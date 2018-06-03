import java.util.*;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.measure.AbstractCentrality;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;


public class BetweennessCentrality extends AbstractCentrality implements Algorithm {

    protected Graph graph;
    protected static double INIT_DISTANCE = -1;
    protected double maxCentralityValue;
    protected double minCentralityValue;
    protected double maxMinusMin;
    protected boolean isMinCentralityValueSet = false;

    protected String centralityAttributeName = "BCV";
    protected String predAttributeName = "predecessorsName";
    protected String sigmaAttributeName = "sigmaValue";
    protected String distAttributeName = "distanceValue";
    protected String deltaAttributeName = "deltaValue";


    public BetweennessCentrality(String attribute, NormalizationMode normalize) {
        super(attribute, normalize);
    }

    public void init(Graph graph) {
        this.graph = graph;
    }

    @Override
    protected void computeCentrality() {
        if (graph != null) {
            this.computeBetweennessCentrality(graph);
            normalize();
        }
    }

    public void computeBetweennessCentrality(Graph graph) {
        double nodeCentrality;
        init(graph);
        initNodesBcValue(graph);

        for (Node sourceNode : graph) {

            Stack<Node> nodesFromSourceNode;
            nodesFromSourceNode = simpleExplore(sourceNode, graph);

            while (!nodesFromSourceNode.isEmpty()) {
                Node w = nodesFromSourceNode.pop();

                for (Node v : predecessorsOf(w)) {

                    double c = ((sigma(v) / sigma(w)) * (1.0 + delta(w)));
                    setDelta(v, delta(v) + c);
                }
                if (w != sourceNode) {
                    nodeCentrality = centrality(w) + delta(w);
                    setCentrality(w, nodeCentrality);
                    if (maxCentralityValue < nodeCentrality) {
                        maxCentralityValue = nodeCentrality;
                    }
                    if (!isMinCentralityValueSet) {
                        minCentralityValue = nodeCentrality;
                        isMinCentralityValueSet = true;
                    }
                    if (minCentralityValue > nodeCentrality) {
                        minCentralityValue = nodeCentrality;
                    }
                    maxMinusMin = maxCentralityValue - minCentralityValue;
                }
            }
        }
    }

    protected Stack<Node> simpleExplore(Node sourceNode, Graph graph) {

        LinkedList<Node> queue = new LinkedList<>();
        Stack<Node> sP = new Stack<Node>();

        setupNodesInitialValue(graph);
        queue.add(sourceNode);
        setSigma(sourceNode, 1.0);
        setDistance(sourceNode, 0.0);

        while (!queue.isEmpty()) {
            Node nodeV = queue.removeFirst();

            sP.add(nodeV);
            for (Iterator<Edge> it = nodeV.getLeavingEdgeIterator(); it.hasNext(); ) {

                Edge neighborsNodeV2 = it.next();
                Node w = neighborsNodeV2.getOpposite(nodeV);

                if (distance(w) == INIT_DISTANCE) {
                    queue.add(w);
                    setDistance(w, distance(nodeV) + 1);
                }

                if (distance(w) == (distance(nodeV) + 1.0)) {
                    setSigma(w, sigma(w) + sigma(nodeV));
                    addToPredecessorsOf(w, nodeV);
                }
            }
        }
        return sP;
    }


    public void normalize() {

        if (this.getNormalizationMode().equals(NormalizationMode.MAX_1_MIN_0)) {

            double max = maxCentralityValue, min = minCentralityValue;
            double maxMinusMin = max - min;
            for (int i = 0; i < graph.getNodeCount(); i++) {
                setCentrality(graph.getNode(i), (getCentrality(i) - minCentralityValue) / maxMinusMin);
            }

        } else if (this.getNormalizationMode().equals(NormalizationMode.SUM_IS_1)) {

            double sum = 0;
            for (int i = 0; i < graph.getNodeCount(); i++) {
                sum += getCentrality(i);
            }
            for (int i = 0; i < graph.getNodeCount(); i++) {
                setCentrality(graph.getNode(i), getCentrality(i) / sum);
            }
        }
    }

    @SuppressWarnings("all")
    protected void addToPredecessorsOf(Node node, Node predecessor) {
        HashSet<Node> preds = (HashSet<Node>) node.getAttribute(predAttributeName);
        preds.add(predecessor);
    }

    protected void clearPredecessorsOf(Node node) {
        HashSet<Node> set = new HashSet<Node>();
        node.setAttribute(predAttributeName, set);
    }

    protected void initNodesBcValue(Graph graph) {
        for (Node node : graph) {
            setCentrality(node, 0.0);
        }
    }

    protected void setupNodesInitialValue(Graph graph) {
        for (Node node : graph) {
            clearPredecessorsOf(node);
            setSigma(node, 0.0);
            setDelta(node, 0.0);
            setDistance(node, INIT_DISTANCE);
        }
    }

    public double getCentrality(int id) {
        return graph.getNode(id).getAttribute("BCV");
    }

    @SuppressWarnings("all")
    protected Set<Node> predecessorsOf(Node node) {
        return (HashSet<Node>) node.getAttribute(predAttributeName);
    }

    protected void setSigma(Node node, double sigma) {
        node.setAttribute(sigmaAttributeName, sigma);
    }

    protected void setDistance(Node node, double distance) {
        node.setAttribute(distAttributeName, distance);
    }

    protected void setDelta(Node node, double delta) {
        node.setAttribute(deltaAttributeName, delta);
    }

    public void setCentrality(Element elt, double centrality) {
        elt.setAttribute(centralityAttributeName, centrality);
    }

    protected double sigma(Node node) {
        return node.getNumber(sigmaAttributeName);
    }

    protected double distance(Node node) {
        return node.getNumber(distAttributeName);
    }

    protected double delta(Node node) {
        return node.getNumber(deltaAttributeName);
    }

    public double centrality(Element elt) {
        return elt.getNumber(centralityAttributeName);
    }
}
