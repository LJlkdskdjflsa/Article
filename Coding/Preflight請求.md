# 衝破次元壁的工程師 | Preflight 請求

tags: python, backend, fastapi, cors, preflight

最近在做 side project 遇到了一個問題，我需要讓所有打 API 必須帶 API key 才能使用。
因為是所有 API 都要擋，之前看到 FastAPI 的範例都是在 controller 層加上 dependence injection 的方式

[範例(testdriven.io)](https://testdriven.io/tips/6840e037-4b8f-4354-a9af-6863fb1c69eb/)
```python
from fastapi import FastAPI, Body, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer

api_keys = [
    "akljnv13bvi2vfo0b0bw"
]  # This is encrypted in the database

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")  # use token authentication


def api_key_auth(api_key: str = Depends(oauth2_scheme)):
    if api_key not in api_keys:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Forbidden"
        )


app = FastAPI()


@app.get("/protected", dependencies=[Depends(api_key_auth)])
def add_post() -> dict:
    return {
        "data": "You used a valid API key."
    }


####################################


# call API
import requests

url = "http://localhost:8000/protected"

# The client should pass the API key in the headers
headers = {
  'Content-Type': 'application/json',
  'Authorization': 'Bearer akljnv13bvi2vfo0b0bw'
}

response = requests.get(url, headers=headers)
print(response.text)  # => "You used a valid API key."
```

但...我就想既然全部要 API 都要擋我就乾脆在 middleware 擋就好啦，只要把要排除的 endpoint 給拿掉就好，像是以下

```python
def set_middleware(_app: FastAPI) -> None:

    _app.add_middleware(
        middleware_class=CORSMiddleware,
        allow_origins=['*'],
        allow_credentials=True,
        allow_methods=['*'],
        allow_headers=['*'],
    )

    # 這次新加入的 middleware
    _app.add_middleware(
        middleware_class=APIKeyMiddleware,
        allow_paths=['/docs', '/openapi.json'],
    )

    _app.add_middleware(
        middleware_class=ErrorHandlerMiddleware,
    )
```

本地端、測試環境測試都沒問題，但部署完後前端居然跑不出來了
而且錯誤居然是 CORS 的問題，我就很疑惑，我明明沒有動到 CORS 的代碼，怎麼以前沒有的問題現在就有了？
難道又是次元壁要阻擋人類的科技發展，於是封鎖了我的 API 嗎？
在幾番確認和寫 sample code 後，發現真的是我的問題，並且如果拿掉這次新加入的 middleware 就可以正常運作了
但有些 tricky 的是，測起來有時候可以，有時候不可以，真是薛丁格的 CORS 啊
去睡了一覺後，為了人類和我項目的未來，我必須要打破次元壁
但理論上不可能啊，如果每次請求都有 api key，那麼應該也可以直接打通才對
後來去搜尋了一下發現了 preflight 這個東西，原來是瀏覽器會先發一個 OPTIONS 的請求，來確認是否可以正常連線
終於找到次元壁了！！！
[preflight 相關文章](https://vii120.coderbridge.io/2020/11/16/preflight-request/)

## preflight 請求

"Preflight" 請求是一種由瀏覽器自動發送的 HTTP OPTIONS 請求，這種請求會在實際的跨域請求之前發送，用以確認實際請求是否安全可行。

以下是一些可能會導致瀏覽器發送 preflight 請求的情況：

1. 請求的 HTTP 方法不是 GET、HEAD 或 POST。
2. POST 請求的 Content-Type 不是 application/x-www-form-urlencoded、multipart/form-data 或 text/plain。
3. 請求包含了除 CORS 安全列出的一些頭部之外的其他頭部信息。

Preflight 請求的目的是檢查網頁應用的實際 HTTP 請求是否被目標伺服器所允許。這是一種安全機制，防止不可接受的跨網站請求。該請求通過發送一個帶有請求方法（例如 PUT）和頭部（例如 API key）的 OPTIONS 請求，以查看伺服器是否接受這樣的請求。如果伺服器回應說它允許這樣的請求，那麼瀏覽器才會發送實際的 HTTP 請求。

> 原來不是請求沒過，而是 preflight 沒過啊！！！

## 這次錯誤的解釋

在 FastAPI 中，middleware 的執行順序對應用程式的運作有很大的影響。middleware 在處理請求和響應時，會按照它們被添加到 FastAPI 實例的順序進行。這意味著第一個添加的 middleware 首先處理請求，並且最後處理響應。相反，最後一個添加的 middleware 將最後處理請求，並且首先處理響應。

在你的情況下，如果 APIKeyMiddleware 在 CORSMiddleware 之前運行，那麼會在跨域請求的 "preflight" 請求（一種由瀏覽器發送的 OPTIONS 請求，用於檢查實際請求是否安全）中需要 API key。這通常是不正確的，因為 "preflight" 請求通常不會包含任何認證信息（例如 API keys）。

因此，你需要先運行 CORSMiddleware 來正確處理這些 "preflight" 請求，然後再運行 APIKeyMiddleware 以處理實際的請求。這就是為什麼你需要調整它們的順序。

換句話說，CORSMiddleware 應該在所有其他 middleware 之前運行，以確保所有跨域 "preflight" 請求都能正確處理，而不會被其他 middleware（例如需要 API key 的 middleware）攔截。

在這種情況下，OPTIONS 請求的處理通常不需要和實際的 GET 或 POST 請求一樣的認證或授權。因此，如果你在處理 OPTIONS 請求的路由器或中間件中需要某種認證（例如 API key），則可能會阻止 preflight 請求的正常工作。因此，我們通常建議將處理 CORS 的 middleware 放在其他 middleware 之前。

## 解決方法

把 middleware 的順序調換就可以了

```python
def set_middleware(_app: FastAPI) -> None:
    _app.add_middleware(
        middleware_class=APIKeyMiddleware,
        allow_paths=['/docs', '/openapi.json'],
    )
    _app.add_middleware(
        middleware_class=CORSMiddleware,
        allow_origins=['*'],
        allow_credentials=True,
        allow_methods=['*'],
        allow_headers=['*'],
    )

    _app.add_middleware(
        middleware_class=ErrorHandlerMiddleware,
    )
```

又衝破了一次次元壁！！！
希望未來風和日麗，世界和平
三體人不要再搞我了

## Reference

- [不只是簡單的跨域請求 Preflight Request](https://vii120.coderbridge.io/2020/11/16/preflight-request/)
- https://testdriven.io/tips/6840e037-4b8f-4354-a9af-6863fb1c69eb/