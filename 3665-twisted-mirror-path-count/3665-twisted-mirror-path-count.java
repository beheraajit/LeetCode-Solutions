class Solution {
    public int uniquePaths(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int mod = 1_000_000_007;

        if (m == 1 && n == 1) {
            return 1;
        }

        long[][][] dp = new long[m][n][2];

        if (n > 1) {
            dp[0][1][0] = 1;
        }

        if (m > 1) {
            dp[1][0][1] = 1;
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (i == 0 && j == 0) {
                    continue;
                }

                if (i == m - 1 && j == n - 1) {
                    continue;
                }

                long right = dp[i][j][0];
                long down = dp[i][j][1];

                if (grid[i][j] == 0) {

                    if (j + 1 < n) {
                        dp[i][j + 1][0] =
                            (dp[i][j + 1][0] + right + down) % mod;
                    }

                    if (i + 1 < m) {
                        dp[i + 1][j][1] =
                            (dp[i + 1][j][1] + right + down) % mod;
                    }

                } else {

                    if (i + 1 < m) {
                        dp[i + 1][j][1] =
                            (dp[i + 1][j][1] + right) % mod;
                    }

                    if (j + 1 < n) {
                        dp[i][j + 1][0] =
                            (dp[i][j + 1][0] + down) % mod;
                    }
                }
            }
        }

        return (int) ((dp[m - 1][n - 1][0] +
                       dp[m - 1][n - 1][1]) % mod);
    }
}