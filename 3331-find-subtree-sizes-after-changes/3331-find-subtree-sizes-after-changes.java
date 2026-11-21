
import java.util.*;

class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        List<Integer>[] tree = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            tree[parent[i]].add(i);
        }

        int[] ans = new int[n];
        int[][] path = new int[26][n];
        int[] top = new int[26];

        int[] stackNode = new int[2 * n + 1];
        boolean[] stackState = new boolean[2 * n + 1];

        int size = 0;

        stackNode[size] = 0;
        stackState[size++] = false;

        while (size > 0) {
            int u = stackNode[--size];
            boolean exit = stackState[size];

            int c = s.charAt(u) - 'a';

            if (!exit) {
                ans[u] = 1;

                path[c][top[c]++] = u;

                stackNode[size] = u;
                stackState[size++] = true;

                for (int i = tree[u].size() - 1; i >= 0; i--) {
                    stackNode[size] = tree[u].get(i);
                    stackState[size++] = false;
                }

            } else {
                int target = parent[u];

                if (top[c] >= 2) {
                    target = path[c][top[c] - 2];
                }

                if (target != -1) {
                    ans[target] += ans[u];
                }

                top[c]--;
            }
        }

        return ans;
    }
}
