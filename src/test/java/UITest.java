import net.sourceforge.jnlp.security.policyeditor.PolicyEditor;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: alexkasko
 * Date: 5/24/17
 */
public class UITest {

    @Test
    public void test() {
        PolicyEditor.PolicyEditorFrame pe = new PolicyEditor.PolicyEditorFrame(new PolicyEditor("target/java.policy"));
        showAndWait(pe);
    }

    public static void showAndWait(final Frame frame) {
            try {
                final Thread[] edtThreadHolder = new Thread[1];
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        frame.addWindowListener(new CloseListener());
                        frame.pack();
//                                jf.setSize(1024, 600);
                        frame.setLocationRelativeTo(null);
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


    private static class CloseListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            for (Frame fr : Frame.getFrames()) {
                fr.dispose();
            }
        }
    }

}
