import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


import static org.junit.Assert.*;

public class DPwithImageTest {
    private SeamCarving sc;
    private int height, width;
    private String fileName = "/home/b351-sandbox/prod/work-area/3/tests" + "/A6/";

    @Before
    public void setUp() throws IOException {
        width = 900;
        height = 600;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // IU Flag
        g2d.setColor(new Color(153, 0, 0));
        g2d.fillRect(0, 0, width, height);

        // create a circle with black
        g2d.setColor(Color.white);
        g2d.fillRect(412, 75, 60, 450);

        g2d.setColor(Color.white);
        g2d.fillRect(392, 75, 100, 30);

        g2d.setColor(Color.white);
        g2d.fillRect(265, 325, 350, 60);

        g2d.setColor(Color.white);
        g2d.fillRect(392, 500, 100, 30);

        g2d.setColor(Color.white);
        g2d.fillRect(265, 185, 60, 200);

        g2d.setColor(Color.white);
        g2d.fillRect(244, 185, 100, 30);

        g2d.setColor(Color.white);
        g2d.fillRect(555, 185, 60, 200);

        g2d.setColor(Color.white);
        g2d.fillRect(534, 185, 100, 30);

        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();

        // Save as JPEG
        File file = new File("myimage.jpg");
        ImageIO.write(bufferedImage, "jpg", file);

        sc = new SeamCarving();
        sc.readImage("myimage.jpg");
    }

    @Test(timeout = 4000)
    @TestCase(score = 1, author = "Amr Sabry / Phuc Pham")// Modify Amr's test code from distribution
    public void getHVneighbors() throws IOException {

        ArrayList<Position> ns;
        ns = sc.getHVneighbors(0, 0);
        assertTrue("Could not find neighbor to the south, Method: getHVneighbors()", ns.contains(new Position(1, 0)));
        assertTrue("Could not find neighbor to the east, Method: getHVneighbors()", ns.contains(new Position(0, 1)));
        assertEquals(2, ns.size());

        ns = sc.getHVneighbors(0, width / 2);
        assertTrue("Could not find neighbor to the west, Method: getHVneighbors()", ns.contains(new Position(0, width / 2 - 1)));
        assertTrue("Could not find neighbor to the east, Method: getHVneighbors()", ns.contains(new Position(0, width / 2 + 1)));
        assertTrue("Could not find neighbor to the south, Method: getHVneighbors()", ns.contains(new Position(1, width / 2)));
        assertEquals(3, ns.size());

        ns = sc.getHVneighbors(0, width - 1);
        assertTrue("Could not find neighbor to the west, Method: getHVneighbors()", ns.contains(new Position(0, width - 2)));
        assertTrue("Could not find neighbor to the south, Method: getHVneighbors()", ns.contains(new Position(1, width - 1)));
        assertEquals(2, ns.size());

        ns = sc.getHVneighbors(height / 2, 0);
        assertTrue("Could not find neighbor to the north, Method: getHVneighbors()", ns.contains(new Position(height / 2 - 1, 0)));
        assertTrue("Could not find neighbor to the south, Method: getHVneighbors()", ns.contains(new Position(height / 2 + 1, 0)));
        assertTrue("Could not find neighbor to the east, Method: getHVneighbors()", ns.contains(new Position(height / 2, 1)));
        assertEquals(3, ns.size());

        ns = sc.getHVneighbors(height / 2, width / 2);
        assertTrue("Could not find neighbor to the north, Method: getHVneighbors()", ns.contains(new Position(height / 2 - 1, width / 2)));
        assertTrue("Could not find neighbor to the south, Method: getHVneighbors()", ns.contains(new Position(height / 2 + 1, width / 2)));
        assertTrue("Could not find neighbor to the west, Method: getHVneighbors()", ns.contains(new Position(height / 2, width / 2 - 1)));
        assertTrue("Could not find neighbor to the east, Method: getHVneighbors()", ns.contains(new Position(height / 2, width / 2 + 1)));
        assertEquals(4, ns.size());

        ns = sc.getHVneighbors(height / 2, width - 1);
        assertTrue("Could not find neighbor to the west, Method: getHVneighbors()", ns.contains(new Position(height / 2, width - 2)));
        assertTrue("Could not find neighbor to the north, Method: getHVneighbors()", ns.contains(new Position(height / 2 - 1, width - 1)));
        assertTrue("Could not find neighbor to the south, Method: getHVneighbors()", ns.contains(new Position(height / 2 + 1, width - 1)));
        assertEquals(3, ns.size());

        ns = sc.getHVneighbors(height - 1, 0);
        assertTrue("Could not find neighbor to the north, Method: getHVneighbors()", ns.contains(new Position(height - 2, 0)));
        assertTrue("Could not find neighbor to the east, Method: getHVneighbors()", ns.contains(new Position(height - 1, 1)));
        assertEquals(2, ns.size());

        ns = sc.getHVneighbors(height - 1, width / 2);
        assertTrue("Could not find neighbor to the north, Method: getHVneighbors()", ns.contains(new Position(height - 2, width / 2)));
        assertTrue("Could not find neighbor to the west, Method: getHVneighbors()", ns.contains(new Position(height - 1, width / 2 - 1)));
        assertTrue("Could not find neighbor to the east, Method: getHVneighbors()", ns.contains(new Position(height - 1, width / 2 + 1)));
        assertEquals(3, ns.size());

        ns = sc.getHVneighbors(height - 1, width - 1);
        assertTrue("Could not find neighbor to the north, Method: getHVneighbors()", ns.contains(new Position(height - 2, width - 1)));
        assertTrue("Could not find neighbor to the west, Method: getHVneighbors()", ns.contains(new Position(height - 1, width - 2)));
        assertEquals(2, ns.size());
    }

    @Test(timeout = 2000)
    @TestCase(score = 1, author = "Amr Sabry / Phuc Pham")// Modify Amr's test code from distribution
    public void getBelowNeighbors() {
        ArrayList<Position> ns;

        ns = sc.getBelowNeighbors(0, 0);
        assertTrue("Could not find neighbor to the south, Method: getBelowNeighbors()", ns.contains(new Position(1, 0)));
        assertTrue("Could not find neighbor to the southeast, Method: getBelowNeighbors()", ns.contains(new Position(1, 1)));
        assertEquals(2, ns.size());

        ns = sc.getBelowNeighbors(0, width / 2);
        assertTrue("Could not find neighbor to the southwest, Method: getBelowNeighbors()", ns.contains(new Position(1, width / 2 - 1)));
        assertTrue("Could not find neighbor to the south, Method: getBelowNeighbors()", ns.contains(new Position(1, width / 2)));
        assertTrue("Could not find neighbor to the southeast, Method: getBelowNeighbors()", ns.contains(new Position(1, width / 2 + 1)));
        assertEquals(3, ns.size());

        ns = sc.getBelowNeighbors(0, width - 1);
        assertTrue("Could not find neighbor to the southwest, Method: getBelowNeighbors()", ns.contains(new Position(1, width - 2)));
        assertTrue("Could not find neighbor to the south, Method: getBelowNeighbors()", ns.contains(new Position(1, width - 1)));
        assertEquals(2, ns.size());

        ns = sc.getBelowNeighbors(height / 2, 0);
        assertTrue("Could not find neighbor to the south, Method: getBelowNeighbors()", ns.contains(new Position(height / 2 + 1, 0)));
        assertTrue("Could not find neighbor to the southeast, Method: getBelowNeighbors()", ns.contains(new Position(height / 2 + 1, 1)));
        assertEquals(2, ns.size());

        ns = sc.getBelowNeighbors(height / 2, width / 2);
        assertTrue("Could not find neighbor to the southwest, Method: getBelowNeighbors()", ns.contains(new Position(height / 2 + 1, width / 2 - 1)));
        assertTrue("Could not find neighbor to the south, Method: getBelowNeighbors()", ns.contains(new Position(height / 2 + 1, width / 2)));
        assertTrue("Could not find neighbor to the southeast, Method: getBelowNeighbors()", ns.contains(new Position(height / 2 + 1, width / 2 + 1)));
        assertEquals(3, ns.size());

        ns = sc.getBelowNeighbors(height / 2, width - 1);
        assertTrue("Could not find neighbor to the southwest, Method: getBelowNeighbors()", ns.contains(new Position(height / 2 + 1, width - 2)));
        assertTrue("Could not find neighbor to the south, Method: getBelowNeighbors()", ns.contains(new Position(height / 2 + 1, width - 1)));
        assertEquals(2, ns.size());

        for (int w = 0; w < width; w++) {
            ns = sc.getBelowNeighbors(height - 1, w);
            assertTrue("there are below neighbors when there should be none", ns.isEmpty());
        }
    }

    @Test(timeout = 2000)
    @TestCase(score = 1, author = "Phuc Pham")
    public void computeEnergy() {
        String failMessage = "Fail to compute Energy: remember the equation = square(c1.getRed() - c2.getRed()) + square(c1.getGreen() - c2.getGreen()) + square(c1.getBlue() - c2.getBlue())";
        assertEquals(failMessage, 949, sc.computeEnergy(183, 249));
        assertEquals(failMessage, 9, sc.computeEnergy(174, 247));
        assertEquals(failMessage, 250289, sc.computeEnergy(105, 472));
        assertEquals(failMessage, 0, sc.computeEnergy(240, 178));
        assertEquals(failMessage, 140, sc.computeEnergy(178, 240));
        assertEquals(failMessage, 240, sc.computeEnergy(212, 239));
        assertEquals(failMessage, 18, sc.computeEnergy(177, 548));
        assertEquals(failMessage, 293, sc.computeEnergy(182, 598));
        assertEquals(failMessage, 80, sc.computeEnergy(543, 474));
        assertEquals(failMessage, 391, sc.computeEnergy(370, 613));
        assertEquals(failMessage, 44, sc.computeEnergy(223, 240));
        assertEquals(failMessage, 453, sc.computeEnergy(212, 616));
        assertEquals(failMessage, 15, sc.computeEnergy(224, 248));
        assertEquals(failMessage, 63, sc.computeEnergy(428, 474));
        assertEquals(failMessage, 11112, sc.computeEnergy(74, 492));
    }

    @Test(timeout = 10000)
    @TestCase(score = 2, author = "Phuc Pham")
    public void findSeam() throws IOException {
        try {
            String failMessage = "Could not find seam: Method findSeam()";
            Pair<List<Position>, Integer> r = sc.findSeam(height - 10, width / 2);
            assertEquals("incorrect cost", 0, (int) r.getSecond());
            List<Position> seam = r.getFirst();
            for (int i = 0; i < r.getFirst().length(); i++) {
                assertEquals(failMessage, new Position(590 + i, 450), seam.getFirst());
                seam = seam.getRest();
            }

            SeamCarving pp = new SeamCarving();
            pp.readImage(fileName + "winter-sun.jpg");
            r = pp.findSeam(0, 0);
            assertEquals("incorrect cost", 473717, (int) r.getSecond());

            assertEquals(failMessage, new Position(189, 28), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(351, 26), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(349, 26), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(129, 11), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(80, 4), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(210, 33), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(244, 27), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(48, 3), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(309, 31), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(394, 21), seam.getFirst());
        } catch (EmptyListE e) {
        }
    }

    @Test(timeout = 2000)
    @TestCase(score = 2, author = "Amr Sabry")
    public void seamSmall() throws IOException {
        String failMessage = "Could not find seam: Method findSeam()";
        SeamCarving sl = new SeamCarving();
        sl.readImage(fileName + "small-line.jpg");
        Pair<List<Position>, Integer> r = sl.findSeam(0, 0);
        assertEquals("incorrect cost", 0, (int) r.getSecond());
        try {
            List<Position> seam = r.getFirst();
            for (int i = 0; i < 21; i++) {
                assertEquals(failMessage, new Position(i, 0), seam.getFirst());
                seam = seam.getRest();
            }
        } catch (EmptyListE e) {
        }
    }

    @Test(timeout = 2000)
    @TestCase(score = 2, author = "Amr Sabry")
    public void seamBig() throws IOException {
        String failMessage = "Could not find seam: Method findSeam()";
        SeamCarving bs = new SeamCarving();
        bs.readImage(fileName + "balloon-sky.jpg");
        height = bs.getHeight();
        width = bs.getWidth();
        Pair<List<Position>, Integer> r = bs.findSeam(height - 10, width / 2);
        assertEquals("incorrect cost", 6, (int) r.getSecond());
        try {
            List<Position> seam = r.getFirst();
            assertEquals(failMessage, new Position(665, 450), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(666, 450), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(667, 450), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(668, 450), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(669, 450), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(670, 450), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(671, 450), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(672, 451), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(673, 451), seam.getFirst());
            seam = seam.getRest();
            assertEquals(failMessage, new Position(674, 451), seam.getFirst());
        } catch (EmptyListE e) {
        }
    }
}