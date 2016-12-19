package ch.abacus.application.ui.data;

import java.util.Arrays;
import java.util.Collection;

public interface DataPersister<DTO> {
 
    default void store(DTO... dtos) {
        if (dtos == null) {
            return;
        }

        store(Arrays.asList(dtos));
    }

    void store(Collection<DTO> dtos);
}
