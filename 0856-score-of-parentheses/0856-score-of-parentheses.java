class Solution {
    public int scoreOfParentheses(String s) {
        Stack<Integer> st = new Stack<>();
        st.push(0);

        for(int i=0;i<s.length();i++) {
            if(s.charAt(i) == '(') {
                st.push(0);
            } else {
                int x = st.pop();
                int y;
                if(x == 0) {
                    y = 1;
                } else y = x * 2;
                st.push(y + st.pop());
            } 
        }
        return st.peek();

    }
}