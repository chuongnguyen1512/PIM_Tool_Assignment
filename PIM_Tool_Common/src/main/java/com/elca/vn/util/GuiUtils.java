package com.elca.vn.util;

import com.google.protobuf.Timestamp;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Utils class for GUI component handling.
 */
public class GuiUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuiUtils.class);

    private GuiUtils() {
    }

    /**
     * Getting value from resource bundle and resolve place holder if needed
     *
     * @param bundle
     * @param bundleID
     * @param params
     * @return
     */
    public static String getAndResolveBundleResource(ResourceBundle bundle, String bundleID, String... params) {
        try {
            String resource = bundle.getString(bundleID);
            return String.format(resource, params);
        } catch (MissingResourceException e) {
            LOGGER.warn("Cannot find resource bundle %", bundleID);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Showing alert dialog
     *
     * @param alertType
     * @param message
     * @param title
     * @param header
     */
    public static void showAlert(Alert.AlertType alertType, String message, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Get {@link Timestamp} value from {@link DatePicker} control
     *
     * @param datePicker
     * @return
     */
    public static Timestamp getDatePickerGoogleTimestampValue(DatePicker datePicker) {
        if (Objects.isNull(datePicker)) {
            return null;
        }

        LocalDate localDate = datePicker.getValue();
        if (Objects.isNull(localDate)) {
            return null;
        }

        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.of("UTC")));
        Date date = Date.from(instant);

        if (Objects.isNull(date)) {
            return null;
        }

        return Timestamp.newBuilder().setSeconds(date.getTime() / 1000).build();
    }

    /**
     * Get multiple values from {@link TextField} control with given separator
     *
     * @param textField
     * @param separator
     * @return
     */
    public static List<String> getMultipleValuesFromTextField(TextField textField, String separator) {
        return StringUtils.isNotBlank(textField.getText()) ? Arrays.asList(StringUtils.split(textField.getText(), separator)) : new ArrayList<>();
    }

    /**
     * Verify GUI data is invalid or not
     *
     * @param controls
     * @param style
     * @return
     */
    public static boolean isEmptyGUIData(List<Control> controls, String style) {
        boolean isInvalidGuidData = false;
        for (Control control : controls) {
            if (isInvalidTextField(control) || isInvalidChoiceBox(control) || isInvalidDatePicker(control)) {
                control.setStyle(style);
                isInvalidGuidData = true;
            }
        }
        return isInvalidGuidData;
    }

    /**
     * Verify {@link TextField} control value is invalid or not
     *
     * @param control
     * @return
     */
    private static boolean isInvalidTextField(Control control) {
        return Objects.nonNull(control) && control instanceof TextField && StringUtils.isBlank(((TextField) control).getText());
    }

    /**
     * Verify {@link ChoiceBox} control value is invalid or not.
     *
     * @param control
     * @return
     */
    private static boolean isInvalidChoiceBox(Control control) {
        return Objects.nonNull(control) && control instanceof ChoiceBox && Objects.isNull(((ChoiceBox) control).getSelectionModel().isEmpty());
    }

    /**
     * Verify {@link DatePicker} control value is invalid or not.
     *
     * @param control
     * @return
     */
    private static boolean isInvalidDatePicker(Control control) {
        return Objects.nonNull(control) && control instanceof DatePicker && Objects.isNull(((DatePicker) control).getValue());
    }
}
