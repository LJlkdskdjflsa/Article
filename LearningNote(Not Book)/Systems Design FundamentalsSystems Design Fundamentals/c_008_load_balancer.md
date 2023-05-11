# Load Balancer (负载均衡器)

<!-- TOC -->

* [Load Balancer (负载均衡器)](#load-balancer--负载均衡器-)
    * [1. What is Load Balancer](#1-what-is-load-balancer)
    * [2. Load Balancer Types](#2-load-balancer-types)
    * [3. Load Balancer Algorithms](#3-load-balancer-algorithms)

<!-- TOC -->

## 1. What is Load Balancer

Load balancer is a device that distributes network or application traffic across a cluster of servers.
It is a hardware or software component that distributes network or application traffic across a cluster of servers.
Load balancers are used to increase capacity (concurrent users) and reliability of applications.
They improve the overall performance of applications by decreasing the burden on servers.
Load balancers are used to distribute traffic across multiple servers. They are used to increase capacity (concurrent
users) and reliability of applications.
They improve the overall performance of applications by decreasing the burden on servers.

DNS round robin is a simple load balancing technique that distributes traffic across multiple servers.

## 2. Load Balancer Types

- Hardware Load Balancer
    - more expensive
- Software Load Balancer
    - more flexible
        - can make more customized

## 3. Load Balancer Algorithms

- random
    - Problem: still may have one server overloaded
- round robin
    - go through the list of servers one by one
- weighted round robin
    - assign weights to servers
    - go through the list of servers one by one
    - the server with higher weight will be selected more frequently
    - useful when some servers are more powerful than others
- base on performance
    - need to health check
    - select the server with the lowest response time
- ip hash
    - hash the ip address of the client
    - always send the same client to the same server
    - useful when
        - the server needs to maintain session state with the client
        - cache is used
- path hash
    - hash the path of the request
    - always send the same request to the same server
    - when new deploy, it will only affect a small portion of server
    - when some functionality is broken, it will only affect a small portion of server
    - useful when
        - cache is used

當設計一個系統時,也可以同時使用很多負載均衡器,並且使用不同的演算法,來達到最好的效果
也可以有多層的 load balancer