package edu.ubb.tableeditor.model.data;

import java.util.HashMap;
import java.util.Map;

public class AugmentedData extends RestrictedData {

    private Map<String, Object> augmentation;

    public AugmentedData() {
        super();
        this.augmentation = new HashMap<>();
    }

    @Override
    public Map<String, Object> getAugmentation() {
        return augmentation;
    }

    @Override
    public void setAugmentation(Map<String, Object> augmentation) {
        this.augmentation = augmentation;
    }

}
