package random;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Main {
	private static boolean flag = false;
	private static long seed = 0;
	
	public static void main(String[] args) {
		boolean doCalc = true;
		for (String arg : args) {
			if ("-nc".equalsIgnoreCase(arg)) doCalc = false;
			try {
				Main.seed = Long.parseLong(arg);
				Main.flag = true;
			} catch (NumberFormatException ignored) {
			}
		}
		
		if (!Main.flag) {
			try (Reader reader = new FileReader("D:/Random/seed.prop")) {
				Properties prop = new Properties();
				prop.load(reader);
				String seeds = prop.getProperty("seed", "");
				String ignored = seeds.replace(" ", "");
				String[] seed = seeds.split(",");
				if (seed.length == 0) throw new NumberFormatException();
				Main.flag = true;
				Main.seed = Long.parseLong(seed[0]);
				StringJoiner newSeed = new StringJoiner(",");
				for (int i = 1; i < seed.length; i++) newSeed.add(seed[i]);
				prop.setProperty("seed", newSeed.toString());
				Writer writer = new FileWriter("D:/Random/seed.prop");
				prop.store(writer, null);
			} catch (FileNotFoundException e) {
				System.err.println("无配置文件");
				Main.flag = false;
			} catch (IOException e) {
				System.err.println("读写配置文件IO错误");
				Main.flag = false;
			} catch (NumberFormatException e) {
				System.err.println("无法解析种子");
				Main.flag = false;
			}
		}
		
		SwingUtilities.invokeLater(Main::createAndShowGUI);
		
		Random rand = new Random();
		if (doCalc) {
			Thread t = new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						System.err.println("线程中断");
					}
					CalcRandom.run(rand.nextLong());
				}
			});
			t.setDaemon(true);
			t.start();
		}
	}
	
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("抽签器升级版 - 作者：yunqibaozha");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 200);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		JLabel label = new JLabel("重要提示：本程序依赖Java 21+");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		JButton button1 = new JButton("抽1个");
		JButton button6 = new JButton("抽6个");
		// JButton button0 = new JButton("加载random对象");
		button1.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		button6.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setFont(new Font("微软雅黑", Font.PLAIN, 36));
		if (Main.flag) area.setText(" ");
		JPanel panel = new JPanel();
		frame.add(label, BorderLayout.NORTH);
		frame.add(area, BorderLayout.CENTER);
		panel.add(button1);
		panel.add(button6);
		// panel.add(button0);
		frame.add(panel, BorderLayout.SOUTH);
		frame.setVisible(true);
		Random rand;
		if (Main.flag) rand = new Random(Main.seed);
		else rand = new Random();
		
		button1.addActionListener(e -> {
			Set<Integer> set = new TreeSet<>();
			set.add(rand.nextInt(62) + 1);
			StringBuilder builder = new StringBuilder();
			for (int n : set) {
				builder.append(n).append(" ");
			}
			area.setText(builder.toString().trim());
		});
		
		button6.addActionListener(e -> {
			Set<Integer> set = new TreeSet<>();
			while (set.size() < 6) {
				set.add(rand.nextInt(62) + 1);
			}
			
			StringBuilder builder = new StringBuilder();
			for (int n : set) {
				builder.append(n).append(" ");
			}
			area.setText(builder.toString().trim());
		});
		
		// button0.addActionListener(e -> {
		// 	File file = new File("D:/Random/rand.obj");
		// 	if (!file.isFile()) System.err.println("没有rand.obj");
		// 	try (InputStream in = new FileInputStream(file); ObjectInputStream objectIn = new ObjectInputStream(in)) {
		// 		Random r = (Random) objectIn.readObject();
		// 		rand.set(r);
		// 		System.out.println(r);
		// 	} catch (IOException ex) {
		// 		System.err.println("读取rand.objIO错误");
		// 	} catch (ClassNotFoundException ex) {
		// 		System.err.println("未找到类Random");
		// 	}
		//
		// });
		
		// frame.addWindowListener(new WindowAdapter() {
		// 	@Override
		// 	public void windowClosing(WindowEvent e) {
		// 		super.windowClosing(e);
		// 		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		// 		try (ObjectOutputStream output = new ObjectOutputStream(buf)) {
		// 			output.writeObject(rand);
		// 		} catch (IOException ex) {
		// 			System.err.println("反序列化IO错误");
		// 		}
		// 		System.out.println(Arrays.toString(buf.toByteArray()));
		// 		File file = new File("D:/Random/rand.obj");
		// 		try {
		// 			if (!file.isFile()) if (!file.createNewFile()) System.err.println("rand.obj创建失败");
		// 		} catch (IOException ex) {
		// 			System.err.println("rand.obj创建IO错误");
		// 		}
		// 		try (OutputStream output = new FileOutputStream(file)) {
		// 			output.write(buf.toByteArray());
		// 		} catch (IOException ex) {
		// 			System.err.println("rand.obj写入IO错误");
		// 		}
		// 	}
		// });
	}
}