package simulation;

import java.util.HashSet;
import java.util.Set;

public class NodeConnection implements Comparable {
	int nodeId;
	Set<Integer> sentAddressMessages;
	NodeConnection(int id){
		nodeId=id;
		sentAddressMessages=new HashSet<Integer>();
	}
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return nodeId=((NodeConnection)arg0).nodeId;
	}
	
}
