package net.argus.game.gol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

public class GameSave {
	
	public static String generateSave(Game game, int period, boolean drawGrid) {
		boolean[][] cells = game.getCells();
		
		String save = "";
		
		save += "-2:" + (drawGrid?1:0) + "\n";
		save += "-1:" + period + "\n";
		
		for(int x = 0; x < GameStatic.WIDTH; x++) {
			String l = x + ":";
			for(int y = 0; y < GameStatic.HEIGHT; y++)
				if(cells[x][y])
					l += y + ",";
			if(l.equals(x + ":"))
				continue;
			save += l + "\n";
			
		}
		
		return save;
	}
	
	public static void save(Game game, int period, boolean drawGrid) {
		SwingUtilities.invokeLater(() -> {
			JFileChooser fc = new JFileChooser(new File("."));
			int ret = fc.showSaveDialog(null);
			
			if(ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if(file.getName().lastIndexOf(".") == -1 || !file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()).equals(".golsave"))
					file = new File(file.getAbsolutePath() + ".golsave");
				
				FileOutputStream fs;
				try {
					fs = new FileOutputStream(file);
					PrintWriter out = new PrintWriter(fs);
					
					out.write(generateSave(game, period, drawGrid));
					out.flush();
					
					out.close();
				}catch(FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static GameLoadedState load(Game game) {
		JFileChooser fc = new JFileChooser(new File("."));
		int ret = fc.showOpenDialog(null);
		
		GameLoadedState state = new GameLoadedState();
			
		if(ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if(file.getName().lastIndexOf(".") == -1 || !file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()).equals(".golsave"))
				file = new File(file.getAbsolutePath() + ".golsave");
			
			String save = "";
			
			FileReader fs;
			try {
				fs = new FileReader(file);
				BufferedReader in = new BufferedReader(fs);
				
				String l = "";
				while((l = in.readLine()) != null)
					save += l + "\n";
				
				in.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
			
			game.clear();
			for(String line : save.split("\n")) {
				if(line.indexOf(':') == -1)
					continue;
				
				String b = line.substring(0, line.indexOf(':'));
				String a = line.substring(line.indexOf(':') + 1);
				
				if(b.equals("-2")) {
					state.setDrawGrid(Boolean.valueOf(a.equals("1")?"true":"false"));
					continue;
				}
				
				if(b.equals("-1")) {
					state.setPeriod(Integer.valueOf(a));
					continue;
				}
				
				for(String y : a.split(",")) {
					if(y.isEmpty())
						continue;
					game.live(Integer.valueOf(b), Integer.valueOf(y));
					
				}
			}
		}
		
		return state;		
	}

}
