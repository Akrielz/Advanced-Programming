package sample;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Integer.max;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public class Graph {
    private int nrNodes;
    private int nrEdges;

    private boolean isTree;

    private List<List<Integer>> adjacentList;
    private List <Integer> parents;

    public Graph() {
        nrNodes = 0;
        nrEdges = 0;
        adjacentList = new ArrayList<>();
        parents = new ArrayList<>();
        isTree = false;
    }

    public void load(String path){
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            nrNodes = scanner.nextInt();
            nrEdges = scanner.nextInt();
            isTree = scanner.nextInt() == 1;

            if (isTree) {
                for (int i = 0; i < nrNodes; i++) {
                    adjacentList.add(new ArrayList<>());
                }

                for (int i = 0; i < nrNodes; i++){
                    int parent = scanner.nextInt();
                    parents.add(parent);

                    if (parent != -1) {
                        adjacentList.get(parent).add(i);
                    }
                }
            }
            else {
                for (int i = 0; i < nrNodes; i++) {
                    adjacentList.add(new ArrayList<>());
                }

                for (int i = 0; i < nrEdges; i++) {
                    int u = scanner.nextInt();
                    int v = scanner.nextInt();

                    adjacentList.get(u).add(v);
                    adjacentList.get(v).add(u);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext graphicsContext){
        if (isTree){
            drawTree(graphicsContext);
        }
        else{
            drawCircular(graphicsContext);
        }
    }

    private int getNodeDepth(int node){
        if (parents.get(node) != -1){
           return 1 + getNodeDepth(parents.get(node));
        }
        return 0;
    }

    private void drawTree(GraphicsContext graphicsContext){
        //DrawingTree
        int centerX = (int) (graphicsContext.getCanvas().getHeight()/2);

        int margin = 40;

        int treeDepth = 0;
        for (int i = 0; i < nrNodes; i++){
            treeDepth = max(treeDepth, getNodeDepth(i));
        }

        int root = -1;
        for (int i = 0; i < nrNodes; i++){
            if (parents.get(i) == -1)
                root = i;
        }

        final int startY = margin;
        final int stopY = (int) (graphicsContext.getCanvas().getHeight() - margin);

        int stepY = 0;
        if (treeDepth != 0)
            stepY = (stopY-startY)/(treeDepth);

        List<Integer> queue = new ArrayList<>();
        queue.add(root);

        double []x_es = new double[nrNodes];
        double []y_es = new double[nrNodes];
        double []rightLimit = new double[nrNodes];
        double []leftLimit = new double[nrNodes];

        x_es[root] = centerX;
        y_es[root] = startY;
        rightLimit[root] = graphicsContext.getCanvas().getWidth()-margin;
        leftLimit[root] = margin;

        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(5);
        for (int i = 0; i < queue.size(); i++){
            int node = queue.get(i);
            queue.addAll(adjacentList.get(node));

            double stepX = (rightLimit[node]-leftLimit[node]) / (adjacentList.get(node).size());
            double currentX = leftLimit[node];
            for (int neighbour : adjacentList.get(node)){
                leftLimit[neighbour] = currentX;
                rightLimit[neighbour] = currentX + stepX;
                currentX += stepX;
                y_es[neighbour] = y_es[node] + stepY;
                x_es[neighbour] = (leftLimit[neighbour]+rightLimit[neighbour])/2;

                graphicsContext.strokeLine(x_es[node], y_es[node], x_es[neighbour], y_es[neighbour]);
            }
        }

        int nodeSize = (int) (50);
        graphicsContext.setFill(Color.PURPLE);

        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.setLineWidth(1);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setTextBaseline(VPos.CENTER);

        for (int i = 0; i < nrNodes; i++){
            graphicsContext.fillOval(x_es[i]-nodeSize/2, y_es[i]-nodeSize/2, nodeSize, nodeSize);
            graphicsContext.strokeText(String.valueOf(i), x_es[i], y_es[i]);
        }

    }

    private void drawCircular(GraphicsContext graphicsContext){
        //DrawCircular
        int circleSize = (int) (Math.min(graphicsContext.getCanvas().getHeight(), graphicsContext.getCanvas().getWidth()) - 100) / 2;
        int centerX = (int) (graphicsContext.getCanvas().getHeight()/2);
        int centerY = (int) (graphicsContext.getCanvas().getWidth()/2);

        int nodeSize = 50;

        double[] y_es = IntStream.range(0, nrNodes)
                .mapToDouble(i -> centerX + circleSize*Math.sin(2*Math.PI*(double)i/(double)nrNodes))
                .toArray();


        double[] x_es = IntStream.range(0, nrNodes)
                .mapToDouble(i -> centerY + circleSize*Math.cos(2*Math.PI*(double)i/(double)nrNodes))
                .toArray();

        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(5);
        for (int u = 0; u < nrNodes; u++){
            for (int v : adjacentList.get(u)){
                graphicsContext.strokeLine(x_es[u], y_es[u], x_es[v], y_es[v]);

            }
        }

        graphicsContext.setFill(Color.PURPLE);

        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.setLineWidth(1);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setTextBaseline(VPos.CENTER);

        for (int i = 0; i < nrNodes; i++){
            graphicsContext.fillOval(x_es[i]-nodeSize/2, y_es[i]-nodeSize/2, nodeSize, nodeSize);
            graphicsContext.strokeText(String.valueOf(i), x_es[i], y_es[i]);
        }
    }

}
