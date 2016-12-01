
import java.rmi.registry.Registry;

public class Main {
	
    /*
     * @numComp number of components, in other words the size of network
     */
	
	Registry registry;
	
	public static void startServer(int numComp){
		try{
			Registry registry = java.rmi.registry.LocateRegistry.createRegistry(1099);
			
			for(int i = 0; i < numComp; i++){
				registry.bind(Integer.toString(i), new ComponentImp(registry, i, numComp));
				new Thread((ComponentImp)(registry.lookup(Integer.toString(i)))).start();
			}
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		int numComp = 6; // Network size
		Main.startServer(numComp);
	}
	

}
