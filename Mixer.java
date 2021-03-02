public enum Mixer{

    ADD{
        public Color mixUp(Color a, Color b){
            double r = a.get_r() + b.get_r();
            double bl = a.get_b() + b.get_b();
            double g = a.get_g() + b.get_g();
            if(r > 255.0) r = 255.0;
            if(bl > 255.0) bl = 255.0;
            if(g > 255.0) g = 255.0;
            return Color.name(r,g,bl);
        }
    },
    MUL{
        public Color mixUp(Color a, Color b){
            double r = a.get_r()* b.get_r();
            double bl = a.get_b()*b.get_b();
            double g = a.get_g()*b.get_g();
            if(r > 255.0) r = 255.0;
            if(bl > 255.0) bl = 255.0;
            if(g > 255.0) g = 255.0;
            return Color.name(r,g,bl);
        }
    },
    AVER{
        public Color mixUp(Color a, Color b){
            return Color.name((a.get_r()+b.get_r())/2, (a.get_g()+b.get_g())/2, (a.get_b()+b.get_b())/2);
        }
    };
    abstract Color mixUp(Color a, Color b);
}