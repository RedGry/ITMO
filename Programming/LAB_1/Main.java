import static java.lang.Math.*;
// First Lab //
class Main {
    public static void main(String[] args) {
        long[] t = new long[11];
        for (int i = 0, j = 2; i < t.length; ++i, j += 2){
            t[i] = j;
            System.out.println(t[i]);
        }

        double[] x = new double[12];
        for (int i = 0; i < x.length; ++i){
            x[i] = (double) ((random() * 18) - 15 );
            System.out.println(x[i]);
        }

        double[][] m = new double[11][12];
        for (int i = 0; i < m.length; ++i){
            for (int j = 0; j < m[i].length; ++j){
                switch ((int) t[i]) {
                    case 20:
                        m[i][j] = asin(pow(E, pow(-abs(t[i]) , 1.0/3.0)));
                        break;
                    case 2:
                    case 4:
                    case 8:
                    case 10:
                    case 18:
                        m [i][j] = sin(tan(pow(t[i] , 1.0/3.0)));
                        break;
                    default:
                        m[i][j] = pow(E, atan(cos(pow(E , t[i]/4.0))));
                }
                System.out.printf("%8.5f ", m[i][j]);
            }
            System.out.println();
        }
        System.out.println("Всё!");
    }
}
