package sample;

import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;

public class Controller_mandelbrot {

    public Canvas canvas;
    public GraphicsContext gc;
    public TextField textfield;
    private double height, width;
    private double x1, y1, x2, y2;
    static private Integer param = 4;
    private double x_def1 = -2.5, x_def2 = 2.5, y_def1 = -2.5, y_def2 = 2.5;
    private final double size = 512;
    WritableImage wr = new WritableImage((int)size, (int)size);
    PixelWriter pw = wr.getPixelWriter();
    static void setParam(Integer s){
        param = s;
    }

    static Integer getParam(){
        return param;
    }

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        clear(gc);
    }

    private void clear(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        x_def1 = -2.5;
        y_def1 = -2.5;
        x_def2 = 2.5;
        y_def2 = 2.5;
    }

    private void rect(GraphicsContext gc) {						// this method draws a rectangle with corners (x1, y1) i (x2, y2)
        double x = x1;
        double y = y1;
        double w = x2 - x1;
        double h = y2 - y1;

        if (w < 0) {
            x = x2;
            w = -w;
        }

        if (h < 0) {
            y = y2;
            h = -h;
        }

        gc.strokeRect(x + 0.5, y + 0.5, w, h);
    }


    public void mouseMoves(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        gc.setGlobalBlendMode(BlendMode.DIFFERENCE);
        gc.setStroke(Color.WHITE);
        rect(gc);
        x2 = x;
        y2 = y;
        rect(gc);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        x2 = x1;
        y2 = y1;
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        rect(gc);
            WritableImage wr1 = null;
            try {
                width = Math.max(Math.abs(x2), Math.abs(x1)) - Math.min(Math.abs(x1), Math.abs(x2));
                height = Math.max(Math.abs(y2), Math.abs(y1)) - Math.min(Math.abs(y2), Math.abs(y1));
                //wr1 = new WritableImage(wr.getPixelReader(),(int) x1, (int) y1, (int) width , (int) height);
                System.out.format("%f %f %f %f\n", x1, y1, x2, y2);
                double temp_x1, temp_x2, temp_y1, temp_y2;
                temp_x1 = (Math.min(x1,x2)/size)*(x_def2 - x_def1) + x_def1;
                temp_x2 = (Math.max(x1,x2)/size)*(x_def2 - x_def1) + x_def1;

                temp_y1 = (Math.min(y1,y2)/size)*(y_def2 - y_def1) + y_def1;
                temp_y2 = (Math.max(y1,y2)/size)*(y_def2 - y_def1) + y_def1;

                x_def1 = Math.min(temp_x1, temp_x2);
                y_def1 = Math.min(temp_y1, temp_y2);
                x_def2 = Math.max(temp_x1, temp_x2);
                y_def2 = Math.max(temp_y1, temp_y2);
                Complex a = new Complex(x_def1, y_def1);
                Complex b = new Complex(x_def2, y_def2);
                new MandelFractal().draw(pw, a, b, (int)size, (int)size);
                gc.setGlobalBlendMode(BlendMode.SRC_OVER);
                gc.drawImage(wr, 0, 0, 512, 512);
               // gc.drawImage(wr1, 0, 0, 512, 512);

            }catch(ArrayIndexOutOfBoundsException e){
                System.err.println("No more zooming");
            }


    }

    public void draw(ActionEvent actionEvent) {

        Complex a = new Complex(x_def1, y_def1);
        Complex b = new Complex(x_def2, y_def2);
        new MandelFractal().draw(pw, a, b,(int) size,(int) size);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.drawImage(wr, 0, 0, size, size);

    }

    public void clearCanvas(ActionEvent actionEvent) {
        clear(gc);
    }

    public void readParam(ActionEvent actionEvent) {
       param = Integer.parseInt(textfield.getText());
    }
}
