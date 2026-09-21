package by.it.group551051.makhmudov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        int count = scanner.nextInt();
        scanner.nextInt();
        Map<String, Character> decodeMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String line = scanner.next();
            char letter = line.charAt(0);
            String code = scanner.next();
            decodeMap.put(code, letter);
        }
        String encoded = scanner.next();
        StringBuilder bits = new StringBuilder();
        for (int i = 0; i < encoded.length(); i++) {
            bits.append(encoded.charAt(i));
            Character ch = decodeMap.get(bits.toString());
            if (ch != null) {
                result.append(ch);
                bits.setLength(0);
            }
        }
        return result.toString();
    }

}
