import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class a3 extends PApplet {
    // TODO COPY FROM LINE BELOW ----------------------------------------------------------------------
    class Target {
        float x;
        float y;
        float zoomedOutX;
        float zoomedOutY;
        float diameter;
        boolean hit;
        boolean highlighted;

        Target(float x, float y, float diameter) {
            this.x = x;
            this.y = y;

            this.zoomedOutX = x;
            this.zoomedOutY = y;

            this.diameter = diameter;
            this.hit = false;
            this.highlighted = false;
        }
    }

    int numTargets = 100;
    int targetDiameter = 15;

    Target[] targets = new Target[numTargets];
    int prevTarget;
    int currentTarget;

    int currentTrial = 0;
    boolean trialInProgress = false;
    int startTime;
    int endTime;
    int errors;

    float zoomLevel = 1;
    boolean displayZoomField = false;

    float zoomFieldX;
    float zoomFieldY;
    float zoomFieldWidth = 100;
    float zoomFieldHeight = 100;

    boolean displayMagnification = false;
    boolean transparentMagnification = false;

    public void settings() {
        size(1500, 900);
    }

    public void setup() {
        createTargets();
    }

    public void createTargets() {
        for (int i = 0; i < numTargets; i++) {
            float x = random(targetDiameter, width - targetDiameter);
            float y = random(targetDiameter, height - targetDiameter);

            // Check if the new target is too close to any existing targets
            while (isOverlapping(x, y, i)) {
                x = random(targetDiameter, width - targetDiameter);
                y = random(targetDiameter, height - targetDiameter);
            }

            targets[i] = new Target(x, y, targetDiameter);
        }
    }

    boolean isOverlapping(float x, float y, int currentIndex) {
        for (int j = 0; j < currentIndex; j++) {
            float otherX = targets[j].x;
            float otherY = targets[j].y;

            // Check if the distance between the centers is less than a certain threshold (e.g., 5 pixels)
            float distance = dist(x, y, otherX, otherY);
            if (distance < 30) {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        background(255);
        drawTargets();

        if (displayZoomField) {
            drawZoomField();
        }

        if (displayMagnification) {
            drawMagnifier(mouseX, mouseY);
        }
    }

    public void drawTargets() {
        for (int i = 0; i < numTargets; i++) {
            if (targets[i].highlighted) {
                fill(255, 255, 0); // Yellow color for the highlighted target
            }
            else {
                fill(150);
            }
            stroke(105);
            strokeWeight(1);
            ellipse(targets[i].x, targets[i].y, targets[i].diameter * zoomLevel, targets[i].diameter * zoomLevel);

            // TODO - DELETE CONTENTS BELOW AFTER DEBUGGING
            // Display number inside the circle
            fill(0);
            textAlign(CENTER, CENTER);
            textSize(12);
            text(i + 1, targets[i].x, targets[i].y);
            // TODO - DELETE CONTENTS ABOVE AFTER DEBUGGING
        }
    }

    public void drawZoomField() {
        noFill();
        stroke(255, 0, 0);
        strokeWeight(2);
        zoomFieldX = mouseX - (zoomFieldWidth / 2);
        zoomFieldY = mouseY - ((zoomFieldHeight / 2));
        rect(zoomFieldX, zoomFieldY, zoomFieldWidth, zoomFieldHeight);
    }

    public void keyPressed() {
        if (keyCode == TAB && !trialInProgress) {
            println("Tab pressed. Trial has begun.");
            trialInProgress = true;
            startTrial();
        }
        if (key == '1' && zoomLevel == 1) {
            zoomIn();
        }
        else if (key == '1' && zoomLevel == 6) {
            zoomOut();
        }
        if (key == '2' && zoomLevel == 1) {
            displayZoomField = true;
        }
        if (key == '2' && zoomLevel == 6) {
            zoomOut();
        }
        if (key == '3' && zoomLevel == 1 && !displayMagnification) {
            displayMagnification = true;
        }
        else if (key == '3' && displayMagnification) {
            displayMagnification = false;
        }
        if (key == '4' && zoomLevel == 1 && !displayMagnification) {
            displayMagnification = true;
            transparentMagnification = true;
        }
        else if (key == '4' && displayMagnification) {
            displayMagnification = false;
            transparentMagnification = false;
        }
    }

    public void keyReleased() {
        if (key == '2' && displayZoomField) {
            displayZoomField = false;
            zoomIn();
        }
    }

    public void drawMagnifier(int mouseX, int mouseY) {
        int magnifySize = 200;
        int zoom = 6;
        int magnifyX = constrain(mouseX - magnifySize / 2, 0, width - magnifySize);
        int magnifyY = constrain(mouseY - magnifySize / 2, 0, height - magnifySize);

        // Draw the magnifying lens
        if (transparentMagnification) {
            fill(200, 200, 255, 150);
        }
        else {
            fill(255);
        }
        rect(magnifyX, magnifyY, magnifySize, magnifySize);

        // Check if the mouse is over the magnifying lens
        if (mouseX > magnifyX && mouseX < magnifyX + magnifySize && mouseY > magnifyY && mouseY < magnifyY + magnifySize) {
            // Find the circle under the magnifying lens
            for (int i = 0; i < numTargets; i++) {
                // Check if the mouse is over the circle in the magnifying lens
                if (dist(targets[i].x, targets[i].y, mouseX, mouseY) < (float) 20 / 2) {
                    // Draw the magnified circle
                    if (targets[i].highlighted) {
                        fill(255, 255, 0);
                    }
                    else {
                        fill(150);
                    }
                    ellipse(targets[i].x, targets[i].y, targets[i].diameter * zoom, targets[i].diameter * zoom);
                    textAlign(CENTER, CENTER);
                    text(i + 1, targets[i].x, targets[i].y);
                }
            }
        }
    }

    public void zoomIn() {
        zoomLevel = 6;
        drawZoomedInCircles(mouseX, mouseY);
    }

    public void zoomOut() {
        zoomLevel = 1;
        restoreCanvas();
    }

    public void drawZoomedInCircles(float zoomX, float zoomY) {
        ArrayList<Target> targetsWithinZoomField = new ArrayList<>();

        for (int i = 0; i < numTargets; i++) {
            if (isTargetWithinZoomField(targets[i].x, targets[i].y)) {
                targetsWithinZoomField.add(targets[i]);
            }
            targets[i].x = ((targets[i].x - zoomX) * zoomLevel) + zoomX;
            targets[i].y = ((targets[i].y - zoomY) * zoomLevel) + zoomY;
        }
        println(targetsWithinZoomField.size() + " were found");

//        if (!targetsWithinZoomField.isEmpty()) {
//            for (int i = 0; i < targetsWithinZoomField.size(); i++) {
//                float targetX = targetsWithinZoomField.get(i).x;
//                float targetY = targetsWithinZoomField.get(i).y;
//
//                targetsWithinZoomField.get(i).x = ((targetX - mouseX) * zoomLevel) + mouseX;
//                targetsWithinZoomField.get(i).y = ((targetY - mouseY) * zoomLevel) + mouseY;
//
//                ellipse(targetsWithinZoomField.get(i).x, targetsWithinZoomField.get(i).y,
//                        targetsWithinZoomField.get(i).diameter,
//                        targetsWithinZoomField.get(i).diameter);
//            }
//        }
    }

    public boolean isTargetWithinZoomField(float targetXCoord, float targetYCoord) {
        return (targetXCoord > zoomFieldX && targetXCoord < zoomFieldX + zoomFieldWidth
                && targetYCoord > zoomFieldY && targetYCoord < zoomFieldY + zoomFieldHeight);
    }

    public void restoreCanvas() {
        for (int i = 0; i < numTargets; i++) {
            targets[i].x = targets[i].zoomedOutX;
            targets[i].y = targets[i].zoomedOutY;
        }
    }

    public void startTrial() {
        if (currentTrial < 5) {
            currentTrial++;

            // Check whether we have completed the first trial. Impossible to have prevTarget on first trial
            if (currentTrial > 1) {
                prevTarget = currentTarget; // Store the previous target
            }
            currentTarget = getNextTarget();
            targets[currentTarget].highlighted = true;
            startTime = millis();
        }
        else {
            trialInProgress = false;
            println("trial has ended");
        }
    }

    public void mousePressed() {
        checkTarget();
    }

    int getNextTarget() {
        int nextTarget;
        if (currentTrial > 1) {
            nextTarget = (int) random(numTargets);

            // Ensure the new target is different from the previous one and not already hit
            while (nextTarget == prevTarget || targets[nextTarget].hit) {
                nextTarget = (int) random(numTargets);
            }
        }
        // If it's the first trial, just select a random target
        else {
            nextTarget = (int) random(numTargets);
        }
        return nextTarget;
    }

    boolean isMouseOverTarget(Target target) {
        float distance = dist(mouseX, mouseY, target.x, target.y);
        if (zoomLevel == 1) {
            return distance < target.diameter / 2;
        }
        return distance < target.diameter * zoomLevel / 2;
    }

    public void checkTarget() {
        if (currentTrial > 0) {
            int clickedTarget = -1;
            for (int i = 0; i < numTargets; i++) {
                if (isMouseOverTarget(targets[i])) {
                    println("mouse is over target");
                    clickedTarget = i;
                    break;
                }
            }

            if (clickedTarget != -1 && targets[clickedTarget].highlighted) {
                targets[clickedTarget].highlighted = false; // Reset the previous target's color
                endTime = millis();
                printTrialDetails();
                zoomOut();
                startTrial();
            }
            else if (clickedTarget != -1) {
                errors++;
            }
        }
    }

    void printTrialDetails() {
        float taskID;

        if (currentTrial > 1) {
            taskID = dist(targets[prevTarget].x, targets[prevTarget].y, targets[currentTarget].x,
                    targets[currentTarget].y);
        }
        else {
            taskID = 0; // Placeholder value for the first trial where no distance is determined
        }

        println("Trial: " + currentTrial + ", Task ID: " + taskID + ", Completion Time: " + (endTime - startTime) + "ms, Errors: " + errors);
    }

    // TODO COPY UNTIL LINE ABOVE ---------------------------------------------------------------------
    public static void main(String[] args) {
        PApplet.main("a3");
    }
}
