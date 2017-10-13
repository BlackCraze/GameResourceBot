package de.blackcraze.grb.ocr;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;

public class PreProcessorGui {

    private final int KEY_P = 112;
    // keys

    final int KEY_PLUS = 43; // plus
    final int KEY_MINUS = 45; // minus

    final int KEY_LR = 113; // Q
    final int KEY_LG = 119; // W
    final int KEY_LB = 101; // E

    final int KEY_UR = 97; // A
    final int KEY_UG = 115; // S
    final int KEY_UB = 100; // D

    final int KEY_T = 116;
    final int KEY_O = 111;
    final int KEY_I = 105;
    final int KEY_V = 118;
    final int KEY_H = 104;
    final int KEY_BACKSPACE = 8;
    // states

    // init with texts

    int lr;
    int lg;
    int lb;

    int ur;
    int ug;
    int ub;

    int threshSrcLow;
    int threshSrcUp;

    int threshMode;

    {
        initNum();
    }

    double thresh = 0.04;

    // modes

    int mode = KEY_PLUS;
    int out = 1;
    int in = 0;

    final String src = "en_iphone7_750px_1334px_1710102230d.png";

    private Mat load;

    private List<File> cropMasked = Collections.emptyList();

    private void run() {
        try {
            int lastKey = -1;
            load = load();
            while (lastKey != 27 && lastKey != 255) {

                switchMode(lastKey);
                changeState(lastKey);
                print(lastKey);

                opencv_highgui.imshow("fragment", process(load));
                if (lastKey == KEY_BACKSPACE) {
                    switchDebug();
                    lastKey = -1;
                }
                int key = opencv_highgui.waitKey();
                lastKey = key;
                System.out.println(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PreProcessorGui gui = new PreProcessorGui();
        gui.run();
    }

    private void initNum() {
        lr = 25;
        lg = 255;
        lb = 50;
        ur = 50;
        ug = 255;
        ub = 255;
        threshSrcLow = 80;
        threshSrcUp = 255;
        threshMode = CV_THRESH_BINARY_INV;
    }

    private void initText() {
        lr = 0;
        lg = 2;
        lb = 0;
        ur = 255;
        ug = 255;
        ub = 255;
        threshSrcLow = 150;
        threshSrcUp = 255;
        threshMode = CV_THRESH_BINARY;
    }

    private Mat load() throws IOException {
        if (cropMasked.isEmpty()) {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(src);
            cropMasked = Preprocessor.load(stream);
        }
        return imread(cropMasked.get(in).getAbsolutePath());
    }

    private void print(int lastKey) {
        switch (lastKey) {
        case KEY_P:
            System.out.println("lowerb[" + lr + "," + lg + "," + lb + "]");
            System.out.println("ubberb[" + ur + "," + ug + "," + ub + "]");
            System.out.println("thresh[" + thresh + "]");
        }
    }

    private void changeState(int lastKey) throws IOException {
        switch (lastKey) {
        case KEY_LR:
            lr = mode == KEY_PLUS ? lr + 1 : lr - 1;
            lr = lr < 0 ? 0 : lr;
            lr = lr > 255 ? 255 : lr;
            break;
        case KEY_LG:
            lg = mode == KEY_PLUS ? lg + 1 : lg - 1;
            lg = lg < 0 ? 0 : lg;
            lg = lg > 255 ? 255 : lg;
            break;
        case KEY_LB:
            lb = mode == KEY_PLUS ? lb + 1 : lb - 1;
            lb = lb < 0 ? 0 : lb;
            lb = lb > 255 ? 255 : lb;
            break;
        case KEY_UR:
            ur = mode == KEY_PLUS ? ur + 1 : ur - 1;
            ur = ur < 0 ? 0 : ur;
            ur = ur > 255 ? 255 : ur;
            break;
        case KEY_UG:
            ug = mode == KEY_PLUS ? ug + 1 : ug - 1;
            ug = ug < 0 ? 0 : ug;
            ug = ug > 255 ? 255 : ug;
            break;
        case KEY_UB:
            ub = mode == KEY_PLUS ? ub + 1 : ub - 1;
            ub = ub < 0 ? 0 : ub;
            ub = ub > 255 ? 255 : ub;
            break;
        case KEY_T:
            thresh = mode == KEY_PLUS ? thresh + 0.001 : thresh - 0.001;
            break;
        case KEY_O:
            out = out == 1 ? 0 : 1;
            break;
        case KEY_I:
            if (mode == KEY_PLUS) {
                if (in + 1 < cropMasked.size()) {
                    in++;
                }
            } else {
                if (in - 1 >= 0) {
                    in--;
                }
            }
            load = load();
            System.out.println("SRC: " + cropMasked.get(in).getAbsolutePath());
            break;
        case KEY_V:
            initNum();
            break;
        case KEY_H:
            initText();
            break;
        case KEY_BACKSPACE:
            switchDebug();
        }
    }

    private void switchDebug() {
        Preprocessor.debug = !Preprocessor.debug; // FIXME this is ugly
    }

    private void switchMode(int lastKey) {
        if (lastKey == KEY_PLUS || lastKey == KEY_MINUS) {
            mode = lastKey;
        }
    }

    private Mat process(Mat src) throws IOException {
        if (out == 0) {
            return src;
        }
        Mat maskLow = new Mat(new double[] { lr, lg, lb });
        Mat maskUp = new Mat(new double[] { ur, ug, ub });
        return Preprocessor.process(src, maskLow, maskUp, thresh, threshSrcLow, threshSrcUp, threshMode, "gui");
    }

}
