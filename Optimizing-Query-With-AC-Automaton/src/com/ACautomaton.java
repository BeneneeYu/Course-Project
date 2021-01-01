package com;
/* @Author:Zhengyu Shen
 * @Date: 17/12/2019
 * This is a programe reading different kinds of commands to modify information related to postings on a tribune.
 * Use AC automaton to improve efficiency.
 * Collect all the phrases in commands concerning 'contains',construct AC automaton.
 * For a certain phrase,we know what key it is related to.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
public class ACautomaton {
    public static void main(String[] args) throws IOException{
        String s;
        TreeMap<String, Long> map = new TreeMap<String, Long>();
        Reader.init(System.in);
        int time = Integer.parseInt(Reader.next());
        int count = 0;
        String[] commands = new String[time];
        HashSet<String> containsSet = new HashSet<>();
        HashSet<String> putSet = new HashSet<>();
        while (count < time) {
            s = Reader.next();
            commands[count] = s;
            String[] strArr = s.split(" ");
            switch (strArr[0]) {
                case "PUT":
                    putSet.add(strArr[1]);
                    break;
                case "ADD":
                    break;
                case "QUERY":
                    break;
                case "DEL":
                    break;
                case "ADDBEGINWITH":
                    break;
                case "QUERYBEGINWITH":
                    break;
                case "DELBEGINWITH":
                    break;
                case "ADDCONTAIN":
                    containsSet.add(strArr[1]);
                    break;
                case "QUERYCONTAIN":
                    containsSet.add(strArr[1]);
                    break;
                case "DELCONTAIN":
                    containsSet.add(strArr[1]);
                    break;
                default:
                    break;
            }
            count++;
        }
        Map<String, ArrayList<String>> newmap = containsWhichs(putSet,containsSet);
        Map<String, HashSet<String>> newmap1 =         reverse(newmap);
        String command;
        String[] strArr;
        for (int i = 0; i < commands.length; i++) {
            command = commands[i];
            strArr = command.split(" ");
            switch (strArr[0]) {
                case "PUT":
                    map.put(strArr[1], Long.parseLong(strArr[2]));
                    break;
                case "ADD":
                    if (map.containsKey(strArr[1])) {
                        long newInt = map.get(strArr[1]) + Long.parseLong(strArr[2]);
                        map.put(strArr[1], newInt);
                    }
                    break;
                case "QUERY":
                    query(map,strArr[1]);
                    break;
                case "DEL":
                    delete(map,strArr[1]);
                    break;
                case "ADDBEGINWITH":
                    addBeginWith(map,strArr[1], Long.parseLong(strArr[2]));
                    break;
                case "QUERYBEGINWITH":
                    queryBeginWith(map,strArr[1]);
                    break;
                case "DELBEGINWITH":
                    deleteBeginWith(map,strArr[1]);
                case "ADDCONTAIN":
                    HashSet<String> influenced = newmap1.get(strArr[1]);
                    ArrayList<String> arrayList = null == influenced?new ArrayList<>():new ArrayList<>(influenced);
                    for (int i1 = 0; i1 < arrayList.size(); i1++) {
                        if (map.containsKey(arrayList.get(i1))) {
                            if (strArr.length >= 3) {
                                long newInt = map.get(arrayList.get(i1)) + Long.parseLong(strArr[2]);
                                map.put(arrayList.get(i1), newInt);
                            }
                        }
                    }
                    break;
                case "QUERYCONTAIN":
                    HashSet<String> influenced1 = newmap1.get(strArr[1]);
                    ArrayList<String> arrayList1 = null == influenced1?new ArrayList<>():new ArrayList<>(influenced1);
                    long sum = 0;
                    for (int i1 = 0; i1 < arrayList1.size(); i1++) {
                        if (map.containsKey(arrayList1.get(i1))) {
                            sum += map.get(arrayList1.get(i1));
                        }
                    }
                    System.out.println(sum);
                    break;
                case "DELCONTAIN":
                    HashSet<String> influenced2 = newmap1.get(strArr[1]);
                    ArrayList<String> arrayList2 = null == influenced2?new ArrayList<>():new ArrayList<>(influenced2);
                    for (int i1 = 0; i1 < arrayList2.size(); i1++) {
                        if (map.containsKey(arrayList2.get(i1))) {
                            map.put(arrayList2.get(i1), (long)0);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
    public static void delete(Map<String, Long> map, String key){
        if (map.containsKey(key)) {
            map.put(key, (long) 0);
        }
    }
    public static void query(Map<String, Long> map, String key){
        if (map.containsKey(key)) {
            System.out.println(map.get(key));
        }
    }
    public static void deleteBeginWith(TreeMap<String,Long>map ,String keyLike){
        Map<String, Long> newMap = map.subMap(keyLike,true,keyLike.substring(0,keyLike.length()-1) + (char)(keyLike.charAt(keyLike.length()-1) + 1),false);
        for (String key : newMap.keySet()) {
            map.put(key, (long) 0);
        }
    }
    public static void queryBeginWith(TreeMap<String,Long>map ,String keyLike){
        long whole = 0;
        Map<String, Long> newMap = map.subMap(keyLike,true,keyLike.substring(0,keyLike.length()-1) + (char)(keyLike.charAt(keyLike.length()-1) + 1),false);
        for (String key : newMap.keySet()) {
            whole += map.get(key);
        }
        System.out.println(whole);
    }
    public static void addBeginWith(TreeMap<String,Long>map ,String keyLike,long value){
        Map<String, Long> newMap = map.subMap(keyLike,true,keyLike.substring(0,keyLike.length()-1) + (char)(keyLike.charAt(keyLike.length()-1) + 1),false);
        for (String key : newMap.keySet()) {
            map.put(key,map.get(key)+value);
        }
    }
    private static Map<String, HashSet<String>> reverse(Map<String, ArrayList<String>> map) {
        Map<String, HashSet<String>> fruit = new HashMap<String, HashSet<String>>();
        for (Map.Entry<String, ArrayList<String>> en : map.entrySet()) {
            for (int i = 0; i < en.getValue().size(); i++) {
                if (fruit.containsKey(en.getValue().get(i))){
                    fruit.get(en.getValue().get(i)).add(en.getKey());
                }
                else{
                    HashSet<String> newSet = new HashSet<String>();
                    newSet.add(en.getKey());
                    fruit.put(en.getValue().get(i),newSet);
                }
            }
        }
        return fruit;
    }
    private static Map<String, ArrayList<String>> containsWhichs(HashSet<String> text,HashSet<String> containPatterns){
        ArrayList<String> list = new ArrayList<String>(containPatterns);
        MyOneACSearchTest.buildTree(list);
        MyOneACSearchTest.addFailure();
        //contain patterns的AC自动机已经建好，开始遍历
        ArrayList<String> textList = new ArrayList<String>(text);
        Map<String, ArrayList<String>> fruit = new HashMap<>();
        for (int i = 0; i < textList.size(); i++) {
            MyOneACSearchTest.StringSearchResult[] findAll = MyOneACSearchTest.findAll(textList.get(i));
            HashSet<String> fruits = new HashSet<String>();
            for (MyOneACSearchTest.StringSearchResult result : findAll) {
                fruits.add(result.keyword());
            }
            fruit.put(textList.get(i),new ArrayList<>(fruits));
        }
        return fruit;
    }
    public static class MyOneACSearchTest {
        MyOneACSearchTest(ArrayList<String> keywords) {
            buildTree(keywords);
            addFailure();
        }
        private static TreeNode root;
        //查找全部的模式串
        public static StringSearchResult[] findAll(String text) {
            //可以找到 转移到下个节点 不能找到在失败指针节点中查找直到为root节点
            ArrayList<StringSearchResult> results = new ArrayList<StringSearchResult>();
            int index = 0;
            TreeNode mid = root;
            while (index < text.length()) {

                TreeNode temp = null;

                while (temp == null) {
                    temp = mid.getSonNode(text.charAt(index));
                    if (mid == root) {
                        break;
                    }
                    if (temp == null) {
                        mid = mid.failure;
                    }
                }
                //mid为root 再次进入循环 不需要处理  或者 temp不为空找到节点 节点位移
                if (temp != null) mid = temp;

                for (String result : mid.getResults()) {
                    results.add(new StringSearchResult(index - result.length() + 1, result));
                }
                index++;
            }
            return results.toArray(new StringSearchResult[results.size()]);
        }
        private static void addFailure() {
            //设置二层失败指针为根节点 收集三层节点
            //DFA遍历所有节点 设置失败节点 原则: 节点的失败指针在父节点的失败指针的子节点中查找 最大后缀匹配
            ArrayList<TreeNode> mid = new ArrayList<TreeNode>();//过程容器
            for (TreeNode node : root.getSonsNode()) {
                node.failure = root;
                for (TreeNode treeNode : node.getSonsNode()) {
                    mid.add(treeNode);
                }
            }
            //广度遍历所有节点设置失败指针 1.存在失败指针 2.不存在到root结束
            while (mid.size() > 0) {
                ArrayList<TreeNode> temp = new ArrayList<TreeNode>();//子节点收集器
                for (TreeNode node : mid) {
                    TreeNode r = node.getParent().failure;
                    while (r != null && !r.containNode(node.getChar())) {
                        r = r.failure;//没有找到,保证最大后缀 (最后一个节点字符相同)
                    }
                    //是根结
                    if (r == null) {
                        node.failure = root;
                    } else {
                        node.failure = r.getSonNode(node.getChar());
                        //重叠后缀的包含
                        for (String result : node.failure.getResults()) {
                            node.addResult(result);
                        }
                    }
                    //收集子节点
                    for (TreeNode treeNode : node.getSonsNode()) {
                        temp.add(treeNode);
                    }
                }
                mid = temp;
            }
            root.failure = root;
        }
        private static void buildTree(ArrayList<String> keywords) {
            root = new TreeNode(null, ' ');
            //判断节点是否存在 存在转移 不存在添加
            for (String word : keywords) {
                TreeNode temp = root;
                for (char ch : word.toCharArray()) {
                    if (temp.containNode(ch)) {
                        temp = temp.getSonNode(ch);
                    } else {
                        TreeNode newNode = new TreeNode(temp, ch);
                        temp.addSonNode(newNode);
                        temp = newNode;
                    }
                }
                temp.addResult(word);
            }
        }
        static class StringSearchResult {
            String keyword;
            int index;
            StringSearchResult(int index, String str) {
                this.index = index;
                this.keyword = str;
            }
            String keyword() {
                return keyword;
            }
            int index() {
                return index;
            }
        }
        static class TreeNode {
            private TreeNode parent;
            private TreeNode failure;
            private char ch;
            private ArrayList<String> results;
            private Hashtable<Character, TreeNode> sonsHash;
            private TreeNode[] sonsNode;
            public TreeNode(TreeNode parent, char ch) {
                this.parent = parent;
                this.ch = ch;
                results = new ArrayList<String>();
                sonsHash = new Hashtable<Character, TreeNode>();
                sonsNode = new TreeNode[]{};
            }
            //添加子节点
            public void addSonNode(TreeNode node) {
                sonsHash.put(node.ch, node);
                sonsNode = new TreeNode[sonsHash.size()];
                Iterator<TreeNode> iterator = sonsHash.values().iterator();
                for (int i = 0; i < sonsNode.length; i++) {
                    if (iterator.hasNext()) {
                        sonsNode[i] = iterator.next();
                    }
                }
            }
            //获取子节点中指定字符节点
            public TreeNode getSonNode(char ch) {
                return sonsHash.get(ch);
            }
            //判断子节点中是否存在该字符
            public boolean containNode(char ch) {
                return getSonNode(ch) != null;
            }
            //添加一个结果到结果字符中
            public void addResult(String result) {
                if (!results.contains(result)) results.add(result);
            }
            //获取字符
            public char getChar() {
                return ch;
            }
            //获取父节点
            public TreeNode getParent() {
                return parent;
            }
            //设置失败指针并且返回
            public TreeNode setFailure(TreeNode failure) {
                this.failure = failure;
                return this.failure;
            }
            //获取所有的孩子节点
            public TreeNode[] getSonsNode() {
                return sonsNode;
            }
            //获取搜索的字符串
            public ArrayList<String> getResults() {
                return results;
            }
        }
    }
    static class Reader {
        static BufferedReader reader;
        static StringTokenizer tokenizer;
        static void init(InputStream input) {
            reader = new BufferedReader(
                    new InputStreamReader(input));
            tokenizer = new StringTokenizer("");
        }

        /**
         * 获取下一段文本
         */
        static String next() throws IOException {
            while (!tokenizer.hasMoreTokens()) {
                //TODO add check for eof if necessary
                tokenizer = new StringTokenizer(
                        reader.readLine(), "\n");
            }
            return tokenizer.nextToken();
        }
    }
}

