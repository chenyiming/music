package com.music.wav;
//WaveFileReadDemo.jave
//RobinTang
//2012-08-23

import javax.swing.JFrame;

import java.io.*;

public class WaveFileReadDemo {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String filename = "D:\\01.wav";
		JFrame frame = new JFrame();
		WaveFileReader reader = new WaveFileReader(filename);
		if(reader.isSuccess()){
			int[] data = reader.getData()[0]; //获取第一声道
			int[] data1 = reader.getData()[1]; //获取第2声道
			
			PrintStream out=new PrintStream(new File("d:\\01.csv"));

			for(int i=0;i<data.length;i++){
			out.println(data[i]+","+data1[i]);
			}
			out.close();
			
			
			DrawPanel drawPanel = new DrawPanel(data); // 创建一个绘制波形的面板
			frame.add(drawPanel);
			frame.setTitle(filename);
			frame.setSize(1200, 400);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		else{
			System.err.println(filename + "不是一个正常的wav文件");
		}
	}
}
