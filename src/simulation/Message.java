package simulation;

public class Message {
	String type;
	int	intData;
	int fromNode;
	int toNode;
	
	Message(int intData, String type,int from,int to){
		this.intData=intData;
		this.type=type;
		this.fromNode=from;
		this.toNode=to;
	}
	
	public String toString(){
		return "t:"+toNode+" f:"+fromNode+" "+type+" "+intData;
		
	}

}
