import java.util.ArrayList;
//import job;

public class SPS {
	static int spaceNum = 64;
	static ArrayList<job> MEMORY = new ArrayList<job>();
	static boolean state = false;// false for first fit, true for best fit
	public static void main(String args[]){
		System.out.println("SPS_start, printing 64 block for simulation");
		System.out.println("0.........1.........2.........3.........4.........5........6....");
		MEMORY.add(new job('0', 64));
		addJob('a', 16);
		addJob('b', 16);
		addJob('c', 16);
		addJob('d', 16);
		releaseJob('d');
		printJob();
		releaseJob('b');
		printJob();
		releaseJob('c');
		printJob();
		String c;
		System.out.println("    [h]\tShow this helping document.");
		System.out.println("    [q]\tQuit this simulation.");
	}
	
	public static void printJob() {
		for (int i = 0; i < MEMORY.size(); i++) {
			if (MEMORY.get(i).name == '0') {
				for (int j = 0; j < MEMORY.get(i).length; j++)
					System.out.print('#');
			}
			else {
				for (int j = 0; j < MEMORY.get(i).length; j++)
					System.out.print('!');
			}
		}
		System.out.println();
	}

	public static void addJob(char name, int length) {
		if (!state) {
			for (int i = 0; i < MEMORY.size(); i++) {
				if (MEMORY.get(i).name == '0' && MEMORY.get(i).length > length) {
					int blank = MEMORY.get(i).length;
					MEMORY.remove(i);
					if (blank != length)
						MEMORY.add(i, new job('0', blank - length));
					MEMORY.add(i, new job(name, length));
					break;
				}
			}
		}
	}

	public static void releaseJob(char name) {
		if (!state) {
			for (int i = 0; i < MEMORY.size(); i++) {
				if (MEMORY.get(i).name == name) {
					if (i == 0) {	// in head
						if (MEMORY.get(i+1).name == '0') {	// back blank
							int blank = MEMORY.get(i+1).length;
							int free = MEMORY.get(i).length;
							MEMORY.remove(i);
							MEMORY.remove(i);
							MEMORY.add(i, new job('0', blank + free));
							break;
						}
						else {	// no blank
							MEMORY.get(i).name = '0'; break;
						}
					}
					else if (i == MEMORY.size()) {	// in tail
						if (MEMORY.get(i-1).name == '0') {	// front blank
							int blank = MEMORY.get(i-1).length;
							int free = MEMORY.get(i).length;
							MEMORY.remove(i-1);
							MEMORY.remove(i-1);
							MEMORY.add(i-1, new job('0', blank + free));
							break;
						}
						else {	// no blank
							MEMORY.get(i).name = '0'; break;
						}
					}
					else {	// in middle
						if (MEMORY.get(i-1).name == '0' && MEMORY.get(i+1).name == '0') {	// both blank
							int fblank = MEMORY.get(i-1).length;
							int bblank = MEMORY.get(i+1).length;
							int free = MEMORY.get(i).length;
							MEMORY.remove(i-1);
							MEMORY.remove(i-1);
							MEMORY.remove(i-1);
							MEMORY.add(i-1, new job('0', fblank + bblank + free));

						}
						else if (MEMORY.get(i-1).name != '0' && MEMORY.get(i+1).name == '0') {	// back blank
							int blank = MEMORY.get(i-1).length;
							int free = MEMORY.get(i).length;
							MEMORY.remove(i-1);
							MEMORY.remove(i-1);
							MEMORY.add(i-1, new job('0', blank + free));
							break;
						}
						else if (MEMORY.get(i-1).name == '0' && MEMORY.get(i+1).name != '0') {	// front blank
							int blank = MEMORY.get(i+1).length;
							int free = MEMORY.get(i).length;
							MEMORY.remove(i);
							MEMORY.remove(i);
							MEMORY.add(i, new job('0', blank + free));
							break;
						}
						else if (MEMORY.get(i-1).name != '0' && MEMORY.get(i+1).name != '0') {	// no blank
							MEMORY.get(i).name = '0'; break;
						}
					}
				}
			}
		}
	}

}