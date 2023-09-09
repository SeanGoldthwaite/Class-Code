import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        int[] arr = new int[17];
        arr[0] = 0;
        arr[1] = 1;
        arr[2] = 1;
        arr[3] = 2;
        arr[4] = 2;

        for (int i = 4; i < arr.length; i++) {
            arr[i] = arr[i - 2] + arr[i - 3] + arr[i - 4];
        }
        for (Integer e : arr)
            System.out.println(e);
    }
}
