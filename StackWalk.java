import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StackWalk {

    public static void main(String[] args) {
        a();
    };

    public static void a(){
        b();
    }
    public static void b(){
        c();
    }
    public static void c(){
        WalkerUse.whoIsIt();
        WalkerUse.printStack();
        WalkerUse.framesInfo();
        WalkerUse.getCaller();

    }

};

class WalkerUse{
    public static void whoIsIt(){
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        System.out.println(walker.getCallerClass());
    }
    public static void printStack(){
        System.out.println("Stack: ");
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        walker.forEach(System.out::println);
        System.out.println("Stack1: ");
        List<StackWalker.StackFrame> frames;
        frames = walker.walk(f->f.collect(Collectors.toList()));
        frames.forEach(System.out::println);
    }
    public static void framesInfo(){
        System.out.println("Frames info: ");
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        walker.forEach(f->
        {
            System.out.printf("Bytecode index: %d%n",
                    f.getByteCodeIndex());
            System.out.printf("Class name: %s%n",
                    f.getClassName());
            System.out.printf("Declaring class: %s%n",
                    f.getDeclaringClass());
            System.out.printf("File name: %s%n",
                    f.getFileName());
            System.out.printf("Is native: %b%n",
                    f.isNativeMethod());
            System.out.printf("Line number: %d%n",
                    f.getLineNumber());
            System.out.printf("Method name: %s%n%n",
                    f.getFileName());
        });
     }

     public static void getCaller(){
        System.out.println("Caller: ");
         StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
         Optional<?> caller = walker.walk(frames ->
                 frames.skip(1).findFirst().map(StackWalker.StackFrame::getClassName)
         );
         caller.ifPresent(System.out::println);
   }
}