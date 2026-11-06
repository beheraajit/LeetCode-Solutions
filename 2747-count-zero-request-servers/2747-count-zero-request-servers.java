
import java.util.*;

class Solution {
    public int[] countServers(int n, int[][] logs, int x, int[] queries) {
        int m = queries.length;
        int[] ans = new int[m];
        int[] count = new int[n + 1];

        Integer[] idx = new Integer[m];

        for (int i = 0; i < m; i++) {
            idx[i] = i;
        }

        Arrays.sort(logs, (a, b) -> Integer.compare(a[1], b[1]));

        Arrays.sort(idx, (a, b) -> Integer.compare(queries[a], queries[b]));

        int left = 0;
        int right = 0;
        int active = 0;

        for (int i : idx) {
            int q = queries[i];
            int start = q - x;

            while (right < logs.length && logs[right][1] <= q) {
                int server = logs[right][0];

                count[server]++;

                if (count[server] == 1) {
                    active++;
                }

                right++;
            }

            while (left < right && logs[left][1] < start) {
                int server = logs[left][0];

                count[server]--;

                if (count[server] == 0) {
                    active--;
                }

                left++;
            }

            ans[i] = n - active;
        }

        return ans;
    }
}
