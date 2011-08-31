package simulation;

import java.util.Random;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main m=new Main();
		m.run();
	}
	static int TOTAL_MAIN_NODES=250000;
	void run(){
		
		
		
		//Create 1st 8 nodes need to do this to ensure they can all get 8 connections.
		for (int i=0;i<9;i++){
			Node n=new Node(i,true); //will be stored in internal static list!
		}
		//Add 8 connections each
		Random r=new Random();
		int randNode;
		for (int i=0;i<9;i++){
			for(int j=0;j<8;j++){
				randNode=r.nextInt(Node.allNodes.size());
				Node.allNodes.get(i).addOutboundConnection(randNode);
			}
		}
		
		//Now create new nodes. Each node we randomly decide if it will accept connections or not.
		//we use a ratio of 4.7 as this is what we see in the real world (see bitcoinstatus.rowit.co.uk)
		for (int i=9;i<TOTAL_MAIN_NODES;i++){
			
			Node n=new Node(i,r.nextInt(100)<47); // the nextInt just sets whether the node will receive connections or not.
			for(int j=0;j<8;j++){
				randNode=r.nextInt(Node.allNodes.size());
				if(!Node.allNodes.get(i).addOutboundConnection(randNode))
					j--;
			}
			if (i%1000==0)
				System.out.println(i);
		}
		
		//This is a simulation to see if you run a client with 80 connections whether you get the message.
		//we create 100 of them and then see what proportion get the message.
		for(int i=0;i<100;i++){
			Node me=new Node(TOTAL_MAIN_NODES+i,false);
			for(int j=0;j<80;j++){
				randNode=r.nextInt(Node.allNodes.size());
				if(!me.addOutboundConnection(randNode))
					j--;
			}
		}
		
		
		//Print out the 1st 200 just for your information.
		for (int i=0;i<200;i++){
			System.out.println(Node.allNodes.get(i).toString());
		}
		
		//Just get the ID of any listening node.
		MessageList ml=new MessageList();
		int nodeToSendTo=0;
		do {
			nodeToSendTo=r.nextInt(Node.allNodes.size());
			
		} while (!Node.allNodes.get(nodeToSendTo).server);
		
		//Create the message. the 9 means nothing - is just some random content!
		ml.addMessage(nodeToSendTo,new Message(9,"addr",-1,nodeToSendTo));
		
		System.out.println("\n\n\nStats from addr message propagation.");
		//cycle through a number of rounds till message queue is clear.
		for(int round=0;round<10000;round++){
			//Cycle through all the nodes processing the messages.
			for(int node=0;node<Node.allNodes.size();node++){
				Node.allNodes.get(node).ProcessMessages(ml);
				
			}
			//Print out the number of messages in queue.
			System.out.println("Messages in queue:"+ml.queue.size()+" messages sent so far:"+ml.messageCount);
			if(ml.queue.size()==0)
				break;
		}
		
		int gotMessages=0;
		int servers=0;
		int serversGot=0;
		int serverConnections=0;
		
		//Now analyse number of nodes that received the message.
		for (Node n: Node.allNodes.values()){
			
			if (n.gotMessage){
				//System.out.println(Node.allNodes.get(i).toString());
				gotMessages++;
				if(n.server)
					serversGot++;
			}
			if(n.server){
				serverConnections+=n.connectionCount;
				servers++;
			}
		
		}
		
		//Now analyse how many of my special test nodes got the messages.
		int meGot=0;
		for(int i=0;i<100;i++){
			if (Node.allNodes.get(TOTAL_MAIN_NODES+i).gotMessage)
				meGot++;
		}
		
		System.out.println("80 Connection nodes that got message (out of 100):"+meGot);
	
		System.out.println("Nodes that got message:"+gotMessages+" Msg sent between nodes:"+ml.messageCount);
		System.out.println("Listening Nodes that got message:"+serversGot+" Total Listening Nodes:"+servers+" Cons"+serverConnections);
		System.out.println("Percent of Client Nodes receiving Message:"+(100*gotMessages/TOTAL_MAIN_NODES)+"%");
		System.out.println("Percent of Server Nodes receiving Message:"+(100*serversGot/servers)+"%");
		
		//now repeat with tx message
		System.out.println("\n\n\nStats from tx message propagation.");
		//Just get the ID of any listening node.
		 ml=new MessageList();
		nodeToSendTo=0;
		do {
			nodeToSendTo=r.nextInt(Node.allNodes.size());
			
		} while (!Node.allNodes.get(nodeToSendTo).server);
		
		//Create the message. the 9 means nothing - is just some random content!
		ml.addMessage(nodeToSendTo,new Message(9,"tx",-1,nodeToSendTo));
		
		//cycle through a number of rounds till message queue is clear.
		for(int round=0;round<10000;round++){
			//Cycle through all the nodes processing the messages.
			for(int node=0;node<Node.allNodes.size();node++){
				Node.allNodes.get(node).ProcessMessages(ml);
				
			}
			//Print out the number of messages in queue.
			System.out.println("Messages in queue:"+ml.queue.size()+" messages sent so far:"+ml.messageCount);
			if(ml.queue.size()==0)
				break;
		}
		
		gotMessages=0;
		servers=0;
		serversGot=0;
		serverConnections=0;
		
		//Now analyse number of nodes that received the message.
		for (Node n: Node.allNodes.values()){
			
			if (n.gotMessage){
				//System.out.println(Node.allNodes.get(i).toString());
				gotMessages++;
				if(n.server)
					serversGot++;
			}
			if(n.server){
				serverConnections+=n.connectionCount;
				servers++;
			}
		
		}
		
		//Now analyse how many of my special test nodes got the messages.
		meGot=0;
		for(int i=0;i<100;i++){
			if (Node.allNodes.get(TOTAL_MAIN_NODES+i).gotMessage)
				meGot++;
		}
		
		
		System.out.println("80 Connection nodes that got message (out of 100):"+meGot);
	
		System.out.println("Nodes that got message:"+gotMessages+" Msg sent between nodes:"+ml.messageCount);
		System.out.println("Listening Nodes that got message:"+serversGot+" Total Listening Nodes:"+servers+" Cons"+serverConnections);
		System.out.println("Percent of Client Nodes receiving Message:"+(100*gotMessages/TOTAL_MAIN_NODES)+"%");
		System.out.println("Percent of Server Nodes receiving Message:"+(100*serversGot/servers)+"%");
		
	}

}
