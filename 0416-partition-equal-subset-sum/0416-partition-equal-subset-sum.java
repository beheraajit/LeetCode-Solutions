class Solution {
    public boolean canPartition(int[] nums) {
        int sum = 0;
        int n = nums.length;
        for(int i=0;i<n;i++){
            sum+= nums[i];
        }

        if(sum % 2 != 0) {
            return false;
        }

        int tar = sum / 2;
        boolean dp[] = new boolean[tar + 1];
        dp[0] = true;

        for(int i=0;i<n;i++){
            for(int j=tar;j>=nums[i];j--){
                dp[j] = dp[j] || dp[j - nums[i]];
            }
        }
        return dp[tar];
    }
}