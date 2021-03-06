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

	JButton brect, bline, boval, broundrect, btext, bimage;
	final int RECT = 1, OVAL = 2, LINE=3, ROUNDRECT=4, TEXT=5, IMAGE=6;
	int type = RECT;

	final int PNG=1, GIF=2, JPG=3;
	int imageType = PNG;

	// 図形の情報はArrayListに格納していく．
	ArrayList<Integer> typeList      = new ArrayList<Integer>();	// 図形の種類
	ArrayList<Integer> x1List        = new ArrayList<Integer>();	// ドラッグ開始位置のx座標
	ArrayList<Integer> x2List        = new ArrayList<Integer>();	// ドラッグ終了位置のx座標
	ArrayList<Integer> y1List        = new ArrayList<Integer>();	// ドラッグ開始位置のy座標
	ArrayList<Integer> y2List        = new ArrayList<Integer>();	// ドラッグ終了位置のy座標
	ArrayList<Integer> drawColorList = new ArrayList<Integer>();	// 塗りつぶしの色
	ArrayList<Integer> lineColorList = new ArrayList<Integer>();	// 線の色
	ArrayList<Integer> clearColorList= new ArrayList<Integer>();	// 透明度
	ArrayList<Integer> lineWidthList = new ArrayList<Integer>();	// 線の太さ
	ArrayList<String> textStringList = new ArrayList<String>();		// 文字, 画像ファイルへのパス

	JLabel position,info;

	int line_color = 0,draw_color = 13, back_color = 11;
	//各カラーのRGBを記憶. alpha値を指定するためにRGBをそれぞれ記憶する．
	String [] list = {"黒","青","シアン","ダークグレー","グレー","緑","ライトグレー","マゼンタ","オレンジ","ピンク","赤","白","黃","なし"};
	int [] red   = {0,   0,   0, 64, 128,   0, 192, 255, 255, 255, 255, 255, 255};
	int [] green = {0,   0, 255, 64, 128, 255, 192,   0, 200, 175,   0, 255, 255};
	int [] blue  = {0, 255, 255, 64, 128,   0, 192, 255,   0, 175,   0, 255,   0};

	//各コンポーネントの宣言
	JButton undoButton,redoButton;
	BevelBorder raiseborder = new BevelBorder(BevelBorder.LOWERED);
	JButton bblack, bblue, bcyan, bdarkGray, bgray, bgreen, blightGray, bmagenta, borange, bpink, bred, bwhite, byellow, bclear;
	JRadioButton lineRadio, drawRadio, backRadio;
	JLabel lineLabel, drawLabel, backLabel;
	JSlider lineSlider, clearSlider;
	File presentFile = null; //現在編集中のファイルを保持
	String textString = null, fileString = null; //Text挿入で挿入する文字列
	boolean okflag = false;
	int lineWidth, fontIndex = 0;
	String[] font;			//コンピュータに入っているフォントを格納

	public DrawGraphics(){

		brect     = new JButton("", new ImageIcon("./img/rect.png"));
		boval     = new JButton("", new ImageIcon("./img/oval.png"));
		bline     = new JButton("", new ImageIcon("./img/line.png"));
		broundrect= new JButton("", new ImageIcon("./img/roundrect.png"));
		btext     = new JButton("", new ImageIcon("./img/text.png"));
		bimage    = new JButton("", new ImageIcon("./img/image.png"));
		// JButtonの枠線を,立体的に見えるように変更. 選択中のボタンは凹んで見える.
		buttonRaised();
		brect.setBorder(raiseborder);
		//ActionListenerの設定
		brect.addActionListener(this);
		boval.addActionListener(this);
		bline.addActionListener(this);
		broundrect.addActionListener(this);
		btext.addActionListener(this);
		bimage.addActionListener(this);

		// ボタンの上にカーソルが来ると,マウスカーソルの画像を手の形に変更する.
		brect.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boval.setCursor(new Cursor(Cursor.HAND_CURSOR));
		bline.setCursor(new Cursor(Cursor.HAND_CURSOR));
		broundrect.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btext.setCursor(new Cursor(Cursor.HAND_CURSOR));
		bimage.setCursor(new Cursor(Cursor.HAND_CURSOR));

		//ボタンを置くパネルを作り，ボタンを配置
		JPanel object = new JPanel();
		object.setBorder(new EmptyBorder( 0, 20, 20, 20));
		object.setLayout(new GridLayout(2,2));
		object.add(brect);
		object.add(boval);
		object.add(bline);
		object.add(broundrect);
		object.add(btext);
		object.add(bimage);

		// JButtonに画像を挿入
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

		// 各ボタンにアクションリスナーを設定.
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

		// カラーパネルのボタンの枠線を変更
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

		// カラーパネルを作成し，枠線で囲む
		JPanel colorButtons = new JPanel();
		colorButtons.setLayout(new GridLayout(3,5));
		colorButtons.setBorder(new EmptyBorder( 5, 20, 20, 20));
		LineBorder inborder1 = new LineBorder(Color.lightGray, 1);
		TitledBorder border1 = new TitledBorder(inborder1, "カラーパレット", TitledBorder.CENTER, TitledBorder.TOP);
		colorButtons.setBorder(border1);

		// カラーパネルにボタンを配置
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

		// 画面下部に，マウスカーソルの位置やメッセージを表示するラベルを設置
		JPanel footer = new JPanel();
		FlowLayout footerlayout = new FlowLayout();	//左詰め
		footerlayout.setAlignment(FlowLayout.LEFT);
		footer.setLayout(footerlayout);
		position = new JLabel("(none,none)");
		info = new JLabel("");
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
		// ラジオボタンをグループ化
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

		undoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		redoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		dopanel.add(undoButton);
		dopanel.add(redoButton);

		// 文字サイズを変更できるスライダー
		lineSlider = new JSlider(0, 64, 5);
		TitledBorder border2 = new TitledBorder(inborder1, "線の太さ・文字サイズ", TitledBorder.CENTER, TitledBorder.TOP);
		lineSlider.setBorder(border2);
		lineSlider.setMajorTickSpacing(10);
		lineSlider.setMinorTickSpacing(1);
		lineSlider.setPaintTicks(true);

		// 透明度を変更できるスライダー
		clearSlider = new JSlider(0, 255, 255);
		TitledBorder border3 = new TitledBorder(inborder1, "透明度", TitledBorder.CENTER, TitledBorder.TOP);
		clearSlider.setBorder(border3);
		clearSlider.setMajorTickSpacing(10);
		clearSlider.setMinorTickSpacing(1);
		clearSlider.setPaintTicks(true);

		// スライダーの上にカーソルが来ると，マウスカーソルの画像を手形に変更.
		lineSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));
		clearSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// 各JPanelを配置
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

	public void buttonRaised(){		//図形選択パネルの全てのボタンをRaiseさせる。
		BevelBorder borderRaised = new BevelBorder(BevelBorder.RAISED);
		brect.setBorder(borderRaised);
		boval.setBorder(borderRaised);
		bline.setBorder(borderRaised);
		broundrect.setBorder(borderRaised);
		btext.setBorder(borderRaised);
		bimage.setBorder(borderRaised);
	}

	public void newFile(){		//状態をリセットする．
		new NewFileDialog();
		if(okflag == true){
			mouse.initList();
			mouse.repaint();
			info.setText("新規作成しました.");
			presentFile = null;
			okflag = false;
		}
	}
	private static boolean checkBeforeReadfile(File file){	//ファイルが存在するか調べる．
		if (file.exists()){
			if (file.isFile() && file.canRead()){
				return true;
			}
		}
		return false;
	}
	public void dataOpen(){		//ベクター情報の保存されているファイルを開く
		mouse.initList(); // 一度ArrayListをクリア
		JFileChooser filechooser = new JFileChooser();
		filechooser.addChoosableFileFilter(new JpfFilter()); // 拡張子jpfにフィルタ
		int selected = filechooser.showOpenDialog(this);
		if (selected == JFileChooser.APPROVE_OPTION){
			File file = filechooser.getSelectedFile();
			presentFile = file;
			//System.out.println(file.getName());
			try{
				if (checkBeforeReadfile(file)){
					BufferedReader br = new BufferedReader(new FileReader(file));
					info.setText(file.getName() + "を読み込みました.");

					String[] element = br.readLine().split(",", -1);
					back_color = Integer.parseInt(element[0]);			//1行目は背景の情報が保存されている.

					String str;
					while((str = br.readLine()) != null){				// ArrayListに追加していく.
						element = str.split(",", -1);
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
					}
					mouse.repaint();		// 描画
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
	public void saveAs(File file){		//名前を付けて保存
		try{
			if (file.exists() == false){
				file.createNewFile();
				info.setText("ファイルが存在しないため作成します．");
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			presentFile = file;

			bw.write(back_color + ",");
			bw.newLine();
			for(int i=0; i < typeList.size() - mouse.doCount ; i++){	// 各図形情報をカンマ区切りで書き込み
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
			info.setText(file.getName() + "にセーブしました.");
		}catch(IOException e){
			System.out.println(e);
			info.setText("ファイルが開けません");
		}
	}
	public void save(){		//上書き保存
		if(presentFile != null){
			try{
				if (presentFile.exists() == false){
					System.out.println("ファイルが見つかりません.");
					return;
				}

				BufferedWriter bw = new BufferedWriter(new FileWriter(presentFile));
				bw.write(back_color + ",");
				bw.newLine();
				for(int i=0; i < typeList.size() - mouse.doCount ; i++){	// 元に戻すを使っている可能性があるので，差分ではなくまるごと保存し直す.
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
				info.setText( presentFile.getName() + "に上書き保存しました.");

			}catch(IOException e){
				System.out.println(e);
				info.setText("ファイルが開けません");
			}
		}else{
			info.setText("上書きできません.一度保存して下さい.");
		}
	}
	public void imageOp(int a){	//メニューバーから選択された出力画像のフォーマットを保存しておく.
		imageType = a;
	}
	public void dataExport(File file){	//画像を出力する．
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
	public void unDo(){		// やり直しの実行
		if(mouse.doCount < typeList.size()){
			mouse.doCount++;
			mouse.repaint();
			info.setText("「もとに戻す」を実行しました");
			mouse.x1 = 0;
			mouse.y1 = 0;
			mouse.x2 = 0;
			mouse.y2 = 0;
		}else{
			info.setText("現在，「元に戻す」は実行できません．");
		}
	}
	public void reDo(){		//元に戻すの実行
		if(mouse.doCount > 0){
			mouse.doCount--;
			mouse.repaint();
			info.setText("「やり直し」を実行しました");
			mouse.x1 = 0;
			mouse.y1 = 0;
			mouse.x2 = 0;
			mouse.y2 = 0;
		}else{
			info.setText("現在，「やり直し」は実行できません．");
		}
	}

	public void setAntiAliasing(boolean state){		//アンチエイリアス
		mouse.antiAliasing = state;
		mouse.repaint();
		if(state == true){
			info.setText("アンチエイリアシングを有効にしました.");
		}else{
			info.setText("アンチエイリアシングを無効にしました.");
		}
	}

	public void setGrid(boolean state){		//グリッド線
		mouse.setGrid = state;			// メニューバーのチェックメニューアイテムの状態を取得し,チェックがついていたらグリッドを表示
		mouse.repaint();
		if(state == true){
			info.setText("グリッド線を表示しました.");
		}else{
			info.setText("グリッド線を非表示にしました.");
		}
	}

	/* アクションイベント処理 */
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==brect){	// 図形の種類を選択するボタン
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
		}else if(obj==broundrect){
			buttonRaised();
			broundrect.setBorder(raiseborder);
			type = ROUNDRECT;
		}else if(obj==bimage){
			buttonRaised();
			bimage.setBorder(raiseborder);

			JFileChooser filechooser = new JFileChooser();
			filechooser.addChoosableFileFilter(new ImageFilter()); // 画像にフィルタ
			filechooser.setDialogTitle("画像を選択"); // titleを変更
			int selected = filechooser.showSaveDialog(this);

			if (selected == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				info.setText(file.getName() + "を貼り付けます");
				fileString = new String(file.getPath().toString());
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
		}else if(obj==bblack){	// カラーパネル上のボタン
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
		}else if(obj==undoButton){	// 元に戻す ボタン
			unDo();
		}else if(obj==redoButton){	// やり直し ボタン
			reDo();
		}
	}

	class DrawByMouse extends JPanel implements MouseListener,MouseMotionListener{	//描画領域

		int x, y, w1, h1, w2, h2, x1, y1, x2, y2;
		int clear_color;
		boolean writeImage = false;
		BufferedImage bi; //オフスクリーンイメージ
		Graphics2D g2;  //Graphicsコンテキスト
		boolean antiAliasing = true, setGrid = true;
		int doCount = 0;

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
			fileString = null;
			back_color = 11;
		}

		/* 元に戻すの情報をリセット */
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

		/* 画面を生成 */
		DrawByMouse(){
			setBackground(Color.white);
			setPreferredSize(new Dimension(600,600));
			addMouseListener(this);
			addMouseMotionListener(this);
			bi = new BufferedImage(600, 600, BufferedImage.TYPE_INT_BGR);	//BufferedImageの作成
			g2 = bi.createGraphics(); //Graphicsコンテキストを得る
		}

		/* x1>x2,y1>y2 となるように値を入れ替え */
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

		/* 描いた図形の情報をArrayListに格納する */
		private void listAdd(){
			if(doCount > 0){
				int index = typeList.size()-1-doCount; //インデックスは0からだから1引く
				typeList.set(index,type);
				x1List.set(index,x1);
				y1List.set(index,y1);
				if(type == TEXT){
					x2List.add(fontIndex);
				}else{
					x2List.add(x2);
				}
				y2List.set(index,y2);
				drawColorList.set(index,draw_color);
				lineColorList.set(index,line_color);
				clearColorList.set(index,clear_color);
				lineWidthList.set(index,lineWidth);
				if(type == IMAGE){
					textStringList.set(index, fileString);	// textの時でも,addしないとインデックスがずれる.
				}else{
					textStringList.set(index,textString);	// textの時でも,addしないとインデックスがずれる.
				}
				doCount--;
			}else{
				typeList.add(type);
				x1List.add(x1);
				y1List.add(y1);
				if(type == TEXT){
					x2List.add(fontIndex);
				}else{
					x2List.add(x2);
				}
				y2List.add(y2);
				drawColorList.add(draw_color);
				lineColorList.add(line_color);
				clearColorList.add(clear_color);
				lineWidthList.add(lineWidth);
				if(type == IMAGE){
					textStringList.add(fileString);	// textの時でも,addしないとインデックスがずれる.
				}else{
					textStringList.add(textString);	// textの時でも,addしないとインデックスがずれる.
				}
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

		/* 図形を描画する */
		private void drawObject(){
			//背景
			g2.setBackground(new Color(red[back_color],green[back_color],blue[back_color]));
			g2.clearRect(0, 0, getWidth(), getHeight());

			// 線の太さと透明度を設定.
			lineWidth = lineSlider.getValue();
			clear_color = clearSlider.getValue();

			if(antiAliasing == true){	// アンチエイリアシング
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			}else{
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
			}

			if(typeList.size() > 0){	// もし過去に図形を描いていれば,それをもう一度描画する.
				for(int i = 0 ; i < typeList.size() - doCount ; i++){

					BasicStroke wideStroke = new BasicStroke((float)lineWidthList.get(i));	// 線の太さ
					g2.setStroke(wideStroke);

					if(typeList.get(i) == LINE){			// 直線の挿入
						if(lineColorList.get(i)!=13){
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							g2.drawLine(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						}
					}else if(typeList.get(i) == RECT){		// 長方形の挿入
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						if(drawColorList.get(i)!=13){
							g2.setColor(new Color(red[drawColorList.get(i)],green[drawColorList.get(i)],blue[drawColorList.get(i)],clearColorList.get(i)));
							g2.fillRect( w1, h1, w2, h2);
						}
						if(lineColorList.get(i)!=13){
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							g2.drawRect( w1, h1, w2, h2);
						}
					}else if(typeList.get(i) == OVAL){		// 円の挿入
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						if(drawColorList.get(i)!=13){
							g2.setColor(new Color(red[drawColorList.get(i)],green[drawColorList.get(i)],blue[drawColorList.get(i)],clearColorList.get(i)));
							g2.fillOval( w1, h1, w2, h2);
						}
						if(lineColorList.get(i)!=13){
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							g2.drawOval( w1, h1, w2, h2);
						}
					}else if(typeList.get(i) == ROUNDRECT){	//角丸長方形の挿入
						exchange(x1List.get(i), y1List.get(i), x2List.get(i), y2List.get(i));
						if(drawColorList.get(i)!=13){
							g2.setColor(new Color(red[drawColorList.get(i)],green[drawColorList.get(i)],blue[drawColorList.get(i)],clearColorList.get(i)));
							g2.fillRoundRect( w1, h1, w2, h2, w2/3, h2/3);
						}
						if(lineColorList.get(i)!=13){
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							g2.drawRoundRect( w1, h1, w2, h2, w2/3, h2/3);
						}
					}else if(typeList.get(i) == TEXT){		// 文字の挿入
						if(lineColorList.get(i)!=13){
							g2.setFont(new Font(font[x2List.get(i)], Font.PLAIN, lineWidthList.get(i)*7)); // fontの種類は余ってるx2Listに入れてる.
							g2.setColor(new Color(red[lineColorList.get(i)],green[lineColorList.get(i)],blue[lineColorList.get(i)],clearColorList.get(i)));
							try{
								g2.drawString(textStringList.get(i), x1List.get(i), y1List.get(i));
							}catch(NullPointerException e){
								info.setText("エラー発生");
								System.out.println(textString+ ","+textStringList.get(i));
							}
						}
					}else if(typeList.get(i) == IMAGE){		// 画像の挿入
						try{
							g2.drawImage(openImage(new File(textStringList.get(i))), x1List.get(i), y1List.get(i), this);
						}catch(NullPointerException e){
							info.setText("エラー発生");
						}
					}
				}
			}



			BasicStroke wideStroke = new BasicStroke((float)lineWidth);	// 線の太さを変更
			g2.setStroke(wideStroke);


			if(type == LINE){				// 直線の描画
				if(line_color!=13){
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawLine(x1,y1,x2,y2);
				}
			}else if(type == RECT){			// 四角形の描画
				exchange(x1,y1,x2,y2);
				if(draw_color!=13){
					g2.setColor(new Color(red[draw_color],green[draw_color],blue[draw_color],clear_color));
					g2.fillRect( w1, h1, w2, h2);
				}
				if(line_color!=13){
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawRect( w1, h1, w2, h2);
				}
			}else if(type == OVAL){			// 円の描画
				exchange(x1,y1,x2,y2);
				if(draw_color!=13){
					g2.setColor(new Color(red[draw_color],green[draw_color],blue[draw_color],clear_color));
					g2.fillOval( w1, h1, w2, h2);
				}
				if(line_color!=13){
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawOval( w1, h1, w2, h2);
				}
			}else if(type == ROUNDRECT){	// 角丸四角形の描画
				exchange(x1,y1,x2,y2);
				if(draw_color!=13){
					g2.setColor(new Color(red[draw_color],green[draw_color],blue[draw_color],clear_color));
					g2.fillRoundRect( w1, h1, w2, h2, w2/3, h2/3);
				}
				if(line_color!=13){
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawRoundRect( w1, h1, w2, h2, w2/3, h2/3);
				}
			}else if(type == TEXT){		// 文字列の挿入
				if(textString != null && line_color!=13){
					g2.setFont(new Font(font[fontIndex], Font.PLAIN, lineWidth*7)); // fontの種類は余ってるx2Listに入れてる.
					g2.setColor(new Color(red[line_color],green[line_color],blue[line_color],clear_color));
					g2.drawString(textString, x, y);
				}
			}else if(type == IMAGE){	// 画像の挿入
				if (fileString != null){
					g2.drawImage(openImage(new File(fileString)), x, y, this);
				}
			}
		}

		/* グリッド線を表示 */
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

		/* グリッド線に合わせて,カーソル位置を補正 */
		public int gridPosition(int a){
			int b;
			for(int i=0;i<=12;i++){
				b = i*50;
				if(Math.abs(b-a) < 8){	//グリッド線との距離が10より小さかったら.
					a = i*50;
					break;
				}
			}
			return a;
		}

		/* 画像ファイルを開く */
		public BufferedImage openImage(File file){
			BufferedImage buffImage = null;
			try {
				buffImage = ImageIO.read(file);
			} catch (Exception e) {
				e.printStackTrace();
				buffImage = null;
			}
			return buffImage;
		}

		public void mousePressed(MouseEvent e){
			if(doCount > 0){	// 元に戻すを使っていたら.カウンタを初期化
				undoReset();
			}

			if(setGrid == true){	//グリッド線を表示している時は,グリッド線に合わせてカーソルの位置を補正
				x1 = gridPosition(e.getX());
				y1 = gridPosition(e.getY());
			}else{
				x1 = e.getX();
				y1 = e.getY();
			}
			x2 = x1; //x2,y2も初期化しないと一瞬おかしな図形が表示される.
			y2 = y1;
		}
		public void mouseDragged(MouseEvent e){ 
			if(doCount > 0){	// 元に戻すを使っていたら.カウンタを初期化
				undoReset();
			}

			if(setGrid == true){	//グリッド線を表示している時は,グリッド線に合わせてカーソルの位置を補正
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
			if(setGrid == true){	//グリッド線を表示している時は,グリッド線に合わせてカーソルの位置を補正
				x = gridPosition(e.getX());
				y = gridPosition(e.getY());
			}else{
				x = e.getX();
				y = e.getY();
			}
			position.setText("(" + Integer.toString(x) + "," + Integer.toString(y) + ")");

			if(textString != null || fileString != null){
				repaint();     //コンポーネント全体を再描画
			}

		}
		public void mouseReleased(MouseEvent e){	//ドラッグが終わったら,図形を描画する.
			if(type != -1){
				listAdd();
			}
			repaint();
		}
		public void mouseClicked(MouseEvent e){
			if(type == TEXT && textString != null){
				if(setGrid == true){
					x1 = gridPosition(e.getX());
					y1 = gridPosition(e.getY());
				}else{
					x1 = e.getX();
					y1 = e.getY();
				}
				textString = null;
				type = -1; x1 = 0; y1 = 0;
				buttonRaised();		//文字列を挿入後は全てのボタンを上げた状態にする．
			}else if(type == IMAGE && fileString != null){
				if(setGrid == true){
					x1 = gridPosition(e.getX());
					y1 = gridPosition(e.getY());
				}else{
					x1 = e.getX();
					y1 = e.getY();
				}
				fileString = null;
				type = -1; x1 = 0; y1 = 0;
				buttonRaised();		//画像を挿入後は全てのボタンを上げた状態にする．
			}
			repaint();     //コンポーネント全体を再描画
		}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){
			position.setText("(none,none)");	//マウスが描画領域から外れた時に,座標の表示を(none,none)にする.
		}
	}

	/* 「文字」を挿入する時に表示するダイアログ.
	 * 挿入する文字列とフォントを指定出来る.*/
	class TextDialog extends JDialog implements ActionListener{
		JTextField field = new JTextField(10);
		JButton save = new JButton("OK");
		JButton cancel = new JButton("キャンセル");

		JLabel sampletext = new JLabel("<html>サンプルテキスト<br>sample text</html>");

		JComboBox combo;

		TextDialog(){
			setSize(250, 250);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setLayout(new FlowLayout());
			setModal(true);
			JPanel panel = new JPanel();
			panel.add(new JLabel("文字列:"));
			panel.add(field);
			add(panel);

			/* コンピュータにインストールされているフォントを取得し，ＣｏｍｂｏＢｏｘを作成. */
			font = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			combo = new JComboBox(font);
			combo.setSelectedItem("Arial");
			combo.setPreferredSize(new Dimension(180, 40));
			add(combo);
			combo.addActionListener(this);
			add(sampletext);
			sampletext.setFont(new Font(font[combo.getSelectedIndex()], Font.PLAIN, 20));
			JPanel panelb = new JPanel();
			panelb.add(save);
			panelb.add(cancel);
			add(panelb);
			save.addActionListener(this);
			cancel.addActionListener(this);
			save.setCursor(new Cursor(Cursor.HAND_CURSOR));
			cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			setVisible(true);
		}

		public void actionPerformed(ActionEvent e){
			String str = field.getText();
			if(e.getSource() == save && str.equals("") == false){
				textString = new String(str);
				fontIndex = combo.getSelectedIndex();
				info.setText("画面をクリックして下さい.");
				setVisible(false);
			}else if(e.getSource() == cancel){
				type = -1;
				buttonRaised();
				setVisible(false);
			}else if(e.getSource() == combo){
				sampletext.setFont(new Font(font[combo.getSelectedIndex()], Font.PLAIN, 20));
			}
		}
	}

	/* 「新規」を作成した時に表示する確認ダイアログ */
	class NewFileDialog extends JDialog implements ActionListener{
		JButton save = new JButton("OK");
		JButton cancel = new JButton("キャンセル");

		NewFileDialog(){
			setSize(200, 120);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setLayout(new FlowLayout());
			setModal(true);
			add(new JLabel("本当にいいですか？"));
			JPanel panel = new JPanel();
			panel.add(save);
			panel.add(cancel);
			add(panel);
			save.addActionListener(this);
			cancel.addActionListener(this);
			save.setCursor(new Cursor(Cursor.HAND_CURSOR));
			cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			setVisible(true);
		}
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == save){
				okflag = true;
			}
			setVisible(false);
		}
	}
}
