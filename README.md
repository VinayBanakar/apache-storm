# apache-storm

Storm is a distributed realtime computation system. Similar to how Hadoop provides a set of general primitives for doing batch processing, Storm provides a set of general primitives for doing realtime computation.

install zookepeer and zermq and jzmq and then set up zookeper

in fact follow this http://10jumps.com/blog/storm-installation-single-machine and setup the system, 

then make sure zookeper is started 
sudo sh zkServer.sh start 

then storm (sudo)

./storm nimbus

./storm supervisor

./storm ui

to run word count toplogy :

./storm jar ~/ccbd-work/storm2/apache-storm-0.10.0/examples/storm-starter/storm-starter-topologies-0.10.0.jar storm.starter.WordCountTopology WordCount -c nimbus.host=localhost

to kill wordcount :
./storm kill WordCount

Ournewtopology.java ==> make sure you rebuild the package with any changes to this, and giver correct path while running the topology 

================================================================================================
tasks: 100 spouts with different pattern of inputs and its respective toplogy to be built, 
look into RandomSentenceSpout.java to see how spouts are setup 
