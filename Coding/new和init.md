```
class ProviderJobService(metaclass=abc.ABCMeta):
    # implement singleton
    _instance = None
    _initialize = False

    # provider service list
    _job_service_list: List[ProviderJobService] = []
    _job_service_map: dict[str, ProviderJobService] = {}

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            # set sub service instance
            cls._job_service_list.append(cls._instance)
        return cls._instance

    def __init__(self):
        if self._initialize is True:
            return

        self._job_id: str = self.get_job_name() + '_' + str(datetime.datetime.today().month) + '_' + str(datetime.date.today().day)
        self._job_service_map[self._job_id] = self

        self._initialize = True
        logging.info(f'{self.get_job_name()} service initialize')

```

在 Python 中，`__new__` 和 `__init__` 都是用於創建物件的，但他們之間的工作方式和目的有所不同。

- `__new__` 是一個靜態方法，當你嘗試創建新的物件實例時，會首先調用這個方法。它負責實例的創建，並在記憶體中為該實例分配空間。它是物件創建的第一步，並且可以返回任何類型的值。當返回的是該類別的實例時，`__init__` 會被調用。

- `__init__` 是一個初始化方法，在 `__new__` 創建實例後被調用。當實例被創建並且記憶體被分配後，該實例會被傳遞給 `__init__` 方法，然後你可以添加任何你希望添加的初始化參數。

總結來說，`__new__` 是創建新實例並為其分配記憶體的方法，而 `__init__` 則是初始化這個新創建的實例的方法。

在大部分情況下，你只需要實現 `__init__` 方法來進行初始化。但是，當你需要控制實例的創建（例如，實現設計模式如單例模式）時，你就需要使用 `__new__`。