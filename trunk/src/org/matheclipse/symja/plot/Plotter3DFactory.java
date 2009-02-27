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

package org.matheclipse.symja.plot;

import javax.media.j3d.*;
import javax.vecmath.*;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Font;

import java.util.*;

/** Utility class for 3D plots.
  */
public class Plotter3DFactory {
    /** The appearance of opaque plots.
      */
    protected static Appearance opaqueAppearance;
    /** The appearance of transparent plots.
      */
    protected static Appearance transparentAppearance;
    /** Represents a plot with a blue-green color.
      */
    public static final int BLUE_GREEN = 0;
    /** Represents a plot with a red-yellow color.
      */
    public static final int RED_YELLOW = 1;
    /** Represents a plot with a purple-pink color.
      */
    public static final int PURPLE_PINK = 2;
    /** The width and height, in pixels (or is that texels? I don't
      * know!), of the grid tiles.
      */
    public static final int IMAGE_SIZE = 8;
    /** The number of points calculated along one edge of the plot.
      */
    protected static int resolution = 250;
    /** Should be removed. Antialiasing is incredibly slow, and
      * should never be enabled in interactive view.
      */
    protected static boolean antialiasing;
    /** Whether or not plots should be rendered transparent.
      */
    protected static boolean isTransparent = false;
    /** The font for numbers and labels in the 3D environment.
      */
    protected static Font3D f3d = new Font3D(new Font("SansSerif",
                                                      Font.PLAIN,
                                                      1),
                                             new FontExtrusion());

    /** Sets up appearance objects.
      */
    static {
        opaqueAppearance = new Appearance();

        opaqueAppearance.setPolygonAttributes(new PolygonAttributes(
                                      PolygonAttributes.POLYGON_FILL,
                                      PolygonAttributes.CULL_NONE,
                                      0f,
                                      true));

        opaqueAppearance.setMaterial(new Material(
                                            new Color3f(0.3f,
                                                        0.3f,
                                                        0.3f),
                                            new Color3f(),
                                            new Color3f(.6f, .6f ,.6f),
                                            new Color3f(1f, 1f, 1f), 64f));

        //opaqueAppearance.setTexture(createTexture());

        opaqueAppearance.setTextureAttributes(new TextureAttributes(
                                          TextureAttributes.DECAL,
                                          new Transform3D(),
                                          new Color4f(),
                                          TextureAttributes.NICEST));
        transparentAppearance =
               (Appearance)opaqueAppearance.cloneNodeComponent(true);
        transparentAppearance.setTransparencyAttributes(
            new TransparencyAttributes(TransparencyAttributes.NICEST,
                                       0.5f));
    }

    /** Creates the grid texture for the plot appearances.
      */
    protected static Texture createTexture() {
        BufferedImage bi = new BufferedImage(IMAGE_SIZE,
                                             IMAGE_SIZE,
                                             BufferedImage
                                                     .TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();

        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, 0, 0, IMAGE_SIZE - 1);
        g2d.drawLine(0, 0, IMAGE_SIZE - 1, 0);
        g2d.drawLine(IMAGE_SIZE - 1,
                     0,
                     IMAGE_SIZE - 1,
                     IMAGE_SIZE - 1);
        g2d.drawLine(0,
                     IMAGE_SIZE - 1,
                     IMAGE_SIZE - 1,
                     IMAGE_SIZE - 1);

        Texture texture = new Texture2D(Texture2D.BASE_LEVEL,
                                        Texture2D.RGBA,
                                        IMAGE_SIZE,
                                        IMAGE_SIZE);
        ImageComponent2D image = new ImageComponent2D(
                                          ImageComponent.FORMAT_RGBA,
                                          IMAGE_SIZE,
                                          IMAGE_SIZE);
        image.set(bi);
        texture.setImage(0, image);

        return texture;
    }

    /** Returns the resolution.
      */
    public static int getResolution() {
        return resolution;
    }

    /** Sets the resolution.
      */
    public static void setResolution(int in) {
        resolution = in;
    }

    /** Returns the opaque appearance.
      */
    public static Appearance getOpaqueAppearance() {
        return opaqueAppearance;
    }

    /** Returns the transparent appearance.
      */
    public static Appearance getTransparentAppearance() {
        return transparentAppearance;
    }

    /** Returns whether plots should be rendered transparent.
      */
    public static boolean getTransparent() {
        return isTransparent;
    }

    /** Sets whether plots should be rendered transparent.
      */
    public static void setTransparent(boolean transparency) {
        isTransparent = transparency;
    }

    /** Gets the 3D font.
      */
    public static Font3D getFont() {
        return f3d;
    }
}
