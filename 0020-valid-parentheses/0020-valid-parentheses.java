class Solution {
    public boolean isValid(String s) {
        int count = 0;
        Stack<Character> w = new Stack<>();

        for(int i=0;i<s.length();i++) {
            char ch = s.charAt(i);
            
            if(ch == '(' || ch == '{' || ch == '[') {
                w.push(ch);
            }
            else {
            if(w.isEmpty()) {
                return false;
            }

            if((w.peek() == '(' && ch == ')') || (w.peek() == '{' && ch == '}') || (w.peek() == '[' && ch == ']')) {
                w.pop();
            }
            else {
                return false;
            }

        }
        }
        return w.isEmpty();

    }
}