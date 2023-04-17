# Hashing

## What is hashing?

hashing is a technique that is used to uniquely identify a specific object from a group of similar objects.

## hashing strategies
- 環狀 hashing
- rendezvous hashing
  - Rendezvous hashing（Rendezvous algorithm、HRW algorithm），又稱Highest Random Weight算法，是一種分散式哈希算法，用於在分布式環境下決定某個數據應該被存儲在哪個節點上。它使用散列值來分配數據到節點，而不需要中央調度器來管理分發。Rendezvous hashing的主要特點是在節點數量較小的情況下表現良好，而在節點數量較大時，它的性能開始下降。由於它可以自動地處理節點的添加和移除，因此它被廣泛用於分布式系統中的負載平衡和數據分散。
  - 找到妳權重最高的

什麼是好的 load balancing hash function?
- 盡量平均分配
- 讓改變量降到最低
  - 某個 server 壞掉時或新增 server 時,重新分配的變動最少
    - 因為要重新分配的資料量最少(caching)