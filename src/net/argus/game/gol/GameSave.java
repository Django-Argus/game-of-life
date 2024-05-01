package net.argus.game.gol;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

public class GameSave {
	
	public static String generateSave(Game game, int period, double scale, int tx, int ty) {
		game.getLock().lock();
		
		String save = "";
		
		save += "scale:" + scale + "\n";
		save += "tx:" + tx + "\n";
		save += "ty:" + ty + "\n";
		save += "period:" + period + "\n";
		
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		
		for(Point p : game.getCells().getCells()) {
			if(!map.containsKey(p.x)) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(p.y);
				map.put(p.x, list);
			}else {
				List<Integer> list = map.get(p.x);
				list.add(p.y);
			}
		}
		
		for(Entry<Integer, List<Integer>> entry : map.entrySet()) {
			String l = entry.getKey() + ":";
			for(Integer i : entry.getValue()) 
				l += i + ",";
			
			save += l + "\n";
		}
		
		return save;
	}
	
	public static void save(Game game, int period, double scale, int tx, int ty) {
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
					
					out.write(generateSave(game, period, scale, tx, ty));
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
				
				if(b.equals("scale")) {
					state.setScale(Double.valueOf(a));
					continue;
				}
				
				if(b.equals("tx")) {
					state.setTx(Integer.valueOf(a));
					continue;
				}
				
				if(b.equals("ty")) {
					state.setTy(Integer.valueOf(a));
					continue;
				}
				
				if(b.equals("period")) {
					state.setPeriod(Integer.valueOf(a));
					continue;
				}
				
				for(String y : a.split(",")) {
					if(y.isEmpty())
						continue;
					game.add(Integer.valueOf(b), Integer.valueOf(y));
					
				}
			}
		}
		
		return state;		
	}

}
