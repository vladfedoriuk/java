import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

public class Var {

    public static void demonstrateSwitchStatement()
    {
        final int integer = 5;
        /*String numericString = switch (integer) {
            case 1 -> "one";
            case 2 -> "two";
        };
        System.out.println(numericString);
         */
    }

   public static void main(String[] args) throws IOException {
       var url = new URL("http://th.if.uj.edu.pl/~atg/Java/Enums.txt");
       var connection = url.openConnection();
       var reader = new BufferedReader(
               new InputStreamReader(connection.getInputStream()));
       String ln;
       while((ln = reader.readLine())!=null){
           System.out.println(ln);
       }
       Function<Integer, String> f = a->{
           var g = "0";
           return a.toString()+g;
       };
       System.out.println(f.apply(2));
   }
}
