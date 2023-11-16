import java.util.List;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;
import java.util.*;
import java.rmi.server.UnicastRemoteObject;
public class AuctionServer extends UnicastRemoteObject implements Auction{
    private List<String> users = new ArrayList<>(); // List of user emails
    private List<AuctionItem> items = new ArrayList<>(); // List of auction items
    private List<Integer> openAuctions = new ArrayList<>(); // List of open auction item IDs
    private List<Integer> reservePrices = new ArrayList<>();// List of reserve prices to compare to highest bid at closeAuction() attempt
    public AuctionServer() throws RemoteException{
        
        items.add(new AuctionItem());
        items.add(new AuctionItem());

        items.get(0).itemID = 1;//User and Item ID's start at 1
        items.get(0).name = "Triangle";
        items.get(0).description = "Yellow Square";
        items.get(0).highestBid = 85;

        items.get(1).itemID = 2;
        items.get(1).name = "Square";
        items.get(1).description = "Blue Square";
        items.get(1).highestBid = 125;

    }

    @Override
    public Integer register(String email) throws RemoteException{
        // Assuming email is unique for each user
        int userID = users.size() + 1;
        users.add(email);
        reservePrices.add(0);
        return userID;
    }
    @Override
    public AuctionItem getSpec(int itemID) throws RemoteException{
        if (itemID <= items.size()){
            return items.get(itemID - 1);
        }else{
            return null;
        }
        
    }
    @Override
    public Integer newAuction(int userID, AuctionSaleItem item) throws RemoteException{
        if (userID < 1 || userID > users.size()){
            throw new RemoteException("User Does Not Exist");
        }

        AuctionItem theNewItem = new AuctionItem();
        theNewItem.itemID = items.size() +1;
        theNewItem.name = item.name;
        theNewItem.highestBid = 0;
        theNewItem.description = item.description;
        items.add(theNewItem);
        reservePrices.set(userID - 1, item.reservePrice);
        openAuctions.add(theNewItem.itemID);
        return theNewItem.itemID;
    }
    @Override
    public AuctionItem[] listItems() throws RemoteException{
        AuctionItem[] a = items.toArray(new AuctionItem[0]);
       return a; 
    }
    @Override
    public AuctionResult closeAuction(int userID, int itemID) throws RemoteException{
        
        if (userID < 1 || userID > users.size()) {//does user exist
            throw new RemoteException("User not found");
        } 
        if (itemID < 1 || itemID > items.size()) {// does item exist
            throw new RemoteException("Item not found");
        }
        if (!openAuctions.contains(itemID)) {//did user start auction
            throw new RemoteException("Auction is not open for this item yet");
        }
        System.out.println(items.get(itemID - 1).highestBid);
        System.out.println(reservePrices.get(userID - 1));
        if (items.get(itemID - 1).highestBid < reservePrices.get(userID - 1)){
            throw new RemoteException("Highest bid did not meet reserved price");
        }        

        // Close the auction and determine the winner
        AuctionItem auctionItem = items.get(itemID - 1); // Adjust index

        int winningBid = auctionItem.highestBid;
        String winningEmail = users.get(userID - 1); // Adjust index

        // Remove the item from the list of open auctions
        openAuctions.remove(Integer.valueOf(itemID));

        // Create and return the result
        AuctionResult result = new AuctionResult();
        result.winningEmail = winningEmail;
        result.winningPrice = winningBid;

        return result;    
    }
    @Override
    public boolean bid(int userID, int itemID, int price) throws RemoteException{
        if (userID < 1 || userID > users.size()) {//does user exist
            throw new RemoteException("User not found");
        } 
        if (itemID < 1 || itemID > items.size()) {// does item exist
            throw new RemoteException("Item not found");
        }
        if (!openAuctions.contains(itemID)) {//did user start auction
            throw new RemoteException("Auction is not open for this item yet");
        }

        if (price <= items.get(itemID-1).highestBid){
            System.out.println("Bid unsuccesful - was less than or equal to current bid on " + items.get(itemID-1).name);
            return false; //Bid unsuccesful - was less than or equal to current bid on corresponding item
            
        }else{
            items.get(itemID-1).highestBid = price;
            System.out.println("Bid Succesful");
            return true;
        }
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
