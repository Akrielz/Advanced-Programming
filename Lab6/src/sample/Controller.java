package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    @FXML
    private Button exitButton, loadButton, saveButton, resetButton;
    @FXML
    private TextField sizeTxt, strokeTxt, sidesTxt, depthTxt;
    @FXML
    private ComboBox<String> shapesCB;
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorCP;

    @FXML
    private CheckBox deleteBox;

    private Figure currentFigure = null;

    private List<Figure> figures;

    boolean deleteMode = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shapesCB.getItems().addAll(
                "Regular Polygon",
                "Circle",
                "Star"
        );

        figures = new ArrayList<>();
    }

    public boolean isNumber(String stringNumber) {
        if (stringNumber == null) {
            return false;
        }
        try {
            double number = Double.parseDouble(stringNumber);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean updateFigure(){
        if (!isNumber(sizeTxt.getText()) || !isNumber(strokeTxt.getText())
                || !isNumber(sidesTxt.getText()) || !isNumber(depthTxt.getText())){

            System.out.println("Field are not numbers.");
            return false;
        }

        if (shapesCB.getValue() == null){
            System.out.println("Shape not selected.");
            return false;
        }

        int size = Integer.parseInt(sizeTxt.getText());
        int stroke = Integer.parseInt(strokeTxt.getText());
        int sides = Integer.parseInt(sidesTxt.getText());
        int depth = Integer.parseInt(depthTxt.getText());

        String figureType = shapesCB.getValue();

        Color color = colorCP.getValue();

        switch (figureType){
            case"Regular Polygon":
                currentFigure = new Figure(0, 0, stroke, size, sides, color, depth);
                break;

            case "Circle":
                currentFigure = new Figure(0, 0, FigureType.CIRCLE, stroke, size, color, depth);
                break;

            case "Star":
                currentFigure = new Figure(0, 0, FigureType.STAR, stroke, size, sides, color, depth);
                break;

            default:
                System.out.println("Type not accepted");
                return false;
        }
        return true;
    }

    public void drawFigure(GraphicsContext graphicsContext, Figure figure){
        if(figure.getFigureType() == FigureType.REGULAR_POLYGON){
            double centerX = figure.getX();
            double centerY = figure.getY();
            double size = figure.getSize();
            double stroke = figure.getStroke();

            int sides = figure.getNrSides();

            double[] x_es = IntStream.range(0,sides)
                    .mapToDouble(i -> centerX + size*Math.sin(2*Math.PI*(double)i/(double)sides))
                    .toArray();


            double[] y_es = IntStream.range(0,sides)
                    .mapToDouble(i -> centerY + size*Math.cos(2*Math.PI*(double)i/(double)sides))
                    .toArray();

            double[] x_esStroke = IntStream.range(0,sides)
                    .mapToDouble(i -> centerX + (size+stroke)*Math.sin(2*Math.PI*(double)i/(double)sides))
                    .toArray();


            double[] y_esStroke = IntStream.range(0,sides)
                    .mapToDouble(i -> centerY + (size+stroke)*Math.cos(2*Math.PI*(double)i/(double)sides))
                    .toArray();

            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillPolygon(x_esStroke, y_esStroke, sides);

            graphicsContext.setFill(figure.getColor());
            graphicsContext.fillPolygon(x_es, y_es, sides);

        }

        if (figure.getFigureType() == FigureType.STAR){
            double centerX = figure.getX();
            double centerY = figure.getY();
            double size = figure.getSize();
            double stroke = figure.getStroke();

            int sides = figure.getNrSides();

            double[] x_es = new double[sides];
            double[] y_es = new double[sides];

            double[] x_esStroke = new double[sides];
            double[] y_esStroke = new double[sides];

            for (int i = 0; i < sides; i++){
                if (i % 2 == 0){
                    x_es[i] = centerX + size*Math.sin(2*Math.PI*(double)i/(double)sides);
                    y_es[i] = centerY + size*Math.cos(2*Math.PI*(double)i/(double)sides);

                    x_esStroke[i] = centerX + (size+stroke)*Math.sin(2*Math.PI*(double)i/(double)sides);
                    y_esStroke[i] = centerY + (size+stroke)*Math.cos(2*Math.PI*(double)i/(double)sides);
                }
                else{
                    x_es[i] = centerX + size/3*Math.sin(2*Math.PI*(double)i/(double)sides);
                    y_es[i] = centerY + size/3*Math.cos(2*Math.PI*(double)i/(double)sides);

                    x_esStroke[i] = centerX + (size/3+stroke)*Math.sin(2*Math.PI*(double)i/(double)sides);
                    y_esStroke[i] = centerY + (size/3+stroke)*Math.cos(2*Math.PI*(double)i/(double)sides);
                }
            }
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillPolygon(x_esStroke, y_esStroke, sides);

            graphicsContext.setFill(figure.getColor());
            graphicsContext.fillPolygon(x_es, y_es, sides);
        }

        if (figure.getFigureType() == FigureType.CIRCLE){
            double centerX = figure.getX();
            double centerY = figure.getY();
            double size = figure.getSize();
            double stroke = figure.getStroke();

            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillOval(centerX-size-stroke, centerY-size-stroke, 2*(size+stroke), 2*(size+stroke));

            graphicsContext.setFill(figure.getColor());
            graphicsContext.fillOval(centerX-size, centerY-size, 2*size, 2*size);
        }
    }

    public void mousePressedOnCanvas(MouseEvent event){
        if (!deleteMode) {
            if (!updateFigure()) {
                return;
            }

            double mouseX = event.getX();
            double mouseY = event.getY();

            currentFigure.setX(mouseX);
            currentFigure.setY(mouseY);

            figures.add(currentFigure);
            figures.sort(Figure::compareTo);

            var graphicContext = canvas.getGraphicsContext2D();

            for (Figure figure : figures) {
                drawFigure(graphicContext, figure);
            }
        }
        else {
            double mouseX = event.getX();
            double mouseY = event.getY();

            int index = -1;
            int i = 0;

            for (Figure figure : figures){
                int centerX = (int)figure.getX();
                int centerY = (int)figure.getY();
                int size = figure.getSize();

                int top = centerY - size;
                int bot = centerY + size;
                int left = centerX - size;
                int right = centerX + size;

                if (left <= mouseX && mouseX <= right && top <= mouseY && mouseY <= bot){
                    if (index == -1){
                        index = i;
                    }

                    if (index != -1){
                        if (figures.get(index).getDepth() > figure.getDepth()){
                            index = i;
                        }
                    }
                }
                i++;
            }

            if (index != -1){
                figures.remove(index);
                var graphicContext = canvas.getGraphicsContext2D();

                drawFigure(graphicContext,new Figure(0,0,0,10000,100,Color.WHITE, 0));

                for (Figure figure : figures) {
                    drawFigure(graphicContext, figure);
                }
            }
        }
    }

    public void loadButtonPressed(){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterPNG =
                new FileChooser.ExtensionFilter("png files (.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilterPNG);

        FileChooser.ExtensionFilter extFilterJPEG =
                new FileChooser.ExtensionFilter("jpg files (.jpg, .jpeg)", "*.jpeg","*.jpg");
        fileChooser.getExtensionFilters().add(extFilterJPEG);

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("any files (.*)", "*");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage primaryStage = (Stage) loadButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(primaryStage);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        if(file != null) {
            resetButtonPressed();

            Image image = new Image("file:" + file.getPath());
            graphicsContext.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    public void loadGraphButtonPressed(){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterTXT =
                new FileChooser.ExtensionFilter("txt files (.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilterTXT);

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("any files (.*)", "*");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage primaryStage = (Stage) loadButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(primaryStage);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        if(file != null) {
            resetButtonPressed();

            Graph graph = new Graph();
            graph.load(file.getPath());
            graph.draw(graphicsContext);
        }
    }

    public void saveButtonPressed(){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("png files (.png)", ".png");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage primaryStage = (Stage) saveButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(primaryStage);

        if(file != null){
            try {
                WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetButtonPressed(){
        var graphicContext = canvas.getGraphicsContext2D();
        drawFigure(graphicContext,new Figure(0,0,0,10000,100,Color.WHITE, 0));
        figures.clear();
    }

    public void exitButtonPressed(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void checkBoxPressed(){
        deleteMode = !deleteMode;
    }
}
