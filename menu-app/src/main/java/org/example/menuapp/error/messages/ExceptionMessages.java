package org.example.menuapp.error.messages;

public final class ExceptionMessages {
    private ExceptionMessages() {}


    public static final String RESOURCE_NOT_FOUND = "%s not found with ID: %s";
    public static final String FILE_NOT_ABLE_TO_SAVE = "Failed to store the image. Please try again";
    public static final String ORDER_NOT_ABLE_TO_DELETE = "Order can not be deleted with status: %s";
    public static final String ORDER_PROCESSED = "Order is already processed, so cannot be updated";
    public static final String SOME_CATEGORIES_ARE_NOT_FOUND = "Some categories are not found.";
    public static final String SOME_ITEMS_ARE_NOT_FOUND = "Some items are not found.";
    public static final String SOME_ADDONS_ARE_NOT_FOUND = "Some addons are not found.";
}

