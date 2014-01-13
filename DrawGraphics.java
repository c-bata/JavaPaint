import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class DrawGraphics extends JPanel implements ActionListener{

	DrawByMouse mouse;

	JButton brect, bline, boval, bpolygon, bpolyline, bselect, btext, bpencil;
	final int RECT = 1, OVAL = 2, LINE=3, POLYGON=4, POLYLINE=5, SELECT=6, TEXT=7, PENCIL=8;
	int type = RECT;

	JLabel position;

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


		//JPanel container = new JPanel();
		//container.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//container.add(object);

		setLayout(new BorderLayout());
		mouse = new DrawByMouse();
		add(mouse, BorderLayout.CENTER);
		position = new JLabel("(none,none)");
		add(position, BorderLayout.SOUTH);
		add(object, BorderLayout.EAST);

	}

	public void actionPerformed(ActionEvent e) {

		//String actionCommand = e.getActionCommand();

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
}
