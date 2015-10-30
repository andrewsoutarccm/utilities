/* Copyright (c) 2015 Andrew Soutar <andrew@andrewsoutar.com>
 *
 * See the COPYING file for details.
 */

package com.andrewsoutar.cmp128;

import java.io.IOException;
import java.util.LinkedList;

public class Utilities {
    /**
     * The type of program: lesson, lab, or homework.
     */
    public static enum ProgramType {
        LESSON,
        LAB,
        HOMEWORK
    }

    /**
     * Create a string comprised of a single repeating character
     *
     * @param chr the character to repeat
     * @param n the number of times to repeat it
     * @return a string comprised of chr repeated n times
     */
    private static String repeat(char chr, int n) {
        return (new String (new char [n]).replace ('\0', chr));
    }

    /**
     * Wrap an array of strings to a specified length at space boundaries
     *
     * @param lines an array of strings, each on its own line
     * @param width the maximum width of any string on its own line
     * @return an array of the strings from lines, each no longer than width
     */
    private static LinkedList <String> wrapLines (String [] lines, int width) {
        // Use a LinkedList because we don't know how many lines we well end up
        // having
        LinkedList <String> ret = new LinkedList <String> ();
        for (String line : lines) {
            if (line.length () > width) {
                // Add a space at the beginning because it matches any sequence
                // of up to 50 characters followed by a space
                String temp = (line + " ").
                    // The regex will look like, i.e. "(.{1,60})\s+" for a max
                    // width of 60 characters
                    replaceAll ("(.{1," + Integer.toString (width) + "})\\s+",
                                "$1\n");
                // The last character will now be a newline, so we lop it off.
                String [] broken =
                    temp.substring (0, temp.length() - 1).split("\n");
                for (String brokenLine : broken) {
                    ret.add (brokenLine);
                }
            } else {
                ret.add (line);
            }
        }
        return (ret);
    }
    
    /**
     * Find the length of the longest string in an array of strings
     *
     * @param lines an array of strings, each intended to go on its own line
     * @return the length of the longest element of lines
     */
    static int longestLine (LinkedList <String> lines) {
        int longest = 0;
        for (String line : lines) {
            longest = Math.max(line.length(), longest);
        }
        return (longest);
    }

    /**
     * Pad a string to a specified width using spaces
     *
     * @param str the string to pad
     * @param width the width to which we should pad
     * @return str followed by enough spaces that it is width wide
     */
    static String pad (String str, int width) {
        return (str + repeat (' ', width - str.length ()));
    }

    /**
     * Center a string
     *
     * @param str the string to center
     * @param width the width of the line on which to center the string
     * @return str centered within spaces so that it is width wide
     */
    static String centerString (String str, int width) {
        // This float represents the exact number of spaces necessary on each
        // side of the string. It may be a fraction if the total amount by which
        // to pad turns out to be odd.
        float padding = ((float) (width - str.length ())) / 2;

        // Use floor and ceil so that the "extra" space goes on the right of the
        // string
        return (repeat (' ', (int) Math.floor (padding)) +
                str +
                repeat (' ', (int) Math.ceil (padding)));
    }
    
    /**
     * Print Prof. Tirrito's CMP128 header
     *
     * @param type a ProgramType indicating the type of program
     * @param lessonNumber the lesson number
     * @param lessonName the name of the lesson
     * @param width the width of the screen, 60 by default
     */
    public static void printHeader (ProgramType type, int lessonNumber,
                                    String lessonName, int width) {
        String typeString = "";
        switch (type) {
        case LESSON:
            typeString = "Lesson";
            break;
        case LAB:
            typeString = "Lab";
            break;
        case HOMEWORK:
            typeString = "Homework";
        }

        String [] lines = {
            "CMP128-80229",
            "Computer Science I",
            "County College of Morris",
            "Fall 2015 - Prof. Tirrito",
            typeString + " #" + lessonNumber + ": " +
            lessonName,
        };

        // Wrap the lines to the appropriate width
        LinkedList <String> linesWrapped = wrapLines (lines, width);

        // Find the longest one, used to generate the width of the header
        int longestLine = longestLine (linesWrapped);

        // Make the underscore border extend one underscore further than the
        // longest line, but no further than width
        String underscoreBorder =
            centerString (repeat ('_', Math.min (longestLine + 2, width)),
                          width);

        System.out.println (underscoreBorder); // Top border
        for (String line : linesWrapped) { // Print header
            System.out.println (centerString (line, width));
        }
        System.out.println (underscoreBorder); // Bottom border

        // Print one last newline to separate the header
        System.out.println(); // Separator
    }
    public static void printHeader (ProgramType type, int lessonNumber,
                                    String lessonName) {
        printHeader (type, lessonNumber, lessonName, 60);
    }

    /**
     * Print a message wrapped in an asterisk border
     *
     * @param lines an array of strings, each one representing one line
     * @param borderChr the border character, '*' by default
     * @param width the width of the message box
     */
    public static void printBordered (String [] lines, char borderChr, int width) {
        // Leave two characters on each side for the border
        int contentWidth = width - 4;

        LinkedList <String> linesWrapped = wrapLines (lines, contentWidth);

        String topBottomBorder = repeat (borderChr, width);

        System.out.println (topBottomBorder); // Top border
        for (String line : linesWrapped) { // Content
            System.out.println (borderChr + " " + pad (line, contentWidth)
                                + " " + borderChr);
        }
        System.out.println (topBottomBorder); // Bottom border

        // Print one last newline as a separator
        System.out.println ();
    }
    public static void printBordered (String [] lines, char borderChr) {
        printBordered (lines, borderChr, 60);
    }
    public static void printBordered (String [] lines, int width) {
        printBordered (lines, '*', width);
    }
    public static void printBordered (String[] lines) {
        printBordered (lines, '*', 60);
    }

    public static void clearScreen () {
        final String ANSI_CLR = "\u001b[2J";
        if (System.getProperty ("os.name").contains ("Windows")) {
            try {
                new ProcessBuilder ("cmd", "/c", "cls")
                    .inheritIO ().start ().waitFor ();
            } catch (IOException e) {
                System.out.print (ANSI_CLR);
                System.out.flush ();
            } catch (Exception e) {}
        } else {
            System.out.print (ANSI_CLR);
            System.out.flush ();
        }
    }
}
