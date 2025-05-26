package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import dao.FoodItemDAO;
import model.OrderedFood;
import model.Invoice;
import model.FoodItem;

public class FoodServiceFrm extends JFrame implements ActionListener {
    private JTextField txtKeyword;
    private JButton btnSearch;
    private JTable tblFood;
    private DefaultTableModel tableModel;
    private JButton btnNext;
    private JLabel lblTotalPrice;
    private JLabel lblPriceValue;
    private JScrollPane scrollPane;
    private JPanel centerPanel;
    private Invoice invoice;
    private List<OrderedFood> existingFoods;

    public FoodServiceFrm(Invoice invoice) {
        this.invoice = invoice;
        existingFoods = invoice.getFoodList() != null ? invoice.getFoodList() : new ArrayList<>();
        setTitle("Food service");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitle = new JLabel("Food service");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        centerPanel = new JPanel(new BorderLayout(10, 0));

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        txtKeyword = new JTextField();
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(this);
        searchPanel.add(txtKeyword, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        String[] columnNames = {"Name", "Size", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        tblFood = new JTable(tableModel);
        tblFood.setRowHeight(30);
        scrollPane = new JScrollPane(tblFood);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        scrollPane.setVisible(false);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTotalPrice = new JLabel("Total price ");
        lblTotalPrice.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalPrice.setPreferredSize(new Dimension(100, 40));
        bottomPanel.add(lblTotalPrice);

        lblPriceValue = new JLabel("0$");
        lblPriceValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblPriceValue.setPreferredSize(new Dimension(100, 40));
        bottomPanel.add(lblPriceValue);

        bottomPanel.add(Box.createHorizontalGlue());

        btnNext = new JButton("Next");
        btnNext.setPreferredSize(new Dimension(100, 40));
        btnNext.addActionListener(this);
        bottomPanel.add(btnNext);

        mainPanel.add(bottomPanel);

        tableModel.addTableModelListener(_ -> updateTotalPrice());
        updateTotalPrice();
        add(mainPanel);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            String keyword = txtKeyword.getText().trim();
            loadFoodItems(keyword);
        } else if (e.getSource() == btnNext) {
            List<OrderedFood> newFoodList = new java.util.ArrayList<>(existingFoods);
            FoodItemDAO foodItemDAO = new FoodItemDAO();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = (String) tableModel.getValueAt(i, 0);
                String size = (String) tableModel.getValueAt(i, 1);
                int qty = 0;
                Object qtyObj = tableModel.getValueAt(i, 2);
                if (qtyObj instanceof Integer) qty = (Integer) qtyObj;
                else if (qtyObj instanceof String) try { qty = Integer.parseInt((String) qtyObj); } catch (Exception ex) { qty = 0; }
                if (qty > 0) {
                    List<FoodItem> items = foodItemDAO.searchFoodItemByKeyword(name);
                    FoodItem selectedItem = null;
                    
                    for (FoodItem item : items) {
                        if (item.getName().equals(name) && item.getSize().equals(size)) {
                            selectedItem = item;
                            break;
                        }
                    }
                    
                    if (selectedItem != null) {
                        newFoodList.add(new OrderedFood(selectedItem.getId(), qty, invoice.getId(), selectedItem));
                    }
                }
            }
            
            double totalPrice = 0;
            for (OrderedFood f : newFoodList) totalPrice += f.getQuantity() * f.getFoodItem().getUnitPrice();
            invoice.setTotal(invoice.getTotal() + totalPrice);
            invoice.setFoodList(newFoodList);
            this.setVisible(false);
            new ConfirmFrm(invoice).setVisible(true);
            return;
        }
    }

    private void loadFoodItems(String keyword) {
        tableModel.setRowCount(0);
        FoodItemDAO foodItemDAO = new FoodItemDAO();
        List<FoodItem> items = foodItemDAO.searchFoodItemByKeyword(keyword);
        for (FoodItem item : items) {
            tableModel.addRow(new Object[]{item.getName(), item.getSize(), 0});
        }
        TableColumn qtyColumn = tblFood.getColumnModel().getColumn(2);
        qtyColumn.setCellEditor(new SpinnerEditor());
        scrollPane.setVisible(items.size() > 0);
        scrollPane.revalidate();
        scrollPane.repaint();
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void updateTotalPrice() {
        double total = 0;
        FoodItemDAO foodItemDAO = new FoodItemDAO();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = (String) tableModel.getValueAt(i, 0);
            String size = (String) tableModel.getValueAt(i, 1);
            int qty = 0;
            Object qtyObj = tableModel.getValueAt(i, 2);
            if (qtyObj instanceof Integer) {
                qty = (Integer) qtyObj;
            } else if (qtyObj instanceof String) {
                try { qty = Integer.parseInt((String) qtyObj); } catch (Exception ex) { qty = 0; }
            }
            if (qty > 0) {
                List<FoodItem> items = foodItemDAO.searchFoodItemByKeyword(name);
                for (FoodItem item : items) {
                    if (item.getName().equals(name) && item.getSize().equals(size)) {
                        total += item.getUnitPrice() * qty;
                        break;
                    }
                }
            }
        }
        lblPriceValue.setText(String.format("%.1f$", total));
    }
}

class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
    private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value != null ? value : 0);
        return spinner;
    }
} 