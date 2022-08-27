package hrtech.wrhstrsync.model.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Status of the order
 */
public enum OrderStatus {
    CREATED {
        /**
         * Return name of the order status
         *
         * @return name of order status
         */
        @Override
        public String getName() {
            return "Created";
        }

        /**
         * Returns the next possible status
         *
         * @return list with possible next status
         */
        @Override
        public List<OrderStatus> getPossibleNextStatus() {
            List<OrderStatus> answer = new ArrayList<>();
            answer.add(SENT);
            answer.add(CANCELED);
            return answer;
        }
    },
    CANCELED {
        /**
         * Return name of the order status
         *
         * @return name of order status
         */
        @Override
        public String getName() {
            return "Canceled";
        }

    },
    SENT {
        /**
         * Return name of the order status
         *
         * @return name of order status
         */
        @Override
        public String getName() {
            return "Sent";
        }
    };

    /**
     * Method that returns an Enum by an id
     *
     * @param id id to retrieve enum
     * @return Optional with enum, if valid; empty otherwise
     */
    public static Optional<OrderStatus> getByID(int id) {
        switch (id) {
            case 1:
                return Optional.of(SENT);
            case 2:
                return Optional.of(CANCELED);
            case 0:
                return Optional.of(CREATED);
            default:
                return Optional.empty();
        }
    }

    /**
     * Return name of the order status
     *
     * @return name of order status
     */
    public String getName() {
        return "";
    }

    /**
     * Returns the next possible status
     *
     * @return list with possible next status
     */
    public List<OrderStatus> getPossibleNextStatus() {
        return new ArrayList<>();
    }
}

