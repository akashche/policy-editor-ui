import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: alexkasko
 * Date: 6/1/17
 */
public class TestUtils {
    public static void showAndWait(final JFrame frame) {
        try {
            final Thread[] edtThreadHolder = new Thread[1];
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    frame.addWindowListener(new CloseListener());
                    frame.pack();
                    frame.setLocationRelativeTo(null);

//                    Boxer.highlightBorders(frame.getContentPane());

                    frame.setVisible(true);
                    edtThreadHolder[0] = Thread.currentThread();
                }
            });
            // join on EDT thread here for test-only purposes
            edtThreadHolder[0].join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static class CloseListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            for (Frame fr : Frame.getFrames()) {
                fr.dispose();
            }
        }
    }
}
