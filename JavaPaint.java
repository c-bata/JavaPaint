import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JavaPaint extends JFrame implements ActionListener{
	DrawByMouse mouse;
	DrawGraphics graphics;
	JButton brect, bline, boval, bpolygon, bpolyline, bselect, btext, bpencil;
	JLabel position;

	final int RECT = 1, OVAL = 2, LINE=3, POLYGON=4, POLYLINE=5, SELECT=6, TEXT=7, PENCIL=8;
	int type = RECT;

	JMenuBar menubar;
	JMenu menu1, menu2, menu3, submenu1;
	JRadioButtonMenuItem radiomenuitem1, radiomenuitem2, radiomenuitem3;
	ButtonGroup group;
	JMenuItem menuitem1_1, menuitem1_2, menuitem1_3, menuitem1_4, menuitem1_5, menuitem1_6 ,menuitem2_1, menuitem2_2;


	public JavaPaint(){
		JFrame frame  = new JFrame("Draw Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menubar = new JMenuBar();

		menu1 = new JMenu("ファイル(F)");
		menu1.setMnemonic(KeyEvent.VK_F);
		menu2 = new JMenu("編集(E)");
		menu2.setMnemonic(KeyEvent.VK_E);
		menu3 = new JMenu("ヘルプ(H)");
		menu3.setMnemonic(KeyEvent.VK_H);
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(Box.createHorizontalGlue());	// ヘルプは右端に設定
		menubar.add(menu3);

		submenu1 = new JMenu("画像の形式を変更");
		radiomenuitem1 = new JRadioButtonMenuItem("PNG");
		radiomenuitem2 = new JRadioButtonMenuItem("GIF");
		radiomenuitem3 = new JRadioButtonMenuItem("JPEG");
		submenu1.add(radiomenuitem1);
		submenu1.add(radiomenuitem2);
		submenu1.add(radiomenuitem3);
		radiomenuitem1.setSelected(true);
		group = new ButtonGroup();
		group.add(radiomenuitem1);
		group.add(radiomenuitem2);
		group.add(radiomenuitem3);

		menuitem1_1 = new JMenuItem("新規(N)");
		menuitem1_1.setMnemonic(KeyEvent.VK_N);
		menuitem1_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		menuitem1_2 = new JMenuItem("開く(O)");
		menuitem1_2.setMnemonic(KeyEvent.VK_O);
		menuitem1_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		menuitem1_3 = new JMenuItem("名前を付けて保存(A)");
		menuitem1_3.setMnemonic(KeyEvent.VK_A);
		menuitem1_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		menuitem1_4 = new JMenuItem("上書き保存(S)");
		menuitem1_4.setMnemonic(KeyEvent.VK_S);
		menuitem1_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		menuitem1_5 = new JMenuItem("画像で出力(E)");
		menuitem1_5.setMnemonic(KeyEvent.VK_E);
		menuitem1_5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		menuitem1_6 = new JMenuItem("終了(Q)");
		menuitem1_6.setMnemonic(KeyEvent.VK_Q);
		menuitem1_6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		menu1.add(menuitem1_1);
		menu1.add(menuitem1_2);
		menu1.addSeparator();
		menu1.add(menuitem1_3);
		menu1.add(menuitem1_4);
		menu1.add(submenu1);
		menu1.add(menuitem1_5);
		menu1.addSeparator();
		menu1.add(menuitem1_6);

		menuitem2_1 = new JMenuItem("元に戻す(U)");
		menuitem2_1.setMnemonic(KeyEvent.VK_U);
		menuitem2_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
		menuitem2_2 = new JMenuItem("やり直し(R)");
		menuitem2_2.setMnemonic(KeyEvent.VK_R);
		menuitem2_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		menu2.add(menuitem2_1);
		menu2.add(menuitem2_2);

		/*
		menuitem1_1.addActionListener(graphics);
		menuitem1_2.addActionListener(graphics);
		menuitem1_3.addActionListener(graphics);
		menuitem1_4.addActionListener(graphics);
		menuitem1_5.addActionListener(graphics);
		menuitem1_6.addActionListener(graphics);
		menuitem2_1.addActionListener(graphics);
		menuitem2_2.addActionListener(graphics);
		*/
		menuitem1_1.addActionListener(this);
		menuitem1_2.addActionListener(this);
		menuitem1_3.addActionListener(this);
		menuitem1_4.addActionListener(this);
		menuitem1_5.addActionListener(this);
		menuitem1_6.addActionListener(this);
		menuitem2_1.addActionListener(this);
		menuitem2_2.addActionListener(this);

		menuitem1_1.setActionCommand("newFile");
		menuitem1_2.setActionCommand("Open");
		menuitem1_3.setActionCommand("Save");
		menuitem1_4.setActionCommand("overWrite");
		menuitem1_5.setActionCommand("export");
		menuitem1_6.setActionCommand("exit");
		menuitem2_1.setActionCommand("undo");
		menuitem2_2.setActionCommand("redo");

		frame.setJMenuBar(menubar);

		graphics = new DrawGraphics();
		frame.add(graphics, BorderLayout.EAST);
		mouse = new DrawByMouse();
		frame.add(mouse, BorderLayout.CENTER);
		position = new JLabel("(none,none)");
		frame.add(position, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}






	class DrawGraphics extends JPanel implements ActionListener{
		public DrawGraphics(){
			brect = new JButton("", new ImageIcon("./img/rect.png"));
			boval = new JButton("", new ImageIcon("./img/oval.png"));
			bline = new JButton("", new ImageIcon("./img/line.png"));
			bpolygon = new JButton("", new ImageIcon("./img/polygon.png"));
			bpolyline = new JButton("", new ImageIcon("./img/polyline.png"));
			bselect = new JButton("", new ImageIcon("./img/select.png"));
			btext = new JButton("", new ImageIcon("./img/text.png"));
			bpencil = new JButton("", new ImageIcon("./img/pencil.png"));
			//ActionListenerの設定
			brect.addActionListener(this);
			boval.addActionListener(this);
			bline.addActionListener(this);
			bpolygon.addActionListener(this);
			bpolyline.addActionListener(this);
			bselect.addActionListener(this);
			btext.addActionListener(this);
			bpencil.addActionListener(this);
			//ボタンを置くパネルを作り，ボタンを配置
			JPanel object = new JPanel();
			object.setLayout(new GridLayout(2,2));
			object.add(brect);
			object.add(boval);
			object.add(bline);
			object.add(bpolygon);
			object.add(bpolyline);
			object.add(bselect);
			object.add(btext);
			object.add(bpencil);

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(object);
			add(new JLabel("test"));
		}
		public void actionPerformed(ActionEvent e) {

			String actionCommand = e.getActionCommand();

			Object obj=e.getSource();
			if(obj==brect){
				type = RECT;
			}else if(obj==boval){
				type = OVAL;
			}else if(obj==bline){
				type = LINE;
			}else if(obj==bpolyline){
				type = POLYLINE;
			}else if(obj==bpolygon){
				type = POLYGON;
			}else if(obj==bpencil){
				type = PENCIL;
			}else if(obj==btext){
				type = TEXT;
			}else if(obj==bselect){
				type = SELECT;
			}else if (actionCommand.equals("exitItem")){
					System.exit(0);
			}
		}
	}







	class DrawByMouse extends JPanel implements MouseListener,MouseMotionListener{
		int x, y, w1, h1, w2, h2, x1, y1, x2, y2;
		DrawByMouse(){
			setBackground(Color.white);
			setPreferredSize(new Dimension(500,500));
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		private void exchange(){
			if(x1 > x2){
				w1 = x2; w2 = x1;
			}else{
				w1 = x1; w2 = x2;
			}
			if(y1 > y2){
				h1 = y2; h2 = y1;
			}else{
				h1 = y1; h2 = y2;
			}
			w2-=w1;	h2-=h1;
		}

		protected void paintComponent(Graphics g){    //paintComponentメソッドを再定義
			super.paintComponent(g);	//superクラスのpaintComponentの実行
			if(type == LINE){
				g.drawLine(x1,y1,x2,y2);
			}else if(type == RECT){
				exchange();
				g.fillRect( w1, h1, w2, h2);
				//g.setColor(c[line_color]);
				g.drawRect( w1, h1, w2, h2);
			}else if(type == OVAL){
				exchange();
				g.fillOval( w1, h1, w2, h2);
				//g.setColor(c[line_color]);
				g.drawOval( w1, h1, w2, h2);
			}
		}

		public void mousePressed(MouseEvent e){ 
			x1 = e.getX();
			y1 = e.getY();
		}
		public void mouseDragged(MouseEvent e){ 
			x2 = e.getX();
			y2 = e.getY();
			repaint();     //コンポーネント全体を再描画
			position.setText("開始(" + Integer.toString(x1) + "," + Integer.toString(y1) + ") -> 現在(" + Integer.toString(x2) + "," + Integer.toString(y2) + ")");
		}
		public void mouseMoved(MouseEvent e){
			x = e.getX();
			y = e.getY();
			position.setText("(" + Integer.toString(x) + "," + Integer.toString(y) + ")");
		}
		public void mouseReleased(MouseEvent e){
		}
		public void mouseClicked(MouseEvent e){
		}
		public void mouseEntered(MouseEvent e){
		}
		public void mouseExited(MouseEvent e){
			position.setText("(none,none)");
		}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		Object obj=e.getSource();
		if(actionCommand.equals("newFile")){
			System.exit(0);
		}else if(actionCommand.equals("Open")){
			System.exit(0);
		}else if(actionCommand.equals("Save")){
			System.exit(0);
		}else if(actionCommand.equals("overWrite")){
			System.exit(0);
		}else if(actionCommand.equals("export")){
			System.exit(0);
		}else if(actionCommand.equals("exit")){
			System.exit(0);
		}else if(actionCommand.equals("undo")){
			System.exit(0);
		}else if(actionCommand.equals("redo")){
			System.exit(0);
		}
	}

	public static void main(String args[]){
		new JavaPaint();
	}
}
