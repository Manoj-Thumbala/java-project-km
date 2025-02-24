package com.techvidvan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.techvidvan.DateLabelFormatter;
import java.util.List;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;

public class CashierPanel extends JFrame {
	private JTextArea billTextArea;
	private int currentOrderID;
	private JComboBox<String> customerBox, productBox;

	public CashierPanel() {
		setTitle("KM Supermarket - Cashier Panel");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		billTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(billTextArea);
		scrollPane.setBounds(400, 30, 350, 500);
		getContentPane().add(scrollPane);
		clearBillArea(billTextArea);

		JLabel lblCustomer = new JLabel("Select Customer:");
		lblCustomer.setBounds(50, 50, 120, 25);
		getContentPane().add(lblCustomer);

		customerBox = new JComboBox<>();
		customerBox.setBounds(180, 50, 200, 25);
		getContentPane().add(customerBox);

		JLabel lblProduct = new JLabel("Select Product:");
		lblProduct.setBounds(50, 100, 120, 25);
		getContentPane().add(lblProduct);

		productBox = new JComboBox<>();
		productBox.setBounds(180, 100, 200, 25);
		getContentPane().add(productBox);

		loadCustomers();
		loadProducts();

		JButton btnNewOrder = new JButton("New Order");
		btnNewOrder.setBounds(75, 150, 250, 25);
		getContentPane().add(btnNewOrder);

		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.setBounds(75, 200, 250, 25);
		btnAddItem.setEnabled(false);
		getContentPane().add(btnAddItem);

		JButton btnRemoveItem = new JButton("Remove Item");
		btnRemoveItem.setBounds(75, 250, 250, 25);
		btnRemoveItem.setEnabled(false);
		getContentPane().add(btnRemoveItem);

		JButton btnDiscardOrder = new JButton("Discard Order");
		btnDiscardOrder.setBounds(75, 300, 250, 25);
		btnDiscardOrder.setEnabled(false);
		getContentPane().add(btnDiscardOrder);

		JButton btnPrintBill = new JButton("Print Bill");
		btnPrintBill.setBounds(75, 350, 250, 25);
		btnPrintBill.setEnabled(false);
		getContentPane().add(btnPrintBill);

		btnNewOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String customer = (String) customerBox.getSelectedItem();
					int custID = Integer.parseInt(customer.split("-|-")[0]);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String timestamp = dateFormat.format(new Date());
					currentOrderID = DatabaseOperations.createNewOrder(custID, timestamp);
					clearBillArea(billTextArea);
					btnAddItem.setEnabled(true);
					btnRemoveItem.setEnabled(true);
					btnDiscardOrder.setEnabled(true);
					btnPrintBill.setEnabled(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String product = (String) productBox.getSelectedItem();
					int prodID = Integer.parseInt(product.split("-|-")[0]);
					String[] productDetails = DatabaseOperations.getProd(prodID);
					int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity:"));

					// Check stock availability
					int stock = DatabaseOperations.getStock(prodID);
					if (quantity > stock) {
						JOptionPane.showMessageDialog(null, "No stock available");
						return;
					}

					// Proceed with adding the item
					float price = Float.parseFloat(productDetails[2]) * quantity;
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String timestamp = dateFormat.format(new Date());
					DatabaseOperations.addOrderItem(currentOrderID, prodID, quantity, price, timestamp);

					// Update the stock
					DatabaseOperations.updateStock(prodID, stock - quantity);

					billTextArea.append("\nProduct: " + productDetails[1] + "\nQuantity: " + quantity + "\nPrice: " + price + "\n");
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Invalid quantity entered.");
				}
			}
		});

		btnRemoveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DatabaseOperations.deleteOrderItem(currentOrderID);
					clearBillArea(billTextArea);
					List<String[]> items = DatabaseOperations.getDailyReport(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					for (String[] item : items) {
						billTextArea.append("\nProduct: " + item[2] + "\nQuantity: " + item[3] + "\nPrice: " + item[4] + "\n");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		btnDiscardOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DatabaseOperations.discardOrder(currentOrderID);
					clearBillArea(billTextArea);
					btnAddItem.setEnabled(false);
					btnRemoveItem.setEnabled(false);
					btnDiscardOrder.setEnabled(false);
					btnPrintBill.setEnabled(false);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String product = (String) productBox.getSelectedItem();
					int prodID = Integer.parseInt(product.split("-|-")[0]);
					String[] productDetails = DatabaseOperations.getProd(prodID);
					int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity:"));

					// Check stock availability
					int stock = DatabaseOperations.getStock(prodID);
					if (quantity > stock) {
						JOptionPane.showMessageDialog(null, "No stock available");
						return;
					}

					// Proceed with adding the item
					float price = Float.parseFloat(productDetails[2]) * quantity;
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String timestamp = dateFormat.format(new Date());
					DatabaseOperations.addOrderItem(currentOrderID, prodID, quantity, price, timestamp);

					// Update the stock
					DatabaseOperations.updateStock(prodID, stock - quantity);

					billTextArea.append("\nProduct: " + productDetails[1] + "\nQuantity: " + quantity + "\nPrice: " + price + "\n");
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Invalid quantity entered.");
				}
			}
		});

		btnPrintBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					float price = DatabaseOperations.getTotalPrice(currentOrderID);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					billTextArea.append("\t\tGrand Total\t" + price);
					billTextArea.append("\nGenerated on: " + dateFormat.format(new Date()) + "\n");
					currentOrderID = 0;
					btnAddItem.setEnabled(false);
					btnRemoveItem.setEnabled(false);
					btnDiscardOrder.setEnabled(false);
					btnPrintBill.setEnabled(false);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnGenerateReport = new JButton("Generate Report");
		btnGenerateReport.setBounds(75, 400, 250, 25);
		getContentPane().add(btnGenerateReport);

		btnGenerateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dateDialog = new JDialog();
				dateDialog.setSize(400, 200);
				dateDialog.setLayout(null);

				JLabel lblSelectReportType = new JLabel("Select Report Type:");
				lblSelectReportType.setBounds(10, 10, 150, 25);
				dateDialog.add(lblSelectReportType);

				JComboBox<String> reportTypeBox = new JComboBox<>();
				reportTypeBox.setBounds(170, 10, 200, 25);
				reportTypeBox.addItem("Daily Report");
				reportTypeBox.addItem("Monthly Report");
				dateDialog.add(reportTypeBox);

				JLabel lblSelectDate = new JLabel("Select Date:");
				lblSelectDate.setBounds(10, 50, 150, 25);
				dateDialog.add(lblSelectDate);

				UtilDateModel dateModel = new UtilDateModel();
				Properties properties = new Properties();
				JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, properties);
				JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
				datePicker.setBounds(170, 50, 200, 25);
				dateDialog.add(datePicker);

				JButton btnFetchReport = new JButton("Fetch Report");
				btnFetchReport.setBounds(130, 100, 150, 25);
				dateDialog.add(btnFetchReport);

				btnFetchReport.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String reportType = (String) reportTypeBox.getSelectedItem();
						String selectedDate = new SimpleDateFormat("yyyy-MM-dd").format((Date) datePicker.getModel().getValue());
						try {
							List<String[]> report;
							if (reportType.equals("Daily Report")) {
								report = DatabaseOperations.getDailyReport(selectedDate);
							} else {
								report = DatabaseOperations.getMonthlyReport(selectedDate);
							}
							displayReport(report, reportType);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						dateDialog.dispose();
					}
				});

				dateDialog.setVisible(true);
			}
		});

		setVisible(true);
	}

	private void displayReport(List<String[]> report, String reportType) {
		JFrame reportFrame = new JFrame(reportType);
		reportFrame.setSize(800, 600);
		reportFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		reportFrame.getContentPane().add(scrollPane);

		model.addColumn("Order ID");
		model.addColumn("Customer Name");
		model.addColumn("Product Name");
		model.addColumn("Quantity");
		model.addColumn("Price");

		for (String[] row : report) {
			model.addRow(row);
		}

		reportFrame.setVisible(true);
	}

	private void clearBillArea(JTextArea textArea) {
		textArea.setText("");
		textArea.append("KM Supermarket\n");
		textArea.append("========================================\n");
	}

	private void loadCustomers() {
		try {
			List<String> customers = DatabaseOperations.getCustomers();
			for (String customer : customers) {
				customerBox.addItem(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadProducts() {
		try {
			List<String> products = DatabaseOperations.getProducts();
			for (String product : products) {
				productBox.addItem(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CashierPanel();
			}
		});
	}
}
