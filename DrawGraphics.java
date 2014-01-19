import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class DrawGraphics extends JPanel implements ActionListener{

	DrawByMouse mouse;
	BufferedImage bi = null;

	JButton brect, bline, boval, bpolygon, bpolyline, bselect, btext, bpencil;
	final int RECT = 1, OVAL = 2, LINE=3, POLYGON=4, POLYLINE=5, SELECT=6, TEXT=7, PENCIL=8;
	int type = RECT;

	final int PNG=1, GIF=2, JPG=3;
	int imageType = PNG;

	ArrayList<Integer> typeList = new ArrayList<Integer>();
	ArrayList<Integer> x1List = new ArrayList<Integer>();
	ArrayList<Integer> x2List = new ArrayList<Integer>();
	ArrayList<Integer> y1List = new ArrayList<Integer>();
	ArrayList<Integer> y2List = new ArrayList<Integer>();
	ArrayList<Integer> drawColorList = new ArrayList<Integer>();
	ArrayList<Integer> lineColorList = new ArrayList<Integer>();
	ArrayList<Integer> lineWidthList = new ArrayList<Integer>();

	JLabel position,info;

	JComboBox linecolor,drawcolor; // 線の色,塗りつぶしの色
	Color [] c = {Color.black, Color.red, Color.yellow, Color.green, Color.blue, Color.white};
	int line_color = 0;
	int draw_color = 0;

	JSlider lineSlider;

	File presentFile = null; //現在編集中のファイルを保持
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

		JPanel footer = new JPanel();
		position = new JLabel("(none,none)");
		info = new JLabel("");
		footer.add(position);
		footer.add(info);

		// 色の選択
		String [] list = {"黒","赤","黃","緑","青","なし"};
		linecolor = new JComboBox(list);
		linecolor.addActionListener(this);
		linecolor.setSelectedIndex(line_color); // 初期化
		drawcolor = new JComboBox(list);
		drawcolor.addActionListener(this);
		linecolor.setSelectedIndex(draw_color); // 初期化

		JPanel color = new JPanel();
		color.setLayout(new GridLayout(2,2));
		color.add(new JLabel("線の色"));
		color.add(linecolor);
		color.add(new JLabel("塗りつぶしの色"));
		color.add(drawcolor);

		// スライダー
		JPanel linewidth = new JPanel();
		lineSlider = new JSlider();
		lineSlider.setMajorTickSpacing(10);
		lineSlider.setMinorTickSpacing(1);
		lineSlider.setPaintTicks(true);
		linewidth.add(new JLabel("線の太さ"));
		linewidth.add(lineSlider);

		JPanel sidepanel = new JPanel();
		sidepanel.setLayout(new BoxLayout(sidepanel, BoxLayout.Y_AXIS));
		sidepanel.add(object);
		sidepanel.add(color);
		sidepanel.add(linewidth);


		/* 構成
		 *
		 * sidepanel(JPanel)              : サイドバー(ボタンとかを配置)
		 *              object(JPanel)  : 描画する図形を選択するボタンが配置
		 *
		 * footer(JPanel)               : フッター
		 *              position(JLabel) : 現在のマウスカーソルの位置を表示
		 * mouse(JPanel)                : 描画画面
		 *
		 *
		 */

		setLayout(new BorderLayout());
		mouse = new DrawByMouse();
		add(mouse, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);
		add(sidepanel, BorderLayout.EAST);
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
		info.setText("新しい画像を生成しました.");
		presentFile = null;
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

			presentFile = file;

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
						drawColorList.add(Integer.parseInt(element[5]));
						lineColorList.add(Integer.parseInt(element[6]));
						lineWidthList.add(Integer.parseInt(element[7]));
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

			presentFile = file;

			for(int i=0; i < typeList.size() - mouse.doCount ; i++){
				bw.write(typeList.get(i) + ",");
				bw.write(x1List.get(i) + ",");
				bw.write(y1List.get(i) + ",");
				bw.write(x2List.get(i) + ",");
				bw.write(y2List.get(i) + ",");
				bw.write(drawColorList.get(i) + ",");
				bw.write(lineColorList.get(i) + ",");
				bw.write(lineWidthList.get(i) + ",");
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
		if(presentFile != null){
			try{
				if (presentFile.exists() == false){
					System.out.println("ファイルが見つかりません.");
					return;
				}

				BufferedWriter bw = new BufferedWriter(new FileWriter(presentFile));
				info.setText( presentFile.getName() + "に上書き保存しました.");

				for(int i=0; i < typeList.size() - mouse.doCount ; i++){
					bw.write(typeList.get(i) + ",");
					bw.write(x1List.get(i) + ",");
					bw.write(y1List.get(i) + ",");
					bw.write(x2List.get(i) + ",");
					bw.write(y2List.get(i) + ",");
					bw.write(drawColorList.get(i) + ",");
					bw.write(lineColorList.get(i) + ",");
					bw.write(lineWidthList.get(i) + ",");
					bw.newLine();
				}
				bw.close();

			}catch(IOException e){
				System.out.println(e);
				info.setText("ファイルが開けません");
			}
		}else{
			info.setText("上書きできません.一度保存して下さい.");
		}
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void imageOp(int a){
		imageType = a;
	}
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void dataExport(File file){
		mouse.writeImage = true;
		mouse.repaint();
		mouse.writeImage = false;
		try {
			if(imageType == PNG){
				ImageIO.write(mouse.bi, "png", file);
			}else if(imageType == GIF){
				ImageIO.write(mouse.bi, "gif", file);
			}else if(imageType == JPG){
				ImageIO.write(mouse.bi, "jpg", file);
			}
			info.setText(file.getName() + "を出力しました.");
		} catch (Exception e) {
			System.out.println("error in write");
		}
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void unDo(){
		if(mouse.doCount < typeList.size()){
			mouse.doCount++;
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
		if(mouse.doCount > 0){
			mouse.doCount--;
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
	public void setAntiAliasing(boolean state){
		mouse.antiAliasing = state;
		mouse.repaint();
		if(state == true){
			info.setText("アンチエイリアシングを有効にしました.");
		}else{
			info.setText("アンチエイリアシングを無効にしました.");
		}
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public void setGrid(boolean state){
		mouse.setGrid = state;
		mouse.repaint();
		if(state == true){
			info.setText("グリッド線を表示しました.");
		}else{
			info.setText("グリッド線を非表示にしました.");
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
		}else if(obj==linecolor){
			line_color = linecolor.getSelectedIndex();
		}else if(obj==drawcolor){
			draw_color = drawcolor.getSelectedIndex();
		}
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	class DrawByMouse extends JPanel implements MouseListener,MouseMotionListener{

		int x, y, w1, h1, w2, h2, x1, y1, x2, y2;
		int lineWidth;
		boolean writeImage = false;
		BufferedImage bi; //オフスクリーンイメージ
		Graphics2D g2;  //Graphicsコンテキスト
		boolean antiAliasing = true, setGrid = true;

		int doCount = 0;

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
		//各リストを初期化
		private void initList(){
			typeList.clear();
			x1List.clear();
			x2List.clear();
			y1List.clear();
			y2List.clear();
			drawColorList.clear();
			lineColorList.clear();
			lineWidthList.clear();
			// typeを0にすると次にボタンを押すまで図形が書けない
			x1 = 0;
			y1 = 0;
			x2 = 0;
			y2 = 0;
			doCount = 0;
		}

		private void undoReset(){
			int a = typeList.size() ,b=typeList.size() - doCount;
			for(int i=b;i<a;i++){
				typeList.remove(b);
				x1List.remove(b);
				x2List.remove(b);
				y1List.remove(b);
				y2List.remove(b);
				drawColorList.remove(b);
				lineColorList.remove(b);
				lineWidthList.remove(b);
			}
			doCount = 0;
		}

		DrawByMouse(){
			setBackground(Color.white);
			setPreferredSize(new Dimension(600,600));
			addMouseListener(this);
			addMouseMotionListener(this);
			bi = new BufferedImage(600, 600, BufferedImage.TYPE_INT_BGR);	//BufferedImageの作成
			g2 = bi.createGraphics(); //Graphicsコンテキストを得る
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
				int index = typeList.size()-1-doCount; //インデックスは0からだから1引く
				typeList.set(index,type);
				x1List.set(index,x1);
				y1List.set(index,y1);
				x2List.set(index,x2);
				y2List.set(index,y2);
				drawColorList.set(index,draw_color);
				lineColorList.set(index,line_color);
				lineWidthList.set(index,lineWidth);
				doCount--;
			}else{
				typeList.add(type);
				x1List.add(x1);
				y1List.add(y1);
				x2List.add(x2);
				y2List.add(y2);
				drawColorList.add(draw_color);
				lineColorList.add(line_color);
				lineWidthList.add(lineWidth);
			}
		}

		protected void paintComponent(Graphics g){    //paintComponentメソッドを再定義
			super.paintComponent(g);	//superクラスのpaintComponentの実行
			drawObject();
			//g.drawImage(bi, 0, 0, this);
			g.drawImage(bi, 0, 0, 600, 600, this); //コンストラクタは結構種類ある(P.144)

			// drawImageの後じゃないと、上書きされる
			if(setGrid == true){
				drawGrid(g);
			}
		}

//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
		private void drawObject(){
			g2.setColor(Color.white); //描画色を黒にする
			g2.fillRect(0,0,600,600); //背景を白くするため描画色で四角を描く

			lineWidth = lineSlider.getValue()/10;

			if(antiAliasing == true){
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			}else{
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
			}

			if(typeList.size() > 0){
				for(int i = 0 ; i < typeList.size() - doCount ; i++){
					BasicStroke wideStroke = new BasicStroke((float)lineWidthList.get(i));	// 線の太さ
					g2.setStroke(wideStroke);
					g2.setColor(c[drawColorList.get(i)]);

					if(typeList.get(i) == LINE){
						g2.setColor(c[lineColorList.get(i)]);
						g2.drawLine(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
					}else if(typeList.get(i) == RECT){
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						g2.fillRect( w1, h1, w2, h2);
						g2.setColor(c[lineColorList.get(i)]);
						g2.drawRect( w1, h1, w2, h2);
					}else if(typeList.get(i) == OVAL){
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						g2.fillOval( w1, h1, w2, h2);
						g2.setColor(c[lineColorList.get(i)]);
						g2.drawOval( w1, h1, w2, h2);
					}
				}
			}

			BasicStroke wideStroke = new BasicStroke((float)lineWidth);	// 線の太さ
			g2.setStroke(wideStroke);
			g2.setColor(c[draw_color]);
			if(type == LINE){
				g2.setColor(c[line_color]);
				g2.drawLine(x1,y1,x2,y2);
			}else if(type == RECT){
				exchange(x1,y1,x2,y2);
				g2.fillRect( w1, h1, w2, h2);
				g2.setColor(c[line_color]);
				g2.drawRect( w1, h1, w2, h2);
			}else if(type == OVAL){
				exchange(x1,y1,x2,y2);
				g2.fillOval( w1, h1, w2, h2);
				g2.setColor(c[line_color]);
				g2.drawOval( w1, h1, w2, h2);
			}
		}

//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
		private void drawGrid(Graphics g){
			for(int i=1;i<12;i++){
				if(i!=6){
					g.setColor(new Color(211,211,211,50));
				}else{
					g.setColor(new Color(0, 0, 0,60));		//真ん中だけちょっと濃い色を表示
				}
				g.drawLine(0,i*50,600,i*50);
				g.drawLine(i*50,0,i*50,600);
			}
		}

		public int gridPosition(int a){	//位置を補正.(グリッド線)
			int b;
			for(int i=0;i<=12;i++){
				b = i*50;
				if(Math.abs(b-a) < 8){				//グリッド線との距離が10より小さかったら.
					a = i*50;
					break;
				}
			}
			return a;
		}

//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
		public void mousePressed(MouseEvent e){

			if(doCount > 0){
				undoReset();
			}


			if(setGrid == true){
				x1 = gridPosition(e.getX());
				y1 = gridPosition(e.getY());
			}else{
				x1 = e.getX();
				y1 = e.getY();
			}
			x2 = x1; //x2,y2も初期化しないと一瞬へんな出力が起きる.
			y2 = y1;
		}
		public void mouseDragged(MouseEvent e){ 
			if(doCount > 0){
				undoReset();
			}

			if(setGrid == true){
				x2 = gridPosition(e.getX());
				y2 = gridPosition(e.getY());
			}else{
				x2 = e.getX();
				y2 = e.getY();
			}
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
			repaint();     //コンポーネント全体を再描画
		}
		public void mouseEntered(MouseEvent e){
		}
		public void mouseExited(MouseEvent e){
			position.setText("(none,none)");
		}

	}
}
