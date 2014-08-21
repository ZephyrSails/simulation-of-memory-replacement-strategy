import java.util.ArrayList;
import java.util.Scanner;
import java.lang.String;
//import job;

public class SPS {
	static int spaceNum = 64;
	static ArrayList<job> MEMORY = new ArrayList<job>();
	static boolean state = false;// false for first fit, true for best fit
	public static void main(String args[]){
		String c;
		MEMORY.add(new job('0', spaceNum));
		try {
			c = args[0];
		}
		catch (ArrayIndexOutOfBoundsException e){
			System.out.println("usage: java SPS  [-f] or [-b] or [-r]");
			System.out.println("legend:");
			System.out.println("    -f\tSimulate first fit strategy.");
			System.out.println("    -b\tSimulate best fit strategy.");
			System.out.println("    -r\tReal time simulation, have fun!");
			System.out.println("\tMade by Zephyr Sails (XiuZhiPing)");
			return;
		}
		if (c.equals("-r")) {
			System.out.println("SPS -r activated, real time simulation started,Default strategy: first fit");
			System.out.println("printing 64 block for monitoring, use [h] command to get help");
			System.out.println("0.........1.........2.........3.........4.........5........6....");
			Scanner input = new Scanner(System.in);

			while (true) {
				System.out.print("command$ ");
				String cmd = input.nextLine();
				action(cmd);
			}
		}
		else if (c.equals("-f")) {
			System.out.println("SPS -f activated, simulation for first fit strategy,\nprinting 64 block for monitoring");
			System.out.println("0.........1.........2.........3.........4.........5........6....");
			state = false;
		}
		else if (c.equals("-b")) {
			System.out.println("SPS -b activated, simulation for best fit strategy,\nprinting 64 block for monitoring");
			System.out.println("0.........1.........2.........3.........4.........5........6....");
			state = true;
		}
		// work bench:
		addJob('1', 13);
		addJob('2', 6);
		addJob('3', 10);
		releaseJob('2');
		addJob('4', 20);
		releaseJob('3');
		releaseJob('1');
		addJob('5', 14);
		addJob('6', 6);
		addJob('7', 5);
		releaseJob('6');
	}

	public static void action(String cmd) {
		switch (cmd.charAt(0)) {
			case 'q': {
				System.out.println("Simulation shut down...hava a nice day");
				System.exit(0);
				break;
			}
			case 'a': {
				try {
					char name = cmd.charAt(2);
					int length = Integer.parseInt(cmd.substring(4));
					addJob(name, length);
					break;
				}
				catch(Exception e) {
					printHelp();
					break;
				}
			}
			case 'r': {
				try {
					char name = cmd.charAt(2);
					releaseJob(name);
					break;
				}
				catch(Exception e) {
					printHelp();
					break;
				}
				
			}
			case 'c': {
				try {
					char strategy = cmd.charAt(2);
					if (strategy == 'f') {
						System.out.println("Strategy changed to first fit.");
						state = false;
					}
					else if (strategy == 'b') {
						System.out.println("Strategy changed to best fit.");
						state = true;
					}
					break;
				}
				catch(Exception e) {
					printHelp();
					break;
				}
			}
			case 'p': {
				System.out.println("0.........1.........2.........3.........4.........5........6....");
				break;
			}
			case 's': {
				System.out.print("Current strategy is: ");
				if (state) {
					System.out.println("best fit");
				}
				else {
					System.out.println("first fit");
				}
				break;
			}
			default: {
				printHelp();
				break;
			}
		}
	}

	public static void printHelp() {
		System.out.println("real-time simulation mode, legend:");
		System.out.println("    [a name num]\n\tAdd process, need a char and a int for process name and space.");
		System.out.println("\tPLEASE NOTICE that process must be a char\n\tand space must be a number ten times smaller than actual one");
		System.out.println("\tFor example: a e 10 means add a process called 'e', and requiring 100 address spaces.");
		System.out.println("    [r name]\tRelease a process called 'name'.");
		System.out.println("    [c strategy]\n\tChange strategy to [f]irst fit or [b]est fit.");
		System.out.println("\tFor example: c f means change strategy to first fit.");
		System.out.println("    [s]\tShow current strategy.");
		System.out.println("    [p]\tPrint the ruler.");
		System.out.println("    [h]\tShow this helping document.");
		System.out.println("    [q]\tQuit this simulation.");
	}
	
	public static void printJob() {
		for (int i = 0; i < MEMORY.size(); i++) {
			if (MEMORY.get(i).name == '0') {
				// System.out.print('!');
				for (int j = 0; j < MEMORY.get(i).length; j++)
					System.out.print('#');
			}
			else {
				// System.out.print('!');
				System.out.print('[');
				int length = MEMORY.get(i).length;
				for (int j = 1; j < length/2; j++) {
					System.out.print('_');
				}
				System.out.print(MEMORY.get(i).name);
				for (int j = length/2+1; j < length-1; j++) {
					System.out.print('_');
				}
				System.out.print(']');
			}
		}
		System.out.println();
	}

	public static void addJob(char name, int length) {
		if (length < 3) {
			System.out.print("To get better printing result, please input length more than 2");
		}
		if (!state) {
			System.out.print("process " + name + " requiring " + length + "0 memory...");
			for (int i = 0; i < MEMORY.size(); i++) {
				if (MEMORY.get(i).name == '0' && MEMORY.get(i).length >= length) {
					int blank = MEMORY.get(i).length;
					MEMORY.remove(i);
					if (blank != length)
						MEMORY.add(i, new job('0', blank - length));
					MEMORY.add(i, new job(name, length));
					System.out.println("ALLOCATED!");
					printJob();
					return;
				}
			}
			System.out.println("DENIED!");
			printJob();
		}
		else {
			System.out.print("process " + name + " requiring " + length + "0 memory...");
			int bestFitBlock = -1;
			int bestFitBlank = spaceNum+1;
			for (int i = 0; i < MEMORY.size(); i++) {
				if (MEMORY.get(i).name == '0' && MEMORY.get(i).length >= length) {
					int l = MEMORY.get(i).length;
					if (l == length) {	// if just fit, no need for more try
						MEMORY.get(i).name = name;
						System.out.println("ALLOCATED!");
						printJob();
						return;
					}
					else if (l < bestFitBlank){	// if else, record the better one
						bestFitBlock = i;
						bestFitBlank = l;
					}
				}
			}
			if (bestFitBlock != -1) {	// fond one, replace
				MEMORY.remove(bestFitBlock);
				MEMORY.add(bestFitBlock, new job('0', bestFitBlank - length));
				MEMORY.add(bestFitBlock, new job(name, length));
				System.out.println("ALLOCATED!");
				printJob();
			}
			else {
				System.out.println("DENIED!");
				printJob();
			}

			//for ()
		}
	}

	public static void releaseJob(char name) {
		System.out.println("process " + name + " is going to be released...");
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
					/*
					else if (MEMORY.get(i+1).name == MEMORY.get(i).name) {	// back same
						int blank = MEMORY.get(i+1).length;
						int free = MEMORY.get(i).length;
						MEMORY.remove(i);
						MEMORY.remove(i);
						MEMORY.add(i, new job(MEMORY.get(i).name, blank + free));
						break;
					}*/
					else {	// no blank
						MEMORY.get(i).name = '0'; break;
					}
				}
				else if (i == MEMORY.size()-1) {	// in tail
					if (MEMORY.get(i-1).name == '0') {	// front blank
						int blank = MEMORY.get(i-1).length;
						int free = MEMORY.get(i).length;
						MEMORY.remove(i-1);
						MEMORY.remove(i-1);
						MEMORY.add(i-1, new job('0', blank + free));
						break;
					}
					/*
					else if (MEMORY.get(i-1).name == MEMORY.get(i).name) {	// front same
						int blank = MEMORY.get(i-1).length;
						int free = MEMORY.get(i).length;
						MEMORY.remove(i-1);
						MEMORY.remove(i-1);
						MEMORY.add(i-1, new job(MEMORY.get(i).name, blank + free));
						break;
					}*/
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
						break;
					}
					else if (MEMORY.get(i-1).name != '0' && MEMORY.get(i+1).name == '0') {	// back blank
						int blank = MEMORY.get(i+1).length;
						int free = MEMORY.get(i).length;
						MEMORY.remove(i);
						MEMORY.remove(i);
						MEMORY.add(i, new job('0', blank + free));
						break;
					}
					else if (MEMORY.get(i-1).name == '0' && MEMORY.get(i+1).name != '0') {	// front blank
						int blank = MEMORY.get(i-1).length;
						int free = MEMORY.get(i).length;
						MEMORY.remove(i-1);
						MEMORY.remove(i-1);
						MEMORY.add(i-1, new job('0', blank + free));
						break;
					}
					/*
					else if (MEMORY.get(i-1).name == MEMORY.get(i).name && MEMORY.get(i+1).name == MEMORY.get(i).name) {	// both same
						int fblank = MEMORY.get(i-1).length;
						int bblank = MEMORY.get(i+1).length;
						int free = MEMORY.get(i).length;
						MEMORY.remove(i-1);
						MEMORY.remove(i-1);
						MEMORY.remove(i-1);
						MEMORY.add(i-1, new job(MEMORY.get(i).name, fblank + bblank + free));
						break;
					}
					else if (MEMORY.get(i-1).name != MEMORY.get(i).name && MEMORY.get(i+1).name == MEMORY.get(i).name) {	// back same
						int blank = MEMORY.get(i+1).length;
						int free = MEMORY.get(i).length;
						MEMORY.remove(i);
						MEMORY.remove(i);
						MEMORY.add(i, new job(MEMORY.get(i).name, blank + free));
						break;
					}
					else if (MEMORY.get(i-1).name == MEMORY.get(i).name && MEMORY.get(i+1).name != MEMORY.get(i).name) {	// front same
						int blank = MEMORY.get(i-1).length;
						int free = MEMORY.get(i).length;
						MEMORY.remove(i-1);
						MEMORY.remove(i-1);
						MEMORY.add(i-1, new job(MEMORY.get(i).name, blank + free));
						break;
					}*/
					else if (MEMORY.get(i-1).name != '0' && MEMORY.get(i+1).name != '0') {	// no blank
						MEMORY.get(i).name = '0'; break;
					}
				}
			}
		}
		printJob();
	}
}