package sample;

public class Complex implements Field<Complex> {

    private double r, i;

    public Complex(){
        r = i = 0.0;
    }
    public Complex(double r){
        this.r = r;
    }
    public Complex(double r, double i){
        this.r = r;
        this.i = i;
    }
    public Complex(Complex a){
        this.r = a.re();
        this.i = a.im();
    }
    public Complex(String s){
        Complex t = valueOf(s);
        r = t.re();
        i = t.im();
    }
    @Override
    public Complex add(Complex a){
        this.r += a.re();
        this.i += a.im();
        return this;
    }
    @Override
    public Complex sub(Complex a){
        this.r -= a.re();
        this.i -= a.im();
        return this;
    }
    @Override
    public Complex mul(Complex a){
        this.r = a.re()* this.r - a.im()*this.i;
        this.i = this.r*a.im() + this.i*a.re();
        return this;
    }
    @Override
    public Complex div(Complex a) throws ArithmeticException{
        if(a.re() == 0.0 && a.im() == 0.0){
            throw new ArithmeticException("an attempt to divide by "+a.toString());
        } else{
            this.r = (this.r * a.re() + this.i * a.im())/(Math.pow(a.re(), 2) + Math.pow(a.im(), 2));
            this.i = (this.i * a.re() - this.r * a.im())/(Math.pow(a.re(), 2) + Math.pow(a.im(), 2));
        }
        return this;
    }

    public double abs(){
        return Math.sqrt(Math.pow(r, 2) + Math.pow(i, 2));
    }

    public double sqrAbs(){
        return Math.pow(r, 2) + Math.pow(i, 2);
    }

    public double phase() throws ArithmeticException{
        if(r == 0.0 && i == 0.0){
            throw new ArithmeticException("phase of 0 + 0i");
        } else{
            return Math.atan2(i, r);
        }
    }
    public double re(){
        return r;
    }
    public double im(){
        return i;
    }
    public static Complex add(Complex a, Complex b) throws NullPointerException{
        if( a == null || b == null) {
            throw new NullPointerException();
        } else
            return new Complex(a.re()+b.re(), a.im() + b.im());
    }
    public static Complex sub(Complex a, Complex b) throws NullPointerException{
        if( a == null || b == null) {
            throw new NullPointerException();
        } else
            return new Complex(a.re() - b.re(), a.im() - b.im());
    }
    public static Complex mul(Complex a, Complex b) throws NullPointerException{
        if( a == null || b == null) {
            throw new NullPointerException();
        } else
            return new Complex(a.re()* b.re() - a.im()*b.im(), a.re()*b.im() + a.im()*b.re());
    }
    static Complex div(Complex a, Complex b)throws ArithmeticException, NullPointerException{
        if( a == null || b == null) {
            throw new NullPointerException();
        } else {
            if (b.re() == 0.0 && b.im() == 0) {
                throw new ArithmeticException("Division by zero");
            } else {
                return new Complex((a.re() * b.re() + a.im() * b.im()) / (Math.pow(b.re(), 2) + Math.pow(b.im(), 2)),
                        (a.im() * b.re() - a.re() * b.im()) / (Math.pow(b.re(), 2) + Math.pow(b.im(), 2)));
            }
        }
    }

    public static double abs(Complex a){
        return Math.sqrt(Math.pow(a.re(), 2) + Math.pow(a.im(), 2));
    }

    public static double phase(Complex a) throws ArithmeticException, NullPointerException{
        if( a == null) {
            throw new NullPointerException();
        } else {
            if (a.re() == 0.0 && a.im() == 0.0) {
                throw new ArithmeticException("phase of 0 + 0i");
            } else {
                return Math.atan2(a.im(), a.re());
            }
        }
    }

    public static double sqrAbs(Complex a) throws NullPointerException{
        if( a == null) {
            throw new NullPointerException();
        } else
            return Math.pow(a.re(), 2) + Math.pow(a.im(), 2);
    }
    public static double re(Complex a) throws NullPointerException{
        if( a == null) {
            throw new NullPointerException();
        } else
            return a.re();

    }
    public static double im(Complex a) throws NullPointerException{
        if( a == null) {
            throw new NullPointerException();
        } else
            return a.im();
    }

    @Override
    public String toString() {
        if(i==0.0)
            return Double.toString(r);
        if(r==0.0)
            return Double.toString(i).concat("i");
            if(i>0)
                return Double.toString(r) + "+" + Double.toString(i)+"i";
            else
                return Double.toString(r) + Double.toString(i)+"i";
    }

    static Complex valueOf(String s) {
        double r = 0, i = 0;
        boolean r_positive = true;
        boolean i_positive = true;

        int minus_num=0;
        int pos = 0;
        for(int j = 0; j < s.length();j++)
            if (s.charAt(j) == '-') {
                minus_num++;
                pos = j;
            }
        if(minus_num == 2) {
            r_positive = false;
            i_positive = false;
        }

        if(minus_num == 1 && pos != 0 ){
            i_positive = false;
        }

        if(minus_num == 1 && pos == 0 && (s.contains("+") || !s.contains("i"))){
            r_positive = false;
        }

        if(minus_num == 1 && pos == 0 && s.contains("i") && !s.contains("+")){
            i_positive = false;
        }




        String[] spl;
        spl = s.split("[+-]");
        if(spl[0].equals("") && spl.length >2){
            spl[0] = spl[1];
            spl[1] = spl[2];
        } else if(spl[0].equals("")){
           if(!spl[1].contains("i")) {
               spl[0] = spl[1];
               spl[1] = "0";
           } else{
               spl[0] = "0";
           }
        }

        if(!r_positive){
            spl[0] = "-" + spl[0];
        }

        if (spl[0].contains("i")){
            spl[0] = spl[0].substring(0, spl[0].length() - 1);
        }

        if(spl.length > 1) {
            if (!i_positive) {
                spl[1] = "-" + spl[1];
            }
            if (spl[1].contains("i")) {
                spl[1] = spl[1].substring(0, spl[1].length() - 1);
            }
        }
        r = Double.parseDouble(spl[0]);
        i = Double.parseDouble(spl[1]);

        return new Complex(r, i);
    }


    public void setRe(double r) {
        this.r = r;
    }


     public void setIm(double i) {
        this.i = i;
    }

    public void setVal(Complex a) throws NullPointerException{
        if(a == null) {
            throw new NullPointerException();
        } else {
            this.r = a.re();
            this.i = a.im();
        }
    }

    public void setVal(double a, double b) {
        this.r = a;
        this.i = b;
    }
}