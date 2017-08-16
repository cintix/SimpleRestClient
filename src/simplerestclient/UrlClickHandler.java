package simplerestclient;


import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author migo
 */
public class UrlClickHandler extends MouseAdapter {
    private final String url;

    public UrlClickHandler(String url) {
        this.url = url;
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(UrlClickHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
