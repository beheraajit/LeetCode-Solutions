class Solution {
    public long maxAlternatingSum(int[] nums) {
    
        int n = nums.length;
        long sq[] = new long[n];
        for(int i=0;i<n;i++){
            sq[i] = (long) nums[i] * nums[i];
        }
        Arrays.sort(sq);
        long ans = 0;
        int m = n /2;
        

        for(int i=0;i<m;i++){
            ans = ans - sq[i];
        }

        for(int i=m;i<n;i++){
            ans = ans + sq[i];
        }

    return ans;

    }
}