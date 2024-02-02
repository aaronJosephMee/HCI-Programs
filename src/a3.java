import processing.core.PApplet;

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
    }

    public void drawTargets() {
        for (int i = 0; i < numTargets; i++) {
            if (targets[i].highlighted) {
                fill(255, 255, 0); // Yellow color for the highlighted target
            }
            else {
                fill(150);
            }
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
        for (int i = 0; i < numTargets; i++) {
            targets[i].x = (targets[i].x - zoomX) * zoomLevel + zoomX;
            targets[i].y = (targets[i].y - zoomY) * zoomLevel + zoomY;
        }
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
