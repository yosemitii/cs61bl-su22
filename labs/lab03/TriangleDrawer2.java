public class TriangleDrawer2{

    public static void main(String args[]){
        int SIZE = 10;
        int row = 0;
        int col =0;
        for (row = 0; row < SIZE; row++){
            for (col = 0; col <= row; col++){
                System.out.print('*');
            }
            System.out.println();
        }

    }
}

