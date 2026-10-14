class Solution {
    public String maxSumOfSquares(int num, int sum) {
        StringBuilder ans = new StringBuilder();
        
        
        if(sum > 9*num){
            return "";
        }

        for(int i=0;i<num;i++){
            int n = Math.min(9,sum);
            ans.append(n);
            sum = sum - n;
        }

        if(sum > 0){
            return "";
        }

        return ans.toString();
    }
}