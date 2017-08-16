package simplerestclient.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class JWindowFrame extends JFrame implements ComponentListener {

    public enum KeepAlignBase {
        WIDTH, HEIGHT, BOTH
    }

    public enum AlignBase {
        LEFT, RIGHT, TOP, BUTTOM
    }

    private LinkedList<JComponent> sized = new LinkedList<>();
    private LinkedList<JComponent> buttom = new LinkedList<>();
    private LinkedList<JComponent> top = new LinkedList<>();
    private Map<KeepAlignBase, LinkedList<AligmentDetails>> basedAlign = new LinkedHashMap<>();
    private Map<AlignBase, LinkedList<AligmentDetails>> basedToAlign = new LinkedHashMap<>();

    private Dimension restrainSize;
    private JPanel container = new JPanel();

    public JWindowFrame() throws HeadlessException {
        init();
    }

    public JWindowFrame(String title) throws HeadlessException {
        setTitle(title);
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.setLocation(0, 0);
        container.setSize(getSize());

        container.setLayout(null);
        addComponentListener(this);
        add(container);
        addToFitMe(container);

        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", getTitle());

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }
    }

    @Override
    public void componentResized(ComponentEvent e) {

        if (restrainSize != null) {
            int height = restrainSize.height;
            int width = restrainSize.width;

            if (getSize().width < width && getSize().height < height) {
                setSize(restrainSize);
            }
            if (getSize().width < width && getSize().height >= height) {
                setSize(width, getSize().height);
            }
            if (getSize().width >= width && getSize().height < height) {
                setSize(getSize().width, height);
            }

        }

        for (JComponent component : sized) {
            component.setSize(getSize());
            component.setLocation(0, 0);
        }

        for (JComponent component : buttom) {
            int height = component.getHeight();
            component.setSize(getSize().width, height);
            component.setLocation(0, getSize().height - height);
        }

        for (JComponent component : top) {
            int height = component.getHeight();
            component.setSize(getSize().width, height);
            component.setLocation(0, 0);
        }

        if (basedAlign.size() > 0) {
            if (basedAlign.containsKey(KeepAlignBase.WIDTH)) {
                for (AligmentDetails aligmentDetails : basedAlign.get(KeepAlignBase.WIDTH)) {
                    JComponent component = aligmentDetails.getComponent();
                    component.setLocation(aligmentDetails.left, aligmentDetails.top);
                    Dimension dimension = component.getSize();
                    dimension.width = getWidth() - aligmentDetails.diff;
                    component.setSize(dimension);
                }
            }
            if (basedAlign.containsKey(KeepAlignBase.HEIGHT)) {
                for (AligmentDetails aligmentDetails : basedAlign.get(KeepAlignBase.HEIGHT)) {
                    JComponent component = aligmentDetails.getComponent();
                    component.setLocation(aligmentDetails.left, aligmentDetails.top);
                    Dimension dimension = component.getSize();
                    dimension.height = getHeight() - aligmentDetails.diff;
                    component.setSize(dimension);
                }
            }
        }

        if (basedToAlign.size() > 0) {
            if (basedToAlign.containsKey(AlignBase.RIGHT)) {
                for (AligmentDetails aligmentDetails : basedToAlign.get(AlignBase.RIGHT)) {
                    JComponent component = aligmentDetails.getComponent();
                    component.setLocation(getWidth() - aligmentDetails.right, aligmentDetails.top);
                }
            }
            if (basedToAlign.containsKey(AlignBase.LEFT)) {
                for (AligmentDetails aligmentDetails : basedToAlign.get(AlignBase.LEFT)) {
                    JComponent component = aligmentDetails.getComponent();
                    component.setLocation(aligmentDetails.left, aligmentDetails.top);
                }
            }

            if (basedToAlign.containsKey(AlignBase.TOP)) {
                for (AligmentDetails aligmentDetails : basedToAlign.get(AlignBase.TOP)) {
                    JComponent component = aligmentDetails.getComponent();
                    component.setLocation(aligmentDetails.left, aligmentDetails.top);
                }
            }
            if (basedToAlign.containsKey(AlignBase.BUTTOM)) {
                for (AligmentDetails aligmentDetails : basedToAlign.get(AlignBase.BUTTOM)) {
                    JComponent component = aligmentDetails.getComponent();
                    component.setLocation(aligmentDetails.left, getHeight() - aligmentDetails.buttom);
                }
            }
        }

    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    public Component addComponent(Component comp) {
        return container.add(comp, 0);
    }

    public void addToButton(JComponent component) {
        buttom.add(0, component);
        container.add(component, 0);
    }

    public void addAlignTo(JComponent component, AlignBase alignment) {
        LinkedList<AligmentDetails> components = basedToAlign.get(alignment);

        if (components == null) {
            components = new LinkedList<AligmentDetails>();
        }
        components.add(new AligmentDetails(component, 0));
        basedToAlign.put(alignment, components);
        container.add(component);
    }

    public void addToAlignWith(JComponent component, KeepAlignBase alignment) {
        LinkedList<AligmentDetails> components = basedAlign.get(alignment);

        if (components == null) {
            components = new LinkedList<AligmentDetails>();
        }

        AligmentDetails details;

        int diff = 0;

        if (alignment == KeepAlignBase.WIDTH) {
            diff = getWidth() - component.getWidth();
        } else if (alignment == KeepAlignBase.HEIGHT) {
            diff = getHeight() - component.getHeight();
        } else {
            components = basedAlign.get(KeepAlignBase.WIDTH);
            if (components == null) {
                components = new LinkedList<AligmentDetails>();
            }

            diff = getWidth() - component.getWidth();
            details = new AligmentDetails(component, diff);
            components.add(details);
            basedAlign.put(KeepAlignBase.WIDTH, components);

            components = basedAlign.get(KeepAlignBase.HEIGHT);
            if (components == null) {
                components = new LinkedList<AligmentDetails>();
            }
            diff = getHeight() - component.getHeight();
            details = new AligmentDetails(component, diff);
            components.add(details);
            basedAlign.put(KeepAlignBase.HEIGHT, components);
            container.add(component);
            return;
        }

        details = new AligmentDetails(component, diff);
        components.add(details);

        basedAlign.put(alignment, components);
        container.add(component);
    }

    public void addToFitMe(JComponent component) {
        if (component == container) {
            return;
        }
        sized.add(0, component);
        container.add(component, 0);
    }

    public void addToTop(JComponent component) {
        top.add(0, component);
        container.add(component, 0);
    }

    public void setRestrainSize(int width, int height) {
        setSize(width, height);
        restrainSize = getSize();
    }

    private class AligmentDetails {

        private final int diff;
        private final JComponent component;
        private final int left;
        private final int top;
        private final int right;
        private final int buttom;

        public AligmentDetails(JComponent component, int diff) {
            this.diff = diff;
            this.component = component;
            left = this.component.getLocation().x;
            top = this.component.getLocation().y;
            right = getWidth() - (left);
            buttom = getHeight() - (top);
        }

        public int getDiff() {
            return diff;
        }

        public int getLeft() {
            return left;
        }

        public int getTop() {
            return top;
        }

        public int getRight() {
            return right;
        }

        public int getButtom() {
            return buttom;
        }

        public JComponent getComponent() {
            return component;
        }

    }

}
