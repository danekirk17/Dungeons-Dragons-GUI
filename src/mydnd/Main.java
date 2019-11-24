package mydnd;

public final class Main {

    /**
     * private constructor to make checkstyle happy.
     */
    private Main() {

    }
    /**
     * main control method for program.
     *
     * @param args allows for String input to program from command line.
     */
    public static void main(String[] args) {
        Level l = new Level(5);
        String des = l.getDescription();
        System.out.println(des);
    }
}
