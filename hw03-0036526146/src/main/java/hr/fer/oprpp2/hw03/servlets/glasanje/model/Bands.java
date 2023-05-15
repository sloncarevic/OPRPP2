package hr.fer.oprpp2.hw03.servlets.glasanje.model;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementation of connected bands
 *
 */
public class Bands implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static Map<Integer, Band> loadBand(String path) {
		
		List<String> rows = null;

		try {
			rows = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Can't parse given file " + path);
			e.printStackTrace();
			return null;
		}
		
		Map<Integer, Band> bands = new LinkedHashMap<Integer, Band>();
		
		for (var row : rows) {
			String[] rowArr = row.split("\\t+");
			Band band = new Band(Integer.parseInt(rowArr[0]), rowArr[1], rowArr[2]);
			bands.put(band.getID(), band);
		}
		
		return bands;
	}

	public static List<Band> getBandsList(Map<Integer, Band> bands, String fileName) throws IOException {
		
		List<String> rows = null;
		rows = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		
		for (var row : rows) {
			String[] rowArr = row.split("\\t+");
			bands.get(Integer.parseInt(rowArr[0])).setVotes(Integer.parseInt(rowArr[1]));
		}
		
		List<Band> bandsList = new ArrayList<>(bands.values());
		bandsList.sort(new Comparator<Band>() {
			@Override
			public int compare(Band o1, Band o2) {
				return o2.getVotes() - o1.getVotes();
			}
		});
		
		return bandsList;
	}
	
	
}
