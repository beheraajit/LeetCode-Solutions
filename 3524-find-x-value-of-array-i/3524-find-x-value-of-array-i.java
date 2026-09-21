class Solution {
    public long[] resultArray(int[] nums, int k) {
        long[] ans = new long[k];
        long[] dp = new long[k];

        for (int num : nums) {
            long[] next = new long[k];
            int mod = num % k;

            next[mod] = 1;

            for (int r = 0; r < k; r++) {
                int newMod = (int) ((long) r * mod % k);
                next[newMod] += dp[r];
            }

            for (int r = 0; r < k; r++) {
                ans[r] += next[r];
            }

            dp = next;
        }

        return ans;
    }
}