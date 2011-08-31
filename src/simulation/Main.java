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
	static int TOTAL_MAIN_NODES=200000;
	void run(){
		
		
		
		//Create 1st 8 nodes
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
		
		for (int i=9;i<TOTAL_MAIN_NODES;i++){
			
			Node n=new Node(i,r.nextInt(47)<10);
			for(int j=0;j<8;j++){
				randNode=r.nextInt(Node.allNodes.size());
				if(!Node.allNodes.get(i).addOutboundConnection(randNode))
					j--;
			}
			if (i%1000==0)
				System.out.println(i);
		}
		
		for(int i=0;i<100;i++){
			Node me=new Node(TOTAL_MAIN_NODES+i,false);
			for(int j=0;j<80;j++){
				randNode=r.nextInt(Node.allNodes.size());
				if(!me.addOutboundConnection(randNode))
					j--;
			}
		}
		
		
		
		for (int i=0;i<200;i++){
		
			System.out.println(Node.allNodes.get(i).toString());
		}
		
		MessageList ml=new MessageList();
		int nodeToSendTo=0;
		do {
			nodeToSendTo=r.nextInt(Node.allNodes.size());
			
		} while (!Node.allNodes.get(nodeToSendTo).server);
		
		ml.addMessage(nodeToSendTo,new Message(9,"addr",-1,nodeToSendTo));
		for(int round=0;round<10000;round++){
			for(int node=0;node<Node.allNodes.size();node++){
				Node.allNodes.get(node).ProcessMessages(ml);
				
			}
			System.out.println(ml);
			if(ml.queue.size()==0)
				break;
		}
		
		int gotMessages=0;
		int servers=0;
		int serversGot=0;
		int serverConnections=0;
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
		int meGot=0;
		for(int i=0;i<100;i++){
			if (Node.allNodes.get(TOTAL_MAIN_NODES+i).gotMessage)
				meGot++;
		}
		System.out.println("Me got:"+meGot);
	
		System.out.println("Got:"+gotMessages+" Msg Count:"+ml.messageCount);
		System.out.println("SGot:"+serversGot+" Servers:"+servers+" Cons"+serverConnections);
	}

}
