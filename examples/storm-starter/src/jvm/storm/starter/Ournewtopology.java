package storm.starter;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.testing.TestWordSpout;
//trying to input data from a new file(source) 
import backtype.storm.topology.OutputFieldsDeclarer;
import storm.starter.spout.RandomSentenceSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;

public class Ournewtopology {
    public static class OurnewBolt extends BaseRichBolt {
    OutputCollector _collector;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
		_collector.emit(tuple, new Values(tuple.getString(0).length()));
		_collector.ack(tuple);
		System.out.println("in execute"+tuple.getString(0).length());			
	}
	@Override 
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word_length"));
		System.out.println("in declare word_length");	
	}
  }
  
    public static class OursecondBolt extends BaseRichBolt {
    OutputCollector _collector;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
     //	int ab = tuple.getString(0).length();

	//if(ab>3)
	//{
		_collector.emit(tuple, new Values(tuple.getString(0).length()));
		_collector.ack(tuple);
		//_collector.emit(tuple, new Values(thresh(tuple)));
		//_collector.ack(tuple);
		System.out.println("in second execute"+tuple.getString(0).length());			
	//}
	//else
	//	{
	//		continue;		
		//}
		
     }
    public int thresh(Tuple tuple)
	{
		int ab = tuple.getString(0).length();
		if(ab>2)
			return ab;
		else 
			return 0;
	}

	@Override 
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("threshold"));
		System.out.println("in declare word_length");	
	}
  }
    public static class OurthirdBolt extends BaseRichBolt {
    OutputCollector _collector;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
     //	int ab = tuple.getString(0).length();

	//if(ab>3)
	//{
		_collector.emit(tuple, new Values(thresh(tuple)));
		_collector.ack(tuple);
		System.out.println("in third execute"+tuple.getString(0).length());			
	//}
	//else
	//	{
	//		continue;		
		//}
		
     }
    public int thresh(Tuple tuple)
	{
		int ab = tuple.getString(0).length();
		if(ab>2)
			return ab;
		else 
			return 0;
	}

	@Override 
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("threshold"));
		System.out.println("in declare word_length");	
	}
  }
public static void main(String[] args) throws Exception {
    TopologyBuilder builder = new TopologyBuilder();
    RandomSentenceSpout some = new RandomSentenceSpout();
    builder.setSpout("sentence", /*some.nextTuple()*/ new RandomSentenceSpout() , 10);
    builder.setBolt("count_letters", new OursecondBolt(), 5).shuffleGrouping("sentence");
    builder.setBolt("divide_letters", new OurthirdBolt(), 5).shuffleGrouping("count_letters");

    //the amount of parrlelism you want for each bolt is set to 5 here ^
    //builder.setBolt("exclaim2", new OurnewBolt(), 2).shuffleGrouping("exclaim1");
    Config conf = new Config();
    conf.setDebug(true);

    if (args != null && args.length > 0) {
      conf.setNumWorkers(3);
      StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
    }
    else {

      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology("test", conf, builder.createTopology());
      Utils.sleep(10000);
      cluster.killTopology("test");
      cluster.shutdown();
    }
  }

}
