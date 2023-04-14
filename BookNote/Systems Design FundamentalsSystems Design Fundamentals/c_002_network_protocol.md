# network protocol

network protocol is a set of rules that governs the exchange of data between two or more devices on a network.

- IP: Internet Protocol
  - data packet
  - contains
    - header
      - from
      - to
      - ip protocol version
        - ipv4
        - ipv6
    - data
      - 2^16 bytes (64KB)
      - if data is too big, it will be split into multiple packets
  - 因為只能傳送64KB的資料,所以會有一個機制,來確保資料的完整性
    - TCP
    - UDP
- TCP: Transmit Control Protocol
  - let ip package be read in order
  - a function warper of ip
  - contain
    - header (in every ip package)
- HTTP: Hyper Text Transfer Protocol
  - request response paradigm
  - 把本來用作傳遞資料的 TCP 加入商業邏輯
  - contain
    - host
    - port
    - method
    - path
    - header: meta data
    - body