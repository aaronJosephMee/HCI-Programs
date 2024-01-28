import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class a2 extends PApplet {
    // TODO COPY FROM LINE BELOW ----------------------------------------------------------------------
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
        MenuItem first;
        ArrayList<MenuItem> menuItems;
        ArrayList<MenuItem> recencyList;
        HashMap<String, Integer> frequencyMap;

        int highestVal;
        int lowestVal;
        ArrayList<String> highestFrequencyList;
        ArrayList<String> middleFrequencyList;
        ArrayList<String> lowestFrequencyList;

        Menu() {
            menuItems = new ArrayList<>();
            recencyList = new ArrayList<>();
            frequencyMap = new HashMap<>();

            highestVal = Integer.MIN_VALUE;
            highestFrequencyList = new ArrayList<>();

            middleFrequencyList = new ArrayList<>();

            lowestVal = Integer.MAX_VALUE;
            lowestFrequencyList = new ArrayList<>();

            String[] menuNames = {"File", "Open", "Favourites", "Sketchbook",
                    "Examples", "Close", "Save", "Save As", "Export",
                    "Page Setup", "Print", "References"};
            String[] names1 =
                    {"Daisy", "Deals", "Derby", "Divot", "Defog", "Dicot", "Dizzy", "Doily", "Donor", "Doubt",
                            "Duchy", "Dryer", "Dozer", "Dolor", "Donut", "Dwelt"};
            String[] names2 =
                    {"Cable", "Candy", "Carps", "Cedar", "Chair", "Champ", "Civet", "Claws", "Cloth", "Coach",
                            "Comic", "Corgi", "Crash", "Creek", "Cubit", "Curve"};
            String[] names3 =
                    {"Baker", "Balms", "Barge", "Bendy", "Bezel", "Biked", "Berry", "Baton", "Bight", "Bilge",
                            "Bland", "Bloom", "Boats", "Bonds", "Borax", "Blitz"};
            String[] names4 =
                    {"Abbey", "Acids", "Adore", "Aisle", "Afoot", "Album", "After", "Affix", "Alder", "Along",
                            "Amuck", "Antsy", "Apply", "Argon", "Ashes", "Awake"};


            float yRectIncrease = 0;
            float yTextIncrease = 0;
            for (int i = 0; i < menuNames.length; i++) {
                // Create menu item
                MenuItem menuItem = new MenuItem(menuNames[i], 290, 117, 200,
                        90, 180, 40);

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
                menuItems.add(menuItem);
            }
            first = menuItems.get(0);
        }
    }

    // Create menu with menu items
    Menu menu = new Menu();
    boolean isMenuOpen = false;
    boolean highlightRecency = false;
    boolean highlightFrequency = false;

    public void settings() {
        size(600, 750);
    }

    public void setup() {
        // Verify correct variables
//        for (MenuItem menuItem : menu.menuItems) {
//            println(menuItem.toString());
//        }

        // Draw first menuItem
        fill(100);
        rect(menu.first.xRect, menu.first.yRect, menu.first.wRect, menu.first.hRect);

        textSize(20);
        textAlign(CENTER);
        fill(255);
        text(menu.first.name, menu.first.xText, menu.first.yText);
    }

    boolean isMouseOverRect(MenuItem menuItem) {
        return mouseX > menuItem.xRect && mouseX < menuItem.xRect + menuItem.wRect &&
                mouseY > menuItem.yRect && mouseY < menuItem.yRect + menuItem.hRect;
    }

    public void draw() {
        if (isMenuOpen) {
            // Gets the first item on the list (the title)
            fill(100);
            stroke(0);
            rect(menu.first.xRect, menu.first.yRect, menu.first.wRect, menu.first.hRect);
            // Draw text
            textSize(20);
            textAlign(CENTER);
            fill(255);
            text(menu.first.name, menu.first.xText, menu.first.yText);

            // Draws all the other items
            for (int i = 1; i < menu.menuItems.size(); i++) {

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

    public void mouseClicked() {
        if (isMouseOverRect(menu.first)) {
            isMenuOpen = true;
        }
        else if (isMenuOpen) {
            for (int i = 1; i < menu.menuItems.size(); i++) {
                MenuItem currentMenuItem = menu.menuItems.get(i);
                if (isMouseOverRect(currentMenuItem)) {
                    // Add to recency list
                    menu.recencyList.add(0, currentMenuItem);
                    println(currentMenuItem.name + " has been added to the recency list");

                    // Add to frequency map
                    // If the frequency map does not contain the string, we need to set our count to be 1
                    if (!menu.frequencyMap.containsKey(currentMenuItem.name)) {
                        menu.frequencyMap.put(currentMenuItem.name, 1);
                        println(currentMenuItem.name + " has been added to the frequency list for the first time");
                    }
                    // Else, increase the current frequency by 1
                    else {
                        int currentFrequency = menu.frequencyMap.get(currentMenuItem.name);
                        menu.frequencyMap.put(currentMenuItem.name, currentFrequency + 1);
                        println(currentMenuItem.name + "'s frequency has been increased by 1, with total being: " +
                                menu.frequencyMap.get(currentMenuItem.name));
                    }

                    // Update highest and lowest frequency values
                    if (menu.frequencyMap.size() == 1) {
                        menu.highestVal = menu.frequencyMap.get(currentMenuItem.name);
                        menu.lowestVal = menu.frequencyMap.get(currentMenuItem.name);
                        println("1 item is in the hashmap; thus, highest and lowest values are: " + "\n" +
                                "Highest Value: " + menu.highestVal + "\n" +
                                "Lowest Value: " + menu.lowestVal);
                    }
                    else {
                        for (Map.Entry<String, Integer> entry : menu.frequencyMap.entrySet()) {
                            // Check if current value is the highest
                            if (entry.getValue() > menu.highestVal) {
                                menu.highestVal = entry.getValue();
                                println("Highest value: " + menu.highestVal);
                            }

                            // Check if current value is the lowest
                            if (entry.getValue() < menu.lowestVal) {
                                menu.lowestVal = entry.getValue();
                                println("Lowest value: " + menu.lowestVal);
                            }
                        }
                    }

                    isMenuOpen = false;
                }
            }
        }
    }

    public void keyPressed() {
        if (key == '1') {
            // Check if recency list is empty
            if (menu.recencyList.isEmpty()) {
                println("Cannot display recency list as no items have been added.");
            }
            else {
                refreshRecencyListDisplay();
                displayRecencyList();
            }
        }
        if (key == '2') {
            if (menu.recencyList.isEmpty()) {
                println("Cannot change menu colour as no items have been added to recency list.");
            }
            else {
                if (isMenuOpen) {
                    highlightRecency = true;
                }
            }
        }
        if (key == '3') {
            if (menu.frequencyMap.isEmpty()) {
                println("Cannot change menu colour as no items have been added to the frequency map.");
            }
            else {
                if (isMenuOpen) {
                    highlightFrequency = true;
                }
            }
        }
    }

    public void closeMenu() {
        fill(203);
        noStroke();
        MenuItem secondItem = menu.menuItems.get(1);
        MenuItem lastItem = menu.menuItems.get(menu.menuItems.size() - 1);

        // Draw a rectangle over the menu
        rect(secondItem.xRect, secondItem.yRect + 1,
                lastItem.xRect + lastItem.wRect, lastItem.yRect + lastItem.hRect);

        // Reset the colour of the recency highlighting
        highlightRecency = false;
        highlightFrequency = false;
    }

    public void displayRecencyList() {
        fill(0);
        text("Recency List: ", 100, 80);
        int textIncrease = 0;
        for (int i = 0; i < menu.recencyList.size(); i++) {
            if (i < 3) {
                textAlign(LEFT);
                text(menu.recencyList.get(i).name, 190 + textIncrease, 80);
                textIncrease += 115;
            }
            else {
                i = menu.recencyList.size();
            }
        }
        println("recency list is being displayed");
    }

    public void refreshRecencyListDisplay() {
        // Clear canvas
        fill(203);
        noStroke();
        rect(40, 35, 600, 50);

        println("recency list should be cleared");
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


    // TODO COPY UNTIL LINE ABOVE ---------------------------------------------------------------------
    public static void main(String[] args) {
        PApplet.main("a2");
    }
}
