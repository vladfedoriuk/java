public class Enum {

    public static void main(String[] args){
        Color res = null;
        for(Color e: Color.values()){
            for(Color d: Color.values()){
                res = Mixer.ADD.mixUp(e, d);
                System.out.println(e +" ADD "+ d +" = "+res);
                res = Mixer.MUL.mixUp(e, d);
                System.out.println(e +" MUL "+ d +" = "+res);
                res = Mixer.AVER.mixUp(e, d);
                System.out.println(e +" AVER "+ d +" = "+res);
            }
        }
    }
}
