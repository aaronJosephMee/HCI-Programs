import java.util.*;
class MenuItem {
        String name;

        float xText;
        float yText;

        float xRect;
        float yRect;
        float wRect;
        float hRect;

        MenuItem(String menuName, float xCoordOfName, float yCoordOfName, float xCoordOfRect, float yCoordOfRect,
                 float menuWidth, float menuHeight) {
            this.name = menuName;

            this.xText = xCoordOfName;
            this.yText = yCoordOfName;

            this.xRect = xCoordOfRect;
            this.yRect = yCoordOfRect;
            this.wRect = menuWidth;
            this.hRect = menuHeight;

        }

        public String toString() {
            return "MenuItem: " + "{'" + name + "', xText = " + xText + ", yText = " + yText +
                    ", xRect = " + xRect + ", yRect = " + yRect +
                    ", wRect = " + wRect + ", hRect = " + hRect + '}' + "\n";
        }
    }

    class Menu {
        MenuItem title;
        ArrayList<MenuItem> menuItems;

        String[] menuNames;
        List<String[]> nameArrays;

        int[] selectedPrompts;
        List<int[]> promptArrays;

        ArrayList<MenuItem> recencyList;
        HashMap<String, Integer> frequencyMap;

        int highestVal;
        int lowestVal;
        ArrayList<String> highestFrequencyList;
        ArrayList<String> lowestFrequencyList;

        String[] names1 =
                {"Daisy", "Deals", "Derby", "Divot", "Defog", "Dicot", "Dizzy", "Doily", "Donor", "Doubt",
                        "Duchy", "Dryer", "Dozer", "Dolor", "Donut"};
        String[] names2 =
                {"Cable", "Candy", "Carps", "Cedar", "Chair", "Champ", "Civet", "Claws", "Cloth", "Coach",
                        "Comic", "Corgi", "Crash", "Creek", "Cubit"};
        String[] names3 =
                {"Baker", "Balms", "Barge", "Bendy", "Bezel", "Biked", "Berry", "Baton", "Bight", "Bilge",
                        "Bland", "Bloom", "Boats", "Bonds", "Borax"};

        int[] prompts1 = {3, 0, 0, 0, 2, 4, 2, 14, 2, 0, 0, 8, 2, 0, 1, 0, 1, 5, 0, 0, 3, 8, 0, 0, 0, 0, 3, 11, 11, 3, 3, 0, 1, 4, 5, 1, 1, 7,
                0, 3, 5, 0, 1, 4, 4, 5, 2, 5, 0, 1, 2, 0, 4, 5, 3, 4, 13, 5, 4, 7, 5, 0, 4, 4, 9, 3, 8, 1, 1, 3, 12, 15, 2, 4, 1, 1,
                0, 0, 0, 1, 1, 0, 2, 0, 2, 2, 4, 6, 3, 1, 3, 1, 1, 5, 6, 0, 3, 4, 3, 0};
        int[] prompts2 = {14, 1, 1, 0, 0, 1, 2, 5, 0, 0, 13, 3, 2, 0, 2, 2, 4, 4, 0, 15, 3, 2, 5, 0, 3, 1, 7, 1, 4, 3, 3, 3, 0, 3, 10, 1, 3, 0,
                2, 1, 3, 6, 1, 2, 0, 0, 1, 0, 3, 0, 4, 12, 4, 5, 8, 2, 7, 2, 10, 2, 8, 2, 2, 6, 0, 8, 6, 8, 10, 1, 4, 3, 1, 0, 1, 9,
                0, 0, 0, 2, 0, 4, 0, 2, 6, 1, 5, 1, 1, 1, 1, 0, 1, 9, 2, 2, 0, 0, 1, 0};
        int[] prompts3 = {5, 11, 3, 12, 1, 3, 1, 1, 4, 3, 5, 4, 9, 12, 6, 3, 3, 2, 4, 0, 4, 2, 0, 1, 0, 1, 5, 3, 3, 1, 2, 2, 4, 1, 0, 8, 5, 4, 2,
                2, 1, 0, 0, 2, 1, 4, 6, 0, 6, 14, 6, 4, 11, 1, 0, 5, 1, 10, 1, 4, 10, 7, 2, 2, 2, 6, 3, 6, 4, 10, 4, 0, 1, 0, 3, 6, 10,
                7, 5, 1, 3, 0, 0, 1, 0, 2, 2, 2, 1, 1, 2, 0, 1, 0, 1, 4, 8, 0, 1, 2};

        Menu() {
            menuItems = new ArrayList<>();

            recencyList = new ArrayList<>();
            frequencyMap = new HashMap<>();

            highestVal = Integer.MIN_VALUE;
            highestFrequencyList = new ArrayList<>();

            lowestVal = Integer.MAX_VALUE;
            lowestFrequencyList = new ArrayList<>();
        }
    }

    // Create menu with menu items
    Menu menu = new Menu();
    String promptName;
    int promptIndex = 0;
    int currentPromptIndex;
    boolean isMenuOpen = false;
    boolean displayRecencyList = false;
    boolean highlightRecency = false;
    boolean highlightFrequency = false;
    boolean isEvaluationActive = false;

    public void settings() {
        size(700, 750);
    }

    public void setup() {
        // Choose random list of names

        // Draw first menuItem
        fill(100);
        rect(100, 90, 180, 40);

        textSize(20);
        textAlign(CENTER);
        fill(255);
        text("Click & Choose", 190, 117);

        // Draw guide:
        textAlign(LEFT);
        fill(255, 0, 0);
        circle(500, 600, 20);
        fill(0);
        text("Most Recent", 530, 607);

        fill(255, 128, 0);
        circle(500, 630, 20);
        fill(0);
        text("Most Frequent", 530, 637);

        fill(127, 0, 255);
        circle(500, 660, 20);
        fill(0);
        text("Least Frequent", 530, 667);

    }

    public void generateEvaluation() {
        // Add array names into the list for random selection
        menu.nameArrays = Arrays.asList(menu.names1, menu.names2, menu.names3);

        // Add prompts into list for random selection
        menu.promptArrays = Arrays.asList(menu.prompts1, menu.prompts2, menu.prompts3);

        // Randomly choose name array and prompt array
        Random random = new Random();
        menu.menuNames = menu.nameArrays.get(random.nextInt(menu.nameArrays.size()));
        menu.selectedPrompts = menu.promptArrays.get(random.nextInt(menu.promptArrays.size()));

        float yRectIncrease = 0;
        float yTextIncrease = 0;
        for (int i = 0; i < menu.menuNames.length; i++) {
            // Create menu item
            MenuItem menuItem = new MenuItem(menu.menuNames[i], 190, 157, 100,
                    130, 180, 40);

            // Since we need to take into account that these menu items are part of a drop down menu,
            // we must increase the value of the menu item's y-coord after each iteration.

            // Thus, if we are creating/adding the very first menu item, then create/add as normal.
            // If this is the second menu item, then increase the y-coord by a certain amount
            if (i > 0) {
                menuItem.yRect += yRectIncrease;
                menuItem.yText += yTextIncrease;
            }

            // Increase y coord amount for next iteration
            yRectIncrease += 40;
            yTextIncrease += 40;

            // Add to menu
            menu.menuItems.add(menuItem);
        }
        menu.title = new MenuItem("Click & Choose", 190, 117, 100,
                90, 180, 40);
        currentPromptIndex = menu.selectedPrompts[promptIndex];
    }

    public void displayPrompt() {
        if (promptIndex < menu.selectedPrompts.length) {
            // Clear name
            fill(203);
            noStroke();
            rect(113, 40, 150, 40);

            // Display current prompt
            promptName = menu.menuNames[currentPromptIndex];
            fill(0);
            textAlign(CENTER);
            text(promptName, 190, 70);
        }
    }

    boolean isMouseOverRect(MenuItem menuItem) {
        return mouseX > menuItem.xRect && mouseX < menuItem.xRect + menuItem.wRect &&
                mouseY > menuItem.yRect && mouseY < menuItem.yRect + menuItem.hRect;
    }

    public void draw() {
        if (isEvaluationActive) {
            if (isMenuOpen) {
                // Display the title
                fill(100);
                stroke(0);
                rect(menu.title.xRect, menu.title.yRect, menu.title.wRect, menu.title.hRect);
                // Draw text
                textSize(20);
                textAlign(CENTER);
                fill(255);
                text(menu.title.name, menu.title.xText, menu.title.yText);

                // Draws all the other items
                for (int i = 0; i < menu.menuItems.size(); i++) {

                    // Create mouse hover effect
                    MenuItem menuItem = menu.menuItems.get(i);
                    if (isMouseOverRect(menu.menuItems.get(i))) {
                        fill(250);
                    }
                    else {
                        fill(200);
                    }

                    // Draw rectangle
                    rect(menuItem.xRect, menuItem.yRect, menuItem.wRect, menuItem.hRect);

                    // Draw text
                    textSize(20);
                    textAlign(CENTER);

                    // Compare items from recentSelectionsList. If it contains a MenuItem from menuItems,
                    // change the colour
                    if (highlightRecency) {
                        highlightRecencyItems();
                    }
                    if (highlightFrequency) {
                        highlightFrequencyItems();
                    }
                    fill(0);
                    text(menuItem.name, menuItem.xText, menuItem.yText);
                }

            }
            else if (!isMenuOpen) {
                closeMenu();
            }
        }
    }

    public void mouseClicked() {
        if (!isEvaluationActive) {
            println("Select keys 1, 2, or 3 to start evaluation");
        }
        else {
            if (isMouseOverRect(menu.title)) {
                isMenuOpen = true;
            }
            else if (isMenuOpen) {
                for (int i = 0; i < menu.menuItems.size(); i++) {
                    MenuItem currentMenuItem = menu.menuItems.get(i);
                    if (isMouseOverRect(currentMenuItem)) {

                        if (currentMenuItem.name.equals(promptName)) {
                            // Add to recency list
                            if (!menu.recencyList.contains(currentMenuItem)) {
                                menu.recencyList.add(0, currentMenuItem);

                                refreshRecencyListDisplay();
                            }

                            // Add to frequency map
                            // If the frequency map does not contain the string, we need to set our count to be 1
                            if (!menu.frequencyMap.containsKey(currentMenuItem.name)) {
                                menu.frequencyMap.put(currentMenuItem.name, 1);

                            }
                            // Else, increase the current frequency by 1
                            else {
                                int currentFrequency = menu.frequencyMap.get(currentMenuItem.name);
                                menu.frequencyMap.put(currentMenuItem.name, currentFrequency + 1);
                            }

                            // Update highest and lowest frequency values
                            if (menu.frequencyMap.size() == 1) {
                                menu.highestVal = menu.frequencyMap.get(currentMenuItem.name);
                                menu.lowestVal = menu.frequencyMap.get(currentMenuItem.name);
                            }
                            else {
                                for (Map.Entry<String, Integer> entry : menu.frequencyMap.entrySet()) {
                                    // Check if current value is the highest
                                    if (entry.getValue() > menu.highestVal) {
                                        menu.highestVal = entry.getValue();
                                    }

                                    // Check if current value is the lowest
                                    if (entry.getValue() < menu.lowestVal) {
                                        menu.lowestVal = entry.getValue();
                                    }
                                }
                            }
                            // Increment indices
                            promptIndex++;
                            currentPromptIndex = menu.selectedPrompts[promptIndex];
                            displayPrompt();

                            // Trigger the close of the menu
                            isMenuOpen = false;
                            displayRecencyList();
                            highlightRecencyItems();
                        }
                    }
                }
            }
        }
    }

    public void keyPressed() {
        if (key == '1' && !isEvaluationActive) {
            displayRecencyList = true;
            isEvaluationActive = true;
            generateEvaluation();
            displayPrompt();
            println("Evaluation 1 is active");

            // Check if recency list is empty
            if (menu.recencyList.isEmpty()) {
                fill(0);
                textAlign(CENTER);
                text("Recency List: ", 430, 117);
                println("Cannot display recency list as no items have been added.");
            }
        }
        if (key == '2' && !isEvaluationActive) {
            highlightRecency = true;
            isEvaluationActive = true;
            generateEvaluation();
            displayPrompt();
            println("Evaluation 2 is active");

            if (menu.recencyList.isEmpty()) {
                println("Cannot change menu colour as no items have been added to recency list.");
            }
        }
        if (key == '3' && !isEvaluationActive) {
            highlightFrequency = true;
            isEvaluationActive = true;
            generateEvaluation();
            displayPrompt();
            println("Evaluation 3 is active");

            if (menu.frequencyMap.isEmpty()) {
                println("Cannot change menu colour as no items have been added to the frequency map.");
            }
        }
    }

    public void closeMenu() {
        fill(203);
        noStroke();
        MenuItem secondItem = menu.menuItems.get(0);
        MenuItem lastItem = menu.menuItems.get(menu.menuItems.size() - 1);

        // Draw a rectangle over the menu
        rect(secondItem.xRect, secondItem.yRect + 1,
                lastItem.xRect + lastItem.wRect, lastItem.yRect + lastItem.hRect);
    }

    public void displayRecencyList() {
        // Check if recency list is empty
        if (menu.recencyList.isEmpty()) {
            fill(0);
            textAlign(CENTER);
            text("Recency List: ", 430, 117);
            println("Cannot display recency list as no items have been added.");
        }

        if (displayRecencyList) {
            fill(0);
            textAlign(CENTER);
            text("Recency List: ", 430, 117);
            int textIncrease = 0;
            for (int i = 0; i < menu.recencyList.size(); i++) {
                if (i < 3) {
                    text(menu.recencyList.get(i).name, 430, 160 + textIncrease);
                    textIncrease += 30;
                }
                else {
                    i = menu.recencyList.size();
                }
            }
        }

    }

    public void refreshRecencyListDisplay() {
        // Clear canvas
        fill(203);
        noStroke();
        rect(355, 85, 150, 200);
    }

    public void highlightRecencyItems() {
        for (int i = 0; i < menu.recencyList.size(); i++) {
            if (i < 3) {
                MenuItem recencyItem = menu.recencyList.get(i);
                if (menu.menuItems.contains(recencyItem)) {
                    fill(255, 0, 0);
                    text(recencyItem.name, recencyItem.xText, recencyItem.yText);
                }
            }
        }
    }

    public void highlightFrequencyItems() {
        // Add highest and lowest items to their respective frequency lists
        for (Map.Entry<String, Integer> entry : menu.frequencyMap.entrySet()) {
            // Add to highest frequency list
            if (entry.getValue() == menu.highestVal) {
                menu.highestFrequencyList.add(entry.getKey());

                // Check if item is located in other list (i.e.: remove duplicates)
                if (menu.lowestFrequencyList.contains(entry.getKey())) {
                    menu.lowestFrequencyList.remove(entry.getKey());
                }
            }
            // Add to lowest frequency list
            if (entry.getValue() == menu.lowestVal) {
                menu.lowestFrequencyList.add(entry.getKey());

                // Check if item is located in other lists (i.e.: remove duplicates)
                if (menu.highestFrequencyList.contains(entry.getKey())) {
                    menu.highestFrequencyList.remove(entry.getKey());
                }
            }
        }

        // Update the colours on the canvas
        for (int i = 0; i < menu.menuItems.size(); i++) {
            MenuItem menuItem = menu.menuItems.get(i);
            if (menu.highestFrequencyList.contains(menuItem.name)) {
                // Orange
                fill(255, 128, 0);
                text(menuItem.name, menuItem.xText, menuItem.yText);
            }
            if (menu.lowestFrequencyList.contains(menuItem.name)) {
                // Purple
                fill(127, 0, 255);
                text(menuItem.name, menuItem.xText, menuItem.yText);
            }

        }

    }
