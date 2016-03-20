package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

/**
 * The action associated with opening the various PDFs contained in eOSB.
 * 
 * @author Caine Jette
 * 
 */
public class OpenPdfAction extends AbstractAction {

  private String pdfToOpen;

  /**
   * @param pdfToOpen the PDF to open
   */
  public OpenPdfAction(String pdfToOpen) {
    this.pdfToOpen = pdfToOpen;
  }

  /** {@inheritDoc} */
  public void actionPerformed(ActionEvent ae) {
    URL pdfUrl = ClassLoader.getSystemClassLoader().getResource(this.pdfToOpen);
    SwingController controller = new SwingController();
    SwingViewBuilder factory = new SwingViewBuilder(controller);
    controller.openDocument(pdfUrl);
    controller.setCompleteToolBar(new JToolBar());
    
    JFrame viewerFrame = factory.buildViewerFrame();
    viewerFrame.setTitle(this.pdfToOpen);
    viewerFrame.pack();
    viewerFrame.setVisible(true);
  }
}
