package random;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class CalcRandom {
	public static void run(long seed) {
		Random rand = new Random(seed);
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < 100; i++) {
			int n = rand.nextInt(62) + 1;
			sb.append(i).append(",").append(n).append("\n");
		}
		File file = new File("D:/Random");
		boolean ignored = file.mkdirs();
		file = new File("D:/Random/" + seed + ".csv");
		if (file.isFile()) return;
		try {
			if (!file.createNewFile()) return;
			System.out.println(file.getCanonicalPath());
		} catch (IOException e) {
			System.err.println("种子文件创建IO错误");
		}
		try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
			writer.write(sb.toString());
		} catch (IOException e) {
			System.err.println("种子文件写入IO错误");
		}
	}
	public static void main(String[] args) {
		Random rand = new Random();
		if (args.length == 0) run(rand.nextLong());
		else {
			long seed = Long.parseLong(args[0]);
			run(seed);
		}
	}
}
