/*
CAS Computer Algebra System
Copyright (C) 2005  William Tracy

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/

package org.matheclipse.symja;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A MathRenderer displays a list of previously entered commands
 * and their results (return values). It displays previously
 * entered commands aligned left and their results aligned
 * right, in an attempt to mimic the displays of TI calculators.
 * Commands and results come in pairs; so, entries with even
 * indexes are assumed to be commands, and entries with odd
 * indexes are assumed to be results.
 */
public class MathRenderer implements ListCellRenderer{
	int r;
	int dr;
	int g;
	int dg;
	int b;
	int db;

    /** Cache of generated Components; see documentation for
      * getListCellRenderer()
      */
	java.util.List components = new ArrayList();

    /** Generates a MathRenderer.
      * Just for fun, MathRenderers display entries with different
      * background colors. The first entry has a background color
      * determined by the arguments r, g, and b. The second entry has
      * a background color of r + dr, g + dg, and b + db. The third
      * entry has a background color of r + 2 * dr, g + 2 * dg, and
      * b + 2 * db, so the result is a kind of gradient down the
      * list.
      *
      * In addition, each component paints itself with a horizontal
      * gradent going from it's "background color" to white.
      */
	public MathRenderer(int r,
                        int g,
                        int b,
                        int dr,
                        int dg,
						int db) {
		this.r = r;
		this.dr = dr;
		this.g = g;
		this.dg = dg;
		this.b = b;
		this.db = db;
	}

    /** Creates a MathRenderer without the gradient effect.
      */
    public MathRenderer(int red, int green, int blue) {
        this.r = red;
        this.g = green;
        this.b = blue;

        dr = 0;
        dg = 0;
        db = 0;
    }

    /** Gets a Component to render a given command or result.
      * The resulting component has a background color dependent on
      * it's position in the list (see the constructor
      * documentation), the font associated with the list, and a text
      * alignment based on whether the entry is a command or result
      * (whether the entry is an even-numbered or odd-numbered one).
      *
      * Additionally, this method caches the generated components. If
      * the index is beyond the length of the list of chached
      * components, or if that entry in the cache list is empty
      * (returns null), a new component is created and inserted into
      * the list at that index. Then, that list entry is fetched and
      * returned for that and all subsequent calls. Without this
      * caching, I've found that performance is so bad that
      * scrolling is laggy even on my 2 GHz Athlon.
      */
    public Component getListCellRendererComponent(JList list,
                                                  Object data,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean isFocused
                                                 ) {
        if (components.size() <= index
                || components.get(index) == null)
            components.add(index, new MathComponent(r + index*dr,
                                                    g + index*dg,
                                                    b + index*db,
                                                    (String)data,
                                                    list.getFont(),
                                                    index % 2 == 1));
        return (Component)components.get(index);
    }
}

/** A MathComponent is responsible for displaying an entry in a list
  * displayed by a MathList. The text rendering code in this class is
  * heavily based on the code from Stephen Kelvin's blog:
  * http://weblogs.java.net/blog/skelvin/archive/2004/08/big_severe_logg.html
  * Thanks, Stephen! :-)
  */
class MathComponent extends JComponent {
	/** Used to paint the colored background.
	  */
	Color backgroundColor;

    /** The ascent of the text; this is the distance of the baseline
      * of the text from the top of the component. In short, this is
      * the distance from the top of the component at which the text
      * is rendered.
      */
	float ascent;

	/** Stores the shape of the text to be displayed onscreen.
	  */
	GlyphVector text;

    /** If true, text is aligned right; if false, text is aligned
	  * left.
	  */
	boolean alignedRight;

    /** Buffer for component drawing.
      * Note that this object is not initialized until the Component
      * is first painted; a VolatileImage cannot be created until its
      * associated Component is displayed. This is a quirk of Java
      * that slightly complicates the code, but not by much, though.
      */
	VolatileImage buffer;

    /** Creates a MathComponent.
      * The resulting object has a "background color" determined by
      * r, g, and b, displays the String text with Font f, aligns it
      * right if isAlignedRight is true, and aligns it left if
      * isAlignedRight is false. ("Background color" is in quotes
      * because that color is used in a background gradient instead
      * of filling the component's entire background.)
      */
    public MathComponent(int r,
                         int g,
                         int b,
                         String textString,
                         Font f,
						 boolean isAlignedRight) {
		super();

		r = bound(r);
		g = bound(g);
		b = bound(b);

		setupText(textString, f, isAlignedRight);

        setMinimumSize(new Dimension(
                          (int)text.getLogicalBounds().getWidth(),
                          (int)text.getLogicalBounds().getHeight()));
        setPreferredSize(new Dimension(
                          (int)text.getLogicalBounds().getWidth(),
                          (int)text.getLogicalBounds().getHeight()));

		setupBackground(r, g, b);
	}

    /** Returns the input value constrained to the range [0, 255]
      * (inclusive). Useful because if a value outside that range is
      * used to create a Color, an IllegalArgumentException results.
      */
	protected static int bound(int x) {
		if (x < 0)
			return 0;
		if (x > 255)
			return 255;
		return x;
	}

    /** Initializes the ascent, GlyphVector object, and alignedRight
      * values.
      */
	protected void setupText(String textString,
                             Font f,
                             boolean isAlignedRight) {
        FontRenderContext frc = new FontRenderContext(null,
                                                      false,
                                                      false);

        ascent = new TextLayout(textString, f, frc).getAscent();
        text = f.createGlyphVector(frc, textString);
        alignedRight = isAlignedRight;
	}

    /** Initializes the GradientPaint and marks this Component as
      * opaque.
	  */
    protected void setupBackground(int r, int g, int b) {
        backgroundColor = new Color(r, g, b);
        setOpaque(true);
    }

    /** Overrides setOpaque() in JComponent.
      * Throws an IllegalArgumentException if a client tries to call
      * setOpaque(false).
      */
    public void setOpaque(boolean b) {
        if (b)
            super.setOpaque(true);
        else
            throw new IllegalArgumentException(
                                    "MathComponents must be opaque");
    }

    /** Paints the component.
      * The image painted by a Component is first rendered to a
      * VolatileImage, and rendered to screen from there as needed.
      * This is probably an unnecessary performance hack, but I'm
      * doing a scrolling effect when new commands are
      * entered (think AOL Instant Messager when someone types a
      * new message); doing this requires repainting these
      * Components frequently, in which case having the Components
      * do their own buffering will be very handy.
      *
      * Buffer is re-instantiated if the Component's size has changed
      * since the last call to paint, and the image is re-rendered.
      * If the buffer has lost its contents (something, usually some
      * Windows app, has managed to wipe the contents of video
      * memory), it is validated (and re-instantiated if necessary)
      * and re-rendered. Then the buffer is rendered to the screen.
      */
    public void paint(Graphics g) {
        if (buffer == null || buffer.getWidth()  != getWidth()
                           || buffer.getHeight() != getHeight()) {
            buffer = createVolatileImage(getWidth(), getHeight());
            drawBuffer();
        }

        if (buffer.contentsLost()) {
            if (buffer.validate(getGraphicsConfiguration())
                                 == VolatileImage.IMAGE_INCOMPATIBLE)
                buffer = createVolatileImage(getWidth(),
                                             getHeight());
            drawBuffer();
        }

        g.drawImage(buffer, 0, 0, this);
    }

    /** Draws the Component's image to the buffer for later rendering
      * to the screen.
      */
    public void drawBuffer() {
        Graphics2D g2d = buffer.createGraphics();

        if (alignedRight) {
            g2d.setPaint(new GradientPaint(new Point(0, 0),
                                           backgroundColor,
                                           new Point(getWidth(), 0),
                                           Color.WHITE));
            g2d.fill(new Rectangle(0, 0, getWidth(), getHeight()));
        } else {
            g2d.setPaint(new GradientPaint(new Point(0, 0),
                                           Color.WHITE,
                                           new Point(getWidth(), 0),
                                           backgroundColor));
            g2d.fill(new Rectangle(0, 0, getWidth(),
                                   getHeight()));
        }
        g2d.setPaint(Color.BLACK);
        if (alignedRight)
            g2d.drawGlyphVector(text,
                            (float)(getWidth()
                               - text.getLogicalBounds().getWidth()),
                            ascent);
        else
            g2d.drawGlyphVector(text, 0, ascent);
    }
}
