import datetime
from decimal import Decimal
from pydantic import BaseModel

class Bin(BaseModel):
    """A bin is a container for a candle"""
    openTime: datetime.datetime
    closeTime: datetime.datetime
    openBid: Decimal
    highBid: Decimal
    lowBid: Decimal
    closeBid: Decimal
    openAsk: Decimal
    highAsk: Decimal
    lowAsk: Decimal
    closeAsk: Decimal

    volume: Decimal

    pnl: Decimal

    length: int

class Trade(BaseModel):
    """A trade is a trade record"""
    uuid: str
    instrument: str

    entryPrice: Decimal
    exitprice: Decimal
    stoplossprice: Decimal
    profitTargetprice: Decimal
        
    commissionrate: Decimal  # 手續費
    commission: Decimal
    profit: Decimal

    quantity: int

    entryTime: datetime.datetime
    exitTime: datetime.datetime

    direction: str
