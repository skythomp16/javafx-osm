package me.skylerthompson.screens;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;
import me.skylerthompson.screens.linear.Row;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.function.Function;

public class LinearProgrammingController {
    public TextField title;
    public TextField numDecisions;
    public TextField numConstraints;
    public RadioButton minimize;
    public RadioButton maximize;
    public ScrollBar horScrollBar;
    public ScrollBar verScrollBar;
    public TableView<Row> table;
    public TableColumn<Row, String> constraintColumn;
    public TableColumn<Row, Number> decision1Column;
    public TableColumn<Row, Number> decision2Column;
    public TableColumn<Row, Row.Operator> operatorColumn;
    public TableColumn<Row, Number> rhsColumn;

    public void initialize() {
        populateTable();
        enableTabbing();
        handleEvents();
    }

    public void populateTable() {
        Row row1 = new Row("Objective", Lists.newArrayList(0.0, 0.0), Row.Operator.EMPTY, 0.0);
        Row row2 = new Row("Constraint 1", Lists.newArrayList(0.0, 0.0), Row.Operator.GREATER, 0.0);
        Row row3 = new Row("Constraint 2", Lists.newArrayList(0.0, 0.0), Row.Operator.GREATER, 0.0);

        ObservableList<Row> rows = FXCollections.observableArrayList(Lists.newArrayList(row1, row2, row3));

        table.getItems().addAll(rows);
        table.setEditable(true);
        Callback<TableColumn.CellDataFeatures<Row, String>, ObservableValue<String>> constraintColumnCellValue;
        constraintColumnCellValue = cellDataFeatures -> {
            Row row = cellDataFeatures.getValue();
            String constraint = row.getConstraint();
            return new SimpleStringProperty(constraint);
        };
        constraintColumn.setCellValueFactory(constraintColumnCellValue);
        constraintColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        constraintColumn.setOnEditCommit(event -> {
            Row row = event.getRowValue();
            row.setConstraint(event.getNewValue());
        });
        Callback<TableColumn.CellDataFeatures<Row, Number>, ObservableValue<Number>> decision1ColumnCellValue;
        decision1ColumnCellValue = cellDataFeatures -> {
            Row row = cellDataFeatures.getValue();
            Double decision1 = row.getDecisions().get(0);
            return new SimpleDoubleProperty(decision1);
        };
        decision1Column.setCellValueFactory(decision1ColumnCellValue);
        decision1Column.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        decision1Column.setOnEditCommit(event -> {
            Row row = event.getRowValue();
            row.setDecisionAtLocation(0, event.getNewValue().doubleValue());
        });
        Callback<TableColumn.CellDataFeatures<Row, Number>, ObservableValue<Number>> decision2ColumnCellValue;
        decision2ColumnCellValue = cellDataFeatures -> {
            Row row = cellDataFeatures.getValue();
            Double decision2 = row.getDecisions().get(1);
            return new SimpleDoubleProperty(decision2);
        };
        decision2Column.setCellValueFactory(decision2ColumnCellValue);
        decision2Column.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        decision2Column.setOnEditCommit(event -> {
            Row row = event.getRowValue();
            row.setDecisionAtLocation(1, event.getNewValue().doubleValue());
        });
        Callback<TableColumn.CellDataFeatures<Row, Row.Operator>, ObservableValue<Row.Operator>> operatorColumnCellValue;
        operatorColumnCellValue = cellDataFeatures -> {
            Row row = cellDataFeatures.getValue();
            Row.Operator operator = row.getOperator();
            return new SimpleObjectProperty<>(operator);
        };
        operatorColumn.setCellValueFactory(operatorColumnCellValue);
        operatorColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new Row.OperatorStringConverter(), Row.Operator.values()));
        operatorColumn.setOnEditCommit(event -> {
            Row row = event.getRowValue();
            row.setOperator(event.getNewValue());
        });
        Callback<TableColumn.CellDataFeatures<Row, Number>, ObservableValue<Number>> rhsColumnCellValue;
        rhsColumnCellValue = cellDataFeatures -> {
            Row row = cellDataFeatures.getValue();
            Double rhs = row.getRhs();
            return new SimpleDoubleProperty(rhs);
        };
        rhsColumn.setCellValueFactory(rhsColumnCellValue);
        rhsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        rhsColumn.setOnEditCommit(event -> {
            Row row = event.getRowValue();
            row.setRhs(event.getNewValue().doubleValue());
        });
    }

    public void handleEvents() {

        //Handle adding and removing constraints
        numConstraints.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!numConstraints.isFocused()) {
                try {
                    int number = Integer.parseInt(numConstraints.getText());
                    // Validate if the number is less than 20
                    if (number < 20 && number > table.getItems().size() - 1) {
                        //Add some rows
                        for (int i = table.getItems().size() - 1; i < number; i++) {
                            List<Double> decisions = Lists.newArrayList();
                            for (int j = 0; j < Integer.parseInt(String.valueOf(numDecisions.getText())); j++) {
                                decisions.add(0.0);
                            }
                            Row row = new Row("Constraint " + (i + 1), decisions, Row.Operator.LESS, 0.0);
                            table.getItems().add(row);
                        }
                    } else if (number < 20 && number < table.getItems().size() - 1) {
                        //remove some rows
                        int diff = (table.getItems().size() - 1) - number;
                        int startIndex = table.getItems().size() - diff;
                        List<Row> subList = table.getItems().subList(startIndex, table.getItems().size());
                        table.getItems().removeAll(subList);
                    } else if (number < 20 && number == table.getItems().size() - 1) {
                        System.out.println("Table is equal to the size");
                    } else {
                        // Custom logic for invalid input
                        System.out.println("Invalid input: Number must be less than 20");
                        numConstraints.setText("2");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e);
                    System.out.println("Invalid input: Please enter a valid number");
                    // Set the value to 2 if validation fails
                    numConstraints.setText("2");
                }
            }
        });

        //Handle adding and removing decision variables
        numDecisions.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!numDecisions.isFocused()) {
                try {
                    int number = Integer.parseInt(numDecisions.getText());
                    if (number < 20) {
                        table.getColumns().add(new TableColumn<>("X3"));
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e);
                    System.out.println("Invalid input: Please enter a valid number");
                    // Set the value to 2 if validation fails
                    numDecisions.setText("2");
                }
            }
        });
    }

    public void enableTabbing() {
        table.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                // If the Tab key is pressed, move to the next cell in the row
                TablePosition<Row, ?> focusedCell = table.getFocusModel().getFocusedCell();
                if (focusedCell != null) {
                    int currentRow = focusedCell.getRow();
                    int currentColumn = focusedCell.getColumn();
                    if (currentColumn < table.getColumns().size() - 1) {
                        // If not in the last column, move to the next column in the same row
                        table.getSelectionModel().clearSelection();
                        table.getSelectionModel().select(currentRow);
                        table.getSelectionModel().select(currentRow, table.getColumns().get(currentColumn + 1));
                        table.edit(currentRow, table.getColumns().get(currentColumn + 1));
                        //commitEditIfEditing(table);
                    } else if (currentRow < table.getItems().size() - 1) {
                        // If in the last column but not in the last row, move to the first column in the next row
                        table.getSelectionModel().clearSelection();
                        table.getSelectionModel().select(currentRow + 1);
                        table.getSelectionModel().select(currentRow + 1, table.getColumns().get(0));
                        table.edit(currentRow + 1, table.getColumns().get(0));
                        //commitEditIfEditing(table);
                    }
                    event.consume();
                }
            }
        });
    }

    /*
    private <T> void commitEditIfEditing(TableView<T> tableView) {
        // Commit the edit if the cell is being edited
        TablePosition<T, ?> editingCell = tableView.getEditingCell();
        if (editingCell != null) {
            TableColumn<T, ?> editingColumn = editingCell.getTableColumn();
            TableCell<T, ?> cell = editingColumn.getCellFactory().call(editingColumn);

            if (cell.isEditing()) {
                cell.commitEdit(cell.getItem());
                cell.cancelEdit(); // Optionally, you can cancel the edit to stop the editing state
            }
        }
    }
     */

}