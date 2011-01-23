package org.rl337.economy.data;

import java.util.ArrayList;
import java.util.List;

import org.rl337.economy.data.Market.Bid;
import org.rl337.economy.data.entity.Entity;
import org.rl337.economy.data.entity.MarketUser;

import junit.framework.TestCase;

public class MarketTests extends TestCase {
    private Market mMarket;
    private TestMarketUser mSeller;
    private TestMarketUser mBuyer;
    
    public void setUp() {
        mMarket = new Market();
        mBuyer = new TestMarketUser("Buyer", null, 0);
        mSeller = new TestMarketUser("Seller", null, 0);
    }
    
    /*
    public void testSimpleOfferAndBuy() {
        mMarket.offer(mSeller, new Bid(mSeller, Resource.Food, 1, 10, 50));
        mMarket.buy(mBuyer, new Bid(mBuyer, Resource.Food, 1, 10, 50));
        
        // This should have triggered a buy.
        
        List<TestExecution> sellerSells = mSeller.getSells();
        List<TestExecution> buyerBuys = mBuyer.getBuys();
        assertEquals("Seller should have no buys.", 0, mSeller.getBuys().size());
        assertEquals("Seller should have one sell.", 1, sellerSells.size());
        assertEquals("Buyer should have one buy.", 1, buyerBuys.size());
        assertEquals("Seller should have no sells.", 0, mBuyer.getSells().size());
        
        TestExecution buy = buyerBuys.get(0);
        assertEquals("Buyer in buy exeuction should be buyer", mBuyer, buy.buy.getMarketUser());
        assertEquals("Seller in buy exeuction should be seller", mSeller, buy.offer.getMarketUser());
        
        TestExecution sell = sellerSells.get(0);
        assertEquals("Buyer in buy exeuction should be buyer", mBuyer, sell.buy.getMarketUser());
        assertEquals("Seller in buy exeuction should be seller", mSeller, sell.offer.getMarketUser());
    }*/
    
    public void testOfferAndBuyExpiration() {
        mMarket.offer(mSeller, new Bid(mSeller, Resource.Food, 1, 10, 50));
        mMarket.executeTick(5);
        
        //assertEquals("Seller should have had 1 expired sell", mSeller.)
        
        mMarket.buy(mBuyer, new Bid(mBuyer, Resource.Food, 1, 10, 50));
    }
    
    
    
    
    private static class TestMarketUser extends Entity implements MarketUser {
        private ArrayList<TestExecution> mBuys;
        private ArrayList<TestExecution> mSells;
        private ArrayList<Market.Bid> mBuyExpirations;
        private ArrayList<Market.Bid> mSellExpirations; 

        public TestMarketUser(String name, Simulation s, long tick) {
            super(name, s, tick);
            mBuys = new ArrayList<TestExecution>();
            mSells = new ArrayList<TestExecution>();
            mBuyExpirations = new ArrayList<Bid>();
            mSellExpirations = new ArrayList<Bid>();
        }

        @Override
        public Entity getEntity() {
            return this;
        }

        @Override
        public void onBuyExecuted(Bid offer, Bid request, boolean partial) {
            mBuys.add(new TestExecution(request, offer, partial));
        }

        @Override
        public void onOfferExecuted(Bid offer, Bid request, boolean partial) {
            mSells.add(new TestExecution(request, offer, partial));
        }
        
        public ArrayList<TestExecution> getBuys() {
            return mBuys;
        }
        
        public ArrayList<TestExecution> getSells() {
            return mSells;
        }

        @Override
        public void onBuyExpired(Bid buy) {
            mBuyExpirations.add(buy);
        }

        @Override
        public void onOfferExpired(Bid offer) {
            mSellExpirations.add(offer);
        }
        
    }
    
    public static class TestExecution {
        public Bid buy;
        public Bid offer;
        public boolean partial;
        
        public TestExecution(Bid b, Bid o, boolean p) {
            buy = b;
            offer = o;
            partial = p;
        }
    }
}