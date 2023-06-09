# caching

## why caching
- reduce latency
- speed up
- reduce connection to
  - DB
  - other service

## 兩種緩存策略
Write-through 和 write-back 是兩種常見的緩存策略，用於管理 CPU 與主存儲之間的數據交換。它們之間的主要區別在於寫操作的處理方式。

### Write-through 緩存：
在 write-through 緩存策略中，當 CPU 對緩存中的數據進行寫操作時，同時也將相同的數據寫入主存儲。這樣做的好處是確保緩存和主存儲中的數據始終保持一致。然而，這會導致更高的寫入延遲，因為每次寫操作都需要將數據寫入兩個地方（緩存和主存儲）。
### Write-back 緩存：
在 write-back 緩存策略中，當 CPU 對緩存中的數據進行寫操作時，僅更新緩存，而不立即將更改寫回主存儲。相反，更改只有在該緩存塊被替換出緩存時才寫回主存儲。這樣做的好處是減少了寫操作的延遲，因為不需要立即將數據寫回主存儲。然而，這種策略可能會導致緩存和主存儲之間的數據不一致。

## eviction policy (淘汰策略)
如何決定哪些數據應該被淘汰，哪些數據應該被保留在緩存中。

### LRU
Least Recently Used (LRU) 淘汰策略是一種常見的淘汰策略，它會淘汰最近最少使用的數據。這種策略的工作原理是，當緩存已滿時，將最近最少使用的數據從緩存中淘汰出去。

### LFU
Least Frequently Used (LFU) 淘汰策略是一種常見的淘汰策略，它會淘汰最不常使用的數據。這種策略的工作原理是，當緩存已滿時，將最不常使用的數據從緩存中淘汰出去。