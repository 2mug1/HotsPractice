package net.hotsmc.practice.elo;

public class EloUtility {

    private static final KFactor[] K_FACTORS = {
            new KFactor(0, 1000, 25),
            new KFactor(1001, 1400, 20),
            new KFactor(1401, 1800, 15),
            new KFactor(1801, 2200, 10)
    };

    private static final int DEFAULT_K_FACTOR = 25;
    private static final int WIN = 1;
    private static final int LOSS = 0;

    public static int getNewRating(int rating, int opponentRating, boolean won) {
        if (won) {
            return EloUtility.getNewRating(rating, opponentRating, EloUtility.WIN);
        } else {
            return EloUtility.getNewRating(rating, opponentRating, EloUtility.LOSS);
        }
    }

    public static int getNewRating(int rating, int opponentRating, int score) {
        double kFactor = EloUtility.getKFactor(rating);
        double expectedScore = EloUtility.getExpectedScore(rating, opponentRating);
        int newRating = EloUtility.calculateNewRating(rating, score, expectedScore, kFactor);

        if (score == 1) {
            if (newRating == rating) {
                newRating++;
            }
        }
        return newRating;
    }

    private static int calculateNewRating(int oldRating, int score, double expectedScore, double kFactor) {
        return oldRating + (int) (kFactor * (score - expectedScore));
    }

    private static double getKFactor(int rating) {
        for (int i = 0; i < EloUtility.K_FACTORS.length; i++) {
            if (rating >= EloUtility.K_FACTORS[i].getStartIndex() && rating <= EloUtility.K_FACTORS[i].getEndIndex()) {
                return EloUtility.K_FACTORS[i].getValue();
            }
        }

        return EloUtility.DEFAULT_K_FACTOR;
    }

    private static double getExpectedScore(int rating, int opponentRating) {
        return 1 / (1 + Math.pow(10, ((double) (opponentRating - rating) / 400)));
    }

}
