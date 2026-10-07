class Solution {
    Set<String> set = new HashSet<>();

    public List<String> removeInvalidParentheses(String s) {
        int leftRemove = 0;
        int rightRemove = 0;

        for (char ch : s.toCharArray()) {
            if (ch == '(') {
                leftRemove++;
            } else if (ch == ')') {
                if (leftRemove > 0) {
                    leftRemove--;
                } else {
                    rightRemove++;
                }
            }
        }

        backtrack(s, 0, leftRemove, rightRemove, 0, 0, new StringBuilder());

        return new ArrayList<>(set);
    }

    private void backtrack(String s, int index, int leftRemove, int rightRemove,
                            int open, int close, StringBuilder curr) {

        if (index == s.length()) {
            if (leftRemove == 0 && rightRemove == 0 && open == close) {
                set.add(curr.toString());
            }
            return;
        }

        char ch = s.charAt(index);

        if (ch == '(' && leftRemove > 0) {
            backtrack(s, index + 1, leftRemove - 1, rightRemove,
                      open, close, curr);
        }

        if (ch == ')' && rightRemove > 0) {
            backtrack(s, index + 1, leftRemove, rightRemove - 1,
                      open, close, curr);
        }

        curr.append(ch);

        if (ch != ')' || open > close) {
            if (ch == '(') {
                backtrack(s, index + 1, leftRemove, rightRemove,
                          open + 1, close, curr);
            } else if (ch == ')') {
                backtrack(s, index + 1, leftRemove, rightRemove,
                          open, close + 1, curr);
            } else {
                backtrack(s, index + 1, leftRemove, rightRemove,
                          open, close, curr);
            }
        }

        curr.deleteCharAt(curr.length() - 1);
    }
}