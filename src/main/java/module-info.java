module org.alabourd.honors_contract {
    requires javafx.controls;

    opens org.alabourd.honors_contract to javafx.fxml;
    exports org.alabourd.honors_contract;
    exports org.alabourd.honors_contract.Comparator;
    opens org.alabourd.honors_contract.Comparator to javafx.fxml;
}