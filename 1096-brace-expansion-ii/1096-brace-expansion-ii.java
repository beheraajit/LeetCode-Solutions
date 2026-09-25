class Solution {
    private TreeSet<String> set = new TreeSet<>();

    public List<String> braceExpansionII(String expression) {
        dfs(expression);
        return new ArrayList<>(set);
    }

    private void dfs(String exp) {
        int j = exp.indexOf('}');

        if (j == -1) {
            set.add(exp);
            return;
        }

        int i = exp.lastIndexOf('{', j);

        String prefix = exp.substring(0, i);
        String suffix = exp.substring(j + 1);

        String[] choices = exp.substring(i + 1, j).split(",");

        for (String choice : choices) {
            dfs(prefix + choice + suffix);
        }
    }
}