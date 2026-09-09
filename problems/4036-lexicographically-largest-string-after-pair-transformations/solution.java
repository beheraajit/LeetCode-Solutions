class Solution {
    public String get(int x) {
        int[] count = new int[26];
        count[0] = x;
        for(int i = 0;i<25;i++) {
            count[i+1] += count[i] / 2;
            count[i] %= 2;
        }
        StringBuilder ans = new StringBuilder();

        for(int i = 25; i>=0;i--) {
            for(int j=0;j<count[i];j++) {
                ans.append((char) ('a' + i));
            }
        }
        return ans.toString();
    }
    public String[] largestString(int[] nums) {
        String[] ans = new String[nums.length];

        for(int i = 0;i<nums.length;i++) {
            ans[i] = get(nums[i]);
        }
        return ans;
    }
}
