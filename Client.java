import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Client{


    public static void main(String[] args){
        try{
            String name = "Auction";
            Registry registry = LocateRegistry.getRegistry("localhost");
            Auction server = (Auction) registry.lookup(name);
        
            //System.out.println(server.register("josephtyee@hotmail.com"));
            //System.out.println((server.listItems())[0].name);
            //System.out.println((server.listItems())[1].name);
            AuctionSaleItem theTestItem = new AuctionSaleItem();
            theTestItem.name = "Rocket";
            theTestItem.description = "its a great rocket";
            theTestItem.reservePrice = 6000;
            int userID = server.register("josephtyee@hotmail.com");
            System.out.println(userID);

            int itemID = server.newAuction(userID, theTestItem);
            System.out.println(itemID);

            System.out.println(server.bid(userID, itemID, 5289));
            System.out.println(server.bid(userID, itemID, 5456));
            System.out.println(server.bid(userID, itemID, 5435));
            System.out.println(server.bid(userID, itemID, 5245));
            System.out.println(server.bid(userID, itemID, 5357));
            System.out.println(server.bid(userID, itemID, 5750));

            AuctionResult result = server.closeAuction(userID, itemID);
            System.out.println("Winner: " + result.winningEmail + " with a bid of £" + result.winningPrice);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}