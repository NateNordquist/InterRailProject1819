import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class DaGUI extends JFrame {

	private final int WIDTH = 1800;
	private final int HEIGTH = 800;
	private final int MAX_NUM_TOWNS = 27;
	Graph graph;
	
	public static void main(String[] args) {
		// Invoke the constructor (to setup the GUI) by allocating an instance
		new DaGUI();
	}

	public DaGUI() {
		this.graph = new Graph();
		JFrame frame = new JFrame();
		this.setSize(WIDTH, HEIGTH);
		GridBagLayout layout = new GridBagLayout();
		createLayout(layout);

		this.setTitle("Wollowski's InterRail Guide");

		setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void createLayout(GridBagLayout layout) {
		BufferedImage pic = null;
		try {
			pic = ImageIO.read(new File("src/InterRail.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(pic));

		JPanel tripPanel1 = new JPanel();
		createLayoutTripPanel(tripPanel1);

		JPanel tripPanel2 = new JPanel();
		createLayoutTripPanel(tripPanel2);

		JPanel tripPanel3 = new JPanel();
		createLayoutTripPanel(tripPanel3);

		this.getContentPane().setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 3;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		this.getContentPane().add(picLabel, constraints);

		constraints.gridx = 1;
		constraints.gridheight = 1;
		this.getContentPane().add(tripPanel1, constraints);

		constraints.gridy = 1;
		this.getContentPane().add(tripPanel2, constraints);

		constraints.gridy = 2;
		this.getContentPane().add(tripPanel3, constraints);
	}

	private void createLayoutTripPanel(JPanel modify) {
		String[] filterList = { "Pleasure Cost", "Price", "Time" };
		String[] towns = new String[MAX_NUM_TOWNS];

		try {
			towns = readText(towns);
		} catch (IOException e) {
			e.printStackTrace();
		}
		modify.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		JComboBox<?> filter = new JComboBox<Object>(filterList);
		modify.add(filter, constraints);

		JList<?> startList = new JList<Object>(towns);
		JScrollPane startPane = new JScrollPane(startList);
		JList<?> endList = new JList<Object>(towns);
		JScrollPane endPane = new JScrollPane(endList);
		JButton searchButton = new JButton("FIND ROUTE");
		
		SearchMouseListener searchListener = new SearchMouseListener(searchButton, startList, endList, this.graph);
		startList.addMouseListener(new CityMouseListener(searchButton, searchListener));
		endList.addMouseListener(new CityMouseListener(searchButton, searchListener));
		searchButton.addMouseListener(new SearchMouseListener(searchButton, startList, endList, this.graph));
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		modify.add(startPane, constraints);
		
		constraints.gridx = 3;
		modify.add(endPane, constraints);
	
		constraints.gridx = 2;
		constraints.gridy = 4;
		modify.add(searchButton, constraints);
	}

	private String[] readText(String[] edit) throws IOException {
		BufferedReader s = new BufferedReader(new FileReader(new File("src/Cities.txt")));
		String txt = s.readLine();

		int i = 0;
		while (txt != null) {
			edit[i] = txt;
			txt = s.readLine();
			i++;
		}
		s.close();
		return  edit;
	}
}
