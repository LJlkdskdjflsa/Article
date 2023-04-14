source: https://lundao.tech/blog/crvusd/

LLAMMA (Lending-Liquidating AMM Algorithm)
這樣的清算機制其實不只可以用在穩定幣上面，其他有清算需求的專案也都可以參考這樣的設計。

## 名詞解釋
- PegKeeper -> print money



## 目前的清算機制(DAI)
目前清算機制的問題：
- 當清算結束後，如果抵押品拍賣的價格比較好，其中一部分的抵押品可能就不會被清算掉，此時就會還給債務人。但是大部分的抵押品通常都會被清算導致損失慘重，即使價格回穩，但清算已經造成傷害。
![](https://lundao.tech/assets/images/traditional-liquidation-a640d1627bd418a2e8130a27e3245a94.png)

## 傳統 AMM
![](https://lundao.tech/assets/images/tranditional-AMM-61827a9c7dbeeb7f14fa826474c39db5.png)

## LLAMMA
清算 + AMM

改進點：
- 價格回升後被清算人不會有損失
- 
清算演算法 LLAMMA，其結合 AMM 的特性讓抵押品在價格下跌時逐漸轉移成穩定幣，讓原本要清償的債務有一定程度的穩定幣可以償還，同時在價格回穩時再逐漸把穩定幣換回抵押品，而不是硬生生的觸發清算導致債務人的虧損。

## 鑄造
鑄造 crvUSD 的時候同樣的會需要存入抵押品如 ETH 到智能合約來，不一樣的是這個抵押品會被放入一個 LLAMMA 特殊設計的 AMM 當中。跟 Uniswap 的 Range Order 一樣，剛存入抵押品到 AMM 時會以單邊的形式存在，而當抵押品價格下跌到一定程度時，就會逐漸換成穩定幣。
舉例來說當 ETH 價格為 3000 美金的時候，使用者可以抵押 1 ETH 到系統的智能合約裡面。此時系統會鑄造出 crvUSD 給使用者，同時抵押的 1 ETH 會被類似 Uniswap v3 的方式被放入一個特殊的 AMM 裡面，比如說流動性會被放在 500-550 USD 這個區間當中，而所取得的 LP 代幣則會只有單邊的 1 ETH + 0 crvUSD 流動性。

![](https://lundao.tech/assets/images/liquidity-of-LLAMMA-257b1cd61a7cf5a0d0c23633505753da.png)

當 ETH 價格跌到 550 USD 以下時，系統會開始慢慢的把 ETH 換成 crvUSD，當跌到 500 USD 以下時，所有的流動性就會變成 crvUSD。當價格跌到這個區間時，系統將會用比市場上更便宜的價格售出 ETH，此時就會吸引市場上的人來到這個特殊的 AMM 裡面來買 ETH 來獲得折扣價。
因為系統會逐漸的把抵押品 ETH 換成穩定幣 crvUSD，所以就不會像 DAI 一樣到硬清算門檻後就直接把所有抵押品都清算掉。更進一步的是當抵押品 ETH 的價格如果回穩後，使用者的流動性又會逐漸的從穩定幣 crvUSD 換成 ETH。
![](https://lundao.tech/assets/images/liquidation-of-LLAMMA-54449a8ac1ba84dd3c2711f048425154.png)

不過這個換回抵押品的過程也可能出現 Path-Dependence 的問題，我們在最後的結論與疑問章節來討論這個問題。

這邊有三件事情要特別注意：

- LLAMMA 跟傳統 AMM 的方向相反：傳統 AMM 放入單邊資產之後，要價格提升才可以換成另外一邊，而 LLAMMA 的特殊 AMM 則是價格下跌之後才會換成另外一個資產（穩定幣）
- LLAMMA 需要依賴外部的預言機 (Oracle) 價格：因為 LLAMMA 需要賣的比外面的更便宜，所以會需引入預言機價格，才可以知道有流動性的時候要如何折扣定價來吸引套利者，外部價格跟內部價格差距越大時，LLAMMA 就會給出更多折扣。
- crvUSD 還是有清算機制：雖然可以透過 LLAMMA 的特殊 AMM 逐漸轉換成穩定幣，但是如果債務健康狀況太糟糕，還是會有最後的清算機制

## Traditional AMM vs LLAMM

- AMM
  - function
    - provide liquidity
    - give price (pricing mechenism)
- LLAMM
  - function
    - provide liquidity
    - keep stable coin over collateral

## 透過測試案例解釋實際運作方式
- 波段
- 存入抵押品來鑄造 crvUSD
- 交易折扣
- crvUSD 的穩定機制 PegKeeper


### 波段 (band)
使用者把抵押品存入系統時，抵押品會被存入的區段，當預言機價格移動到這個區間時，使用者的抵押品就會開始被轉換成穩定幣。而剛開始建立 AMM 時會根據預言機的價格切分波段，其中預言機價格所在的波段會是波段 0 (band 0)，預言機價格越低，波段編號越高。
![](https://lundao.tech/assets/images/bands-and-oracle-price-20be22f01f9c17e5000584d1a51b0f32.png)

band 其實跟 TraderJoe 的 Liquidity Book 機制相同
延伸閱讀：https://medium.com/avalanchetw/traderjoe-v2-liquidity-book-%E6%B5%81%E5%8B%95%E6%80%A7%E8%A8%82%E5%96%AE%E7%B0%BF-%E4%B8%AD%E6%96%87%E7%BF%BB%E8%AD%AF-f72c50c75d4a



### 存入抵押品來鑄造 crvUSD

存入的抵押品將會平均的佈署在這五個波段上，也可以透過 AMM.bands_y(band_number) 來取得特定波段的抵押品數量，比如說 bands_y(170) 會得到該波段總共有 200 WETH 的抵押品，查詢 170 - 174 都會是 200 WETH。

### 跟 LLAMMAS 交易折扣的 WETH 抵押品
curve-stablecoin 提供了許多方便的工具可以使用，如測試用的預言機讓我們可以直接修改預言機價格進行測試。

當預言機價格變低時，AMM 拋售抵押品的價格也會進一步的降低，讓套利者願意到系統裡面來套利，進一步的協助債務人的債務有更多穩定幣支撐。

## crvUSD 的穩定機制 PegKeeper

crvUSD 
- LLAMMA 執行轉換與清算
- PegKeeper 合約進行穩定幣的錨定功能

crvUSD + 3Crv -> MetaPool。

when crvUSD > 1 USD 
- PegKeeper 會鑄造出無抵押的 crvUSD 並且注入這個 StableSwap

when crvUSD < 1 USD 
- PegKeeper 可以從 StableSwap 抽出資金並且銷毀，並且讓套利者進來重新平衡價格，讓市場上的 crvUSD 上漲靠近 1 USD。

> 這個 PegKeeper 合約雖然可以鑄造與銷毀 crvUSD，但它在智能合約的邏輯限制只能夠針對特定的 StableSwap 池子注入與抽出 crvUSD 來限制用途。

## 小結
### LLAMMA 清算機制
#### 優
- LLAMMA 清算機制
  - 對於債務人更有友善,當價格回穩的時候損失的程度就會大幅降低 (但也還是有清算機制)
  - 其他平台：當外部的 DEX 流動性不足時，穩定幣/借貸平台就有可能產生壞帳
  - 需要清算的部位就會比傳統平台要小得多
    - 對於外部 DEX 的流動性的依賴就會低很多，加強平台的自主運作能力

#### 劣
- 交換代幣的折扣的隱憂
  -  Path-Dependence
     -  LLAMMA 內部用的 AMM （以下稱為 LLAMM）的定價 p 會參考外部預言機的定價 p0
        -  價格緩慢上漲
           -  LLAMM 池子可能會以些微折扣的方式買回 ETH，比如說均價會一點點低於標定價格
        -  價格快速上漲
           -  快到套利者沒有在這之間進行套利(跟不上預言機價格) - 用更高價收購
​
> 如果站在 LLAMM 池子的角度來說，價格緩慢上漲的狀況會讓收購價格比較合理，但是如果遇到快速上漲時，就會造成 LLAMM 池子承受價差損失，而這個價差損失也就是債務人的損失。
> 當然，如果跟完全被清算相較起來，或許這樣的價差損失還是比較小。但由於之中還有許多不確定的因素，還會需要更多的資訊來判斷可能造成的損失程度。
> 總體來說，LLAMMA 確實是一個可以進一步研究的新清算方法，而且不一定是穩定幣，有需要清算的場合如借貸平台或是去中心化的衍生性商品協議也可以考慮用同樣的清算方式來減低債務人在清算抵押品帶來的損失。


### PegKeeper 穩定機制
PegKeeper 透過 Curve MetaPool 所進行的穩定掛勾
- DAI 是透過升息、降息的方式調整市場熱度
- PegKeeper 則是更為激進直接透過類似量化寬鬆/緊縮的方式印出鈔票投到所需的市場
  - Alought: 智能合約的限制還是會比起量化寬鬆更為透明
  - But: 還需要進一步深究是否有影響市場經濟所帶來的副作用。


MetaPool 當中新鑄造的 crvUSD 是無抵押生成的
- 或者是說是以另外一側的 LP 代幣如 3crv 作為抵押生成，而透過套利者的搬運來讓 crvUSD 重新錨定到 1 美金
- 會損害流動性提供者的利益。因為套利者透過價差套利時，流動性提供者就作為他的對家承受損失。
- 還不知道交易費是否足以覆蓋價差的損失，可能還需要進一步的機制去補償流動性提供者才可以完成整個穩定機制。如果沒有獎勵機制，要如何鼓勵使用者來為 crvUSD 提供穩定機制？
- ?: 賄選?

## the code deep dive
### test_create_repay.py

test_create_loan
-> create_loan
test_max_borrowable
existing_loan
test_repay_all
test_repay_half
test_add_collateral
test_borrow_more

