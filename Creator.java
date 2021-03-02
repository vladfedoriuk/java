import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Creator {
    public static Object create(String[] args){
        Object obj = null;
        try {
            Class<?> cClass = Class.forName(args[0]);
            Constructor<?>[] cnstr = cClass.getConstructors();
            Class<?>[] parametrTypes;
            Object[] params = new Object[args.length -1];
            for(Constructor c: cnstr){
                parametrTypes = c.getParameterTypes();
                if(parametrTypes.length == args.length -1){
                    for(int i = 0; i<parametrTypes.length;i++){
                      Constructor<?> constr = parametrTypes[i].getConstructor(String.class);
                      params[i] = constr.newInstance(args[i+1]);
                    }
                    obj = c.newInstance(params);
                    break;
                }
            }

        }catch(ClassNotFoundException | NoSuchMethodException e){
            System.err.println(e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }
    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object obj = create(args);
        System.out.println(obj);
        Scanner scn = new Scanner(System.in);
        String method = scn.nextLine();
        Method m = obj.getClass().getMethod(method, null);
        System.out.println(m.invoke(obj, null));
    }
}

