public class SPS {
	static int jobNum = 7;
	static int spaceNum = 64;
	static boolean memory[] = new boolean[spaceNum];
	static boolean job[][] = new boolean[jobNum][spaceNum];
	static boolean meanNoting;
	public static void main(String args[]){
		System.out.println("SPS_start, printing 64 block for simulation");
		System.out.println("0.........1.........2.........3.........4.........5........6....");
		sertJob(1, 0, 12);
		sertJob(3, 16, 18);
		printMemory();
	}

	public static void sertJob(int jobs, int begin, int end) {
		for (int i = begin; i <= end; i++) {
			job[jobs][i] = true;
			memory[i] = true;
		}
	}

	public static void printMemory() {
		for (int i = 0; i < jobNum; i++) {
			int begin = 0, end = 0;
			boolean inJob = false;
			for (int j = 0; j < spaceNum; j++) {
				if (!job[i][j] && !inJob) {
					System.out.print(" ");
				}
				else if (job[i][j] && !inJob) {
					System.out.print("[");
					begin = j;
					inJob = true;
				}
				else if (job[i][j] && inJob) {
					// do nothing
				}
				else if (!job[i][j] && inJob) {
					end = j;
					int length = end - begin;
					int jobPlace = length / 2;
					for (int k = begin+1; k < begin + jobPlace; k++) {
						System.out.print("_");
					}
					System.out.print(i);
					for (int k = begin + jobPlace + 1; k < end-1; k++) {
						System.out.print("_");
					}
					System.out.print("]");
					inJob = false;
				}
			}
			System.out.println();
		}
	}
}