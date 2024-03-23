import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class a5 extends PApplet {
    // TODO COPY FROM LINE BELOW ----------------------------------------------------------------------
    public static class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "{" + x + "f, " + y + "f}";
        }

        public float calculateDistance(Point p2) {
            float dx = p2.x - this.x;
            float dy = p2.y - this.y;
            return (float) Math.sqrt((dx * dx) + (dy * dy));
        }

        public Point interpolate(float threshold, Point p2) {
            float fraction = threshold / calculateDistance(p2);

            float newX = this.x + (fraction * (p2.x - this.x));
            float newY = this.y + (fraction * (p2.y - this.y));

            return new Point(newX, newY);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    public class Gesture {
        public ArrayList<Point> rawPoints = new ArrayList<>();
        ArrayList<Point> resampledPoints = new ArrayList<>();
        ArrayList<Point> resampledPointsInOrigin = new ArrayList<>();
        ArrayList<Point> resampledPointsOriginRescaled = new ArrayList<>();
        BoundingBox boundingBox;

        public void draw() {
            for (Point p : rawPoints) {
                noStroke();
                fill(255, 255, 0);
                ellipse(p.x, p.y, 10, 10);
            }

//            for (Point p : resampledPoints) {
//                fill(255, 0, 0);
//                ellipse(p.x, p.y, 10, 10);
//            }

//            for (Point p : resampledPointsInOrigin) {
//                fill(255, 0, 0);
//                ellipse(p.x, p.y, 10, 10);
//            }
//
//            for (Point p : resampledPointsOriginRescaled) {
//                fill(0, 0, 255);
//                ellipse(p.x, p.y, 10, 10);
//            }
//
//            if (boundingBox != null) {
//                noFill();
//                stroke(255, 0, 0);
//                rect(boundingBox.left, boundingBox.top, boundingBox.right - boundingBox.left,
//                        boundingBox.bottom - boundingBox.top);
//            }
        }

        public void resamplePoints() {
            float threshold = getGestureLength() / 64f;
            float currentPathLength = 0;

            for (int i = 0; i < rawPoints.size(); i++) {
                if (i == 0) {
                    resampledPoints.add(rawPoints.get(0));
                }
                // If it's not the first point, calculate the distance
                if (i != 0) {
                    Point currentPoint = rawPoints.get(i);
                    Point previousPoint = rawPoints.get(i - 1);
                    float distance = previousPoint.calculateDistance(currentPoint);

                    if (currentPathLength + distance > threshold) {
                        float thresholdMinusPathLength = threshold - currentPathLength;
                        Point interpolated = previousPoint.interpolate(thresholdMinusPathLength, currentPoint);
                        rawPoints.add(rawPoints.indexOf(currentPoint), interpolated);
                        resampledPoints.add(interpolated);
                        currentPathLength = 0;
                    }
                    else {
                        currentPathLength += previousPoint.calculateDistance(currentPoint);
                    }

                }
            }
            // Gets the resampled Point with the minimum x value, and gets its float x value
            float minX = Collections.min(resampledPoints, Comparator.comparing(Point::getX)).getX();
            float maxX = Collections.max(resampledPoints, Comparator.comparing(Point::getX)).getX();
            float minY = Collections.min(resampledPoints, Comparator.comparing(Point::getY)).getY();
            float maxY = Collections.max(resampledPoints, Comparator.comparing(Point::getY)).getY();

            boundingBox = new BoundingBox(minX, minY, maxX, maxY);

            // Moves all the resampledPoints to the origin of the canvas (0, 0)
            for (Point p : resampledPoints) {
                resampledPointsInOrigin.add(new Point(p.x - minX, p.y - minY));
            }

            // Gets the max x and y for the resampled Points in origin
            float maxXOrigin = Collections.max(resampledPointsInOrigin, Comparator.comparing(Point::getX)).getX();
            float maxYOrigin = Collections.max(resampledPointsInOrigin, Comparator.comparing(Point::getY)).getY();
            for (Point p : resampledPointsInOrigin) {
                resampledPointsOriginRescaled.add(new Point(p.x * (400 / maxXOrigin), (p.y) * (400 / maxYOrigin)));
            }
        }

        public float getGestureLength() {
            float length = 0;
            for (int i = 0; i < rawPoints.size() - 1; i++) {
                Point p1 = rawPoints.get(i);
                Point p2 = rawPoints.get(i + 1);
                length += p1.calculateDistance(p2);
            }
            return length;
        }

        @Override
        public String toString() {
            return rawPoints.toString();
        }
    }

    public class BoundingBox {
        float left, right, top, bottom;

        public BoundingBox(float left, float top, float right, float bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }

    ArrayList<Gesture> gestures = new ArrayList<>();
    Gesture currentGesture;
    Gesture circleGesture;
    Gesture squareGesture;
    Gesture alphaGesture;
    float[][] circleTemplate = {{197.4359f, 0.0f}, {175.87047f, 0.0f}, {154.92166f, 1.6587176f},
            {136.97968f, 7.3655047f}, {119.48872f, 14.285715f}, {104.410774f, 23.656572f}, {87.970795f, 33.403976f},
            {71.219734f, 46.355156f}, {56.668484f, 57.14286f}, {46.343136f, 71.21765f}, {30.769232f, 84.71218f},
            {20.512821f, 97.31363f}, {12.820513f, 116.11956f}, {7.286307f, 137.59526f}, {2.5641026f, 157.7721f}, {2.5641026f, 181.80211f}, {0.0f, 204.64867f}, {0.0f, 228.67868f}, {2.9484286f, 251.85683f}, {7.692308f, 272.02362f}, {12.820513f, 292.01303f}, {19.525812f, 308.57144f}, {30.769232f, 325.0941f}, {43.589745f, 341.5331f}, {54.061306f, 357.38266f}, {69.9655f, 368.57144f}, {85.8792f, 375.69397f}, {103.27982f, 382.85715f}, {121.76322f, 387.10754f}, {141.72238f, 391.4286f}, {160.7237f, 394.28574f}, {179.72499f, 397.14285f}, {201.29044f, 397.14285f}, {222.85588f, 397.14285f}, {243.35922f, 400.0f}, {264.92468f, 400.0f}, {286.4901f, 400.0f}, {303.36728f, 391.4286f}, {319.57202f, 381.04834f}, {332.43332f, 365.7143f}, {343.58975f, 352.44202f}, {356.82248f, 339.54068f}, {369.23077f, 327.9324f}, {381.55322f, 314.28574f}, {392.3077f, 298.89188f}, {398.13242f, 279.2239f}, {400.0f, 256.05588f}, {400.0f, 232.02585f}, {400.0f, 207.99583f}, {397.4359f, 185.14928f}, {392.3077f, 165.15987f}, {387.1795f, 145.17047f}, {379.48718f, 126.36453f}, {369.82385f, 111.42857f}, {358.97437f, 96.14062f}, {349.5907f, 78.115326f}, {339.70526f, 62.857143f}, {327.2782f, 50.395683f}, {311.58917f, 38.627975f}, {293.33615f, 29.717365f}, {275.82288f, 22.857143f}, {257.88364f, 17.142857f}, {238.44237f, 11.428572f}, {217.93903f, 8.571428f}};

    float[][] squareTemplate = {{2.5477707f, 12.82541f}, {2.5477707f, 40.765335f}, {0.0f, 67.377144f}, {0.0f,
            95.31707f}, {0.0f, 123.25699f}, {2.5477707f, 149.86879f}, {2.5477707f, 177.80872f}, {2.5477707f,
            205.74864f}, {2.5477707f, 233.68857f}, {2.5477707f, 261.6285f}, {2.5477707f, 289.56842f}, {2.5477707f,
            317.50836f}, {2.5477707f, 345.44827f}, {0.0f, 372.0601f}, {0.0f, 400.0f}, {20.514849f, 397.5877f}, {41.660656f, 394.38135f}, {62.20501f, 387.96866f}, {84.406105f, 387.96866f}, {105.55191f, 384.7623f}, {127.75304f, 384.7623f}, {149.95413f, 384.7623f}, {172.15521f, 384.7623f}, {194.35631f, 384.7623f}, {216.5574f, 384.7623f}, {238.7585f, 384.7623f}, {260.9596f, 384.7623f}, {283.16068f, 384.7623f}, {305.36176f, 384.7623f}, {327.56287f, 384.7623f}, {349.76395f, 384.7623f}, {371.96506f, 384.7623f}, {394.16614f, 384.7623f}, {400.0f, 364.16428f}, {400.0f, 336.22433f}, {400.0f, 308.28442f}, {397.45224f, 281.6726f}, {394.90445f, 255.0608f}, {392.3567f, 228.44899f}, {389.80893f, 201.83719f}, {384.82806f, 176.49371f}, {382.16562f, 149.9417f}, {377.07007f, 126.53624f}, {377.07007f, 98.59631f}, {374.5223f, 73.86275f}, {374.5223f, 45.922817f}, {374.5223f, 17.982893f}, {364.06265f, 3.2063525f}, {345.46466f, 9.619058f}, {323.26358f, 9.619058f}, {303.61026f, 12.82541f}, {281.40915f, 12.82541f}, {259.20807f, 12.82541f}, {237.00697f, 12.82541f}, {214.8059f, 12.82541f}, {193.66013f, 9.619058f}, {171.45903f, 9.619058f}, {151.80571f, 6.412705f}, {130.65991f, 3.2063525f}, {108.45877f, 3.2063525f}, {86.25768f, 3.2063525f}, {64.056595f, 3.2063525f}, {41.8555f, 3.2063525f}, {22.202179f, 0.0f}, {0.001088525f, 0.0f}};

    float[][] alphaTemplate = {{398.1664f, 0.0f}, {386.9674f, 16.528927f}, {378.00607f, 34.05676f}, {367.9259f,
            48.180447f}, {357.84576f, 64.240616f}, {345.24554f, 82.80448f}, {335.16537f, 102.737656f}, {324.15482f,
            120.22877f}, {312.2397f, 139.1648f}, {298.5426f, 157.13264f}, {283.80164f, 176.46979f}, {269.64435f,
            196.12402f}, {257.14557f, 214.74297f}, {245.9685f, 236.0166f}, {230.4895f, 253.01614f}, {218.57437f, 271.95218f}, {205.9211f, 288.55072f}, {190.12463f, 305.96674f}, {173.8828f, 318.6789f}, {155.5909f, 323.96695f}, {134.74403f, 323.96695f}, {113.89717f, 323.96695f}, {93.05031f, 323.96695f}, {73.24729f, 320.66116f}, {55.96431f, 314.0496f}, {38.248966f, 304.13223f}, {27.720446f, 284.78723f}, {17.640284f, 262.91757f}, {10.080162f, 239.6786f}, {5.040081f, 215.07034f}, {0.0f, 189.87321f}, {0.0f, 162.52634f}, {0.27270702f, 135.53719f}, {6.76215f, 113.44342f}, {16.895376f, 93.5391f}, {28.822271f, 77.89347f}, {39.001488f, 62.809917f}, {50.40081f, 48.480225f}, {65.12676f, 36.88087f}, {85.21541f, 33.057854f}, {103.54223f, 29.752068f}, {124.389084f, 29.752068f}, {144.03107f, 33.56782f}, {160.43124f, 42.97521f}, {173.8828f, 56.549324f}, {183.96295f, 72.60949f}, {196.5484f, 89.2562f}, {206.68593f, 109.14685f}, {216.72348f, 129.10318f}, {226.21432f, 151.29305f}, {237.39139f, 172.56667f}, {249.48401f, 193.34283f}, {262.3997f, 212.3979f}, {275.85947f, 232.94638f}, {289.1359f, 253.66817f}, {301.05103f, 272.6042f}, {311.68927f, 290.9091f}, {324.36447f, 307.43802f}, {335.44687f, 324.33627f}, {349.88202f, 339.96652f}, {365.35913f, 353.71902f}, {376.5581f, 370.24792f}, {389.23337f, 386.77686f}, {399.99997f, 400.0f}};


    @Override
    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);
        currentGesture = new Gesture();
        currentGesture.rawPoints.add(new Point(event.getX(), event.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        super.mouseDragged(event);
        currentGesture.rawPoints.add(new Point(event.getX(), event.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        super.mouseReleased(event);
        currentGesture.rawPoints.add(new Point(event.getX(), event.getY()));
        currentGesture.resamplePoints();
        gestures.add(currentGesture);
    }

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        circleGesture = new Gesture();
        squareGesture = new Gesture();
        alphaGesture = new Gesture();

        for (float[] coord : circleTemplate) {
            Point p = new Point(coord[0], coord[1]);
            circleGesture.rawPoints.add(p);
        }
        for (float[] coord : squareTemplate) {
            Point p = new Point(coord[0], coord[1]);
            squareGesture.rawPoints.add(p);

        }
        for (float[] coord : alphaTemplate) {
            Point p = new Point(coord[0], coord[1]);
            alphaGesture.rawPoints.add(p);
        }

        circleGesture.resamplePoints();
        squareGesture.resamplePoints();
        alphaGesture.resamplePoints();
    }

    public void draw() {
        // Saved gestures
        for (Gesture g : gestures) {
            g.draw();
        }

        if (currentGesture != null) {
            currentGesture.draw();

            // Draws line in between points
            stroke(255, 255, 0);
            strokeWeight(1);
            for (int i = 0; i < currentGesture.rawPoints.size() - 1; i++) {
                Point p1 = currentGesture.rawPoints.get(i);
                Point p2 = currentGesture.rawPoints.get(i + 1);
                line(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }


    // TODO COPY UNTIL LINE ABOVE ---------------------------------------------------------------------
    public static void main(String[] args) {
        PApplet.main("a5");
    }
}
