class Solution {
    public int minAddToMakeValid(String s) {
        Stack<Character> ch = new Stack<>();

        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == '(') {
                ch.push('(');       
            } else {
                if(!ch.isEmpty() && ch.peek() == '(') {
                    ch.pop();
                } else {
                    ch.push(')');
                }
            }
            


            
        }
        return ch.size();
        
    }
}