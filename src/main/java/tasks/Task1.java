package tasks;

import java.util.HashMap;
import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        String str1 = "Task1";

        //   stringBuilderReverse(str1);

        //  stringReverse(str1);

        //   changeInt();
        String str2 = "Task task Task from Task from Home";
        countingNumber(str2);
    }

    // Напишите программу на Java для подсчета количества конкретных слов в строке,
    // используя HashMap.
    private static void countingNumber(String str2) {
        String[] str = str2.split(" ");
        HashMap<String, Integer> keyValue = new HashMap<>();
        for (int i = 0; i < str.length; i++) {
            if(keyValue.containsKey(str[i])){
                int count= keyValue.get(str[i]);
                keyValue.put(str[i], count+1);
            }else keyValue.put(str[i],1);
        }
        System.out.println(keyValue);
    }

    // Напишите программу на Java, чтобы поменять местами значения, хранящиеся в двух переменных,
    // без использования третьей переменной.
    private static void changeInt() {
        int a, b, temp;
        System.out.println("Vvedite znachenia a, b");
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        b = scanner.nextInt();
        System.out.println("Do obmena " + a + b);
        temp = a;
        a = b;
        b = temp;
        System.out.println("Posle obmena " + a + b);
    }


    // Напишите программу на Java для переворачивания строки, изменив расположение символов в строке задом
    // наперёд без использования встроенных в String функций.
    private static void stringBuilderReverse(String str1) {
        StringBuilder sb = new StringBuilder();
        sb.append(str1);
        StringBuilder reverse = sb.reverse();
        System.out.println(reverse);
    }

    // Напишите программу на Java для переворота последовательности символов
    // в строке без использования встроенной в String функции reverse().
    private static void stringReverse(String str1) {
        // Способ 1:
        char[] chars = str1.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            System.out.print(chars[i]);

        }
        System.out.println();
//        Способ 2:
        String[] split = str1.split("");
        for (int i = split.length - 1; i >= 0; i--) {
            System.out.print(split[i]);
        }
    }

}
