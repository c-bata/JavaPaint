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

	int doCount = 0;

	ArrayList<Integer> typeList = new ArrayList<Integer>();
	ArrayList<Integer> x1List = new ArrayList<Integer>();
	ArrayList<Integer> x2List = new ArrayList<Integer>();
	ArrayList<Integer> y1List = new ArrayList<Integer>();
	ArrayList<Integer> y2List = new ArrayList<Integer>();
	ArrayList<Boolean> addInfoList = new ArrayList<Boolean>();

	JLabel position,info;

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
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
// 実装済み
	public void newFile(){
		mouse.initList();
		mouse.repaint();
		System.out.println("Creating new file ....");
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
//ArrayListを初期化→ファイルの内容をArrayListに追加していく.
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

		mouse.initList(); // 一度ArrayListをクリア

		JFileChooser filechooser = new JFileChooser();
		filechooser.addChoosableFileFilter(new JsonFilter()); // jsonにフィルタ

		int selected = filechooser.showOpenDialog(this);
		if (selected == JFileChooser.APPROVE_OPTION){
			File file = filechooser.getSelectedFile();

			//System.out.println(file.getName());

			try{
				if (checkBeforeReadfile(file)){
					BufferedReader br = new BufferedReader(new FileReader(file));
					info.setText(file.getName() + "を読み込みました.");

					String str;
					while((str = br.readLine()) != null){
						String[] element = str.split(",", -1);
						typeList.add(Integer.parseInt(element[0]));
						x1List.add(Integer.parseInt(element[1]));
						y1List.add(Integer.parseInt(element[2]));
						x2List.add(Integer.parseInt(element[3]));
						y2List.add(Integer.parseInt(element[4]));
						//System.out.println(Integer.parseInt(element[0]) + " , " + Integer.parseInt(element[1]) + " , " + Integer.parseInt(element[2]) + " , " + Integer.parseInt(element[3]) + " , " + element[4] + " , " + typeList.size());
					}
					mouse.repaint();

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
			info.setText(file.getName() + "にセーブしました.");


			for(int i=0; i < typeList.size() - doCount ; i++){
				bw.write(typeList.get(i) + ",");
				bw.write(x1List.get(i) + ",");
				bw.write(y1List.get(i) + ",");
				bw.write(x2List.get(i) + ",");
				bw.write(y2List.get(i) + ",");
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
		info.setText("データを上書き保存しました.");
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void dataExport(){
		System.out.println("dataExport ...");
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void unDo(){
		if(doCount < typeList.size()){
			doCount++;
			mouse.repaint();
			info.setText("undo!");
			mouse.x1 = 0;
			mouse.y1 = 0;
			mouse.x2 = 0;
			mouse.y2 = 0;
		}else{
			info.setText("can't undo!");
		}
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void reDo(){
		System.out.println("redo ....");
		if(doCount > 0){
			doCount--;
			mouse.repaint();
			info.setText("redo!");
			mouse.x1 = 0;
			mouse.y1 = 0;
			mouse.x2 = 0;
			mouse.y2 = 0;
		}else{
			info.setText("can't redo!");
		}

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

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	class DrawByMouse extends JPanel implements MouseListener,MouseMotionListener{

		int x, y, w1, h1, w2, h2, x1, y1, x2, y2;

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
//各リストを初期化
	private void initList(){
		typeList.clear();
		x1List.clear();
		x2List.clear();
		y1List.clear();
		y2List.clear();
		addInfoList.clear();
		// typeを0にすると次にボタンを押すまで図形が書けない
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		// doCountをreset
		doCount = 0;
	}

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
			if(doCount > 0){
				int index = typeList.size()-1-doCount;
				typeList.set(index,type); //インデックスは0からだから1引く
				x1List.set(index,x1);
				y1List.set(index,y1);
				x2List.set(index,x2);
				y2List.set(index,y2);
				addInfoList.set(index,false);
			}else{
				typeList.add(type);
				x1List.add(x1);
				y1List.add(y1);
				x2List.add(x2);
				y2List.add(y2);
				//if(type!=LINE || type!=RECT || type!=OVAL){
				//	addInfoList.add(true);
				//}else
				addInfoList.add(false);
			}
		}


		protected void paintComponent(Graphics g){    //paintComponentメソッドを再定義
			super.paintComponent(g);	//superクラスのpaintComponentの実行

			if(typeList.size() > 0){
				for(int i = 0 ; i < typeList.size() - doCount ; i++){
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

			// unDo,reDoのバグシュウセイ用
			System.out.println("リストのサイズ: " + typeList.size());
			System.out.println("doCountの値: " + doCount);
		}

		public void mousePressed(MouseEvent e){ 
			x1 = e.getX();
			y1 = e.getY();
			x2 = e.getX(); //x2,y2も初期化しないと一瞬へんな出力が起きる.
			y2 = e.getY();
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
