import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JavaPaint extends JFrame{

	public JavaPaint(){
		JFrame frame  = new JFrame("Draw Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



		DrawGraphics graphics = new DrawGraphics();
		frame.add(graphics, BorderLayout.EAST);
		DrawByMouse mouse = new DrawByMouse();
		frame.add(mouse, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	class DrawGraphics extends JPanel{
		public DrawGraphics(){
			add(new JLabel("test"));
		}
	}

	class DrawByMouse extends JPanel{
		DrawByMouse(){
			add(new JLabel("test"));
		}
	}


	public static void main(String args[]){
		new JavaPaint();
	}
}
