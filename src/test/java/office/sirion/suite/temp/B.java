package office.sirion.suite.temp;

class B extends A {
    B() {
        this(3, 7);
    }
    B(int a) {
        super();
    }
    B(boolean c) {
        super(7);
    }
    B(int a, int c) { // Calls super() implicitly
    }
    
    B b = new B(true);
}
