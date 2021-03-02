package sample;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

interface ComplexDrawable {
    void draw(PixelWriter pw, Complex a, Complex b, int w, int h);
}

public class MandelFractal implements ComplexDrawable {

    @Override
    public void draw(PixelWriter pw, Complex a, Complex b, int w, int h) {
        double precision = Math.max(Math.abs((a.re() - b.re())) / w, (Math.abs(a.im() - b.im())) / h);
        int convergence_steps = 100;
        for (double re = a.re(), x = 0; x < w; re = re + precision, x++) {
            for (double im = a.im(), y = 0; y < h; im = im + precision, y++) {
                Complex c = new Complex(re, im);
                int convergenceValue = check_convergence(c, convergence_steps);
                //System.out.println(convergenceValue);
                int t1 = convergenceValue / convergence_steps;

                if (convergenceValue < convergence_steps - 1) {
                    pw.setColor((int)x, (int)y, Color.rgb(5*convergenceValue%71, 73*convergenceValue%127, 43*convergenceValue%113));

                } else
                    pw.setArgb((int) x, (int) y, 0xFFFF00FF);
            }
        }
    }

    private int check_convergence(Complex c, int convergence_steps) {
        Complex z = new Complex();
        int i;
        for (i = 0; i < convergence_steps; i++) {
            //z_1 = z_0^2 + C;
            double r = (Complex.mul(z, z).re() + c.re());
            double im = (Complex.mul(z, z).im() + c.im());
            z.setRe(r);
            z.setIm(im);
            if (z.abs() >= Controller_mandelbrot.getParam())
                break;
            //it_used = i
        }
        return i;
    }

}
