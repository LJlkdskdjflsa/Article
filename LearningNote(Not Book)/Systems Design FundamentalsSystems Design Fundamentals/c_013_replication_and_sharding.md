# Replication and Sharding

## Replication
Main database server is called the master, and the other servers are called slaves. 
The master is the only server that can write to the database. 
The slaves are read-only copies of the master. 
The slaves are used to handle read requests.
The slaves are updated by copying data from the master. 
The master and slaves are usually on different machines.

replication is used to increase the read throughput of a database.
## Sharding
Sharding is a way to distribute data across multiple machines.
Sharding is used to increase the write throughput of a database.
