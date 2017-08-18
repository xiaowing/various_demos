luckymoney
=============

简介
----

使用GO语言实现了一个类似微信抢红包的Demo。该Demo实现了以下特性:

1. 抢红包的算法
2. 利用PostgreSQL的**advisory_lock**功能实现了一个简易的分布式锁
3. 通过`github.com/garyburd/redigo/redis`将金额数据在Redis中存取

特别注意
----
1. 使用PostgreSQL的**advisory_lock**实现分布式锁纯粹只是为了实现的方便。由于`advisory_lock`在PostgreSQL的主备集群中不会被同步至备机。因此，这就导致了advisory_lock本身无法做到高可用.一个无法做到高可用的分布式锁是不能用于生产环境的