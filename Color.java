public enum Color{
    RED (255.0, 0.0, 0.0),
    YELLOW (255.0, 255.0, 0.0),
    BLACK(0.0, 0.0, 0.0),
    WHITE(255.0, 255.0, 255.0),
    GREY(128.0, 128.0, 128.0),
    GREEN(0.0, 255.0, 0.0),
    BLUE(0.0, 0.0, 255.0),
    CYAN(0.0, 255.0, 255.0),
    MAGENTA(255.0, 0.0, 255.0);


     double r;
     double g;
     double b;

    private Color(double r, double g, double b) throws NumberFormatException{
        if ((r < 0 || r>255) || (b < 0 || b>255) || (g < 0 || g>255)) throw new NumberFormatException();
        this.r = r;
        this.g = g;
        this.b = b;
    }

    boolean compare(double r, double g, double b){
        return r==this.r && b==this.b && g==this.g;
    }

    public static Color name(double r, double g, double b){
        Color col = null;
        for(Color a: values()){
            if(a.compare(r, g, b)){
                col = a;
                break;
            }
        }
        return col;
    }
    double get_r(){ return r;}
    double get_b(){ return b;}
    double get_g(){ return g;}
}
