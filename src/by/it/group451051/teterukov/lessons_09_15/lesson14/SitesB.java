package by.it.group451051.teterukov.lessons_09_15.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class SitesB {

    public static void main(String[] args) throws Exception {
        Map<String, String>  parent = new HashMap<>();
        Map<String, Integer> rank   = new HashMap<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) break;

            String[] parts = line.split("\\+");
            String a = parts[0].trim();
            String b = parts[1].trim();

            if (!parent.containsKey(a)) { parent.put(a, a); rank.put(a, 0); }
            if (!parent.containsKey(b)) { parent.put(b, b); rank.put(b, 0); }

            String ra = find(a, parent);
            String rb = find(b, parent);
            if (!ra.equals(rb)) {
                int rankA = rank.get(ra), rankB = rank.get(rb);
                if      (rankA < rankB) parent.put(ra, rb);
                else if (rankA > rankB) parent.put(rb, ra);
                else { parent.put(rb, ra); rank.put(ra, rankA + 1); }
            }
        }

        Map<String, Integer> sizes = new HashMap<>();
        for (String site : parent.keySet()) {
            String root = find(site, parent);
            sizes.put(root, sizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> result = new ArrayList<>(sizes.values());
        result.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int s : result) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(s);
        }
        System.out.println(sb);
    }

    static String find(String x, Map<String, String> parent) {
        if (!parent.get(x).equals(x))
            parent.put(x, find(parent.get(x), parent));
        return parent.get(x);
    }
}