package ui.paquete;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import models.Paquete;

public class DateEditingCellPaquete extends TableCell<Paquete, Date> {
    private DatePicker datePicker;
    private DateTimeFormatter dateFormatter;
    private String dateFormat;

    public DateEditingCellPaquete() {
        dateFormat = ResourceBundle.getBundle("config/config")
                        .getString("date.format");
        this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDateText());
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(convertToLocalDate(getItem()));
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                setText(getDateText());
                setGraphic(null);
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(convertToLocalDate(getItem()));
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        datePicker.focusedProperty().addListener(
            (ObservableValue<? extends Boolean> arg0, Boolean oldFocus, Boolean newFocus) -> {
                if (!newFocus) {
                    commitEdit(convertToDate(datePicker.getValue()));
                }
            });

        datePicker.setOnAction(event -> {
            commitEdit(convertToDate(datePicker.getValue()));
        });
    }

    private String getDateText() {
        if (getItem() == null) {
            return "";
        }
        return convertToLocalDate(getItem()).format(dateFormatter);
    }

    // Convert java.util.Date to java.time.LocalDate
    private LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // Convert java.time.LocalDate to java.util.Date
    private Date convertToDate(LocalDate dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}