/*
 * Copyright (c) 2005, William Tracy (afishionado@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.matheclipse.symja;

import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.*;

/** Launches the ScrollStarter runnable.
  * ScrollStarter smoothly scrolls down to the bottom of the scroll
  * pane. The ScrollStarter is launched via EventQueue.invokeLater()
  * because otherwise if it is called in the same method that adds
  * to the contents of the scroll pane, the event will not have
  * trickled up to change the bounds of the scroll pane, and it
  * may programmatically appear to already be scrolled to the bottom.
  *
  * @author William Tracy, 22/06/05
  */
public class Scroller {
    /** Starts the vertical scrolling action.
      */
    protected Runnable starter;

    /** Starts the horizontal scrolling action.
      */
    protected Runnable sideStarter;

    /** Controls whether or not the horizontal scrolling action is
      * activated.
      */
    protected boolean doesSideScroll = true;

    /** Creates a scroller that scrolls the passed pane.
      * @param pane the JScrollPane that should be scrolled
      */
    public Scroller(JScrollPane pane) {
        starter = new ScrollStarter(pane.getVerticalScrollBar());
        sideStarter = new ScrollStarter(
                                      pane.getHorizontalScrollBar());
    }

    /** Scrolls to the bottom of the pane. Scrolls to the right edge
      * if doesSideScroll is true.
      */
    public void scrollToEnd() {
        EventQueue.invokeLater(starter);
        if (doesSideScroll)
            EventQueue.invokeLater(sideStarter);
    }

    /** Returns true if panel scrolls to the right when scrollToEnd()
      * is called.
      * @return true if the scroller scrolls horizontally, false otherwise
      */
    public boolean doesSideScroll() {
        return doesSideScroll();
    }

    /** Changes whether the panel scrolls to the right when
      * scrollToEnd() is called.
      * @param doesScroll whether the Scroller should scroll horizontally
      */
    public void setDoesSideScroll(boolean doesScroll) {
        doesSideScroll = doesScroll;
    }
}

/** Uses a Swing Timer to move the scrollbar at regular intervals.
  */
class ScrollStarter implements Runnable, ActionListener {
    /** The rate at which the scroll bar accelerates and decelerates.
      * There's not particular reason for it being public.
      */
    public static final int ACCELERATION = 1;

    /** The point at which the scroll bar starts.
      */
    protected int start;

    /** The current speed of the sroll bar.
      */
    protected int speed;

    /** The point at which the scroll bar stops.
      */
    protected int end;

    /** The point at which the scroll bar stops accelerating and
      * starts decelerating.
      */
    protected int midPoint;

    /** The scroll bar.
      */
    protected JScrollBar scrollee;

    /** Triggers the motion events at regular intervals.
      */
    protected Timer timer;


    /** Creates a ScrollStarter that acts on scrollee.
      * @param scrolle the scroll bar to scroll
      */
    public ScrollStarter(JScrollBar scrollee) {
        this.scrollee = scrollee;
        timer = new Timer(30, this);
    }

    /** Computes starting,ending, and midpoint positions and initial
      * speed, then launches the timer than runs ScrollAction.
      */
    public void run() {
        start = scrollee.getValue();
        speed = ACCELERATION;
        end = scrollee.getMaximum() - scrollee.getModel()
                                                        .getExtent();
        midPoint = start + (end - start) / 2;

        timer.restart();
    }

    /** Moves the scroll bar. The scroll bar accelerates by
      * ACCELERATION each update until it reaches the mid point; then
      * it decelerates by ACCELERATION each update until it reaches
      * its final position.
      * @param e the event that triggered the update
      */
 
    public void actionPerformed(ActionEvent e) {
        scrollee.setValue(scrollee.getValue() + speed);
        if (scrollee.getValue() <= midPoint) {
            speed+= ACCELERATION;
        } else {
            speed-=ACCELERATION;
        }

        /* Cheap hack: Sometimes the scrollbar start decelerating
        too soon due to rounding errors, and this bit of code
        keeps it from prematurely stopping completely.*/
        if (speed <= 0) {
            speed = ACCELERATION;
        }

        if (scrollee.getValue() >= end)
            timer.stop();
    }
}
