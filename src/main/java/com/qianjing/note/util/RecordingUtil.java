package com.qianjing.note.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class RecordingUtil {
    private byte[] bts = new byte[10000];

    private static ByteArrayOutputStream baos = null;

    private static boolean stopFlag;

    private static TargetDataLine td = null;

    private static ByteArrayInputStream bais = null;
    // 定义音频输入流
    private static AudioInputStream ais = null;

    private static AudioFormat af = null;
    // 定义源数据行,源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传递给混频器。
    private static SourceDataLine sd = null;

    //开始录音
    public synchronized static void start() throws LineUnavailableException {
        af = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
        td = (TargetDataLine) (AudioSystem.getLine(info));
        td.open(af);
        td.start();
        Record record = new Record();
        Thread t1 = new Thread(record);
        t1.start();
    }

    // 停止录音,并保存
    public synchronized static void stop() {
        stopFlag = true;
        save();
    }

    private synchronized static AudioFormat getAudioFormat() {
        // 下面注释部分是另外一种音频格式，两者都可以
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 8000f;
        int sampleSize = 16;
        boolean bigEndian = true;
        int channels = 1;
        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }

    // 录音类，因为要用到MyRecord类中的变量，所以将其做成内部类
    static class Record implements Runnable {
        // 定义存放录音的字节数组,作为缓冲区
        byte bts[] = new byte[10000];

        // 将字节数组包装到流里，最终存入到baos中
        // 重写run函数
        public synchronized void run() {
            baos = new ByteArrayOutputStream();
            try {
                stopFlag = false;
                while (stopFlag != true) {
                    // 当停止录音没按下时，该线程一直执行
                    // 从数据行的输入缓冲区读取音频数据。
                    // 要读取bts.length长度的字节,cnt 是实际读取的字节数
                    int cnt = td.read(bts, 0, bts.length);
                    if (cnt > 0) {
                        baos.write(bts, 0, cnt);
                    }

                    // 开始从音频流中读取字节数
                    byte copyBts[] = bts;
                    bais = new ByteArrayInputStream(copyBts);
                    ais = new AudioInputStream(bais, af, copyBts.length / af.getFrameSize());
                    try {
                        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);
                        sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                        sd.open(af);
                        sd.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    // intBytes = -1;
                    // 关闭打开的字节数组流
                    if (baos != null) {
                        baos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    td.close();
                }
            }
        }
    }

    // 保存录音
    private synchronized static void save() {
        af = getAudioFormat();
        byte audioData[] = baos.toByteArray();
        bais = new ByteArrayInputStream(audioData);
        ais = new AudioInputStream(bais, af, audioData.length / af.getFrameSize());
        // 定义最终保存的文件名
        File file = null;
        // 写入文件
        try {
            // 以当前的时间命名录音的名字
            // 将录音的文件存放到D盘下
            File filePath = new File("F:/");
            String tarPath = "F:/";
            if (!filePath.exists()) {// 如果文件不存在，则创建该目录
                filePath.mkdirs();
            }
            long time = System.currentTimeMillis();
            file = new File(filePath + "/" + time + ".wav");
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (bais != null) {
                    bais.close();
                }
                if (ais != null) {
                    ais.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
