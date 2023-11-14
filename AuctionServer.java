public class AuctionServer extends java.io.Serializable implements Auction{
    public AuctionServer()
    {

    }
    @Override
    public int register(String email) throws RemoteException{
        
    }
    @Override
    public AuctionItem getSpec(int itemID) throws RemoteException{

    }
    @Override
    public int newAuction(int userID, AuctionSaleItem item) throws RemoteException{

    }
    @Override
    public AuctionItem[] listItems() throws RemoteException{

    }
    @Override
    public AuctionResult closeAuction(int userID, int itemID) throws RemoteException{

    }
    @Override
    public boolean bid(int userID, int itemID, int price) throws RemoteException{

    }

    public static void main(String[] args)
    {

    }
}
