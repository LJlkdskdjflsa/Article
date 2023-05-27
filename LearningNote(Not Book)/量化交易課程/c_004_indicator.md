# indicator

- 如何設計 indicator
- 使用 cumulative 的積分選取 indicator 參數
- 系統化高效批量分析 indicator
- 量化交易的工作方法和特點

## 如何設計 indicator
- 常見:
  - 均線
  - RSI
  - MACD
  - KDJ
  - Bolling
- 自己設計 indicator
- 將 indicator 引入策略
  - 單靠波動率歸納出的策略很可能掃瞄完所有參數還是沒有一個賺錢的

### Indicator 的要求: 平穩的時間序列(Stationality)
拿到時間序列後,在forcast 之前要先預處理, 把某些參數剔除掉,才有意義

平穩時間序列的定義:
- Mean, Variance 是常數
- 沒有 trend 和 seasonality

當然實操時不一定要那麼嚴格去檢驗

![picture 1](https://i.imgur.com/fQJ0DhU.jpg)  
只有第一個是平穩的時間序列、

![picture 2](https://i.imgur.com/LAeQzG5.jpg)  
- 兩均線的差 -> 不平穩,因為 varience 不恆定
- 兩均線的差/波動率 -> 平穩
- 不要使用網上的 indicator
  - 網站上的很多都沒有正規化過,需要正規化
  - 網上的很多數值都是約定成俗,沒有理論依據
    - 7,9,26 日的 MACD
    - 要自己去嘗試

最簡單的方法: 除以波動率
-> 做 indicator 也要盡量符合

![picture 3](https://i.imgur.com/lgJSn1n.jpg)  
技巧：暴力硬幹,把所有參數都枚舉出來,然後再去看哪些參數是有意義的

先測試過,後問人
做量化交易就是搞科研

![picture 4](https://i.imgur.com/hantDmS.jpg)  
未來函數：用某時段還沒發生的數據來回測這個時刻
-> 當函數複雜時,這是很容易犯的錯誤
結果太好很可能就是用了未來函數

## 使用 cumulative 的積分選取 indicator 參數

如何使用 Indicator:
- 金融市場: 信噪比很低 -> 需要濾波
- 濾波 -> 做積分
- Cumulative 曲線

![picture 5](https://i.imgur.com/2aVH2PR.jpg)  
對所有交易做積分
綠線：RSI == 25.13 
紅線：RSI == 37.93
過濾一定會過濾掉虧損和盈利
只是要確保統計上過濾掉的虧損>盈利

哪個 indicator 好用呢?
- 不存在"超級" indicator
- 理解系統的運作 -> 孤立單個的結論
- 批量分析所有能夠想到的 indicator, 尋找大尺度的 pattern

最大成功不是哪個 indicator 好
而是產生 indicator 的系統化方式和流程


## 系統化高效批量分析 indicator
-> 生成可視化分析報告,用肉眼觀察分析

![picture 6](https://i.imgur.com/oQNnolK.png)  

![picture 7](https://i.imgur.com/aiWSi7E.png)  
![picture 8](https://i.imgur.com/HLlC2VH.png)  
![picture 9](https://i.imgur.com/MqW9k9T.jpg)  

-> 找 indicator 是體力活
-> 大機構之所以成功是因為他們有好的基礎建設
- 用程式分析
- 用經驗選擇



## 量化交易的工作方法和特點
- 大圖像,系統 > 單個策略的細節
- 沒有神秘的超級攻勢/超級參數
- 獨立研究,像是科研
- 把自己當博士,而不是工程師

![picture 10](https://i.imgur.com/18Qlc0Y.png)  
重點：減法思維