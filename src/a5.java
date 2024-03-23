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
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
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
