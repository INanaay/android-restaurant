package ca.ulaval.ima.mp.ui.reviewRestaurant.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ReviewDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static DummyItem createDummyItem(String id, String first_name, String last_name, String star, String img, String comment, String date) {
        return new DummyItem(id, first_name, last_name, star, img, comment, date);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String first_name;
        public final String last_name;
        public final String star;
        public final String img;
        public final String comment;
        public final String date;

        public DummyItem(String id, String first_name, String last_name, String star, String img, String comment, String date) {
            this.id = id;
            this.first_name = first_name;
            this.last_name = last_name;
            this.star = star;
            this.img = img;
            this.comment = comment;
            this.date = date;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
