package gui.layout;

import gui.Container;
import gui.GUIElement;
import gui.GUILayoutManager;

import java.awt.*;

/**
 * Created by Chris on 4/28/2016.
 */
public class GUIGridLayout extends GUILayoutManager {

    int rows;
    int cols;

    public GUIGridLayout(int rows, int cols) {

        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void layout() {

        Container parent = getParent();

        int maxPrefWidth = 0;
        int maxPrefHeight = 0;

        for (GUIElement child : parent.getChildren()) {

            if (child.getPreferredSize().width > maxPrefWidth)
                maxPrefWidth = child.getPreferredSize().width;
            if (child.getPreferredSize().height > maxPrefHeight)
                maxPrefHeight = child.getPreferredSize().height;
        }

        int currentRow = 0;
        int currentCols = 0;
        int x = parent.getX();
        int y = parent.getY();
        Rectangle bounds = new Rectangle(x, y, maxPrefWidth, maxPrefHeight);
        for (GUIElement child : parent.getChildren()) {

            child.setBounds(bounds);
            currentCols = (currentCols + 1) % cols;
            currentRow = (currentRow + 1) % rows;
        }
    }

    @Override
    public void setConstraint(GUIElement element, Object constraint) {

    }

    @Override
    public Dimension getPreferredSize() {
        //make prefsize a multiple of rows/cols and prefsize of children
        return getParent().getSize();
    }
}
