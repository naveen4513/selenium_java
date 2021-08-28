package office.sirion.suite.temp;

import java.util.Arrays;

public class repeat_number {

	public static void main(String[] args) {
		 int[] numbers = {1,2,2,3,5,5,8,6,7};

         Arrays.sort(numbers);
         //System.out.println("Sorted : "+numbers.length);

         for(int i = 1; i < numbers.length; i++) {
        	 System.out.println(numbers[i]);
        	 System.out.println(numbers[i-1]);
             if(numbers[i] == numbers[i - 1]) {
                 System.out.println("Duplicate: " + numbers[i]);
             }
         }

	}

}
