/** class that stores information about a sale */
public class Sales {

    /** member data */
    private Item items[];
    //private int quantities[];
    private double subtotal;
    private double totalWithTax;

    /** constructor */
    public Sales(double subtotal, double change, double tenderedNum, double totalWithTax){
        this.items = new Item[200];     //only allow up to 200 items
        //this.quantities = new int[200];
        this.subtotal = subtotal;
        this.totalWithTax = totalWithTax;
    }

    public Sales(){
        this.items = new Item[200];     //only allow up to 200 items
        //this.quantities = new int[200];
        this.subtotal = 0;
        this.totalWithTax = 0;
    }

    /** setters */
    public void SetSubTotal(double subtotal){
        this.subtotal = subtotal;
    }
    public void SetTotalWithTax(double totalWithTax){
        this.totalWithTax = totalWithTax;
    }

    /** getters */
    public double GetSubTotal(){
        return subtotal;
    }
    public double GetTotalWithTax(){
        return totalWithTax;
    }
    public Item[] GetItemList(){
        return items;
    }

    /** adds an item to its list. accepts the item and quantity */
    public void AddItem(Item item, int quantity){
        //continue searching through the internal items array until we find an empty slot
        int i = 0;
        while (this.items[i] != null && i < 199){
            i ++;
        }

        //if we've hit our 200 inventory limit, break
        if (i >= 199){
            System.out.println("Maximum inventory for this sale achieved!");
            return;
        }

        //add the items to the array
        for (int j = 0; j < quantity; j ++){
            this.items[i] = item;

            //add to the subtotal
            this.subtotal += item.GetUnitPrice();

            //update total
            this.totalWithTax = subtotal * 1.06f;

            i ++;
        }
    }
}
