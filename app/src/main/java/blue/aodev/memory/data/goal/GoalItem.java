package blue.aodev.memory.data.goal;

/**
 * A goal item, from the goals API.
 */
public class GoalItem {
    private Item item;

    public Item getItem() {
        return item;
    }

    public static class Item {
        private int id;
        private Cue cue;
        private Response response;

        public int getId() {
            return id;
        }

        public Cue getCue() {
            return cue;
        }

        public Response getResponse() {
            return response;
        }

        public static class Cue {
            private String text;
            private String language;

            public String getText() {
                return text;
            }

            public String getLanguage() {
                return language;
            }
        }

        public static class Response {
            private String text;
            private String language;

            public String getText() {
                return text;
            }

            public String getLanguage() {
                return language;
            }
        }
    }
}
