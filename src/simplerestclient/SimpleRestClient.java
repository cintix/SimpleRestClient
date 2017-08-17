package simplerestclient;

import com.apple.eawt.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.w3c.tidy.Tidy;
import simplerestclient.gui.HeaderTable;
import simplerestclient.gui.JGrandientPanel;
import simplerestclient.gui.JWindowFrame;
import simplerestclient.gui.TextBubbleBorder;
import simplerestclient.gui.TextLineNumber;
import simplerestclient.net.HTTPRestClient;

/**
 *
 * @author migo
 */
public class SimpleRestClient implements ActionListener, KeyListener {

    private JWindowFrame frame = new JWindowFrame();
    private String[] httpActionNames = {"GET", "POST", "PUT", "DELETE"};
    private JComboBox httpActions;
    private JTextField urlField;
    private JTable requestHeaders;
    private JTable responseHeaders;
    private JTextArea requestData;
    private RSyntaxTextArea responseData;
    private JButton sendBnt;
    private JTabbedPane detailsPanel;

    public SimpleRestClient() throws HeadlessException {
        initMainGUI();

        httpActions = new JComboBox(httpActionNames);
        httpActions.setLocation(10, 115);
        httpActions.setSize(100, 40);
        httpActions.setLightWeightPopupEnabled(true);
        frame.addComponent(httpActions);

        urlField = new JTextField();
        urlField.setSize(600, 28);
        urlField.setLocation(110, 120);
        urlField.setBorder(new TextBubbleBorder(new Color(102, 204, 255), 2, 4, 0));
        urlField.addKeyListener(this);
        frame.addToAlignWith(urlField, JWindowFrame.KeepAlignBase.WIDTH);

        sendBnt = new JButton("Send");
        sendBnt.setBounds(715, 117, 65, 35);
        sendBnt.setForeground(Color.DARK_GRAY.darker());
        sendBnt.addActionListener(this);
        frame.addAlignTo(sendBnt, JWindowFrame.AlignBase.RIGHT);

        JLabel requestParams = new JLabel("Request Headers");
        requestParams.setSize(200, 30);
        requestParams.setLocation(15, 155);
        frame.addComponent(requestParams);

        requestHeaders = new JTable(new HeaderTable());
        requestHeaders.setEnabled(true);
        JScrollPane scrollPaneForHeaders = new JScrollPane(requestHeaders);
        scrollPaneForHeaders.setSize(frame.getWidth() - 30, 66);
        scrollPaneForHeaders.setLocation(15, 180);

        requestHeaders.setValueAt("Content-Type", 0, 0);
        requestHeaders.setValueAt("*/*", 0, 1);

        requestHeaders.setValueAt("Accept", 1, 0);
        requestHeaders.setValueAt("*/*", 1, 1);

        requestHeaders.setValueAt("User-Agent", 2, 0);
        requestHeaders.setValueAt("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2", 2, 1);

        frame.addToAlignWith(scrollPaneForHeaders, JWindowFrame.KeepAlignBase.WIDTH);

        detailsPanel = new JTabbedPane();
        detailsPanel.setLocation(6, 250);
        detailsPanel.setSize(frame.getWidth() - 12, 310);

        requestData = new JTextArea();
        requestData.setMargin(new Insets(5, 5, 5, 5));
        requestData.setLineWrap(true);

        JScrollPane scrollPaneForRequestData = new JScrollPane(requestData);
        TextLineNumber textLineNumberForRequest = new TextLineNumber(requestData);

        textLineNumberForRequest.setBackground(new Color(65, 65, 65));
        textLineNumberForRequest.setForeground(Color.white);
        textLineNumberForRequest.setCurrentLineForeground(Color.ORANGE);

        scrollPaneForRequestData.setRowHeaderView(textLineNumberForRequest);
        detailsPanel.addTab("Request", new JPanel().add(scrollPaneForRequestData));

        responseData = new RSyntaxTextArea();

        try {
            Theme.load(getClass().getResourceAsStream("eclipse.xml")).apply(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        responseData.setMargin(new Insets(5, 5, 5, 5));
        //responseData.setLineWrap(true);

        JScrollPane scrollPaneForResponseData = new JScrollPane(responseData);
        TextLineNumber textLineNumberForResponseData = new TextLineNumber(responseData);

        textLineNumberForResponseData.setBackground(new Color(65, 65, 65));
        textLineNumberForResponseData.setForeground(Color.white);
        textLineNumberForResponseData.setCurrentLineForeground(Color.ORANGE);

        scrollPaneForResponseData.setRowHeaderView(textLineNumberForResponseData);

        responseHeaders = new JTable(new HeaderTable());
        responseHeaders.setEnabled(true);

        JScrollPane scrollPaneForResponseHeaders = new JScrollPane(responseHeaders);

        JPanel responsePanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(responsePanel, BoxLayout.Y_AXIS);
        responsePanel.setLayout(boxLayout);

//        Dimension sizeForscrollPaneForResponseData = scrollPaneForResponseData.getSize();
//        sizeForscrollPaneForResponseData.height = 160;
//        scrollPaneForResponseData.setMinimumSize(sizeForscrollPaneForResponseData);
//
//        Dimension sizeForscrollPaneForResponseHeaders = scrollPaneForResponseHeaders.getSize();
//        sizeForscrollPaneForResponseHeaders.height = 300;
//        sizeForscrollPaneForResponseHeaders.width = 9000;
//        scrollPaneForResponseHeaders.setMaximumSize(sizeForscrollPaneForResponseHeaders);
//
//        sizeForscrollPaneForResponseHeaders = scrollPaneForResponseHeaders.getSize();
//        sizeForscrollPaneForResponseHeaders.height = 150;
//        scrollPaneForResponseHeaders.setMinimumSize(sizeForscrollPaneForResponseData);

        responsePanel.add(scrollPaneForResponseData);
        
        //responsePanel.add(scrollPaneForResponseHeaders);
        
        detailsPanel.addTab("Response", responsePanel);
        detailsPanel.addTab("Response Headers", new JPanel().add(scrollPaneForResponseHeaders));
        frame.addToAlignWith(detailsPanel, JWindowFrame.KeepAlignBase.BOTH);
        frame.setVisible(true);
    }

    private void initMainGUI() {
        frame.setTitle("Simple Rest Client 1.0");
        frame.setRestrainSize(800, 650);
        frame.setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(getClass().getResource("icon.png"));
        frame.setIconImage(icon.getImage());

        //Application.getApplication().setDockIconBadge("");
        Application.getApplication().setDockIconImage(icon.getImage());

        JGrandientPanel buttomPanel = new JGrandientPanel();
        buttomPanel.setLayout(null);
        buttomPanel.setSize(frame.getSize().width, 100);

        JLabel hyperlink = new JLabel("http://cintix.dk");
        hyperlink.setForeground(new Color(0, 204, 255));
        hyperlink.setLocation(20, 20);
        hyperlink.setSize(200, 40);
        hyperlink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hyperlink.addMouseListener(new UrlClickHandler(hyperlink.getText()));
        buttomPanel.add(hyperlink);

        frame.addToButton(buttomPanel);

        JPanel topPanelBackground = new JPanel();
        topPanelBackground.setLayout(null);
        topPanelBackground.setLocation(0, 0);
        topPanelBackground.setSize(frame.getSize().width, 100);
        topPanelBackground.setBackground(Color.DARK_GRAY);

        frame.addToTop(topPanelBackground);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setLocation(0, 0);
        topPanel.setSize(frame.getSize().width, 92);
        topPanel.setBackground(new Color(210, 210, 210));

        JLabel logoContainer = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH)));
        logoContainer.setSize(75, 75);
        logoContainer.setLocation(10, 10);
        topPanel.add(logoContainer, 0);

        JLabel logoText = new JLabel();
        logoText.setText("REST Client 1.0");
        logoText.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        logoText.setForeground(Color.DARK_GRAY);
        logoText.setSize(500, 40);
        logoText.setLocation(85, 15);
        topPanel.add(logoText, 1);

        JLabel logoTextUnderline = new JLabel();
        logoTextUnderline.setText("I just had to make one");
        logoTextUnderline.setFont(new Font(Font.DIALOG, Font.ITALIC | Font.BOLD, 12));
        logoTextUnderline.setForeground(new Color(51, 153, 255));
        logoTextUnderline.setSize(500, 40);
        logoTextUnderline.setLocation(90, 40);
        topPanel.add(logoTextUnderline, 2);

        frame.addToTop(topPanel);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SimpleRestClient();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doRestCall();
    }

    private void doRestCall() {
        try {
            String url = urlField.getText();
            if (!url.toLowerCase().startsWith("http://")) {
                String tmp = "http://" + url;
                url = tmp;
            }
            String method = httpActions.getSelectedItem().toString();
            String postData = requestData.getText().trim();
            HeaderTable requestModel = (HeaderTable) requestHeaders.getModel();
            HeaderTable responseModel = (HeaderTable) responseHeaders.getModel();
            Map<String, String> headers = new LinkedHashMap<>();
            System.out.println("model.getRowCount()  " + responseModel.getRowCount());

            if (!url.isEmpty()) {
                sendBnt.setEnabled(false);
                System.out.println("Okay so fare so good");
                int rows = requestModel.getRowCount();
                for (int row = 0; row < rows; row++) {
                    String key = requestModel.getValueAt(row, 0).toString().trim();
                    String value = requestModel.getValueAt(row, 1).toString().trim();
                    if (!key.isEmpty() && !value.isEmpty()) {
                        headers.put(key, value);
                    }
                }

                HTTPRestClient client = new HTTPRestClient("", HTTPRestClient.HTTPContentType.APPLICATION_DEFAULT);
                client.setHeadersMap(headers);

                System.out.println("Going online!");
                String actionData = client.action(method, url, postData);

                responseModel.fillFromMap(client.getResponseHeadersMap());
                String contentType = responseModel.getKey("content-type");                
                if (contentType != null) {
                    if (contentType.toLowerCase().contains("html")) {
                        String tmp = prettyFormat(actionData, false);
                        responseData.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
                        actionData = tmp;
                    }

                    if (contentType.toLowerCase().contains("xml")) {
//                        if (actionData.toLowerCase().contains("<?xml")) {
//                            int start = actionData.toLowerCase().indexOf("<?xml");
//                            int end = actionData.indexOf("?>", start+1);
//                            actionData = actionData.substring(end +2);
//                        }
                        String tmp = prettyFormat(actionData, true);
                        responseData.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
                        actionData = tmp;
                    }

                    if (contentType.toLowerCase().contains("json")) {
                        JsonParser parser = new JsonParser();
                        JsonObject json = parser.parse(actionData).getAsJsonObject();
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        actionData = gson.toJson(json);
                        responseData.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
                    }

                }

                responseData.setText(actionData);
                responseData.setCaretPosition(0);
                responseHeaders.setModel(responseModel);
                responseHeaders.updateUI();

                detailsPanel.setSelectedIndex(1);

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        sendBnt.setEnabled(true);

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
            doRestCall();
        }
    }

    public static String prettyFormat(String input, boolean xml) {
        try {
            StringWriter stringWriter = new StringWriter();
            Tidy tidy = new Tidy();
            tidy.setOutputEncoding("utf-8");
            tidy.setSmartIndent(true);
            tidy.setXHTML(false);
            tidy.setQuiet(true);
            tidy.setSmartIndent(false);
//            tidy.setMakeClean(true);
            tidy.setForceOutput(true);
            if (xml) {
                tidy.setXmlTags(true);
            }
            tidy.parse(new StringReader(input), stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
