public class ProductDatabase {

    public static String[][] getFishHookData() {
        return new String[][] {
                { App.getBundle().getString("sebastian"), "8.99", "src/assets/fishHook/sebastian.png",
                        App.getBundle().getString("sebastianDesc") },
                { App.getBundle().getString("destructor"), "12.30", "src/assets/fishHook/wildlifeDestructor.png",
                        App.getBundle().getString("destructorDesc") },
                { App.getBundle().getString("crazyEye"), "4.99", "src/assets/fishHook/crazyEye.png",
                        App.getBundle().getString("crazyEyeDesc") },
                { App.getBundle().getString("classicHook"), "2.10", "src/assets/fishHook/hook?.png",
                        App.getBundle().getString("classicHookDesc") },
                { App.getBundle().getString("ladyHook"), "5.75", "src/assets/fishHook/lady.png",
                        App.getBundle().getString("ladyHookDesc") },
                { App.getBundle().getString("gamingHook"), "9.50", "src/assets/fishHook/gamingHook.png",
                        App.getBundle().getString("gamingHookDesc") },
                { App.getBundle().getString("joaquinCortes"), "15.00", "src/assets/fishHook/joaquinCortes.png",
                        App.getBundle().getString("joaquinCortesDesc") }
        };
    }

    public static String[][] getStoolData() {
        return new String[][] {
                { App.getBundle().getString("commonStool"), "3.25", "src/assets/stool/commonStool.png",
                        App.getBundle().getString("commonStoolDesc") },
                { App.getBundle().getString("flamingoStool"), "14.90", "src/assets/stool/flamingo.png",
                        App.getBundle().getString("flamingoStoolDesc") },
                { App.getBundle().getString("fridgeStool"), "29.99", "src/assets/stool/fridge.png",
                        App.getBundle().getString("fridgeStoolDesc") },
                { App.getBundle().getString("furStool"), "49.50", "src/assets/stool/fur.png",
                        App.getBundle().getString("furStoolDesc") },
                { App.getBundle().getString("parkStool"), "11.00", "src/assets/stool/park.png",
                        App.getBundle().getString("parkStoolDesc") },
                { App.getBundle().getString("turtleStool"), "19.75", "src/assets/stool/turtle.png",
                        App.getBundle().getString("turtleStoolDesc") }
        };
    }

}