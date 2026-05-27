
public class CartItem {
    private String name;
    private double price;
    private String imagePath;
    private int quantity;

    public CartItem(String name, double price, String imagePath, int quantity) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}