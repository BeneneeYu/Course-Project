import java.io.*;
import java.util.*;

public class HuffmanCode {

    public static void main(String[] args) {
        //测试压缩文件
        String srcFile = "D:\\Programming\\JAVA_programming\\Sophomore Algorithm and Data Structure\\src\\projects\\project1\\Test Cases\\test1 - single file\\27.mp3";
        String dstFile = "D:\\Programming\\JAVA_programming\\Sophomore Algorithm and Data Structure\\src\\projects\\project1\\Test Cases\\test1 - single file\\27.zip";
        zipFile(srcFile, dstFile);
        //测试解压文件
        String zipFile = "D:\\Programming\\JAVA_programming\\Sophomore Algorithm and Data Structure\\src\\projects\\project1\\Test Cases\\test1 - single file\\27.zip";
        String dstFile2 = "D:\\Programming\\JAVA_programming\\Sophomore Algorithm and Data Structure\\src\\projects\\project1\\Test Cases\\test1 - single file\\2727.mp3";
        unZipFile(zipFile, dstFile2);

    }

    public static void unZipFile(String sourceFile, String destinationFile) {
        InputStream is = null;
        ObjectInputStream ois = null;
        OutputStream os = null;
        File file = new File(sourceFile);
        //空文件的压缩方法
        if (!file.exists() || file.length() == 0) {
            File file1 = new File(destinationFile);
            try {
                boolean flag1 = file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                is = new FileInputStream(sourceFile);
                //创建一个和  is关联的对象输入流
                ois = new ObjectInputStream(is);
                //读取byte数组  huffmanBytes
                byte[] huffmanBytes = (byte[]) ois.readObject();
                //读取赫夫曼编码表
                Map<Byte, String> huffmanCodes = (Map<Byte, String>) ois.readObject();

                //解码
                byte[] bytes = decode(huffmanCodes, huffmanBytes);
                //将bytes 数组写入到目标文件
                os = new FileOutputStream(destinationFile);
                //写数据到 dstFile 文件
                os.write(bytes);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }

                    if (os != null) {
                        os.close();
                    }
                } catch (Exception e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    //文件的压缩方法
    //遍历每个文件
    public static void zipFile(String srcFile, String dstFile) {

        long  startTime = System.currentTimeMillis();
        OutputStream os = null;
        ObjectOutputStream oos = null;
        FileInputStream fis = null;
        File file = new File(srcFile);
        //空文件的压缩方法
        if (!file.exists() || file.length() == 0) {
            File file1 = new File(dstFile);
            try {
                boolean flag1 = file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                fis = new FileInputStream(srcFile);
                //将整个文件作为字节数组读入
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                //得到Huffman编码
                byte[] huffmanBytes = huffmanZip(bytes);
                os = new FileOutputStream(dstFile);
                oos = new ObjectOutputStream(os);
                //写下极度压缩的字节数组和解码规则
                oos.writeObject(huffmanBytes);
                oos.writeObject(huffmanCodes);
                huffmanCodes = new HashMap<Byte,String>();
                stringBuilder = new StringBuilder();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (oos != null) {
                        oos.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            long endTime = System.currentTimeMillis();    //获取结束时间
            System.out.println( "程序运行时间：" + (endTime - startTime) + "ms" + srcFile);    //输出程序运行时间
        }
    }
    public static void unZipDirectory(String sourceFile, String destinationFile) {
        InputStream is = null;
        ObjectInputStream ois = null;
        OutputStream os = null;
        File file = new File(sourceFile);
        File[] fs = file.listFiles();

        File dst = new File(destinationFile);
        //空文件夹的解压
        if(file.exists() && file.length() == 0) {
            boolean flag = dst.mkdir();
        }
        else {
            boolean flag = dst.mkdir();
            try {
                is = new FileInputStream(sourceFile);
                //创建一个和  is关联的对象输入流
                ois = new ObjectInputStream(is);
                while (true) {
                    String name = (String) ois.readObject();
                    //读取byte数组  huffmanBytes
                    byte[] huffmanBytes = (byte[]) ois.readObject();
                    //读取赫夫曼编码表
                    Map<Byte, String> huffmanCodes = (Map<Byte, String>) ois.readObject();
                    //解码
                    if (null == name && null == huffmanBytes) {
                        break;

                    }
                    //遇到文件夹
                    else if(null == huffmanBytes && null == huffmanCodes){
                        System.out.println("遇到文件夹1");

                        File dst1 = new File(destinationFile + "\\" + name);
                        boolean flag1 = dst1.mkdir();
                        System.out.println("mkdir " + dst1.getName());

                        while (true) {
                            String name1 = (String) ois.readObject();
                            //读取byte数组  huffmanBytes
                            byte[] huffmanBytes1 = (byte[]) ois.readObject();
                            //读取赫夫曼编码表
                            Map<Byte, String> huffmanCodes1 = (Map<Byte, String>) ois.readObject();
                            //解码
                            //读完了
                            if (null == name1) {
                                break;
                            }
                            //遇到文件夹
                            else if(null == huffmanBytes1 && null == huffmanCodes1) {
                                File dst2 = new File(destinationFile + "\\" + name + "\\" + name1);
                                System.out.println("遇到文件夹2");
                                boolean flag2 = dst2.mkdir();
                                System.out.println(name1);
                                System.out.println("mkdir" + dst2.getName());
                                while (true) {
                                    String name2 = (String) ois.readObject();
                                    //读取byte数组  huffmanBytes
                                    byte[] huffmanBytes2 = (byte[]) ois.readObject();
                                    //读取赫夫曼编码表
                                    Map<Byte, String> huffmanCodes2 = (Map<Byte, String>) ois.readObject();
                                    //解码
                                    if (null == name2) {
                                        break;
                                    }
                                    //遇到普通文件
                                    else{
                                        System.out.println("遇到文件3");

                                        byte[] bytes = decode(huffmanCodes2, huffmanBytes2);
                                        //将bytes 数组写入到目标文件

                                        String thisdestinationFile2 = destinationFile + "\\" + name + "\\" + name1 + "\\" + name2;
                                        os = new FileOutputStream(thisdestinationFile2);
                                        //写数据到 dstFile 文件
                                        os.write(bytes);
                                    }

                                }
                            }
                            //遇到普通文件
                            else{
                                System.out.println("遇到文件2");

                                byte[] bytes = decode(huffmanCodes1, huffmanBytes1);
                                //将bytes 数组写入到目标文件

                                String thisdestinationFile1 = destinationFile + "\\" + name + "\\" + name1;
                                os = new FileOutputStream(thisdestinationFile1);
                                //写数据到 dstFile 文件
                                os.write(bytes);
                            }

                        }
                    }
                    //遇到普通文件
                    else{
                        System.out.println("遇到文件1");

                        byte[] bytes = decode(huffmanCodes, huffmanBytes);
                        //将bytes 数组写入到目标文件

                        String thisdestinationFile = destinationFile + "\\" + name;
                        os = new FileOutputStream(thisdestinationFile);
                        //写数据到 dstFile 文件
                        os.write(bytes);
                    }
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        try {
            if (is != null) {
                is.close();
            }
            if (ois != null) {
                ois.close();
            }

            if (os != null) {
                os.close();
            }
        } catch (Exception e2) {
            System.out.println(e2.getMessage());
        }
    }
    //文件夹的压缩方法
    public static void zipDirectory(String srcFile, String dstFile) {
        OutputStream os = null;
        ObjectOutputStream oos = null;
        FileInputStream fis = null;
        File file = new File(srcFile);
        File[] fs = file.listFiles();
        long  startTime = System.currentTimeMillis();

        try {
            //空文件夹的压缩
            if (null == fs){









                os = new FileOutputStream(dstFile);
            }
            else{
                os = new FileOutputStream(dstFile);
                oos = new ObjectOutputStream(os);
                for(File f:fs) {
                    if (!f.isDirectory()) {
                        fis = new FileInputStream(f);
                        //将整个文件作为字节数组读入
                        byte[] bytes = new byte[fis.available()];
                        fis.read(bytes);
                        //得到Huffman编码

                        byte[] huffmanBytes = huffmanZip(bytes);
                        //写下极度压缩的字节数组和解码规则
                        oos.writeObject(f.getName());
                        oos.writeObject(huffmanBytes);
                        Map<Byte, String> huffmanCodes1 = new HashMap<Byte,String>();
                        mapCopy(huffmanCodes1,huffmanCodes);
                        oos.writeObject(huffmanCodes1);
                        huffmanCodes = new HashMap<Byte,String>();
                        stringBuilder = new StringBuilder();
                    }
                    else{
                        oos.writeObject(f.getName());
                        oos.writeObject(null);
                        oos.writeObject(null);
                        File file1 = new File(f.getAbsolutePath());
                        File[] fs1 = file1.listFiles();
                        for(File ff:fs1) {
                            if (!ff.isDirectory()) {
                                fis = new FileInputStream(ff);
                                //将整个文件作为字节数组读入
                                byte[] bytes = new byte[fis.available()];
                                fis.read(bytes);
                                //得到Huffman编码

                                byte[] huffmanBytes = huffmanZip(bytes);
                                //写下极度压缩的字节数组和解码规则
                                oos.writeObject(ff.getName());
                                System.out.println("压缩了" + ff.getName());
                                oos.writeObject(huffmanBytes);
                                Map<Byte, String> huffmanCodes1 = new HashMap<Byte,String>();
                                mapCopy(huffmanCodes1,huffmanCodes);
                                oos.writeObject(huffmanCodes1);
                                huffmanCodes = new HashMap<Byte,String>();
                                stringBuilder = new StringBuilder();
                            }
                            else{
                                oos.writeObject(ff.getName());
                                oos.writeObject(null);
                                oos.writeObject(null);
                                File file2 = new File(ff.getAbsolutePath());
                                File[] fs2 = file2.listFiles();
                                for(File fff:fs2) {
                                    if (!fff.isDirectory()) {
                                        fis = new FileInputStream(fff);
                                        //将整个文件作为字节数组读入
                                        byte[] bytes = new byte[fis.available()];
                                        fis.read(bytes);
                                        //得到Huffman编码

                                        byte[] huffmanBytes = huffmanZip(bytes);
                                        //写下极度压缩的字节数组和解码规则
                                        oos.writeObject(fff.getName());
                                        System.out.println("压缩了" + fff.getName());

                                        oos.writeObject(huffmanBytes);
                                        Map<Byte, String> huffmanCodes1 = new HashMap<Byte,String>();
                                        mapCopy(huffmanCodes1,huffmanCodes);
                                        oos.writeObject(huffmanCodes1);
                                        huffmanCodes = new HashMap<Byte,String>();
                                        stringBuilder = new StringBuilder();

                                    }
                                }
                                oos.writeObject(null);
                                byte[] a = {1};
                                oos.writeObject(a);
                                oos.writeObject(null);
                            }
                        }
                        oos.writeObject(null);
                        byte[] a = {1};
                        oos.writeObject(a);
                        oos.writeObject(null);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (oos != null) {
                    oos.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println( "程序运行时间：" + (endTime - startTime) + "ms" + srcFile);    //输出程序运行时间
    }

    private static byte[] decode(Map<Byte,String> huffmanCodes, byte[] huffmanBytes) {
        Map<String, Byte>  map = new HashMap<String,Byte>();
        //创建要给集合，存放byte
        List<Byte> list = new ArrayList<>();
        //将字符串解码，把赫夫曼编码表进行调换，反向查询
        for(Map.Entry<Byte, String> entry: huffmanCodes.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        StringBuilder stringBuilder = new StringBuilder();
        //将byte数组转成二进制的字符串
        int count1 = 0;
        for(int i = 0; i < huffmanBytes.length; i++) {
            byte b = huffmanBytes[i];
            //如果是最后一个字节，需要补位
            boolean flag = (i == huffmanBytes.length - 1);
            stringBuilder.append(byteToBitString(!flag, b));
            count1++;
            if (count1 == 10000) {
                int f = 0;
                b:for(int  j = 0; j < stringBuilder.length(); ) {
                    int count = 1; // 小的计数器
                    boolean flag1 = true;
                    Byte bb = null;
                    while(flag1) {
                        String key = stringBuilder.substring(j, j+count);//i 不动，让count移动，指定匹配到一个字符
                        bb = map.get(key);
                        if(bb == null) {
                            count++;
                            if (j+count >= stringBuilder.length()){break b;}
                        }else {
                            //在huffman编码表中匹配到
                            flag1 = false;
                        }
                    }
                    f = j+count;
                    list.add(bb);
                    j += count;//i 直接移动到 count
                }
                stringBuilder = new StringBuilder(stringBuilder.substring(f));
                count1 = 0;
            }
        }
        a:for(int  i = 0; i < stringBuilder.length(); ) {
            int count = 1;
            boolean flag = true;
            Byte b = null;
            while(flag) {
                //双标记移动取
                String key = stringBuilder.substring(i, i+count);
                b = map.get(key);
                if(b == null) {
                    count++;
                    if (i+count >= stringBuilder.length()){break a;}

                }else {
                    //匹配到
                    flag = false;
                }
            }
            list.add(b);
            i += count;//i前进
        }


        //把list 中的数据放入到byte[] 并返回
        byte b[] = new byte[list.size()];
        for(int i = 0;i < b.length; i++) {
            b[i] = list.get(i);
        }
        return b;

    }
    //flag是true，则补高位，是false则不用补（最后一字节）
    private static String byteToBitString(boolean flag, byte b) {
        //使用变量保存 b
        int temp = b; //将 b 转成 int
        //如果是正数我们还存在补高位
        if(flag) {
            temp |= 256; //按位与 256  1 0000 0000  | 0000 0001 => 1 0000 0001
        }
        String str = Integer.toBinaryString(temp); //返回的是temp对应的二进制的补码
        if(flag) {
            return str.substring(str.length() - 8);
        } else {
            return str;
        }
    }

    //传入原始数组，得到压缩数组
    private static byte[] huffmanZip(byte[] bytes) {
        List<Node> nodes = getNodes(bytes);
        //根据 nodes 创建的赫夫曼树
        Node huffmanTreeRoot = createHuffmanTree(nodes);
        //对应的赫夫曼编码(根据 赫夫曼树)
        Map<Byte, String> huffmanCodes = getCodes(huffmanTreeRoot);
        //根据生成的赫夫曼编码，压缩得到压缩后的赫夫曼编码字节数组
        byte[] huffmanCodeBytes = zip(bytes, huffmanCodes);
        return huffmanCodeBytes;
    }


    private static byte[] zip(byte[] bytes, Map<Byte, String> huffmanCodes) {
        //1.利用 huffmanCodes 将  bytes 转成  赫夫曼编码对应的字符串
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList a = new ArrayList();
        //遍历bytes 数组,生成超长01串
        //边获得边压缩

        for(byte b: bytes) {
            stringBuilder.append(huffmanCodes.get(b));
            while (stringBuilder.length() > 8){
                String strByte = stringBuilder.substring(0, 8);
                a.add((byte) Integer.parseInt(strByte, 2));
                stringBuilder = new StringBuilder(stringBuilder.substring(8));
            }
        }
        a.add((byte) Integer.parseInt(stringBuilder.toString(), 2));
        byte[] huffmanCodeBytes = new byte[a.toArray().length];
        for (int i = 0; i < huffmanCodeBytes.length; i++) {
            huffmanCodeBytes[i] = (byte)a.get(i);
        }
        return huffmanCodeBytes;
    }




    //用Huffman树生成Huffman编码
    //1. 将赫夫曼编码表存放在 Map<Byte,String> 形式
    //   生成的赫夫曼编码表{32=01, 97=100, 100=11000, 117=11001, 101=1110, 118=11011, 105=101, 121=11010, 106=0010, 107=1111, 108=000, 111=0011}
    static Map<Byte, String> huffmanCodes = new HashMap<Byte,String>();
    //2. 在生成赫夫曼编码表示，需要去拼接路径, 定义一个StringBuilder 存储某个叶子结点的路径
    static StringBuilder stringBuilder = new StringBuilder();

    private static Map<Byte, String> getCodes(Node root) {
        if(root == null) {
            return null;
        }
        //处理root的左子树
        getCodes(root.left, "0", stringBuilder);
        //处理root的右子树
        getCodes(root.right, "1", stringBuilder);
        return huffmanCodes;
    }

    //得到node所有叶子结点的Huffman编码，stringBuilder用来拼接
    private static void getCodes(Node node, String code, StringBuilder stringBuilder) {
        StringBuilder stringBuilder2 = new StringBuilder(stringBuilder);
        //将code 加入到 stringBuilder2
        stringBuilder2.append(code);
        if(node != null) { //如果node == null不处理
            //判断当前node 是叶子结点还是非叶子结点
            if(node.data == null) { //非叶子结点
                //向左递归
                getCodes(node.left, "0", stringBuilder2);
                //向右递归
                getCodes(node.right, "1", stringBuilder2);
            } else { //说明是一个叶子结点
                //就表示找到某个叶子结点的最后
                huffmanCodes.put(node.data, stringBuilder2.toString());
            }
        }
    }


    //接受字节数组，获得带有频次统计的表
    private static List<Node> getNodes(byte[] bytes) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        //遍历 bytes , 统计 每一个byte出现的次数->map[key,value]
        Map<Byte, Integer> counts = new HashMap<>();
        for (byte b : bytes) {
            Integer count = counts.get(b);
            if (count == null) { // Map还没有这个字符数据,第一次
                counts.put(b, 1);
            } else {
                counts.put(b, count + 1);
            }
        }
        //把每一个键值对转成一个Node 对象，并加入到nodes集合
        for(Map.Entry<Byte, Integer> entry: counts.entrySet()) {
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }
        return nodes;

    }

    //可以通过List 创建对应的赫夫曼树
    private static Node createHuffmanTree(List<Node> nodes) {
        while(nodes.size() > 1) {
            Collections.sort(nodes);
            Node leftNode = nodes.get(0);
            Node rightNode = nodes.get(1);
            //创建一颗新的二叉树,它的根节点 没有data, 只有权值
            Node parent = new Node(null, leftNode.weight + rightNode.weight);
            parent.left = leftNode;
            parent.right = rightNode;
            //将已经处理的两颗二叉树从nodes删除
            nodes.remove(leftNode);
            nodes.remove(rightNode);
            //将新的二叉树，加入到nodes
            nodes.add(parent);
        }
        //nodes 最后的结点，就是赫夫曼树的根结点
        return nodes.get(0);
    }
    public static void mapCopy(Map resultMap, Map paramsMap) {
        if (resultMap == null) resultMap = new HashMap();
        if (paramsMap == null) return;

        Iterator it = paramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            resultMap.put(key, paramsMap.get(key) != null ? paramsMap.get(key) : "");

        }
    }
}
//创建Node ,待数据和权值
class Node implements Comparable<Node>  {
    Byte data; // 存放数据(字符)本身，比如ASCII码表
    int weight; //字符频率
    Node left;
    Node right;
    public Node(Byte data, int weight) {

        this.data = data;
        this.weight = weight;
    }
    @Override
    public int compareTo(Node o) {
        // 从小到大排序
        return this.weight - o.weight;
    }
    public String toString() {
        return "Node (data = " + data + " weight=" + weight + ")";
    }
}

