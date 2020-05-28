package com.elca.vn.converter;

import com.elca.vn.proto.model.PimGroup;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Objects;

public class ChoiceBoxGroupStringConverter extends StringConverter<PimGroup> {

    private List<PimGroup> data;

    public ChoiceBoxGroupStringConverter(List<PimGroup> data) {
        this.data = data;
    }

    @Override
    public String toString(PimGroup object) {
        if (Objects.isNull(object)) {
            return null;
        }
        return object.getGroupName();
    }

    @Override
    public PimGroup fromString(String text) {
        return data.stream()
                .filter(x -> x.getGroupName().equals(text))
                .findFirst()
                .orElse(null);
    }

    public List<PimGroup> getData() {
        return data;
    }

    public void setData(List<PimGroup> data) {
        this.data = data;
    }
}
