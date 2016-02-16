// Bind this and target on execution and change it with proceed
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class A9 {
  M newM2 = new M("2");
  M newM3 = new M("3");

  @Around("execution(void M.method(String)) && args(p) && this(t) && target(t2)")
  public void a( ProceedingJoinPoint pjp, M t,M t2,String p) throws Throwable {
    System.err.println("advice from ataj aspect");
    pjp.proceed(new Object[]{newM2,newM3,"faked"});
  }

  public static void main(String []argv) {
    M.main(argv);
  }
}

class M {

 String prefix;

 public M(String prefix) { this.prefix = prefix; }

 public static void main( String[] args ) {
   M m = new M("1");
   m.methodCaller("real");
 }

 public void methodCaller(String param) {
   method(param);
 }

 public void method(String s) { System.err.println(prefix+s); }

}
