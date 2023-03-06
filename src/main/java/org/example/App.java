package org.example;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.wav.WavFileReader;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] argv) throws IOException, UnsupportedAudioFileException {
        LibVosk.setLogLevel(LogLevel.DEBUG);

//        File file = new File(App.class.getClassLoader().getResource("/resources/01.wav").getFile());

//        InputStream is = App.class.getResourceAsStream("/resources/01.wav");
//        File file = new File("E:\\WorkSpaces\\Intelliji\\vosk\\voskTest\\src\\main\\resources\\01.wav");

        Microphone microphone = SpeechSourceProvider.getMicrophone();
        microphone.startRecording();
        try (Model model = new Model("E:\\WorkSpaces\\Intelliji\\vosk\\voskTest\\src\\test\\java\\org\\example\\model\\vosk-model-cn-0.22");
             InputStream ais = microphone.getStream();
//             InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
             Recognizer recognizer = new Recognizer(model,  16000)) {

            int nbytes;
            byte[] b = new byte[4096];
            while ((nbytes = ais.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    System.out.println(recognizer.getResult());
                } else {
//                    System.out.println(recognizer.getPartialResult());
                }
            }

            System.out.println(recognizer.getFinalResult());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取音频文件的采样率
     */
    private static Float getSampleRate(File file) throws Exception {
        WavFileReader fileReader = new WavFileReader();
        AudioFile audioFile = fileReader.read(file);
        String sampleRate = audioFile.getAudioHeader().getSampleRate();
        return Float.parseFloat(sampleRate);
    }
}
