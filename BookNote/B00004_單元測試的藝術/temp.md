幫我設計一個 Python 單元測試的範例框架 (要符合 pythonic 的風格)

測試範式：
- 每個被測試類都有一個抽象測試類
  - 有一個類屬性,指向被測試類
- 每個被測試方法都有一個測試類,繼承抽象測試類
  - 有一個類屬性,指向被測試方法

測試框架：
- ClassTestBaseClass
- MethodTestBaseClass
- FunctionTestBaseClass

三步驟：
prepare
run function to be tested
check result

假設每個測試都是一個函數
```python
def test_func(
    prepare,
    func,
    arg, 
    answer
    ):
    prepare()
    result = func(arg)
    assert result == answer


```