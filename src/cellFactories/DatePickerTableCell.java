package cellFactories;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DatePickerTableCell<S> extends TableCell<S, Date> {

    private final DatePicker datePicker;
    private final DateTimeFormatter dateFormatter;

    public DatePickerTableCell() {
        this.datePicker = new DatePicker();
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Configurar el formato de fecha

        // Configurar el cellFactory para el DatePicker
        datePicker.setDayCellFactory(createDayCellFactory());

        // Listener para cambios en el valor del DatePicker
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isEditing()) {
                commitEdit(toDate(newValue)); // Convertir LocalDate a Date antes de guardar
            }
        });

        // Cancelar edición si el foco se pierde
        datePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && isEditing()) {
                cancelEdit();
            }
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(datePicker);

        if (getItem() != null) {
            datePicker.setValue(toLocalDate(getItem())); // Convertir Date a LocalDate para el DatePicker
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() != null ? formatDate(getItem()) : null);
        setGraphic(null);
    }

    @Override
    protected void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                datePicker.setValue(toLocalDate(item)); // Convertir Date a LocalDate
                setText(null);
                setGraphic(datePicker);
            } else {
                setText(formatDate(item)); // Formatear la fecha como texto
                setGraphic(null);
            }
        }
    }

    /**
     * Convierte un objeto Date a LocalDate.
     */
    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convierte un objeto LocalDate a Date.
     */
    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Formatea un objeto Date a String usando el DateTimeFormatter.
     */
    private String formatDate(Date date) {
        return dateFormatter.format(toLocalDate(date));
    }

    /**
     * Método para crear un cellFactory personalizado para el DatePicker.
     */
    private Callback<DatePicker, DateCell> createDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    // Deshabilitar fines de semana
                    if (item.getDayOfWeek().getValue() >= 6) {
                        setDisable(true);
                        setStyle("-fx-background-color: #f08080;");
                    }

                    // Deshabilitar fechas anteriores a hoy
                    if (item.isBefore(LocalDate.now())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #d3d3d3;");
                    }
                }
            }
        };
    }
}