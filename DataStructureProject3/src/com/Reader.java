package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Reader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** 调用这个方法来初始化reader，即InputStream*/
    static void init(InputStream input) {
        reader = new BufferedReader(
                new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** 获取下一段文本 */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                    reader.readLine(),"\n");
        }
        return tokenizer.nextToken();
    }
}