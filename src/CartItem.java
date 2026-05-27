
public class CartItem {
    private String name;
    private double price;
    private String imagePath;
    private int quantity;
    private String description;
    private boolean lastPurchase;

    public CartItem(String name, double price, String imagePath, int quantity) {
        this(name, price, imagePath, quantity, "");
    }

    public CartItem(String name, double price, String imagePath, int quantity, String description) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.quantity = quantity;
        this.description = description != null ? description : "";
        this.lastPurchase = false;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(boolean lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartItem copy() {
        CartItem copy = new CartItem(name, price, imagePath, quantity, description);
        copy.setLastPurchase(lastPurchase);
        return copy;
    }
}