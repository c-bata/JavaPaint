import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.border.*;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

class DrawGraphics extends JPanel implements ActionListener{

	DrawByMouse mouse;
	BufferedImage bi = null;

	JButton brect, bline, boval, broundrect, bpolyline, bselect, btext, bimage;
	final int RECT = 1, OVAL = 2, LINE=3, ROUNDRECT=4, POLYLINE=5, SELECT=6, TEXT=7, IMAGE=8;
	int type = RECT;

	final int PNG=1, GIF=2, JPG=3;
	int imageType = PNG;

	ArrayList<Integer> typeList      = new ArrayList<Integer>();
	ArrayList<Integer> x1List        = new ArrayList<Integer>();
	ArrayList<Integer> x2List        = new ArrayList<Integer>();
	ArrayList<Integer> y1List        = new ArrayList<Integer>();
	ArrayList<Integer> y2List        = new ArrayList<Integer>();
	ArrayList<Integer> drawColorList = new ArrayList<Integer>();
	ArrayList<Integer> lineColorList = new ArrayList<Integer>();
	ArrayList<Integer> clearColorList= new ArrayList<Integer>();
	ArrayList<Integer> lineWidthList = new ArrayList<Integer>();
	ArrayList<String> textStringList = new ArrayList<String>();

	JLabel position,info;

	Color [] c = {Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray, Color.green, Color.lightGray, Color.magenta, Color.orange, Color.pink, Color.red, Color.white, Color.yellow};
	int line_color = 0,draw_color = 13, back_color = 11;

	//各カラーのRGBを記憶
	int [] red   = {0, 0, 0, 64, 128, 0, 192, 255, 255, 255, 255, 255, 255};
	int [] green = {0, 0, 255, 64, 128,255,192,0,200,175,0,255,255};
	int [] blue  = {0, 255,255,64,128,0,192,255,0,175,0,255,0};
	
	JButton undoButton,redoButton;

	BevelBorder raiseborder = new BevelBorder(BevelBorder.LOWERED);
	JButton bblack, bblue, bcyan, bdarkGray, bgray, bgreen, blightGray, bmagenta, borange, bpink, bred, bwhite, byellow, bclear;
	String [] list = {"黒","青","シアン","ダークグレー","グレー","緑","ライトグレー","マゼンタ","オレンジ","ピンク","赤","白","黃","なし"};
	JRadioButton lineRadio, drawRadio, backRadio;
	JLabel lineLabel, drawLabel, backLabel;
	JSlider lineSlider, clearSlider;

	File presentFile = null; //現在編集中のファイルを保持

	String textString = null; //Text挿入で挿入する文字列

	public DrawGraphics(){

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
		brect     = new JButton("", new ImageIcon("./img/rect.png"));
		boval     = new JButton("", new ImageIcon("./img/oval.png"));
		bline     = new JButton("", new ImageIcon("./img/line.png"));
		broundrect  = new JButton("", new ImageIcon("./img/roundrect.png"));
		bpolyline = new JButton("", new ImageIcon("./img/polyline.png"));
		bselect   = new JButton("", new ImageIcon("./img/select.png"));
		btext     = new JButton("", new ImageIcon("./img/text.png"));
		bimage   = new JButton("", new ImageIcon("./img/image.png"));
		//枠線の追加
		buttonRaised();
		brect.setBorder(raiseborder);
		//ActionListenerの設定
		brect.addActionListener(this);
		boval.addActionListener(this);
		bline.addActionListener(this);
		broundrect.addActionListener(this);
		bpolyline.addActionListener(this);
		bselect.addActionListener(this);
		btext.addActionListener(this);
		bimage.addActionListener(this);
		//ボタンを置くパネルを作り，ボタンを配置
		JPanel object = new JPanel();
		object.setBorder(new EmptyBorder( 0, 20, 20, 20));
		object.setLayout(new GridLayout(2,2));
		object.add(brect);
		object.add(boval);
		object.add(bline);
		object.add(broundrect);
		object.add(bpolyline);
		object.add(bselect);
		object.add(btext);
		object.add(bimage);

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
		bblack      = new JButton("", new ImageIcon("./img/black.png"));
		bblue       = new JButton("", new ImageIcon("./img/blue.png"));
		bcyan       = new JButton("", new ImageIcon("./img/cyan.png"));
		bdarkGray   = new JButton("", new ImageIcon("./img/darkGray.png"));
		bgray       = new JButton("", new ImageIcon("./img/gray.png"));
		bgreen      = new JButton("", new ImageIcon("./img/green.png"));
		blightGray  = new JButton("", new ImageIcon("./img/lightGray.png"));
		bmagenta    = new JButton("", new ImageIcon("./img/magenta.png"));
		borange     = new JButton("", new ImageIcon("./img/orange.png"));
		bpink       = new JButton("", new ImageIcon("./img/pink.png"));
		bred        = new JButton("", new ImageIcon("./img/red.png"));
		bwhite      = new JButton("", new ImageIcon("./img/white.png"));
		byellow     = new JButton("", new ImageIcon("./img/yellow.png"));
		bclear      = new JButton("", new ImageIcon("./img/clear.png"));

		bblack.addActionListener(this);
		bblue.addActionListener(this);
		bcyan.addActionListener(this);
		bdarkGray.addActionListener(this);
		bgray.addActionListener(this);
		bgreen.addActionListener(this);      
		blightGray.addActionListener(this);
		bmagenta.addActionListener(this);
		borange.addActionListener(this);
		bpink.addActionListener(this);
		bred.addActionListener(this);
		bwhite.addActionListener(this);
		byellow.addActionListener(this);
		bclear.addActionListener(this);

		Border colorborder = new EmptyBorder( 1, 1, 1, 1);
		bblack.setBorder(colorborder);
		bblue.setBorder(colorborder);
		bcyan.setBorder(colorborder);
		bdarkGray.setBorder(colorborder);
		bgray.setBorder(colorborder);
		bgreen.setBorder(colorborder);
		blightGray.setBorder(colorborder);
		bmagenta.setBorder(colorborder);
		borange.setBorder(colorborder);
		bpink.setBorder(colorborder);
		bred.setBorder(colorborder);
		bwhite.setBorder(colorborder);
		byellow.setBorder(colorborder);
		bclear.setBorder(colorborder);
		JPanel colorButtons = new JPanel();
		colorButtons.setLayout(new GridLayout(3,5));
		colorButtons.setBorder(new EmptyBorder( 5, 20, 20, 20));

		LineBorder inborder1 = new LineBorder(Color.lightGray, 1);
		TitledBorder border1 = new TitledBorder(inborder1, "カラーパレット", TitledBorder.CENTER, TitledBorder.TOP);
		//colorButtons.setPreferredSize(new Dimension(100,100));
		colorButtons.setBorder(border1);

		colorButtons.add(bblack);
		colorButtons.add(bwhite);
		colorButtons.add(bdarkGray);
		colorButtons.add(bgray);
		colorButtons.add(blightGray);
		colorButtons.add(bblue);
		colorButtons.add(bcyan);
		colorButtons.add(bgreen);
		colorButtons.add(bmagenta);
		colorButtons.add(borange);
		colorButtons.add(bpink);
		colorButtons.add(bred);
		colorButtons.add(byellow);
		colorButtons.add(bclear);

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
		JPanel footer = new JPanel();
		FlowLayout footerlayout = new FlowLayout();	//左詰め
		footerlayout.setAlignment(FlowLayout.LEFT);
		footer.setLayout(footerlayout);
		position = new JLabel("(none,none)");

		info = new JLabel("");
		//info.setForeground(Color.white);
		//info.setBackground(Color.black); //Macのルック&フィールでは適用されない
		footer.add(position);
		footer.add(info);

		// 色の選択
		JPanel color = new JPanel();
		color.setBorder(new EmptyBorder( 20, 20, 20, 20));
		color.setLayout(new GridLayout(3,2));
		lineRadio = new JRadioButton("線の色");
		drawRadio = new JRadioButton("塗りつぶし");
		backRadio = new JRadioButton("背景色");
		lineRadio.setSelected(true);
		drawRadio.setSelected(false);
		backRadio.setSelected(false);
		ButtonGroup group = new ButtonGroup();
		group.add(lineRadio);
		group.add(drawRadio);
		group.add(backRadio);

		color.add(lineRadio);
		lineLabel = new JLabel(list[line_color]);
		drawLabel = new JLabel(list[draw_color]);
		backLabel = new JLabel(list[back_color]);
		color.add(lineLabel);
		color.add(drawRadio);
		color.add(drawLabel);
		color.add(backRadio);
		color.add(backLabel);


		//undo,redo
		JPanel dopanel = new JPanel();
		undoButton     = new JButton("元に戻す", new ImageIcon("./img/undo1.png"));
		redoButton     = new JButton("やり直し", new ImageIcon("./img/redo1.png"));
		undoButton.addActionListener(this);
		redoButton.addActionListener(this);
		//redoButton.setBorder(border);
		dopanel.add(undoButton);
		dopanel.add(redoButton);

		// スライダー
		lineSlider = new JSlider(0, 64, 5);
		TitledBorder border2 = new TitledBorder(inborder1, "線の太さ", TitledBorder.CENTER, TitledBorder.TOP);
		lineSlider.setBorder(border2);
		lineSlider.setMajorTickSpacing(10);
		lineSlider.setMinorTickSpacing(1);
		lineSlider.setPaintTicks(true);

		// スライダー
		clearSlider = new JSlider(0, 255, 255);
		TitledBorder border3 = new TitledBorder(inborder1, "透明度", TitledBorder.CENTER, TitledBorder.TOP);
		clearSlider.setBorder(border3);
		clearSlider.setMajorTickSpacing(10);
		clearSlider.setMinorTickSpacing(1);
		clearSlider.setPaintTicks(true);

		JPanel sidepanel = new JPanel();
		sidepanel.setBorder(new EmptyBorder( 5, 20, 5, 20));
		sidepanel.setLayout(new BoxLayout(sidepanel, BoxLayout.Y_AXIS));
		sidepanel.add(dopanel);
		sidepanel.add(object);
		sidepanel.add(new JSeparator());
		sidepanel.add(color);
		sidepanel.add(colorButtons);
		sidepanel.add(new JSeparator());
		sidepanel.add(lineSlider);
		sidepanel.add(clearSlider);

		setLayout(new BorderLayout());
		mouse = new DrawByMouse();
		add(mouse, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);
		add(sidepanel, BorderLayout.EAST);
	}

	public void buttonRaised(){
		BevelBorder borderRaised = new BevelBorder(BevelBorder.RAISED);
		brect.setBorder(borderRaised);
		boval.setBorder(borderRaised);
		bline.setBorder(borderRaised);
		broundrect.setBorder(borderRaised);
		bpolyline.setBorder(borderRaised);
		bselect.setBorder(borderRaised);
		btext.setBorder(borderRaised);
		bimage.setBorder(borderRaised);
	}

	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	public void newFile(){
		mouse.initList();
		mouse.repaint();
		info.setText("新しい画像を生成しました.");
		presentFile = null;
	}
	//////////////////////////////////////////////////////////////////////
	private static boolean checkBeforeReadfile(File file){
		if (file.exists()){
			if (file.isFile() && file.canRead()){
				return true;
			}
		}
		return false;
	}
	public void dataOpen(){

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
						clearColorList.add(Integer.parseInt(element[7]));
						lineWidthList.add(Integer.parseInt(element[8]));
						textStringList.add(element[9]);
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
				bw.write(clearColorList.get(i) + ",");
				bw.write(lineWidthList.get(i) + ",");
				bw.write(textStringList.get(i) + ",");
				bw.newLine();
			}
			bw.close();

		}catch(IOException e){
			System.out.println(e);
			info.setText("ファイルが開けません");
		}
	}
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
					bw.write(clearColorList.get(i) + ",");
					bw.write(lineWidthList.get(i) + ",");
					bw.write(textStringList.get(i) + ",");
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
	public void imageOp(int a){
		imageType = a;
	}
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
	public void actionPerformed(ActionEvent e) {
		//String actionCommand = e.getActionCommand();
		Object obj=e.getSource();
		if(obj==brect){
			buttonRaised();
			brect.setBorder(raiseborder);
			type = RECT;
		}else if(obj==boval){
			buttonRaised();
			boval.setBorder(raiseborder);
			type = OVAL;
		}else if(obj==bline){
			buttonRaised();
			bline.setBorder(raiseborder);
			type = LINE;
		}else if(obj==bpolyline){
			buttonRaised();
			bpolyline.setBorder(raiseborder);
			type = POLYLINE;
		}else if(obj==broundrect){
			buttonRaised();
			broundrect.setBorder(raiseborder);
			type = ROUNDRECT;
		}else if(obj==bimage){
			buttonRaised();
			bimage.setBorder(raiseborder);

			JFileChooser filechooser = new JFileChooser();
			filechooser.setDialogTitle("画像を選択"); // titleを変更
			int selected = filechooser.showSaveDialog(this);

			if (selected == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				info.setText(file.getName());
				mouse.openImage(file);
				mouse.repaint();
			}else if (selected == JFileChooser.CANCEL_OPTION){
				System.out.println("キャンセルされました");
			}else if (selected == JFileChooser.ERROR_OPTION){
				System.out.println("エラー又は取消しがありました");
			}


			type = IMAGE;
		}else if(obj==btext){
			buttonRaised();
			btext.setBorder(raiseborder);
			type = TEXT;
			new TextDialog();
		}else if(obj==bselect){
			buttonRaised();
			bselect.setBorder(raiseborder);
			type = SELECT;
		}else if(obj==bblack){
			if(lineRadio.isSelected() == true){
				line_color = 0;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 0;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 0;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bblue){
			if(lineRadio.isSelected() == true){
				line_color = 1;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 1;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 1;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bcyan){
			if(lineRadio.isSelected() == true){
				line_color = 2;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 2;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 2;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bdarkGray){
			if(lineRadio.isSelected() == true){
				line_color = 3;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 3;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 3;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bgray){
			if(lineRadio.isSelected() == true){
				line_color = 4;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 4;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 4;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bgreen){
			if(lineRadio.isSelected() == true){
				line_color = 5;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 5;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 5;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==blightGray){
			if(lineRadio.isSelected() == true){
				line_color = 6;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 6;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 6;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bmagenta){
			if(lineRadio.isSelected() == true){
				line_color = 7;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 7;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 7;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==borange){
			if(lineRadio.isSelected() == true){
				line_color = 8;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 8;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 8;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bpink){
			if(lineRadio.isSelected() == true){
				line_color = 9;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 9;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 9;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bred){
			if(lineRadio.isSelected() == true){
				line_color = 10;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 10;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 10;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bwhite){
			if(lineRadio.isSelected() == true){
				line_color = 11;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 11;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 11;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==byellow){
			if(lineRadio.isSelected() == true){
				line_color = 12;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 12;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				back_color = 12;
				backLabel.setText(list[back_color]);
				mouse.repaint();
			}
		}else if(obj==bclear){
			if(lineRadio.isSelected() == true){
				line_color = 13;
				lineLabel.setText(list[line_color]);
			}else if(drawRadio.isSelected() == true){
				draw_color = 13;
				drawLabel.setText(list[draw_color]);
			}else if(backRadio.isSelected() == true){
				info.setText("背景にその色は設定できません");
			}
		}else if(obj==undoButton){
			unDo();
		}else if(obj==redoButton){
			reDo();
		}
	}
//////////////////////////////////////////////////////////////////////
	class DrawByMouse extends JPanel implements MouseListener,MouseMotionListener{

		int x, y, w1, h1, w2, h2, x1, y1, x2, y2;
		int lineWidth, clear_color;
		boolean writeImage = false;
		BufferedImage bi; //オフスクリーンイメージ
		Graphics2D g2;  //Graphicsコンテキスト
		boolean antiAliasing = true, setGrid = true;

		int doCount = 0;

		BufferedImage readImage = null;

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
			clearColorList.clear();
			lineWidthList.clear();
			textStringList.clear();
			// typeを0にすると次にボタンを押すまで図形が書けない
			x1 = 0;
			y1 = 0;
			x2 = 0;
			y2 = 0;
			doCount = 0;
			textString = null;
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
				clearColorList.remove(b);
				lineWidthList.remove(b);
				textStringList.remove(b);
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
				clearColorList.set(index,clear_color);
				lineWidthList.set(index,lineWidth);
				if(textString == null){ textString = new String("???");}	//もしnullまら???を入れる
				textStringList.set(index,textString);	// textの時でも,addしないとインデックスがずれる.
				doCount--;
			}else{
				typeList.add(type);
				x1List.add(x1);
				y1List.add(y1);
				x2List.add(x2);
				y2List.add(y2);
				drawColorList.add(draw_color);
				lineColorList.add(line_color);
				clearColorList.add(clear_color);
				lineWidthList.add(lineWidth);
				textStringList.add(textString);	// textの時でも,addしないとインデックスがずれる.
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

			//背景
			g2.setBackground(c[back_color]);
			g2.clearRect(0, 0, getWidth(), getHeight());

			// 画像
			if (readImage != null){
				g2.drawImage(readImage, 0, 0, this);
			}

			////////////////////////////////////////////////////

			lineWidth = lineSlider.getValue();
			clear_color = clearSlider.getValue();

			if(antiAliasing == true){
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			}else{
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
			}

			if(typeList.size() > 0){
				for(int i = 0 ; i < typeList.size() - doCount ; i++){
					BasicStroke wideStroke = new BasicStroke((float)lineWidthList.get(i));	// 線の太さ
					g2.setStroke(wideStroke);

					if(typeList.get(i) == LINE){
						if(lineColorList.get(i)!=13){
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							g2.drawLine(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						}
					}else if(typeList.get(i) == RECT){
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						if(drawColorList.get(i)!=13){
							g2.setColor(new Color(red[drawColorList.get(i)],green[drawColorList.get(i)],blue[drawColorList.get(i)],clearColorList.get(i)));
							g2.fillRect( w1, h1, w2, h2);
						}
						if(lineColorList.get(i)!=13){
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							g2.drawRect( w1, h1, w2, h2);
						}
					}else if(typeList.get(i) == OVAL){
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						if(drawColorList.get(i)!=13){
							g2.setColor(new Color(red[drawColorList.get(i)],green[drawColorList.get(i)],blue[drawColorList.get(i)],clearColorList.get(i)));
							g2.fillOval( w1, h1, w2, h2);
						}
						if(lineColorList.get(i)!=13){
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							g2.drawOval( w1, h1, w2, h2);
						}
					}else if(typeList.get(i) == TEXT){
						if(lineColorList.get(i)!=13){
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							try{
								g2.drawString(textStringList.get(i), x1List.get(i), y1List.get(i));
							}catch(NullPointerException e){
								info.setText("エラー発生");
								System.out.println(textString+ ","+textStringList.get(i));
							}
						}
					}
				}
			}

			BasicStroke wideStroke = new BasicStroke((float)lineWidth);	// 線の太さ
			g2.setStroke(wideStroke);
			if(type == LINE){
				if(line_color!=13){
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawLine(x1,y1,x2,y2);
				}
			}else if(type == RECT){
				exchange(x1,y1,x2,y2);
				if(draw_color!=13){
					g2.setColor(new Color(red[draw_color],green[draw_color],blue[draw_color],clear_color));
					g2.fillRect( w1, h1, w2, h2);
				}
				if(line_color!=13){
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawRect( w1, h1, w2, h2);
				}
			}else if(type == OVAL){
				exchange(x1,y1,x2,y2);
				if(draw_color!=13){
					g2.setColor(new Color(red[draw_color],green[draw_color],blue[draw_color],clear_color));
					g2.fillOval( w1, h1, w2, h2);
				}
				if(line_color!=13){
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawOval( w1, h1, w2, h2);
				}
			}else if(type == TEXT){
				if(textString != null){
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawString(textString, x, y);
				}
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

		public void openImage(File file){
			try {
				readImage = ImageIO.read(file);
			} catch (Exception e) {
				e.printStackTrace();
				readImage = null;
			}
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
			if(setGrid == true){
				x = gridPosition(e.getX());
				y = gridPosition(e.getY());
			}else{
				x = e.getX();
				y = e.getY();
			}
			position.setText("(" + Integer.toString(x) + "," + Integer.toString(y) + ")");
			if(textString != null){
				repaint();     //コンポーネント全体を再描画
			}
		}
		public void mouseReleased(MouseEvent e){
			if(type != -1){
				listAdd();
			}
			repaint();
		}
		public void mouseClicked(MouseEvent e){
			if(textString != null){
				if(setGrid == true){
					x1 = gridPosition(e.getX());
					y1 = gridPosition(e.getY());
				}else{
					x1 = e.getX();
					y1 = e.getY();
				}
				//listAdd();
				textString = null;
				type = -1; x1 = 0; y1 = 0;
				buttonRaised();
			}
			repaint();     //コンポーネント全体を再描画
		}
		public void mouseEntered(MouseEvent e){
		}
		public void mouseExited(MouseEvent e){
			position.setText("(none,none)");
		}

	}

	//////////////////////////////////////////////////////////////
	class TextDialog extends JDialog implements ActionListener{
		JTextField field = new JTextField(8);
		JButton save = new JButton("OK");
		JButton cancel = new JButton("キャンセル");
		TextDialog(){
			setSize(200, 100);
			//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setLayout(new FlowLayout());
			setModal(true);
			JPanel panel = new JPanel();
			panel.add(new JLabel("文字列:"));
			panel.add(field);
			add(panel);
			JPanel panelb = new JPanel();
			panelb.add(save);
			panelb.add(cancel);
			add(panelb);
			save.addActionListener(this);
			cancel.addActionListener(this);
			setVisible(true);
		}
		public void actionPerformed(ActionEvent e){
			String str = field.getText();
			if(e.getSource() == save && str.equals("") == false){
				textString = new String(str);
				info.setText("画面をクリックして下さい.");
			}else{
				type = -1;
				buttonRaised();
			}
			setVisible(false);
		}
	}

}
