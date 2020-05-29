package com.elca.vn.converter;

import com.elca.vn.model.GUIStatusModel;
import javafx.util.StringConverter;

import java.util.Objects;

public class ChoiceBoxStatusStringConverter extends StringConverter<GUIStatusModel> {

    @Override
    public String toString(GUIStatusModel object) {
        if (Objects.isNull(object)) {
            return null;
        }
        return object.statusValue;
    }

    @Override
    public GUIStatusModel fromString(String string) {
        return GUIStatusModel.valueOfStatusValue(string);
    }
}
