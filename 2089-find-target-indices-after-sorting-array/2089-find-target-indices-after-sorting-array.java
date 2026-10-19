class Solution {
    public List<Integer> targetIndices(int[] nums, int target) {
        Arrays.sort(nums);
        ArrayList<Integer> arr = new ArrayList<>();
        int n = nums.length;
        
        for(int i=0;i<n;i++){
            if(nums[i] == target){
                arr.add(i);
            }
        }
        return arr;
    }
}