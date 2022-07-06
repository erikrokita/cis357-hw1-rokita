/** class that stores information about each grocery item */
public class Item {

    /** member data */
    private int itemCode;
    private String itemName;
    private double unitPrice;

    /** constructor */
    public Item(int code, String name, double price){
        itemCode = code;
        itemName = name;
        unitPrice = price;
    }

    /** setters */
    public void SetItemCode(int code){
        itemCode = code;
    }
    public void SetItemName(String name){
        itemName = name;
    }
    public void SetUnitPrice(double price){
        unitPrice = price;
    }

    /** getters */
    public int GetItemCode(){
        return itemCode;
    }
    public String GetItemName(){
        return itemName;
    }
    public double GetUnitPrice(){
        return unitPrice;
    }
}
