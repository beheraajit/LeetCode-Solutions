class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        long operations = (long) k1 + k2;
        int n = nums1.length;
        int[] diff = new int[n];
        int max = 0;

        for (int i = 0; i < n; i++) {
            diff[i] = Math.abs(nums1[i] - nums2[i]);
            max = Math.max(max, diff[i]);
        }

        if (operations >= Arrays.stream(diff).asLongStream().sum()) {
            return 0;
        }

        int low = 0, high = max;

        while (low < high) {
            int mid = low + (high - low) / 2;
            long needed = 0;

            for (int d : diff) {
                if (d > mid) {
                    needed += d - mid;
                }
            }

            if (needed <= operations) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        long remaining = operations;

        for (int i = 0; i < n; i++) {
            if (diff[i] > low) {
                remaining -= diff[i] - low;
                diff[i] = low;
            }
        }

        long result = 0;

        for (int i = 0; i < n; i++) {
            if (remaining > 0 && diff[i] == low && low > 0) {
                diff[i]--;
                remaining--;
            }

            result += (long) diff[i] * diff[i];
        }

        return result;
    }
}