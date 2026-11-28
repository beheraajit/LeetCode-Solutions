
import java.util.*;

class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;
        int maxFactor = n;

        int[] freq = new int[maxFactor + 1];

        int minPrice = Integer.MAX_VALUE;

        for (int[] item : items) {
            freq[item[0]]++;
            minPrice = Math.min(minPrice, item[1]);
        }

        int[] gain = new int[maxFactor + 1];

        for (int f = 1; f <= maxFactor; f++) {
            int count = 0;

            for (int j = f; j <= maxFactor; j += f) {
                count += freq[j];
            }

            gain[f] = count - 1;
        }

        Arrays.sort(items, (a, b) -> Integer.compare(a[1], b[1]));

        long remaining = budget;
        long answer = 0;

        for (int[] item : items) {
            int factor = item[0];
            int price = item[1];

            if (price >= 2L * minPrice) {
                break;
            }

            long buy = Math.min(gain[factor], remaining / price);

            answer += buy * 2;
            remaining -= buy * price;
        }

        answer += remaining / minPrice;

        return (int) answer;
    }
}
