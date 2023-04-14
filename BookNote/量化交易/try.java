public class Bin {
    public Calendar openTime;
    public Calendar closeTime;

    public double openBid;
    public double highBid;
    public double lowBid;
    public double closeBid;
    public double openAsk;
    public double highAsk;
    public double lowAsk;
    public double closeAsk;
    
    public double volume;

    public double pnl;

    public int length;
}

public class Trade {
    private UUID uuid;

    private String instrument;

    private double entryPrice;
    private double exitprice;
    private double stoplossprice;
    private double profitTargetprice;
    private double commissionrate;
    private double commission;
    private double profit;

    private int quantity;

    private Calendar entryTime;
    private Calendar exitTime;

    private DirectionType direction;
}

class Bin:
    def __init__(self):
        self.openTime = None
        self.closeTime = None

        self.openBid = None
        self.highBid = None
        self.lowBid = None
        self.closeBid = None
        self.openAsk = None
        self.highAsk = None
        self.lowAsk = None
        self.closeAsk = None

        self.volume = None

        self.pnl = None

        self.length = None

class Trade:
    def __init__(self):
        self.uuid = None

        self.instrument = None

        self.entryPrice = None
        self.exitprice = None
        self.stoplossprice = None
        self.profitTargetprice = None
        self.commissionrate = None
        self.commission = None
        self.profit = None

        self.quantity = None

        self.entryTime = None
        self.exitTime = None

        self.direction = None