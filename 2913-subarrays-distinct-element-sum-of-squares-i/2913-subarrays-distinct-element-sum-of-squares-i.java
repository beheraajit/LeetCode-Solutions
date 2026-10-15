class Solution {
    public int sumCounts(List<Integer> nums) {
        int n = nums.size();
        int ans =0;

        for(int i=0;i<n;i++){
            HashSet<Integer> s = new HashSet<>();

            for(int j=i;j<n;j++){
                s.add(nums.get(j));
                int count = s.size();
                ans = ans + (count * count);
            }
        }
        return ans;
    }
}