import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DrawGraphics extends JPanel implements ActionListener{
	DrawByMouse area;
	JButton brect, bline, boval, bpolygon, bpolyline, bselect, btext, bpencil;
	JLabel position;
	
	JComboBox linecolor,drawcolor;
	Color [] c = {Color.white, Color.black, Color.red, Color.yellow, Color.green, Color.blue};
	int line_color = 1,draw_color = 0;
	int linewidth = 2;

	final int RECT = 1, OVAL = 2, LINE=3, POLYGON=4, POLYLINE=5, SELECT=6, TEXT=7, PENCIL=8;
	int type = RECT;

	public DrawGraphics(){

		//描画域を生成
		area = new DrawByMouse();
		// 図形選択
		brect = new JButton("", new ImageIcon("./rect.png"));
		boval = new JButton("", new ImageIcon("./oval.png"));
		bline = new JButton("", new ImageIcon("./line.png"));
		bpolygon = new JButton("", new ImageIcon("./polygon.png"));
		bpolyline = new JButton("", new ImageIcon("./polyline.png"));
		bselect = new JButton("", new ImageIcon("./select.png"));
		btext = new JButton("", new ImageIcon("./text.png"));
		bpencil = new JButton("", new ImageIcon("./pencil.png"));
		/*
		brect.setPreferredSize(new Dimension(10, 10));
		boval.setPreferredSize(new Dimension(10, 10));
		bline.setPreferredSize(new Dimension(10, 10));
		bpolygon.setPreferredSize(new Dimension(10, 10));
		bpolyline.setPreferredSize(new Dimension(10, 10));
		bselect.setPreferredSize(new Dimension(10, 10));
		btext.setPreferredSize(new Dimension(10, 10));
		bpencil.setPreferredSize(new Dimension(10, 10));
		*/


		//AcitionListenerの設定
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
		
		// 色の選択
		String [] list = {"なし","黒","赤","黃","緑","青"};
		linecolor = new JComboBox(list);
		linecolor.addActionListener(this);
		linecolor.setSelectedIndex(line_color); // 線の色はデフォルトで黒を選択
		drawcolor = new JComboBox(list);
		drawcolor.addActionListener(this);

		JPanel color = new JPanel();
		color.setLayout(new GridLayout(4,2));
		color.add(new JLabel("線の色"));
		color.add(linecolor);
		color.add(new JLabel("塗りつぶしの色"));
		color.add(drawcolor);

		JPanel sidepanel = new JPanel();
		sidepanel.setLayout(new BoxLayout(sidepanel, BoxLayout.Y_AXIS));
		sidepanel.setBackground(c[1]);
		sidepanel.add(object);
		sidepanel.add(color);
		
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BorderLayout());
		mainpanel.add(area, BorderLayout.CENTER);
		position = new JLabel("(none,none)");	// Mouseの位置を表示してみる
		mainpanel.add(position, BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());
		add(mainpanel, BorderLayout.CENTER);
		add(sidepanel, BorderLayout.EAST);
	}

	public void actionPerformed(ActionEvent e) {
		//押されたボタンに応じて，typeにRECT, OVAL, LINEの値を設定
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
		}else if(obj==linecolor){
			line_color = linecolor.getSelectedIndex();
		}else if(obj==drawcolor){
			draw_color = drawcolor.getSelectedIndex();
		}

	}

	//描画域のパネル，内部クラスとして定義
	class DrawByMouse extends JPanel implements MouseListener,MouseMotionListener {
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
			//typeの値に応じて，四角，円，直線を描く。

			g.setColor(c[draw_color]);
			//g.setColor(c[line_color]);

			if(type == LINE){
				g.drawLine(x1,y1,x2,y2);
			}else if(type == RECT){
				exchange();
				g.fillRect( w1, h1, w2, h2);
				g.setColor(c[line_color]);	//配列cにインデックスに対応するカラーが格納されている.
				g.drawRect( w1, h1, w2, h2);
				/*
				g.fillRect( w1, h1, w2, h2);
				g.setColor(c[draw_color]);
				g.fillRect( w1+2, h1+2, w2-4, h2-4);	//塗りつぶし
				*/
			}else if(type == OVAL){
				exchange();
				g.fillOval( w1, h1, w2, h2);
				g.setColor(c[line_color]);	//配列cにインデックスに対応するカラーが格納されている.
				g.drawOval( w1, h1, w2, h2);
				/*
				g.fillOval( w1, h1, w2, h2);
				g.setColor(c[draw_color]);
				g.fillOval( w1+2, h1+2, w2-4, h2-4);	//塗りつぶし
				*/
			/*
			}else if(type == PENCIL){
				g.fillOval(x2-linewidth,w1-linewidth,linewidth*2,linewidth*2);
			*/
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


	///////////////////////////////////////////////////////////////////

	public static void main(String args[]){
		JFrame frame  = new JFrame("Draw Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menubar = new JMenuBar();

		JMenu menu1 = new JMenu("ファイル(F)");
		menu1.setMnemonic(KeyEvent.VK_F);
		JMenu menu2 = new JMenu("編集(E)");
		menu2.setMnemonic(KeyEvent.VK_E);
		JMenu menu3 = new JMenu("ヘルプ(H)");
		menu3.setMnemonic(KeyEvent.VK_H);
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(Box.createHorizontalGlue());	// ヘルプは右端に設定
		menubar.add(menu3);

		JMenu submenu1 = new JMenu("画像の形式を変更");
		JRadioButtonMenuItem radiomenuitem1 = new JRadioButtonMenuItem("PNG");
		JRadioButtonMenuItem radiomenuitem2 = new JRadioButtonMenuItem("GIF");
		JRadioButtonMenuItem radiomenuitem3 = new JRadioButtonMenuItem("JPEG");
		submenu1.add(radiomenuitem1);
		submenu1.add(radiomenuitem2);
		submenu1.add(radiomenuitem3);
		radiomenuitem1.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(radiomenuitem1);
		group.add(radiomenuitem2);
		group.add(radiomenuitem3);

		JMenuItem menuitem1_1 = new JMenuItem("新規(N)");
		menuitem1_1.setMnemonic(KeyEvent.VK_N);
		menuitem1_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem1_2 = new JMenuItem("開く(O)");
		menuitem1_2.setMnemonic(KeyEvent.VK_O);
		menuitem1_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem1_3 = new JMenuItem("名前を付けて保存(A)");
		menuitem1_3.setMnemonic(KeyEvent.VK_A);
		menuitem1_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem1_4 = new JMenuItem("上書き保存(S)");
		menuitem1_4.setMnemonic(KeyEvent.VK_S);
		menuitem1_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem1_5 = new JMenuItem("画像で出力(E)");
		menuitem1_5.setMnemonic(KeyEvent.VK_E);
		menuitem1_5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem1_6 = new JMenuItem("終了(Q)");
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

		JMenuItem menuitem2_1 = new JMenuItem("元に戻す(U)");
		menuitem2_1.setMnemonic(KeyEvent.VK_U);
		menuitem2_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
		JMenuItem menuitem2_2 = new JMenuItem("やり直し(R)");
		menuitem2_2.setMnemonic(KeyEvent.VK_R);
		menuitem2_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		menu2.add(menuitem2_1);
		menu2.add(menuitem2_2);

		// Listenerの設定
		/*
		menuitem1_1.addActionListener(area);
		menuitem1_2.addActionListener(area);
		menuitem1_3.addActionListener(area);
		menuitem1_4.addActionListener(area);
		menuitem1_5.addActionListener(area);
		menuitem1_6.addActionListener(area);
		menuitem2_1.addActionListener(area);
		menuitem2_2.addActionListener(area);
		*/

		frame.setJMenuBar(menubar);
		DrawGraphics e = new DrawGraphics();
		frame.add(e, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
