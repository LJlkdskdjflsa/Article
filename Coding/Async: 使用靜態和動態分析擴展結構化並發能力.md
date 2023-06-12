# Async: 使用靜態和動態分析擴展結構化並發能力

本文為翻譯和理解來自：[Zac Hatfield-Dodds: Async: scaling structured concurrency with static and dynamic analysis](https://www.youtube.com/watch?v=FrpUb6OEbcc)

## 簡介

非同步 Python 是相對近期加入 Python 的並發選項之一，另外兩種長期存在的選項為進程和線程，它提供了非常不同的程式設計體驗。進程獨立運行，而線程在內核調度器的任意切換下運作，非同步任務則採取不同的權衡方式：管理共享狀態像在單線程同步 Python 中一樣容易，但是你需要確保有足夠的 await、async for 和 async with 語句在任務可以切換的地方，以確保穩定進展。

在這次的演講中，我們將探索結構化並發性的優點 - 特別是錯誤處理、超時、取消和易讀的程式碼 - 以及減輕合作並發問題（當一個不合作的慢任務可能讓你的整個程式停滯）的便利且可靠的方法。我將向你介紹如何使用 flake8-trio 進行靜態分析，並解釋如何撰寫你自己的基於 AST 的工具，並顯示如何透過動態分析幫助我們捕捉任何滑過那個快速且方便檢查的問題。

有了這樣的系統，你不必成為經驗豐富或過於謹慎的軟體工程師就能撰寫出美觀的非同步程式碼 - 服務或抓取網站、控制一組進程，或寫遊戲 - 它讀起來就像普通的 Python，你的工具會在失敗時幫忙補救。

## 目錄
- Async -> cooprative concurrency
- dynamic analysis
- structured concurrency
- static analysis & codemods

## Async python is cooperative concurrency

在計算機科學中，並發性是指能夠讓多個任務在同一時間進行的能力。而合作並發性（Cooperative Concurrency）是一種特殊的並發模型，這種模型下的任務需要主動地釋放控制，使得其他任務可以被執行。這是相對於抢占式並發性（Preemptive Concurrency），後者是由系統來決定何時切換到不同的任務。

在 Python 的非同步（Async）模型中，任務（通常稱為協程，coroutines）會在某些點（例如，當它們正在等待 I/O 操作完成時）主動釋放控制。這就是所謂的 "合作並發性"，因為每個任務都需要合作（即主動釋放控制），以便其他任務可以進行。非同步 Python 的特點之一就是使用這種合作並發性模型。

### 程式運行的三種基本方式（一次做多件事情）：
- serial：每個任務都會等待前一個任務完成後才能開始執行
  - 優點：
    - 在以下兩個部分有很好的表現：
      - 確定性(determinism)
      - 可除錯性(debugability)
  - 缺點：
    - 慢
- parallel：任務同時執行（但 Python 中的 GIL 會阻止真正的平行執行）
  - 優點：
    - 快
  - 缺點：
    - 難以除錯
    - 難以確定
- concurrent：任務交替執行（一個任務可以在另一個任務等待時執行）
  - 優點：
    - 快
    - 可除錯
    - 確定
    - 大部分情況下，這是最好的選擇（運行速度不會比平行執行慢太多，但是除錯和確定性都會好很多）
  - 缺點：
    - 需要合作

### 兩種並發性模型

- preemptive concurrency：系統決定何時切換到不同的任務（可以再任意時間切換）
  - 優點：
    - 簡單
    - 不需要鎖
  - 缺點：
    - 難以除錯
    - 難以確定
- cooperative concurrency：任務主動釋放控制，使得其他任務可以進行（只能在 check point 時切換）
  - 優點：
    - 可除錯（可以確定任務在何時會停止運行）
    - 確定
  - 缺點：
    - 需要合作
    - 需要鎖

### Async Python

在 await/ async for / async with 時切換任務


## Dynamic analysis
在代碼運作期間找尋問題

### 一個爛任務可以破壞你的整個程式

![picture 1](https://i.imgur.com/uXfdB7w.png)  

如果沒有設置 break point 非同步就無法切換任務
-> 就變成只是看起來更複雜的 serial

可以使用不同模式運行來 debug (非同步的 debug 模式會對慢任務進行警告)
```
PYTHONDEVMODE=1
PYTHONASYNCIODEBUG=1
python -X dev -m myapp
```

### 好的找出問題方法

- Trio instruments（一個用於檢測運行時的靈活框架）
- not_in_async 裝飾器

### Dynamic analysis 解決所有問題了嗎？

要確定：
- 測試所有的 edge case
- 測試所有的任務會穿插執行
- 不要介意測試時會等待很久

### 兼顧正確性的並行 -> structured concurrency

為什麼需要 structured concurrency？
- 子任務
- 超時

![picture 2](https://i.imgur.com/aWt2fIp.png)  
如果沒有 structured concurrency，你的程式可能會有以上問題

![picture 3](https://i.imgur.com/Pyimipk.png)  
-> 子任務必須在父任務結束前結束或是取消

如果沒有超時，任務可能會永遠等待
```python
with trio.move_on_after(1):
    await trio.sleep_forever()

with trio.fail_after(1):
    await trio.sleep_forever()
```
![picture 4](https://i.imgur.com/5Gp002C.jpg)  

## Static analyse
write a flake8 plugin to find bugs

[LibCST](https://github.com/Instagram/LibCST)



https://youtu.be/FrpUb6OEbcc?t=339

更多資訊：
https://us.pycon.org/2023/schedule/presentation/85/