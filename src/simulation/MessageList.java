package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageList {
	Map<Integer, List<Message>> queue;
	int messageCount=0;
	MessageList(){
		queue=new HashMap<Integer,List<Message>>();
	}
	
	public String toString(){
		String s="L:"+queue.size();
		return s;
	}
	
	void addMessage(int id, Message m){
		messageCount++;
		if(queue.containsKey(id)){
			queue.get(id).add(m);
		} else {
			List<Message> l = new ArrayList<Message>();
			l.add(m);
			queue.put(id,l);
		}
	}
}
