import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

class DrawGraphics extends JPanel implements ActionListener{

	DrawByMouse mouse;

	JButton brect, bline, boval, bpolygon, bpolyline, bselect, btext, bpencil;
	final int RECT = 1, OVAL = 2, LINE=3, POLYGON=4, POLYLINE=5, SELECT=6, TEXT=7, PENCIL=8;
	int type = RECT;

	ArrayList<Integer> typeList = new ArrayList<Integer>();
	ArrayList<Integer> x1List = new ArrayList<Integer>();
	ArrayList<Integer> x2List = new ArrayList<Integer>();
	ArrayList<Integer> y1List = new ArrayList<Integer>();
	ArrayList<Integer> y2List = new ArrayList<Integer>();
	ArrayList<Boolean> addInfoList = new ArrayList<Boolean>();

	JLabel position,info;

	//引数なしのコンストラクタ
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

		JPanel footer = new JPanel();
		position = new JLabel("(none,none)");
		info = new JLabel("");
		footer.add(position);
		footer.add(info);

		setLayout(new BorderLayout());
		mouse = new DrawByMouse();
		add(mouse, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);
		add(object, BorderLayout.EAST);
	}


	//引数ありのコンストラクタ.newFileの時はこっちのオブジェクト生成する。
	//public DrawGraphics(String str){
	//}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void newFile(){
		System.out.println("Creating new file ....");
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	// dataOpenメソッドで利用
	private static boolean checkBeforeReadfile(File file){
		if (file.exists()){
			if (file.isFile() && file.canRead()){
				return true;
			}
		}
		return false;
	}

	public void dataOpen(){ // ほぼ実装済み

		JFileChooser filechooser = new JFileChooser();
		filechooser.addChoosableFileFilter(new JsonFilter()); // jsonにフィルタ

		int selected = filechooser.showOpenDialog(this);
		if (selected == JFileChooser.APPROVE_OPTION){
			File file = filechooser.getSelectedFile();

			//System.out.println(file.getName());

			try{
				if (checkBeforeReadfile(file)){
					BufferedReader br = new BufferedReader(new FileReader(file));

					String str;
					while((str = br.readLine()) != null){
						info.setText(str);
					}

					br.close();
				}else{
					info.setText("ファイルが見つからないか開けません");
				}
			}catch(FileNotFoundException err){
				System.out.println(err);
			}catch(IOException err){
				System.out.println(err);
			}
		}
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////

	public void saveAs(File file){
		try{

			if (file.exists() == false){
				file.createNewFile();
				System.out.println("ファイルがないので作ります");
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));


			for(int i=0; i < typeList.size() ; i++){
				bw.write(typeList.get(i) + ",");
				bw.write(x1List.get(i) + ",");
				bw.write(y1List.get(i) + ",");
				bw.write(x2List.get(i) + ",");
				bw.write(y2List.get(i));
				bw.newLine();
			}
			bw.close();

		}catch(IOException e){
			System.out.println(e);
			info.setText("ファイルが開けません");
		}
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void save(){
		System.out.println("Saving Data ....");
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void dataExport(){
		System.out.println("dataExport ...");
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void unDo(){
		System.out.println("undo ....");
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void reDo(){
		System.out.println("redo ....");
	}



//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
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

		private void exchange(int a1, int b1, int a2, int b2){
			if(a1 > a2){
				w1 = a2; w2 = a1;
			}else{
				w1 = a1; w2 = a2;
			}
			if(b1 > b2){
				h1 = b2; h2 = b1;
			}else{
				h1 = b1; h2 = b2;
			}
			w2-=w1;	h2-=h1;
		}

		private void listAdd(){
			typeList.add(type);
			x1List.add(x1);
			x2List.add(x2);
			y1List.add(y1);
			y2List.add(y2);
			//if(type!=LINE || type!=RECT || type!=OVAL)
			//	addInfoList.add(true);
			//}else
			addInfoList.add(false);
		}


		protected void paintComponent(Graphics g){    //paintComponentメソッドを再定義
			super.paintComponent(g);	//superクラスのpaintComponentの実行

			if(typeList.size() > 0){
				for(int i = 0 ; i < typeList.size() ; i++){
					if(typeList.get(i) == LINE){
						g.drawLine(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
					}else if(typeList.get(i) == RECT){
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						g.fillRect( w1, h1, w2, h2);
						//g.setColor(c[line_color]);
						g.drawRect( w1, h1, w2, h2);
					}else if(typeList.get(i) == OVAL){
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						g.fillOval( w1, h1, w2, h2);
						//g.setColor(c[line_color]);
						g.drawOval( w1, h1, w2, h2);
					}
				}
			}


			if(type == LINE){
				g.drawLine(x1,y1,x2,y2);
			}else if(type == RECT){
				exchange(x1,y1,x2,y2);
				g.fillRect( w1, h1, w2, h2);
				//g.setColor(c[line_color]);
				g.drawRect( w1, h1, w2, h2);
			}else if(type == OVAL){
				exchange(x1,y1,x2,y2);
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
			listAdd();
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
