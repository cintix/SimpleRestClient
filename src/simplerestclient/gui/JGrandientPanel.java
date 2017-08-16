/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerestclient.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

/**
 *
 * @author migo
 */
public class JGrandientPanel extends JPanel {

    public JGrandientPanel() {
        setBackground(new Color(64, 64, 64));

    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        Point2D start = new Point2D.Float(getWidth(), 0);
        Point2D end = new Point2D.Float(getWidth(), getHeight());
        float[] dist = {0.0f, 0.07f, 0.5f, 0.93f, 1f};
        Color[] colors = {Color.BLACK, getBackground().darker(), getBackground(), getBackground().darker(), Color.BLACK};
        LinearGradientPaint linearGradientPaint = new LinearGradientPaint(start, end, dist, colors);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Paint paint = graphics2D.getPaint();

        //Top
        graphics2D.setPaint(linearGradientPaint);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);

        //Border 
        float thickness = 1.2f;
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(thickness));
        graphics2D.drawRect(0, 0, getWidth(), getHeight());
        graphics2D.setStroke(oldStroke);

    }
}
