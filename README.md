# Distributed-Systems-Bloom-Filters-Coded-Bloom-Filter-Counting-Bloom-Filter
In this project, I have implemented  Bloom Filter, Coded Bloom Filter, Counting Bloom Filter. These are used in systems such as Google Bigtable, Apache HBase and Apache Cassandra and PostgreSQL etc.

Google Bigtable, Apache HBase and Apache Cassandra and PostgreSQL use Bloom filters to reduce the disk lookups for non-existent rows or columns. Avoiding costly disk lookups considerably increases the performance of a database query operation.

Counting filters provide a way to implement a delete operation on a Bloom filter without recreating the filter afresh. In a counting filter, the array positions (buckets) are extended from being a single bit to being an multibit counter. In fact, regular Bloom filters can be considered as counting filters with a bucket size of one bit.
