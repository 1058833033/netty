package com.qf.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ChenJie
 * @date 2020-06-09 21:02:59
 * 功能说明
 */
public class FileCopy {
    private static String inPath = "D:\\Desktop\\9.jpg";
    private static String outPath = "D:\\Desktop\\10.jpg";

    public static void main(String[] args){
        try (
                FileChannel inChannel = new FileInputStream(inPath).getChannel();
                FileChannel outChannel = new FileOutputStream(outPath).getChannel()
        ){

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10); //10k
            // 循环从输入文件读取数据到byteBuffer中
            while (inChannel.read(byteBuffer) != -1){
                // 界限放到的位置，位置置空
                byteBuffer.flip();

                outChannel.write(byteBuffer);

                // 界限放到容量位置，位置置空
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("完成！");

    }
}
