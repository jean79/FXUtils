package net.fxutils.utils;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.fxutils.common.Function1;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FxFactories {

    public static <T> Callback<ListView<T>, ListCell<T>> createPropertyCellFactory(Function1<String, T> converter) {
        return new Callback<ListView<T>, ListCell<T>>() {
            public ListCell<T> call(ListView<T> p) {
                final ListCell<T> cell = new ListCell<T>() {
                    @Override
                    protected void updateItem(T t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(converter.to(t));
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
    }

    public static <T> StringConverter<T> createStringConverter(Function1<String, T> converter, Collection<T> dataProvider) {

        return new StringConverter<T>() {

            @Override
            public String toString(T item) {
                return item != null ? converter.to(item) : null;
            }

            @Override
            public T fromString(String s) {
                List<T> matchingItems = dataProvider.stream().filter((item) -> toString(item).toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
                if (matchingItems.size() == 1) {
                    return matchingItems.get(0);
                } else {
                    return null;
                }
            }
        };

    }

}
