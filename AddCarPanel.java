import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.TreeUI;

/**
 * The panel used for adding cars to the CarSalesSystem
 * @
 *
 * PUBLIC FEATURES:
 * // Constructors
 * public AddCarPanel(CarSalesSystem carSys, JPanel dest)
 *
 * // Methods
 * public void actionPerformed(ActionEvent ev)
 *
 * COLLABORATORS:
 * CarDetailComponents
 *
 * @version 1.0, 16 Oct 2004
 * @author Adam Black
 */
public class AddCarPanel extends JPanel implements ActionListener {
	private CarSalesSystem carSystem;
	private JLabel headingLabel = new JLabel("Add a Car");
	private JButton resetButton = new JButton("Reset");
	private JButton saveButton = new JButton("Save");
	private JPanel buttonPanel = new JPanel();
	private CarDetailsComponents carComponents = new CarDetailsComponents();

	/**
	 * @param carSys links to a CarSalesSystem object
	 * @param dest   where the components will be placed
	 */
	public AddCarPanel(CarSalesSystem carSys) {
		carSystem = carSys;

		resetButton.addActionListener(this);
		saveButton.addActionListener(this);
		headingLabel.setAlignmentX(0.5f);

		buttonPanel.add(resetButton);
		buttonPanel.add(saveButton);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(Box.createVerticalStrut(10));
		add(headingLabel);
		add(Box.createVerticalStrut(15));
		carComponents.add(buttonPanel, "Center");
		add(carComponents);
	}

	/**
	 * check which buttons were pressed
	 *
	 * @param ev ActionEvent object
	 */
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == resetButton)
			resetButtonClicked();
		else if (ev.getSource() == saveButton)
			saveButtonClicked();
	}

	private void resetButtonClicked() {
		carComponents.clearTextFields();
	}

	private void saveButtonClicked() {
		String manufacturer = "";
		String model = "";
		String info = "";
		double kilometers = 0;
		int price = 0;
		int year = 0;
		boolean valid = false;
		boolean cont = true;
		try {
			/*
			 * retrieve all the values from the text field, and convert them into an
			 * appropriate
			 * format
			 */

			info = carComponents.getInfoText().trim();

			// manufacturer
			manufacturer = carComponents.getManufacturerText().trim();
			if (manufacturer.length() < 3 || manufacturer.length() > 15) {
				JOptionPane.showMessageDialog(carSystem,
						"The \"Manufacturer\" field must contain at least 2 charcters and at most 15",
						"Invalid field", JOptionPane.ERROR_MESSAGE);
				cont = false;
			}
			for (int i = 0; i < manufacturer.length(); i++) {
				System.out.println((int) manufacturer.charAt(i));
				if ((int) manufacturer.charAt(i) < 65
						|| ((int) manufacturer.charAt(i) > 90 && (int) manufacturer.charAt(i) < 97)
						|| (int) manufacturer.charAt(i) > 122) {
					JOptionPane.showMessageDialog(carSystem,
							"The \"Manufacturer\" field must contain alphabets only",
							"Invalid field", JOptionPane.ERROR_MESSAGE);
					cont = false;
					break;
				}
			}

			// model
			model = carComponents.getModelText().trim();
			if (model.length() < 2 || model.length() > 10) {
				JOptionPane.showMessageDialog(carSystem,
						"The \"Model\" field must contain at least 2 charcters and at most 10",
						"Invalid field", JOptionPane.ERROR_MESSAGE);
				cont = false;
			}

			// year
			try {
				year = Integer.parseInt(carComponents.getYearText().trim());
				if (carComponents.getYearText().trim().length() != 4) {
					JOptionPane.showMessageDialog(carSystem, "The \"Year\" field must contain four numeric digits only",
							"Invalid field", JOptionPane.ERROR_MESSAGE);
					cont = false;
				}
				if (year <= 1900 || year > 2021) {
					JOptionPane.showMessageDialog(carSystem,
							"An error has occured due to incorrect \"Year\" text field data.\nThis text field must be in the form, YYYY. ie, 2007.",
							"Invalid field", JOptionPane.ERROR_MESSAGE);
					cont = false;
				}
			} catch (NumberFormatException exp) {
				JOptionPane.showMessageDialog(carSystem, "The \"Year\" field must contain numerical digits only",
						"Invalid field", JOptionPane.ERROR_MESSAGE);
				cont = false;
			}

			// price
			try {
				price = Integer.parseInt(carComponents.getPriceText().trim());
				if (price < 9999
						|| price > 100000000) {
					JOptionPane.showMessageDialog(carSystem,
							"The \"Price\" field must contain a valid integer between 9,999 and 100,000,000",
							"Invalid field", JOptionPane.ERROR_MESSAGE);
					cont = false;
				}
			} catch (NumberFormatException exp) {
				JOptionPane.showMessageDialog(carSystem,
						"The \"Price\" field must contain a valid integer with no decimal places and no \",\" ",
						"Invalid field", JOptionPane.ERROR_MESSAGE);
				cont = false;
			}

			// kms
			try {
				kilometers = Double.parseDouble(carComponents.getKmText().trim());
				if (kilometers < 0 || kilometers > 500000) {
					JOptionPane.showMessageDialog(carSystem,
							"The \"Km Traveled\" field must contain a number between 0 and 500000",
							"Invalid field", JOptionPane.ERROR_MESSAGE);
					cont = false;
				}
			} catch (NumberFormatException exp) {
				JOptionPane.showMessageDialog(carSystem,
						"The \"Km Traveled\" field must contain a number",
						"Invalid field", JOptionPane.ERROR_MESSAGE);
				cont = false;
			}

			// begin validation process
			if (cont) {
				if (validateString(manufacturer)) {
					if (year >= 1900 && year <= 2021) {
						if (validateString(model)) {
							if (validateKilometers(carComponents.getKmText().trim())) {
								valid = true;
							} else
								JOptionPane.showMessageDialog(carSystem,
										"An error has occured due to incorrect \"Km Traveled\" text field data.\nThis text field must contain a number with one decimal place only.",
										"Invalid field", JOptionPane.ERROR_MESSAGE);
						} else
							JOptionPane.showMessageDialog(carSystem,
									"An error has occured due to incorrect \"Model\" text field data.\nThis text field must contain any string of at least two non-spaced characters.",
									"Invalid field", JOptionPane.ERROR_MESSAGE);
					} else
						JOptionPane.showMessageDialog(carSystem,
								"An error has occured due to incorrect \"Year\" text field data.\nThis text field must be in the form, YYYY. ie, 2007.",
								"Invalid field", JOptionPane.ERROR_MESSAGE);
				} else
					JOptionPane.showMessageDialog(carSystem,
							"An error has occured due to incorrect \"Manufacturer\" text field data.\nThis text field must contain any string of at least two non-spaced characters.",
							"Invalid field", JOptionPane.ERROR_MESSAGE);
			}
		}
		/*
		 * NumberFormatException would usually be thrown if the text fields contain
		 * invalid data,
		 * for example a price field containing letters.
		 */
		catch (NumberFormatException exp) {
			kilometers = Double.parseDouble(carComponents.getKmText().trim());
			price = Integer.parseInt(carComponents.getPriceText().trim());
			year = Integer.parseInt(carComponents.getYearText().trim());
			JOptionPane.showMessageDialog(carSystem,
					"An unknown error has occured. Please ensure your fields meet the following requirements:\n" +
							"The \"Year\" field must contain four numeric digits only\nThe \"Price\" field must contain a valid integer with no decimal places\nThe \"Km Traveled\" field must contain a number which can have a maximum of one decimal place",
					"Invalid field", JOptionPane.ERROR_MESSAGE);
		}

		if (valid && cont) {
			// create a car object from validated data.
			Car myCar = new Car(manufacturer, model, info);
			myCar.setKilometers(kilometers);
			myCar.setPrice(price);
			myCar.setYear(year);

			// attempt to add the new car to the system.
			int result = carSystem.addNewCar(myCar);

			// if the car was added successfully
			if (result == CarsCollection.NO_ERROR) {
				carSystem.setCarsUpdated();
				JOptionPane.showMessageDialog(carSystem, "Record added.", "Confirmation",
						JOptionPane.INFORMATION_MESSAGE);
				resetButtonClicked();
				carComponents.setFocusManufacturerTextField();
			}
			// for that manufacturer, the limit has been reached
			else if (result == CarsCollection.CARS_MAXIMUM_REACHED)
				JOptionPane.showMessageDialog(carSystem,
						"The maximum amount of cars for that manufacturer has been reached.\nUnfortunately you cannot add any further cars to this manufacturer",
						"Problem adding car", JOptionPane.WARNING_MESSAGE);
			// the car system has reached the maximum number of manufacturers allowed
			else if (result == CarsCollection.MANUFACTURERS_MAXIMUM_REACHED)
				JOptionPane.showMessageDialog(carSystem,
						"The maximum amount of manufacturers in the car system has been reached.\nUnfortunately you cannot add any further manufacturers to this system",
						"Problem adding car", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * checks the argument. It is valid if there is more than 2 non-spaced
	 * characters.
	 *
	 * @param arg string to test
	 * @return true if valid, false otherwise
	 */
	private boolean validateString(String arg) {
		boolean valid = false;
		String[] splitted = arg.split(" "); // splits argument around spaces and creates an array

		for (int i = 0; i < splitted.length; i++) {
			// checks if the number of characters between a space is greater than 2
			valid = (splitted[i].length() > 2);
			if (valid)
				break;
		}

		return valid;
	}

	/**
	 * checks the argument It is valid if it contains a decimal value, with only one
	 * decimal place
	 *
	 * @param distance a double value expressed in a string
	 * @return true if valid, false otherwise
	 */
	private boolean validateKilometers(String distance) {
		boolean valid = false;
		String rem;
		StringTokenizer tokens = new StringTokenizer(distance, "."); // look for decimal point

		tokens.nextToken();

		if (tokens.hasMoreTokens()) // if true, there is a decimal point present
		{
			// get string representation of all numbers after the decimal point
			rem = tokens.nextToken();
			// if there's only one number after the decimal point, then it's valid
			if (rem.length() == 1)
				valid = true;
			else {
				// check if the user has typed something like 3.00, or even 3.00000
				if ((Integer.parseInt(rem)) % (Math.pow(10, rem.length() - 1)) == 0)
					valid = true;
				else
					valid = false;
			}
		} else // doesn't have a decimal place
			valid = true;

		return valid;
	}
}