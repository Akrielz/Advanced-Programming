package sample;

import javafx.scene.paint.Color;

enum FigureType{
    REGULAR_POLYGON,
    STAR,
    CIRCLE
}

public class Figure implements Comparable<Figure> {
    private double x;
    private double y;

    private FigureType figureType;

    private int stroke;
    private int size;
    private Color color;
    private int nrSides;
    private int depth;

    //REGULATED POLYGON
    public Figure(int x, int y, int stroke, int size, int nrSides, Color color, int depth) {
        this.x = x;
        this.y = y;
        this.stroke = stroke;
        this.size = size;
        this.nrSides = nrSides;
        this.figureType = FigureType.REGULAR_POLYGON;
        this.color = color;
        this.depth = depth;
    }

    //STAR
    public Figure(double x, double y, FigureType figureType, int stroke, int size, int nrSides, Color color, int depth) {
        this.x = x;
        this.y = y;
        this.figureType = figureType;
        this.stroke = stroke;
        this.size = size;
        this.color = color;

        if (figureType == FigureType.STAR){
            this.nrSides = 2*nrSides;
        }

        this.depth = depth;
    }

    //CIRCLE
    public Figure(int x, int y, FigureType figureType, int stroke, int size, Color color, int depth) {
        this.x = x;
        this.y = y;
        this.figureType = figureType;
        this.stroke = stroke;
        this.size = size;

        if (figureType == FigureType.CIRCLE){
            this.nrSides = 360;
        }

        this.color = color;

        this.depth = depth;
    }

    @Override
    public String toString() {
        return "Figure{" +
                "x=" + x +
                ", y=" + y +
                ", figureType=" + figureType +
                ", stroke=" + stroke +
                ", size=" + size +
                ", color=" + color +
                ", nrSides=" + nrSides +
                '}';
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public FigureType getFigureType() {
        return figureType;
    }

    public void setFigureType(FigureType figureType) {
        this.figureType = figureType;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getNrSides() {
        return nrSides;
    }

    public void setNrSides(int nrSides) {
        this.nrSides = nrSides;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public int compareTo(Figure figure) {
        return figure.depth - this.depth;
    }
}