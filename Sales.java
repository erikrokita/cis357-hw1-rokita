/** */
public class Sales {
    private Item items[];
    //private int quantities[];
    private float subtotal;
    private float totalWithTax;

    public Sales(float subtotal, float change, float tenderedNum, float totalWithTax){
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

    //setters
    public void SetSubTotal(float subtotal){
        this.subtotal = subtotal;
    }
    public void SetTotalWithTax(float totalWithTax){
        this.totalWithTax = totalWithTax;
    }

    //getters
    public float GetSubTotal(){
        return subtotal;
    }
    public float GetTotalWithTax(){
        return totalWithTax;
    }
    public Item[] GetItemList(){
        return items;
    }

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
