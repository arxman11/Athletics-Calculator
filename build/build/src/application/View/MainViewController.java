package application.View;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import application.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class MainViewController {
	private TreeMap<Double, Double> kuus_list = new TreeMap<Double, Double>();
	private TreeMap<Double, Double> pall_list = new TreeMap<Double, Double>();
	private TreeMap<Double, Double> kaugus_list = new TreeMap<Double, Double>();

	private Main mainApp;
	@FXML
	private TextField kaugus;
	@FXML
	private TextField kuuskend;
	@FXML
	private TextField pall;
	@FXML
	private TextField summa;
	@FXML
	private Label pallTekst;
	@FXML
	private Label kuuskendTekst;
	@FXML
	private Label kaugusTekst;
	@FXML
	private Button arvuta;

	public MainViewController() {
	}

	@FXML
	private void initialize() {
		readData();

	}

	public void setListener() {
		Scene scene = mainApp.getPrimaryStage().getScene();
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				
				switch (event.getCode()) {
				case TAB:
					update();
					break;
				case ESCAPE:
					setDefault();
					break;
				case ENTER:
					update();
					break;
				}
				}
				
			
		});
	}

	protected void setDefault() {
		pall.setText("0");
		kuuskend.setText("0");
		kaugus.setText("0");
		update();

	}

	private void readData() {
		try {
			FileInputStream file = new FileInputStream(new File("resources/data/punktitabel_new.xls"));

			// Create Workbook instance holding reference to .xlsx file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			rowIterator.next();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell value = cellIterator.next();
				String valueString = value.toString();
				if (valueString.equals("null")) {// tühi väärtus, faili lõpp
					break;
				}

				Cell kaug = cellIterator.next();
				String kaugString = kaug.toString();
				if (!kaugString.equals("null")) {// tühi väärtus
					kaugus_list.put(Double.valueOf(value.toString()), Double.valueOf(kaugString));
				}

				Cell pal = cellIterator.next();
				String palString = pal.toString();
				if (!palString.equals("null")) {// tühi väärtus
					pall_list.put(Double.valueOf(valueString), Double.valueOf(palString));
				}

				Cell kuuskend = cellIterator.next();
				String kuusString = kuuskend.toString();
				if (!kuusString.equals("null")) {// tühi väärtus
					kuus_list.put(Double.valueOf(valueString), Double.valueOf(kuusString));
				}

			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void update() {
		calcAndUpdate();

	}

	private Double toDouble(TextField input) {
		Double number = 0.0;
		String tekst=input.getText();
		String output = tekst.replace(",",".");
		number = Double.valueOf(output);
		return number;
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	public void calcAndUpdate() {
		double pall_score = getPallSkoor(toDouble(pall));
		double kaugus_score = getKaugusSkoor(toDouble(kaugus));
		double kuuskend_score = getKuuskendSkoor(toDouble(kuuskend));
		double kogusumma = pall_score + kaugus_score + kuuskend_score;
		pallTekst.setText(String.valueOf(pall_score));
		kaugusTekst.setText(String.valueOf(kaugus_score));
		kuuskendTekst.setText(String.valueOf(kuuskend_score));
		summa.setText(String.valueOf(kogusumma));
	}

	public double getKaugusSkoor(Double input) {
		if (input == 0.0) {
			return 0.0;
		} else {
			double previous = 0.0;
			for (double skoor : kaugus_list.keySet()) {
				if (input < kaugus_list.get(skoor)) {
					break;
				}
				previous = skoor;
			}
			return previous;
		}
	}

	public double getPallSkoor(Double input) {
		if (input == 0.0) {
			return 0.0;
		} else {
			double previous = 0.0;
			for (double skoor : pall_list.keySet()) {
				if (input < pall_list.get(skoor)) {
					break;
				}
				previous = skoor;
			}
			return previous;
		}
	}

	public double getKuuskendSkoor(Double input) {
		if (input == 0.0) {
			return 0.0;
		} else {
			double previous = 0.0;
			for (double skoor : kuus_list.keySet()) {
				if (input > kuus_list.get(skoor)) {
					break;
				}
				previous = skoor;
			}
			return previous;
		}
	}

}
