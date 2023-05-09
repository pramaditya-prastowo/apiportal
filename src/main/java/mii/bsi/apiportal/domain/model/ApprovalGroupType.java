package mii.bsi.apiportal.domain.model;


public enum ApprovalGroupType {
    ANY_GROUP("enum.approval_group_type_anygroup"),
    CROSS_GROUP("enum.approval_group_type_crossgroup"),
    SAME_GROUP("enum.approval_group_type_samegroup"),
    SELECTED_GROUP("enum.approval_group_type_selectedgroup");

    private final String text;

    ApprovalGroupType(String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }

}

