class Solution {
    public int minMoves(int[] nums) {
        int ans = 0;
        Arrays.sort(nums);
        int n = nums.length;
        for(int i=0;i<n-1;i++){
            ans += Math.abs(nums[i] - nums[n-1]);
        }
        return ans;
    }
}