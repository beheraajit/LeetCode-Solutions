class Solution {
    public String removeOuterParentheses(String s) {
        int count = 0;
        StringBuilder ans = new StringBuilder();

        for(int i=0;i<s.length();i++) {
            if(s.charAt(i) == '(') {
                count++;
                if(count >=2){
                    ans.append('(');
                }
            }
            else {
                count--;
                if(count >= 1){
                    ans.append(')');
                }
                // count = Math.max(0,count);
            } 
        }
        return ans.toString();
    }
}