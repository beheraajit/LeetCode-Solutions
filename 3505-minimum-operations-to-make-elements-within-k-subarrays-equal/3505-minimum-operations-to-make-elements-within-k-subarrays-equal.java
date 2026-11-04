
import java.util.*;

class Solution {
    TreeMap<Integer, Integer> low = new TreeMap<>();
    TreeMap<Integer, Integer> high = new TreeMap<>();

    int lowSize = 0, highSize = 0;
    long lowSum = 0, highSum = 0;

    void add(TreeMap<Integer, Integer> map, int val) {
        map.put(val, map.getOrDefault(val, 0) + 1);
    }

    void remove(TreeMap<Integer, Integer> map, int val) {
        int count = map.get(val);

        if (count == 1) {
            map.remove(val);
        } else {
            map.put(val, count - 1);
        }
    }

    void insert(int val) {
        if (low.isEmpty() || val <= low.lastKey()) {
            add(low, val);
            lowSize++;
            lowSum += val;
        } else {
            add(high, val);
            highSize++;
            highSum += val;
        }

        balance();
    }

    void erase(int val) {
        if (low.containsKey(val)) {
            remove(low, val);
            lowSize--;
            lowSum -= val;
        } else {
            remove(high, val);
            highSize--;
            highSum -= val;
        }

        balance();
    }

    void balance() {
        while (lowSize > highSize + 1) {
            int val = low.lastKey();

            remove(low, val);
            lowSize--;
            lowSum -= val;

            add(high, val);
            highSize++;
            highSum += val;
        }

        while (lowSize < highSize) {
            int val = high.firstKey();

            remove(high, val);
            highSize--;
            highSum -= val;

            add(low, val);
            lowSize++;
            lowSum += val;
        }
    }

    long getCost() {
        long median = low.lastKey();

        return median * lowSize - lowSum
             + highSum - median * highSize;
    }

    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;

        long[] cost = new long[n - x + 1];

        for (int i = 0; i < x; i++) {
            insert(nums[i]);
        }

        cost[0] = getCost();

        for (int i = x; i < n; i++) {
            erase(nums[i - x]);
            insert(nums[i]);

            cost[i - x + 1] = getCost();
        }

        long INF = Long.MAX_VALUE / 4;

        long[][] dp = new long[k + 1][n + 1];

        for (long[] row : dp) {
            Arrays.fill(row, INF);
        }

        dp[0][0] = 0;

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                if (dp[j][i] == INF) {
                    continue;
                }

                if (i < n) {
                    dp[j][i + 1] = Math.min(
                        dp[j][i + 1],
                        dp[j][i]
                    );
                }

                if (j < k && i + x <= n) {
                    dp[j + 1][i + x] = Math.min(
                        dp[j + 1][i + x],
                        dp[j][i] + cost[i]
                    );
                }
            }
        }

        return dp[k][n];
    }
}
