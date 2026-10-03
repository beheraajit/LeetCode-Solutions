class Solution {
    public int longestValidParentheses(String s) {
        Stack<Integer> q = new Stack<>();
        int max = 0;
        q.push(-1);

        for(int i=0;i<s.length();i++) {
            if(s.charAt(i) == '(') {
                q.push(i);
            } else {
                q.pop();

                if(q.isEmpty()) {
                    q.push(i);
                } else {
                    max = Math.max(max,i-q.peek());
                }
            }
        }
        return max;
    }
}