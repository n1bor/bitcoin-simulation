package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Node {
	static Map<Integer,Node> allNodes;
	int seed;
	int nodeId;
	boolean server;
	List<NodeConnection> connections;
	Map<Integer,Integer> addressNodeMap;
	int connectionCount;
	static int MAX_CONNECTIONS=125;
	boolean gotMessage=false;
	
	
	public String toString(){
		String s=nodeId+" "+server+" "+connections.size()+" gotMg:"+gotMessage+" Cons:";
		for (int i=0;i<connections.size();i++){
			s=s+","+connections.get(i).nodeId;
		}
		return s;
	}
	
	Node(int id,boolean server){
		if (allNodes==null)
			allNodes=new HashMap<Integer,Node>(60000);
		
		allNodes.put(id,this);
		this.server=server;
		this.nodeId=id;
		connections=new ArrayList<NodeConnection>();
		addressNodeMap=new HashMap<Integer,Integer>();
		seed=new Random().nextInt(Integer.MAX_VALUE);
		connectionCount=0;
	}
	
	boolean addInboundConnection(int id){
		if(!server)
			return false;
		return addConnection(id,false);
	}
	
	boolean addOutboundConnection(int id){
		return addConnection(id,true);
	}
	
	private boolean addConnection(int id, boolean addOther){
		if(id==nodeId)
			return false;
		
		
		if (connectionCount>=MAX_CONNECTIONS)
			return false;
		NodeConnection n=new NodeConnection(id);
		
		if (connections.contains(n))
			return false;
		
		Node other=allNodes.get(id);
		
		if(!addOther){
			connections.add(n);
			connectionCount++;
			return true;
		}
		
		if( other.addInboundConnection(nodeId)){
			connections.add(n);
			connectionCount++;
			return true;		
		}
		
		return false;
	}
	
	void ProcessMessages(MessageList ml){
		
		List<Message> list=ml.queue.remove(nodeId);
		if(list==null)
			return;
		
		for (Message m : list){
			if (m==null)
				return;
			
			if(m.type=="addr"){
				gotMessage=true;
				Random r=new Random(seed+m.intData);
				//send to connections.
				
				for (int i=0;i<2;i++){
				
					int connId=r.nextInt(connections.size());
					int sendNodeId=connections.get(connId).nodeId;
					Message sendMes=new Message(m.intData,m.type,nodeId,sendNodeId);
					if(!connections.get(connId).sentAddressMessages.contains(m.intData)){
						ml.addMessage(sendNodeId, sendMes);
						connections.get(connId).sentAddressMessages.add(m.intData);
					}
				}
			}
		}
	}
}
