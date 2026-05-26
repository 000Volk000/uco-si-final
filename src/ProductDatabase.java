public class ProductDatabase {

    public static String[][] getFishHookData() {
        return new String[][] {
                { App.getBundle().getString("sebastian_name"), "8.99", "src/assets/fishHook/sebastian.png",
                        App.getBundle().getString("SebastianDesc") },
                { App.getBundle().getString("destructor_name"), "12.30", "src/assets/fishHook/wildlifeDestructor.png",
                        "" },
                { App.getBundle().getString("crazyEye_name"), "4.99", "src/assets/fishHook/crazyEye.png", "" },
                { App.getBundle().getString("classicHook_name"), "2.10", "src/assets/fishHook/hook?.png", "" },
                { App.getBundle().getString("ladyHook_name"), "5.75", "src/assets/fishHook/lady.png", "" },
                { App.getBundle().getString("gamingHook_name"), "9.50", "src/assets/fishHook/gamingHook.png", "" },
                { App.getBundle().getString("joaquinCortes_name"), "15.00", "src/assets/fishHook/joaquinCortes.png",
                        "" }
        };
    }

}