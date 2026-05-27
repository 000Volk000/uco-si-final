
public class CartItem {
    private String name;
    private double price;
    private String imagePath;
    private int quantity;
    private String description;

    public CartItem(String name, double price, String imagePath, int quantity) {
        this(name, price, imagePath, quantity, "");
    }

    public CartItem(String name, double price, String imagePath, int quantity, String description) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.quantity = quantity;
        this.description = description != null ? description : "";
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public int getQuantity() { return quantity; }
    public String getDescription() { return description; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartItem copy() {
        return new CartItem(name, price, imagePath, quantity, description);
    }
}