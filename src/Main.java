import pkg.*;

public class Main 
{
    // Комментарий на русском языке.
    public static void main( String[] args )
    {
        Test test = new Test();

        System.out.println("Hello, world!\n");
        System.out.println("Invoke Test.foo(): " + test.foo(1, 2));
    }
}