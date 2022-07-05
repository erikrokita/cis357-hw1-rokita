/** */
public class Item {
    private int itemCode;
    private String itemName;
    private float unitPrice;

    public Item(int code, String name, float price){
        itemCode = code;
        itemName = name;
        unitPrice = price;
    }

    //setters
    public void SetItemCode(int code){
        itemCode = code;
    }
    public void SetItemName(String name){
        itemName = name;
    }
    public void SetUnitPrice(float price){
        unitPrice = price;
    }

    //getters
    public int GetItemCode(){
        return itemCode;
    }
    public String GetItemName(){
        return itemName;
    }
    public float GetUnitPrice(){
        return unitPrice;
    }
}
