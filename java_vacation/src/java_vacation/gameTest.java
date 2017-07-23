package java_vacation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.*;
import javax.sound.sampled.*;
import javax.swing.*;

class Zombiworld extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	public int ImageWidthValue(String file) {
		int x = 0;
		try {
			File f = new File(file);
			BufferedImage bi = ImageIO.read(f);

			x = bi.getWidth();
		} catch (Exception e) {
		}
		return x;

	}

	public int ImageHeigthValue(String file) {
		int y = 0;
		try {
			File f = new File(file);
			BufferedImage bi = ImageIO.read(f);
			y = bi.getHeight();
		} catch (Exception e) {
		}
		return y;
	}

	double nowX = 550.0, nowY = 300.0;
	double pressX, pressY;

	boolean keyUp = false;
	boolean keyDown = false;
	boolean keyLeft = false;
	boolean keyRight = false;
	boolean playerMove = false;
	boolean keyUpRight = false;
	boolean shoot = false;

	Toolkit tk = Toolkit.getDefaultToolkit(); // �̹� ������� ��ü(�̹��� ����)

	// �ý���
	Image buffimg, background;
	Image[] Cloud_img;
	// �޴�
	Image End_img, startMenu, startButton, GameOver, Loading, restart;
	// ����
	Image Missile_img;
	Image target_img;
	// ���� , ĳ����
	Image Enemy_img, img;
	// ����
	Image tree_img, Store, Skilliven1, Skillstore1, storeitem1, invenitem1;
	// �׷��Ƚ�
	Graphics charactergc, missilegc, cursergc, cloudgc, scoregc, itemgc, backgroundgc, newGameButtongc, storeButtongc,
			restartButtongc, gungc;

	Thread th;
	ArrayList Missile_List = new ArrayList();
	ArrayList Enemy_List = new ArrayList();
	ArrayList Item_List = new ArrayList();
	ArrayList Cloud_List = new ArrayList();

	int ex = 0, ey = 0;
	int charX, charY;
	int f_width = 1300;
	int f_height = 800;
	int count;

	// ĳ����
	int HP = 50, MaxHP = 50;
	int itemscore = 0;
	int level = 50;
	int playStatus = 0;
	int moveStatus = 0, player_Speed = 5;
	int missile_Speed = 15; // �̻��� ������ �ӵ� ����
	int HPitem = 0, skill = 0;
	int cnt = 0;

	int stage = 1, countdown = 0, point = 0, damage = 1, time = 0;
	int monsterkill = 0, monsterspeed = 10, monstercnt = 30;;

	int[] itemcount = new int[6];
	boolean[] itemstatus = new boolean[6];

	boolean clearstatus = false;
	boolean invencode = false;
	boolean statecode = false;
	boolean loddingtime = false;
	boolean keySkill = false;

	int[] cx = { 0, 0, 0, 0 };

	Missile ms;
	RandomEnemy en;
	RandomEnemy en1;
	Item it;

	int m_w, m_h;
	int e_w, e_h;

	Zombiworld() {
		setTitle("�׽�Ʈ");
		setSize(f_width, f_height);
		init();
		start1();

		Dimension screen = tk.getScreenSize();

		int xpos = (int) (screen.getWidth() / 2 - getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - getHeight() / 2);
		setLocation(xpos, ypos);
		setResizable(false);
		setVisible(true);
	}

	public void init() {
		charX = 550;
		charY = 300;

		img = new ImageIcon("ĳ����.png").getImage();
		Missile_img = new ImageIcon("�����.png").getImage();
		target_img = new ImageIcon("Ŀ��.png").getImage();
		Enemy_img = new ImageIcon("����.png").getImage();
		tree_img = new ImageIcon("star.png").getImage();
		End_img = new ImageIcon("����ȭ��2.png").getImage();
		startMenu = new ImageIcon("���ӽ���2.png").getImage();
		startButton = new ImageIcon("NewGame.png").getImage();
		GameOver = new ImageIcon("GameOver.png").getImage();
		Loading = new ImageIcon("�ε�ȭ��2.png").getImage();
		Store = new ImageIcon("����.png").getImage();
		restart = new ImageIcon("�����.png").getImage();
		background = new ImageIcon("���ȭ��.png").getImage();
		storeitem1 = new ImageIcon("����.png").getImage();
		invenitem1 = new ImageIcon("�����κ�.png").getImage();
		Skilliven1 = new ImageIcon("��ų1.png").getImage();
		Skillstore1 = new ImageIcon("��ų����1.png").getImage();
		Cloud_img = new Image[4];
		for (int i = 0; i < Cloud_img.length; ++i) {
			Cloud_img[i] = new ImageIcon("cloud" + i + ".png").getImage();
		}
		m_w = ImageWidthValue("�����.png");
		m_h = ImageHeigthValue("�ɹ��.png");
		e_w = ImageWidthValue("����.png");
		e_h = ImageHeigthValue("����.png");
	}

	public void Sound(String file, boolean Loop) {
		Clip clip;
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			if (Loop)
				clip.loop(-1);
			// Loop ����true�� ������������ѹݺ���ŵ�ϴ�.
			// false�� �ѹ��������ŵ�ϴ�.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start1() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		Cursor invisCursor = tk.createCustomCursor(tk.createImage(""), new Point(), null);
		this.setCursor(invisCursor);
		this.getGlassPane().setVisible(true);
		th = new Thread(this);
		th.start();
	}

	public void paint(Graphics g) {
		buffimg = createImage(f_width, f_height);

		scoregc = buffimg.getGraphics();
		charactergc = buffimg.getGraphics();
		missilegc = buffimg.getGraphics();
		cursergc = buffimg.getGraphics();
		cloudgc = buffimg.getGraphics();
		itemgc = buffimg.getGraphics();
		backgroundgc = buffimg.getGraphics();
		newGameButtongc = buffimg.getGraphics();
		storeButtongc = buffimg.getGraphics();
		restartButtongc = buffimg.getGraphics();
		gungc = buffimg.getGraphics();

		if (playStatus == 0)
			startMenu(g);
		if (playStatus == 1)
			loading(g);
		if (playStatus == 2)
			update(g);
		if (playStatus == 3) {
			for (int z = 0; z < Missile_List.size(); ++z)
				Missile_List.remove(z);
			for (int z = 0; z < Item_List.size(); ++z)
				Item_List.remove(z);
			for (int z = 0; z < Enemy_List.size(); ++z)
				Enemy_List.remove(z);
			StoreMenu(g);
		}

		if (playStatus == 4)
			End(g);
	}

	public void startMenu(Graphics g) {
		backgroundgc.drawImage(startMenu, 0, 0, this);
		newGameButtongc.drawImage(startButton, 520, 630, this);
		newGameButtongc.setClip(550, 630, 250, 63);
		backgroundgc.setColor(Color.white);
		backgroundgc.drawString("������: ILL , JS , HS , JW", 1150, 785);
		Draw_target();
		g.drawImage(buffimg, 0, 0, this);
		
	}

	public void loading(Graphics g) {
		charactergc.drawImage(Loading, 0, 0, this);
		charactergc.setColor(Color.red);
		charactergc.setFont(new Font("Default", Font.BOLD, 30));
		charactergc.drawString("Tip", 150, 200);
		charactergc.drawString("���� ���� óġ�� ���� �̵��� ���� �� ���� ����", 150, 255);
		charactergc.drawString("�������� Ŭ����� �־��� ���� ����Ʈ 5 �־���", 150, 300);
		charactergc.setColor(Color.WHITE);
		charactergc.drawString("���� ����", 150, 400);
		charactergc.drawString("H P  : ü�� �ִ�ġ +10", 150, 450);
		charactergc.drawString("���ݷ�   : ���ݷ� +1", 150, 500);
		charactergc.drawString("�̵��ӵ�: �̼� +1", 150, 550);
		Draw_target();

		if (loddingtime == false) {
			// �ε� ��
			charactergc.setFont(new Font("Default", Font.BOLD, 50));
			charactergc.drawString("�� �� ��", 550, 700);
			charactergc.setColor(Color.white);
			charactergc.fill3DRect(0, 720, 1300, 40, true);
			charactergc.setColor(Color.red);
			charactergc.fill3DRect(0, 720, time, 40, true);
			if (time <= 1300) {
				time += Math.random() * 50;
			} else if (time > 1300)
				loddingtime = true;
		}
		if (loddingtime) {
			charactergc.setFont(new Font("Default", Font.BOLD, 50));
			charactergc.drawString("�� �� �� �� �� �� �� �� �� Ŭ ��", 280, 700);
			charactergc.setColor(Color.red);
			charactergc.fill3DRect(0, 720, 1300, 40, true);
		}

		g.drawImage(buffimg, 0, 0, this);
	}

	public void DrawImg() {
		MoveImage(img, charX, charY, 130 / 4, 195 / 4); // ĳ���� �ɾ�� �޼ҵ�
		if (HP > MaxHP) {
			HP = MaxHP;
		}

		if (invencode == false) {
			backgroundgc.draw3DRect(1200, 770, 100, 30, true);
			backgroundgc.drawString("�κ��丮", 1220, 790);
		} else if (invencode) {
			backgroundgc.draw3DRect(1000, 700, 100, 100, true);
			backgroundgc.draw3DRect(1100, 700, 100, 100, true);
			backgroundgc.draw3DRect(1200, 700, 100, 100, true);
			backgroundgc.draw3DRect(1000, 600, 100, 100, true);
			backgroundgc.draw3DRect(1100, 600, 100, 100, true);
			backgroundgc.draw3DRect(1200, 600, 100, 100, true);
			backgroundgc.draw3DRect(1000, 550, 300, 50, true);
			backgroundgc.draw3DRect(1250, 550, 50, 50, true);
			backgroundgc.setFont(new Font("Default", Font.BOLD, 30));
			backgroundgc.drawString("�κ��丮", 1050, 585);
			backgroundgc.drawString("X", 1265, 585);

			// �κ��丮
			if (itemstatus[0] && itemcount[0] > 0) {
				backgroundgc.setFont(new Font("Default", Font.BOLD, 15));
				backgroundgc.drawString("" + itemcount[0], 1010, 615);
				backgroundgc.drawImage(invenitem1, 1000, 600, this);
			}
			if (itemstatus[1] && itemcount[1] > 0) {
				backgroundgc.setFont(new Font("Default", Font.BOLD, 15));
				backgroundgc.drawString("" + itemcount[1], 1110, 615);
				backgroundgc.drawImage(Skilliven1, 1100, 600, this);
			}

		}

		if (statecode == false && invencode == false) {
			backgroundgc.draw3DRect(1200, 740, 100, 30, true);
			backgroundgc.drawString("����", 1235, 760);
		} else if (statecode == true) {
			backgroundgc.setFont(new Font("Default", Font.BOLD, 30));
			backgroundgc.draw3DRect(1000, 600, 300, 50, true);
			backgroundgc.drawString("HP", 1050, 635);
			backgroundgc.drawString("+", 1250, 635);
			backgroundgc.draw3DRect(1000, 650, 300, 50, true);
			backgroundgc.drawString("���ݷ�", 1050, 685);
			backgroundgc.drawString("+", 1250, 685);
			backgroundgc.draw3DRect(1000, 700, 300, 50, true);
			backgroundgc.drawString("�̵��ӵ�", 1050, 735);
			backgroundgc.drawString("+", 1250, 735);

			backgroundgc.draw3DRect(1000, 550, 300, 50, true);
			backgroundgc.draw3DRect(1250, 550, 50, 50, true);

			backgroundgc.drawString("����", 1050, 585);
			backgroundgc.drawString("+" + point, 1150, 585);
			backgroundgc.drawString("X", 1265, 585);
		}

	}

	public void StoreMenu(Graphics g) {
		backgroundgc.drawImage(Store, 0, 0, this);
		backgroundgc.drawImage(storeitem1, 60, 125, this);
		backgroundgc.drawImage(Skillstore1, 510, 155, this);
		backgroundgc.drawImage(tree_img, 410, 45, this);
		backgroundgc.setColor(Color.WHITE);
		backgroundgc.setFont(new Font("Default", Font.BOLD, 30));
		backgroundgc.drawString(" = " + itemscore, 435, 70);
		backgroundgc.drawImage(tree_img, 340, 140, this);
		backgroundgc.drawString(" = " + 6, 365, 165);
		backgroundgc.drawImage(tree_img, 745, 140, this);
		backgroundgc.drawString(" = " + 6, 770, 165);
		backgroundgc.drawImage(tree_img, 1150, 140, this);
		backgroundgc.drawString(" = " + 6, 1175, 165);
		backgroundgc.drawImage(tree_img, 340, 460, this);
		backgroundgc.drawString(" = " + 6, 365, 485);
		backgroundgc.drawImage(tree_img, 745, 460, this);
		backgroundgc.drawString(" = " + 6, 770, 485);
		backgroundgc.drawImage(tree_img, 1150, 460, this);
		backgroundgc.drawString(" = " + 6, 1175, 485);
		Draw_target();
		g.drawImage(buffimg, 0, 0, this);
	}

	public void End(Graphics g) {
		backgroundgc.drawImage(End_img, 0, 0, this);
		restartButtongc.setColor(Color.RED);
		restartButtongc.drawImage(restart, 510, 700, this);
		restartButtongc.setClip(510, 700, 250, 63);
		backgroundgc.setFont(new Font("Default", Font.BOLD, 20));
		backgroundgc.setColor(Color.WHITE);
		backgroundgc.drawString("���� : " + itemscore, 1100, 730);
		backgroundgc.drawString("���� ���� �� : " + monsterkill, 1100, 760);
		Draw_target();
		Endinit();
		g.drawImage(buffimg, 0, 0, this);
	}

	public void Endinit() {
		for (int z = 0; z < Missile_List.size(); ++z)
			Missile_List.remove(z);
		for (int z = 0; z < Item_List.size(); ++z)
			Item_List.remove(z);
		for (int z = 0; z < Enemy_List.size(); ++z)
			Enemy_List.remove(z);
		monsterkill = 0;
		itemscore = 0;
		HP = 30;
		time = 0;
		loddingtime = false;
		monstercnt = 30;
		stage = 1;
		charX = 550;
		charY = 300;
		invencode = false;
		statecode = false;
		loddingtime = false;
		itemcount = null;
		itemstatus = null;
		point = 0;
		damage = 1;
		countdown = 0;
		f_width = 1300;
		f_height = 800;
		HP = 50;
		MaxHP = 50;
		itemscore = 0;
		level = 50;
		monsterkill = 0;
		monsterspeed = 10;
	}

	public void update(Graphics g) {
		DrawUI();
		DrawImg();
		Draw_target();
		if (countdown == 200) {
			Draw_Missile();
			Draw_item();
			Draw_enemy();

		}

		g.drawImage(buffimg, 0, 0, this);

	}

	public void DrawUI() { // ���� ���� �κ�
		int random;

		backgroundgc.drawImage(background, 0, 0, this);
		for (int i = 0; i < cx.length; ++i) {
			random = (int) (Math.random() * 3 + 1);
			if (cx[i] < 1400) {
				cx[i] += 5 + i * 3;
			} else {
				cx[i] = 0;
			}
			cloudgc.drawImage(Cloud_img[i], 1200 - cx[i], 60 + i * 250, this);
		}
		// UI�κ�

		backgroundgc.drawString("HP", 10, 715);
		backgroundgc.setColor(Color.WHITE);
		backgroundgc.fill3DRect(30, 720, MaxHP * 2, 40, true);
		backgroundgc.setColor(Color.red);
		backgroundgc.fill3DRect(30, 720, HP * 2, 40, true);

		// hpǥ��
		backgroundgc.setColor(Color.white);
		backgroundgc.setFont(new Font("Default", Font.BOLD, 15));
		backgroundgc.drawString("0", 20, 760);
		backgroundgc.drawString("" + MaxHP, MaxHP * 2 + 35, 760);
		backgroundgc.setFont(new Font("Default", Font.BOLD, 40));
		backgroundgc.drawString("Stage :" + stage, 1110, 70);
		// ��ǥġ
		backgroundgc.setColor(Color.white);
		backgroundgc.setFont(new Font("Default", Font.BOLD, 15));
		backgroundgc.drawString("��ǥ KILL:" + monstercnt + "   ���� KILL:" + monsterkill, 150, 50);
		backgroundgc.drawImage(tree_img, 150, 50, this);
		backgroundgc.setColor(Color.white);
		backgroundgc.drawString(":" + itemscore, 180, 70);

		if (countdown > 140 && countdown < 200) {
			charactergc.setColor(Color.WHITE);
			charactergc.setFont(new Font("Default", Font.BOLD, 200));
			charactergc.drawString("1", 550, 400);
		} else if (countdown > 70 && countdown <= 140) {
			charactergc.setColor(Color.WHITE);
			charactergc.setFont(new Font("Default", Font.BOLD, 200));
			charactergc.drawString("2", 550, 400);
		} else if (countdown > 0 && countdown <= 70) {
			charactergc.setColor(Color.WHITE);
			charactergc.setFont(new Font("Default", Font.BOLD, 200));
			charactergc.drawString("3", 550, 400);
		}

		if (countdown < 200)
			countdown++;
		else
			countdown = 200;

		if (clearstatus == true) {
			storeButtongc.setColor(Color.RED);
			storeButtongc.setFont(new Font("Default", Font.BOLD, 150));
			storeButtongc.drawString("Stage Clear!", 270, 350);
			storeButtongc.setColor(Color.white);
			storeButtongc.setFont(new Font("Default", Font.BOLD, 20));
			storeButtongc.drawString("�� �� �������� �̵�", 700, 500);

			if (cnt > 140 && cnt < 200) {
				storeButtongc.setColor(Color.WHITE);
				storeButtongc.setFont(new Font("Default", Font.BOLD, 40));
				storeButtongc.drawString("1", 670, 510);
			} else if (cnt > 70 && cnt <= 140) {
				storeButtongc.setColor(Color.WHITE);
				storeButtongc.setFont(new Font("Default", Font.BOLD, 40));
				storeButtongc.drawString("2", 670, 510);
			} else if (cnt > 0 && cnt <= 70) {
				storeButtongc.setColor(Color.WHITE);
				storeButtongc.setFont(new Font("Default", Font.BOLD, 40));
				storeButtongc.drawString("3", 670, 510);
			}

			if (cnt < 200)
				cnt++;

			if (cnt >= 200) {
				playStatus = 3;
				stage++;
				clearstatus = false;

			}

			for (int z = 0; z < Missile_List.size(); ++z)
				Missile_List.remove(z);
			for (int z = 0; z < Item_List.size(); ++z)
				Item_List.remove(z);
			for (int z = 0; z < Enemy_List.size(); ++z)
				Enemy_List.remove(z);
		}
	}

	public void Draw_target() {
		cursergc.drawImage(target_img, (int) nowX - 7, (int) nowY - 8, this);
	}

	public void Draw_item() {
		for (int i = 0; i < Item_List.size(); ++i) {
			it = (Item) Item_List.get(i);
			itemgc.setClip(it.x, it.y, 25, 25);
			itemgc.drawImage(tree_img, it.x, it.y, this);
			if (Crash(charX, charY, it.x, it.y, charactergc, itemgc)) {
				Item_List.remove(i);
				Sound("����������.wav", false);
				itemscore++;
			}
		}
	}

	public void Draw_enemyHP(int x, int y, int HP, int maxHP) {

		backgroundgc.drawString("HP", 10, 715);
		backgroundgc.setColor(Color.WHITE);
		backgroundgc.fill3DRect(x, y - 10, maxHP * 20, 10, true);
		backgroundgc.setColor(Color.red);
		backgroundgc.fill3DRect(x, y - 10, HP * 20, 10, true);
	}

	public void Draw_enemy() {

		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (RandomEnemy) (Enemy_List.get(i));
			MoveEnemy(Enemy_img, (int) en.x, (int) en.y, 130 / 4, 195 / 4, en.enemystatus);
			Draw_enemyHP((int) en.x, (int) en.y, en.monsterHP, en.monsterMaxHP);
			en.move(charX, charY);
			for (int j = 0; j < Missile_List.size(); ++j) {
				ms = (Missile) Missile_List.get(j);
				RandomEnemy en1 = null;
				;
				if (Crash((int) ms.realX, (int) ms.realY, (int) en.x, (int) en.y, missilegc, cloudgc)) {
					en.monsterHP -= damage;
					if (en.monsterHP <= 0) {
						it = new Item((int) en.x, (int) en.y, 33, 23);
						Enemy_List.remove(i);
						Item_List.add(it);
						monsterkill++;
					}

					Missile_List.remove(j);

					if (monsterkill == monstercnt) {
						clearstatus = true;
						cnt = 0;
						point += 3;
						monsterkill = 0;
						monstercnt += 20;

					}
				}
			}

			if (Crash((int) charX, (int) charY, (int) en.x, (int) en.y, charactergc, cloudgc)) {
				Enemy_List.remove(i);
				HP -= 10;
				if (HP == 0) {
					for (int z = 0; z < Missile_List.size(); ++z)
						Missile_List.remove(z);
					for (int z = 0; z < Item_List.size(); ++z)
						Item_List.remove(z);
					for (int z = 0; z < Enemy_List.size(); ++z)
						Enemy_List.remove(z);
					playStatus = 4;
				}
			} else if (en.x > f_width + 205)
				Enemy_List.remove(i);
			else if (en.x < -205)
				Enemy_List.remove(i);
			else if (en.y > f_height + 205)
				Enemy_List.remove(i);
			else if (en.y < -205)
				Enemy_List.remove(i);

		}

	}

	public void MoveEnemy(Image img, int x, int y, int width, int height, int status) {
		cloudgc.setClip(x, y, width, height); // �̹��� ¥����

		if (count / 10 % 4 == 3) {
			cloudgc.drawImage(img, x - (width * 0), y - (height * status), this);
		} else if (count / 10 % 4 == 2) {
			cloudgc.drawImage(img, x - (width * 1), y - (height * status), this);
		} else if (count / 10 % 4 == 0) {
			cloudgc.drawImage(img, x - (width * 2), y - (height * status), this);
		} else if (count / 10 % 4 == 1) {
			cloudgc.drawImage(img, x - (width * 1), y - (height * status), this);
		} else {
			cloudgc.drawImage(img, x - (width * 1), y - (height * status), this);
		}
	}

	public void EnemyProcess() {
		if (playStatus == 2) {
			int enx, eny;
			int random;
			random = (int) (Math.random() * level) + 1;

			if (random == 1) {
				enx = (int) (Math.random() * (400 + f_width)) - 200;
				while (true) {
					if (enx >= 0 && enx <= f_width)
						enx = (int) (Math.random() * (400 + f_width)) - 200;
					if (enx < 0 || enx > f_width) {
						eny = (int) (Math.random() * (400 + f_height)) - 200;
						break;
					}
				}
				if (countdown == 200) {
					en = new RandomEnemy(charX, charY, enx, eny, monsterspeed, stage, stage);
					Enemy_List.add(en);
				}
			} else if (random == 2) {
				eny = (int) (Math.random() * (400 + f_height)) - 200;
				while (true) {
					if (eny >= 0 && eny <= f_height)

						eny = (int) (Math.random() * (400 + f_height)) - 200;
					if (eny < 0 || eny > f_height) {
						enx = (int) (Math.random() * (400 + f_width)) - 200;
						break;
					}
				}
				if (countdown == 200) {
					en = new RandomEnemy(charX, charY, enx, eny, monsterspeed, stage, stage);
					Enemy_List.add(en);
				}
			}
		}

	}

	public void Draw_Missile() {

		for (int i = 0; i < Missile_List.size(); ++i) {
			ms = (Missile) (Missile_List.get(i));
			missilegc.setClip((int) ms.realX, (int) ms.realY, 10, 9);
			missilegc.drawImage(Missile_img, (int) ms.realX, (int) ms.realY, this);
			ms.move();
			if (ms.realX > f_width + 100)
				Missile_List.remove(i);
			if (ms.realX < 0)
				Missile_List.remove(i);
			if (ms.realY > f_height + 100)
				Missile_List.remove(i);
			if (ms.realY < 0)
				Missile_List.remove(i);
		}
	}

	public void MoveImage(Image img, int x, int y, int width, int height) {
		charactergc.setClip(x, y, width, height); // �̹��� ¥����

		if (playerMove) {
			if (count / 10 % 4 == 3) {
				charactergc.drawImage(img, x - (width * 0), y - (height * moveStatus), this);
			} else if (count / 10 % 4 == 2) {
				charactergc.drawImage(img, x - (width * 1), y - (height * moveStatus), this);
			} else if (count / 10 % 4 == 0) {
				charactergc.drawImage(img, x - (width * 2), y - (height * moveStatus), this);
			} else if (count / 10 % 4 == 1) {
				charactergc.drawImage(img, x - (width * 1), y - (height * moveStatus), this);
			}
		} else {
			charactergc.drawImage(img, x - (width * 1), y - (height * moveStatus), this);

		}
	}

	// Crash(ms.realX, ms.realX, en.x, en.y, m_w, m_h, 31, 48)
	// Math.abs ���방 ��ȯ
	public boolean Crash(int x1, int y1, int x2, int y2, Graphics img1, Graphics img2) {

		boolean check = false;
		Rectangle r = img1.getClipBounds();
		Rectangle h = img2.getClipBounds();

		if (Math.abs((x1 + r.width / 2) - (x2 + h.width / 2)) < (h.width / 2 + r.width / 2)
				&& Math.abs((y1 + r.height / 2) - (y2 + h.height / 2)) < (h.height / 2 + r.height / 2)) {
			check = true;
		} else {
			check = false;
		}
		return check;
	}

	public boolean Button(int x, int y, Graphics g) {
		boolean check = false;
		Rectangle r = g.getClipBounds();
		if (r.x <= x && (r.x + r.width) >= x && r.y <= y && (r.y + r.height) >= y) {
			check = true;
		} else
			check = false;
		return check;
	}

	public boolean Crash(int x2, int y2) {

		if (playStatus == 1) {
			if (x2 >= 0 && x2 <= 1300 && y2 >= 0 && y2 <= 800) {
				return true;
			}
		}
		if (playStatus == 2) {

			// �κ��丮â Ű�� ����
			if (x2 >= 1200 && x2 <= 1300 && y2 >= 770 && y2 <= 800) {
				invencode = true;
				statecode = false;
				return false;
			}
			if (invencode && x2 >= 1250 && x2 <= 1300 && y2 >= 550 && y2 <= 600) {
				invencode = false;
				return false;
			}
			// ������ ���
			if (invencode && x2 >= 1000 && x2 < 1100 && y2 >= 600 && y2 < 700 && HPitem > 0) {

				return true;

			}
			if (invencode && x2 >= 1100 && x2 < 1200 && y2 >= 600 && y2 < 700 && skill > 0) {

				return true;

			}
			// ����â Ű�� ����
			if (invencode == false && x2 >= 1200 && x2 <= 1300 && y2 >= 740 && y2 <= 770) {
				statecode = true;
				invencode = false;
				return false;
			}
			if (invencode == false && statecode && x2 >= 1250 && x2 <= 1300 && y2 >= 550 && y2 <= 600) {
				statecode = false;
				return false;
			}
			if (point > 0) {
				// HP , ���ݷ� , �̵��ӵ�
				if (x2 >= 1250 && x2 <= 1265 && y2 >= 610 && y2 <= 635) {
					MaxHP += 10;
					HP += 10;
					point--;
					return false;
				}
				if (x2 >= 1250 && x2 <= 1265 && y2 >= 665 && y2 <= 685) {
					damage += 1;
					point--;
					return false;
				}
				if (x2 >= 1250 && x2 <= 1265 && y2 >= 710 && y2 <= 735) {
					player_Speed += 1;
					point--;
					return false;
				}
			}

		}
		if (playStatus == 3) {
			// ������
			if (x2 >= 1137 && x2 <= 1250 && y2 >= 35 && y2 <= 95) {
				return true;
			}
			// ������ 1��~6��
			if (x2 >= 100 && x2 <= 400 && y2 >= 360 && y2 <= 410) {
				if (itemscore >= 6) {
					itemstatus[0] = true;
					itemcount[0]++;
					itemscore -= 6;
					HPitem++;
				}
				return false;
			}
			if (x2 >= 500 && x2 <= 800 && y2 >= 360 && y2 <= 410) {
				if (itemscore >= 6) {
					itemstatus[1] = true;
					itemcount[1]++;
					itemscore -= 6;
					skill++;
				}
				return false;
			}
			if (x2 >= 900 && x2 <= 1200 && y2 >= 360 && y2 <= 410) {
				itemstatus[2] = true;
				return false;
			}
			if (x2 >= 100 && x2 <= 400 && y2 >= 670 && y2 <= 720) {
				itemstatus[3] = true;
				return false;
			}
			if (x2 >= 500 && x2 <= 800 && y2 >= 670 && y2 <= 720) {
				itemstatus[4] = true;
				return false;
			}
			if (x2 >= 900 && x2 <= 1200 && y2 >= 670 && y2 <= 720) {
				itemstatus[5] = true;
				return false;
			} else
				return false;
		}
		return false;

	}

	public void keyProcess() {
		playerMove = false;
		if (keyUp) {
			playerMove = true;
			if (charY > 25) {
				charY -= player_Speed;
				moveStatus = 3;
			}
		}
		if (keyDown) {
			playerMove = true;
			if (charY < f_height - 50) {
				charY += player_Speed;
				moveStatus = 0;
			}
		}
		if (keyLeft) {
			playerMove = true;
			if (charX > 0) {
				charX -= player_Speed;
				moveStatus = 1;
			}
		}
		if (keyRight) {
			playerMove = true;
			if (charX < f_width - 25) {
				charX += player_Speed;
				moveStatus = 2;
			}
		}
	}

	public void mouseProcess() {
		if (shoot && (count % 10 == 0)) {
			if (countdown == 200) {
				ms = new Missile(charX + 10, charY + 10, pressX, pressY, 5);
				Missile_List.add(ms);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_A:
			keyLeft = true;
			break;
		case KeyEvent.VK_D:
			keyRight = true;
			break;
		case KeyEvent.VK_W:
			keyUp = true;
			break;
		case KeyEvent.VK_S:
			keyDown = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_A:
			keyLeft = false;
			break;
		case KeyEvent.VK_D:
			keyRight = false;
			break;
		case KeyEvent.VK_W:
			keyUp = false;
			break;
		case KeyEvent.VK_S:
			keyDown = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override

	public void run() {
		while (true) {

			try {
				keyProcess();
				mouseProcess();
				EnemyProcess();
				repaint();
				Thread.sleep(20);
				count++;

			} catch (Exception e) {
			}
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		nowX = (double) e.getX();
		nowY = (double) e.getY();
		pressX = (double) e.getX();
		pressY = (double) e.getY();

		shoot = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		nowX = (double) e.getX();
		nowY = (double) e.getY();

		if (playStatus == 0) {
			if (e.getX() >= 550 && e.getX() <= 550 + ImageWidthValue("NewGame.PNG") && e.getY() >= 630
					&& e.getY() <= 630 + ImageHeigthValue("NewGame.PNG")) {
				startButton = new ImageIcon("NewGame_move.png").getImage();
			} else
				startButton = new ImageIcon("NewGame.png").getImage();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressX = (double) e.getX();
		pressY = (double) e.getY();
		if (playStatus == 2) {
			if (countdown == 200) {
				ms = new Missile(charX + 10, charY + 10, e.getX(), e.getY(), 5);
				Missile_List.add(ms);
			}
		}

		if (playStatus == 0 && Button(e.getX(), e.getY(), newGameButtongc)) {
			playStatus = 1;
		}
		// �ε�ȭ�� ��ġ
		if (playStatus == 1 && time > 1300 && Crash(e.getX(), e.getY())) {
			playStatus = 2;
		}
		// �κ�,����â
		if (playStatus == 2 && Crash(e.getX(), e.getY())) {
		}
		// ����������
		if (playStatus == 3 && Crash(e.getX(), e.getY())) {
			countdown = 0;

			playStatus = 2;
		} // ������
		if (playStatus == 2 && Crash(e.getX(), e.getY()) && itemstatus[0] && itemcount[0] > 0) {
			itemcount[0]--;
			HP += 30;
			HPitem--;
		}
		if (playStatus == 2 && Crash(e.getX(), e.getY()) && itemstatus[1] && itemcount[1] > 0) {
			itemcount[1]--;
			skill--;
			for (int s_x = 0; s_x <= f_width; s_x += 100) {
				for (int s_y = 0; s_y <= f_height; s_y += 100) {
					ms = new Missile(charX + 10, charY + 10, s_x, s_y, missile_Speed);
					Missile_List.add(ms);
				}
			}
		}

		// ��ų���

		if (playStatus == 4 && Button(e.getX(), e.getY(), restartButtongc)) {
			playStatus = 0;
		} // ����ŸƮ ȭ��
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		shoot = false;
	}

	class Missile {

		double realX, realY;
		double tarX, tarY;
		int speed;
		double plusX, plusY;
		int x = 5;

		public Missile(int x, int y, double pressX, double pressY, int speed) {
			realX = (double) x;
			realY = (double) y;
			tarX = pressX;
			tarY = pressY;

			if (tarX >= realX && tarY < realY) {
				plusY = -1 * ((realY - tarY)
						/ Math.sqrt(((realY - tarY) * (realY - tarY)) + ((tarX - realX) * (tarX - realX))));
				plusX = ((tarX - realX)
						/ Math.sqrt(((realY - tarY) * (realY - tarY)) + ((tarX - realX) * (tarX - realX))));
			} else if (tarX >= realX && tarY >= realY) {
				plusY = ((tarY - realY)
						/ Math.sqrt(((tarY - realY) * (tarY - realY)) + ((tarX - realX) * (tarX - realX))));
				plusX = ((tarX - realX)
						/ Math.sqrt(((tarY - realY) * (tarY - realY)) + ((tarX - realX) * (tarX - realX))));
			} else if (tarX < realX && tarY < realY) {
				plusY = -1 * ((realY - tarY)
						/ Math.sqrt(((realY - tarY) * (realY - tarY)) + ((realX - tarX) * (realX - tarX))));
				plusX = -1 * ((realX - tarX)
						/ Math.sqrt(((realY - tarY) * (realY - tarY)) + ((realX - tarX) * (realX - tarX))));
			} else if (tarX < realX && tarY >= realY) {
				plusY = ((tarY - realY)
						/ Math.sqrt(((tarY - realY) * (tarY - realY)) + ((realX - tarX) * (realX - tarX))));
				plusX = -1 * ((realX - tarX)
						/ Math.sqrt(((tarY - realY) * (tarY - realY)) + ((realX - tarX) * (realX - tarX))));
			} else if (tarX == realX && tarY < realY)
				x = 0;
			else if (tarX == realX && tarY > realY)
				x = 2;
			else if (tarX > realX && tarY == realY)
				x = 1;
			else if (tarX < realX && tarY == realY)
				x = 3;
			else if (tarX == realX && tarY == realY)
				x = 0;

			this.speed = speed;
		}

		public void move() {
			if (x == 0) {
				realY -= 8 * speed;
			} else if (x == 1) {
				realX += 8 * speed;
			} else if (x == 2) {
				realY += 8 * speed;
			} else if (x == 3) {
				realX -= 8 * speed;
			} else if (x == 5) {
				realX += plusX * speed;
				realY += plusY * speed;
			}
		}
	}

	class RandomEnemy {
		double x, y;
		double chX, chY;
		double xPlus, yPlus;
		int enemystatus;
		int enemyspeed;
		int z = 5;
		int monsterHP = stage;
		int monsterMaxHP = stage;

		public RandomEnemy(int charX, int charY, int ranX, int ranY, int speed, int HP, int maxHP) {
			x = (double) ranX;
			y = (double) ranY;
			chX = (double) charX;
			chY = (double) charY;

			if (chX > x && chY < y) {
				yPlus = -1 * ((y - chY) / Math.sqrt(((y - chY) * (y - chY)) + ((chX - x) * (chX - x))));
				xPlus = ((chX - x) / Math.sqrt(((y - chY) * (y - chY)) + ((chX - x) * (chX - x))));
				if (Math.cos(Math.toRadians(45)) > xPlus)
					enemystatus = 3;
				else
					enemystatus = 2;
			} else if (chX > x && chY > y) {
				yPlus = ((chY - y) / Math.sqrt(((chY - y) * (chY - y)) + ((chX - x) * (chX - x))));
				xPlus = ((chX - x) / Math.sqrt(((chY - y) * (chY - y)) + ((chX - x) * (chX - x))));
				if (Math.cos(Math.toRadians(45)) > xPlus)
					enemystatus = 0;
				else
					enemystatus = 2;
			} else if (chX < x && chY < y) {
				yPlus = -1 * ((y - chY) / Math.sqrt(((y - chY) * (y - chY)) + ((x - chX) * (x - chX))));
				xPlus = -1 * ((x - chX) / Math.sqrt(((y - chY) * (y - chY)) + ((x - chX) * (x - chX))));
				if (Math.cos(Math.toRadians(45)) > (xPlus * -1))
					enemystatus = 3;
				else
					enemystatus = 1;
			} else if (chX < x && chY > y) {
				yPlus = ((chY - y) / Math.sqrt(((chY - y) * (chY - y)) + ((x - chX) * (x - chX))));
				xPlus = -1 * ((x - chX) / Math.sqrt(((chY - y) * (chY - y)) + ((x - chX) * (x - chX))));
				if (Math.cos(Math.toRadians(45)) > (xPlus * -1))
					enemystatus = 0;
				else
					enemystatus = 1;
			} else if (chX == x && chY < y) {
				enemystatus = 3;
				z = 0;
			} else if (chX == x && chY > y) {
				enemystatus = 0;
				z = 2;
			} else if (chX > x && chY == y) {
				enemystatus = 2;
				z = 1;
			} else if (chX < x && chY == y) {
				enemystatus = 1;
				z = 3;
			} else if (chX == x && chY == y) {
				enemystatus = 1;
				z = 3;
			}
			enemyspeed = speed;
			monsterHP = HP;
			monsterMaxHP = maxHP;
		}

		public void move() {
			if (z == 0) {
				y -= 8;
			} else if (z == 1) {
				x += 8;
			} else if (z == 2) {
				y += 8;
			} else if (z == 3) {
				x -= 8;
			} else if (z == 5) {

				x += xPlus * enemyspeed / 2;
				y += yPlus * enemyspeed / 2;
			}

		}

		public void move(int chx, int chy) {
			chX = (double) chx;
			chY = (double) chy;

			if (chX > x && chY < y) {
				yPlus = -1 * ((y - chY) / Math.sqrt(((y - chY) * (y - chY)) + ((chX - x) * (chX - x))));
				xPlus = ((chX - x) / Math.sqrt(((y - chY) * (y - chY)) + ((chX - x) * (chX - x))));
				if (Math.cos(Math.toRadians(45)) > xPlus)
					enemystatus = 3;
				else
					enemystatus = 2;
			} else if (chX > x && chY > y) {
				yPlus = ((chY - y) / Math.sqrt(((chY - y) * (chY - y)) + ((chX - x) * (chX - x))));
				xPlus = ((chX - x) / Math.sqrt(((chY - y) * (chY - y)) + ((chX - x) * (chX - x))));
				if (Math.cos(Math.toRadians(45)) > xPlus)
					enemystatus = 0;
				else
					enemystatus = 2;
			} else if (chX < x && chY < y) {
				yPlus = -1 * ((y - chY) / Math.sqrt(((y - chY) * (y - chY)) + ((x - chX) * (x - chX))));
				xPlus = -1 * ((x - chX) / Math.sqrt(((y - chY) * (y - chY)) + ((x - chX) * (x - chX))));
				if (Math.cos(Math.toRadians(45)) > (xPlus * -1))
					enemystatus = 3;
				else
					enemystatus = 1;
			} else if (chX < x && chY > y) {
				yPlus = ((chY - y) / Math.sqrt(((chY - y) * (chY - y)) + ((x - chX) * (x - chX))));
				xPlus = -1 * ((x - chX) / Math.sqrt(((chY - y) * (chY - y)) + ((x - chX) * (x - chX))));
				if (Math.cos(Math.toRadians(45)) > (xPlus * -1))
					enemystatus = 0;
				else
					enemystatus = 1;
			} else if (chX == x && chY < y) {
				enemystatus = 3;
				z = 0;
			} else if (chX == x && chY > y) {
				enemystatus = 0;
				z = 2;
			} else if (chX > x && chY == y) {
				enemystatus = 2;
				z = 1;
			} else if (chX < x && chY == y) {
				enemystatus = 1;
				z = 3;
			} else if (chX == x && chY == y) {
				enemystatus = 1;
				z = 3;
			}
			move();
		}
	}

	class Item {
		int x, y;
		int width, height;

		public Item(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}
}

public class gameTest {
	public static void main(String[] args) {
		new Zombiworld();
	}
}
