import java.util.List;

public class AuctionServer extends java.io.Serializable implements Auction{
    private List<String> emails;
    private AuctionItem[] items;



    public AuctionServer(){
        items = new AuctionItem[10];

        items[0].itemID = 1;
        items[0].name = "Triangle";
        items[0].description = "Yellow Square";
        items[0].highestBid = 85;

        items[1].itemID = 2;
        items[1].name = "Square";
        items[1].description = "Blue Square";
        items[1].highestBid = 125;

    }

    @Override
    public Integer register(String email) throws RemoteException{
        emails.add(email);
    }
    @Override
    public AuctionItem getSpec(int itemID) throws RemoteException{
        if (itemID < items.length){
            return items(itemID - 1);
        }else{
            return null;
        }
        
    }
    @Override
    public Integer newAuction(int userID, AuctionSaleItem item) throws RemoteException{
        
    }
    @Override
    public AuctionItem[] listItems() throws RemoteException{
        for(int elm = 0; elm < items.length; elm++){
            System.out.println("ItemID: " + items[elm].itemID);
            System.out.println("Name: " + items[elm].name);
            System.out.println("Description: " + items[elm].description);
            System.out.println("Highest Bid: " + items[elm].highestBid);
        }
    }
    @Override
    public AuctionResult closeAuction(int userID, int itemID) throws RemoteException{

    }
    @Override
    public boolean bid(int userID, int itemID, int price) throws RemoteException{

    }

    public static void main(String[] args)
    {
        try{
            AuctionServer s = new AuctionServer();
            String name = "Auction";
            if ( UnicastRemoteObject.unexportObject(s, true)){
                System.out.println("The object was already exported");
            }
            Auction stub = (Auction) UnicastRemoteObject.exportObject(s, 0);
            Registry reg = LocateRegistry.getRegistry();
            reg.rebind(name, stub);
            System.out.println("Server ready");
        }catch(Exception e){
            System.err.println("Exception:");
            e.printStackTrace();     
        }
    }
}
