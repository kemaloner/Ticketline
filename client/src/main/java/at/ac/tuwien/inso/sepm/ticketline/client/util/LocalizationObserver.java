package at.ac.tuwien.inso.sepm.ticketline.client.util;

/**
 * This interface is responsible for updating the language of the GUI
 */

@FunctionalInterface
public interface LocalizationObserver {

    /**
     * Changes the language of the GUI depending on
     * selection.
     */
    void update();
}
