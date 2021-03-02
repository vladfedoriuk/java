import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Signature {
    public static void process(String arg){
        try {
            Class argClass;
            if(arg.equals(Signature.class.toString())){
                argClass = Signature.class;
            }else
                argClass = Class.forName(arg);
            System.out.println("Class: "+argClass);
            Constructor[] constr = argClass.getConstructors();
            System.out.println("Constructors: ");
            for(Constructor c: constr){
                System.out.println(" " +c);
            }
            Field[] fields = argClass.getFields();
            System.out.println("Fields: ");
            for(Field c: fields){
                System.out.println(" "+c);
            }
            Method[] methods = argClass.getMethods();
            System.out.println("Methods: ");

            for(Method m: methods){
                System.out.println(" "+m);
            }

        }catch(ClassNotFoundException e){
            System.err.println(e);
        }

    }
    public static void main(String[] args){
        for(String cl: args){
            process(cl);
        }
        process(Signature.class.toString());
    }
}
