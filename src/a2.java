import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        ArrayList<MenuItem> selectedItems;
        ArrayList<MenuItem> recencyList;
        HashMap<String, Integer> frequencyMap;

        Menu() {
            menuItems = new ArrayList<>();
            selectedItems = new ArrayList<>();
            recencyList = new ArrayList<>();
            frequencyMap = new HashMap<>();

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
    boolean changeColour = false;

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
                if (changeColour) {
                    highlightRecencyList();
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
            println("Hit the menu title");
        }
        else if (isMenuOpen) {
            for (int i = 1; i < menu.menuItems.size(); i++) {
                if (isMouseOverRect(menu.menuItems.get(i))) {
                    menu.recencyList.add(0, menu.menuItems.get(i));

                    println("item has been selected and added to the list");
                    println("3 most recent menu items: " + menu.recencyList.toString());

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
                    changeColour = true;
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
        changeColour = false;
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

    public void highlightRecencyList() {
        for (int i = 0; i < menu.recencyList.size(); i++) {
            if (i < 3) {
                MenuItem recencyItem = menu.recencyList.get(i);
                if (menu.menuItems.contains(recencyItem)) {
                    fill(255, 0, 0);
                    text(recencyItem.name, recencyItem.xText, recencyItem.yText);
                }
            }
        }
        println("recency list should be highlighted");
    }


    // TODO COPY UNTIL LINE ABOVE ---------------------------------------------------------------------
    public static void main(String[] args) {
        PApplet.main("a2");
    }
}
