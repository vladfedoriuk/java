package sample;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;

public class Controller {
    public Label label;
    public Canvas canvas;
    private GraphicsContext gc;
    private double x1, y1, x2, y2;

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        clear(gc);
    }

    private void clear(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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

    public void sayHello(ActionEvent actionEvent) {
        label.setText("Hello");
    }

    public void mouseMoves(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        //gc.setGlobalBlendMode(BlendMode.DIFFERENCE);
        gc.setStroke(Color.WHITE);
        rect(gc);
        x2 = x;
        y2 = y;
        rect(gc);
    }

    public void drawRect(ActionEvent actionEvent) {
        gc.setStroke(Color.web("#FFF0F0"));
       // gc.setGlobalBlendMode(BlendMode.MULTIPLY);
        gc.strokeRect(100.5, 100.5, 200, 200);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        x2 = x1;
        y2 = y1;
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        rect(gc);
        System.out.format("%f %f %f %f\n", x1, y1, x2, y2);
    }

    public void clearCanvas(ActionEvent actionEvent) {
        clear(gc);
    }

    public void draw(ActionEvent actionEvent) {
        final int size = 512;
        WritableImage wr = new WritableImage(size, size);
        PixelWriter pw = wr.getPixelWriter();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                pw.setArgb(x, y, (x & y) == 0 ? 0xFFFF00FF : 0xFFFFFFFF);	// draws a triangle
            }
        }

        //gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.drawImage(wr, 0, 0, 512, 512);
    }
}
